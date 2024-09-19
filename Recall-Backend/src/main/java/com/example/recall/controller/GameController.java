package com.example.recall.controller;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.recall.model.Deck;
import com.example.recall.model.Player;

public class GameController {
    private int timer = 0;
    private Player playerA, playerB;
    private boolean startGame = false;
    

    private ScheduledExecutorService scheduler;

    @PostConstruct
    public void init() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            run();
            timer++;
            System.out.println("Timer updated: " + timer);
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void run(){
        if (timer < 5){
            return;
        }
        if (!startGame && timer > 5){
            // just fetch the database reference and then set status : READY
            // send a start signal to the firebase session to start the game
            startGame = true;
            
            // play gamez
        }
    


    }

    

    public Deck assignDeck() {
        return null;
    }


    public void syncData() {
        // send firebase all the data
    }

    public int getTimer() {
        return timer;
    }

    @PreDestroy
    public void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
