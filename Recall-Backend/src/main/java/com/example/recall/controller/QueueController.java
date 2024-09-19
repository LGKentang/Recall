package com.example.recall.controller;

import com.example.recall.model.SearchPlayer;
import com.example.recall.data.QueueRepository;
import com.example.recall.model.GameSession;
import com.google.firebase.database.*;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Controller;

@Controller
public class QueueController {

    private List<SearchPlayer> queueList;
    private EloController eloController;
    private ScheduledExecutorService scheduler;
    private DatabaseReference queuedPlayersRef;

    private final Lock lock = new ReentrantLock();
    @PostConstruct
    public void init() {
        System.out.println("Initiated");
        this.queueList = new ArrayList<>();
        this.eloController = new EloController();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.queuedPlayersRef = FirebaseDatabase.getInstance().getReference("queued_players");
        startQueueProcessor();
        setupFirebaseListener();
    }

    private void startQueueProcessor() {
        scheduler.scheduleAtFixedRate(this::connectPlayers, 0, 1, TimeUnit.SECONDS);
    }

    private void setupFirebaseListener() {
        queuedPlayersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                synchronized (queueList) {
                    Map<String, Object> data = (Map<String, Object>) dataSnapshot.getValue();

                    if (data != null) {
                        String uuid = (String) data.get("uuid");
                        long eloLong = (long) data.get("elo");
                        int elo = (int) eloLong;

                        SearchPlayer player = new SearchPlayer(uuid, elo);

                        if (!queueList.contains(player)) {
                            System.out.println("Player is added");
                            queueList.add(player);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // Handle any updates to players if necessary
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                synchronized (queueList) {
                    SearchPlayer player = dataSnapshot.getValue(SearchPlayer.class);
                    if (player != null) {
                        queueList.remove(player);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // Handle child moved if necessary
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error listening to queued players: " + databaseError.getMessage());
            }
        });
    }

    public void connectPlayers() {
        System.out.println("Connecting Players...");
        List<SearchPlayer> matchedPlayers = new ArrayList<>();
    
        lock.lock(); 
        try {
            for (int i = 0; i < queueList.size(); i++) {
                SearchPlayer player1 = queueList.get(i);
                boolean foundMatch = false;
                for (int j = i + 1; j < queueList.size(); j++) {
                    SearchPlayer player2 = queueList.get(j);
                    if (eloController.isInEloRange(player1.getElo(), player2.getElo(), 50)) {
                        createGameSession(player1, player2);
                        matchedPlayers.add(player1);
                        matchedPlayers.add(player2);
                        foundMatch = true;
                        break; 
                    }
                }
                if (foundMatch) {
                    break;
                }
            }
    
            queueList.removeAll(matchedPlayers);
            updateFirebaseQueue(matchedPlayers);
        } finally {
            lock.unlock();
        }
    }
    
    
    private void updateFirebaseQueue(List<SearchPlayer> matchedPlayers) {
        lock.lock();
        try {
            for (SearchPlayer player : matchedPlayers) {
                queuedPlayersRef.child(player.getUuid()).removeValueAsync();
            }
        } finally {
            lock.unlock();
        }
    }
    
    public int increaseEloRange(int eloRange) {
        return eloRange + 10;
    }

    private void createGameSession(SearchPlayer player1, SearchPlayer player2) {
        GameSession gameSession = new GameSession(player1, player2);
        saveGameSession(gameSession);
        gameSession.start();
    }

    private void saveGameSession(GameSession gameSession) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("gameSessions");
        String sessionId = ref.push().getKey();
        ref.child(sessionId).setValueAsync(gameSession);
    }

    public void stop() {
        scheduler.shutdown();
    }
}
