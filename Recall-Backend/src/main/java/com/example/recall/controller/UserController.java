package com.example.recall.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import jakarta.annotation.PostConstruct;

@RestController
public class UserController {
    private boolean initialDataLoaded = false;
    private final Lock lock = new ReentrantLock();

    @PostConstruct
    public void init() {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user");
        userRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (initialDataLoaded) {
                    // Handle the new child according to your business logic
                    System.out.println("HELLO: A new user has been added.");
                } else {
                    // Ignore initial data synchronization
                    System.out.println("Skipping initial data synchronization.");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("HELLO: A user's data has changed.");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("HELLO: A user has been removed.");
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onChildMoved'");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'onCancelled'");
            }
        });
        
        initialDataLoaded = true;
    }

    @GetMapping("/users/add")
    public String addUser() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> user = new HashMap<>();
        user.put("name", "John Doe");
        user.put("email", "johndoe@example.com");

        DatabaseReference newUserRef = database.child("user").push();

        newUserRef.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.err.println("Data could not be saved. Error: " + databaseError.getMessage());
                } else {
                    System.out.println("Data saved successfully.");
                }
            }
        });

        return "User addition to Firebase initiated.";
    }

    @GetMapping("/users/get")
    public String getAllUsers() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("user");
        StringBuilder allUsers = new StringBuilder();
        CountDownLatch latch = new CountDownLatch(1);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> user = (Map<String, Object>) userSnapshot.getValue();
                    allUsers.append("Name: ").append(user.get("name")).append(", Email: ").append(user.get("email"))
                            .append("\n");
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("The read failed: " + databaseError.getMessage());
                latch.countDown();
            }
        });

        try {
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "Error fetching users.";
        }

        return allUsers.toString();
    }

    @GetMapping("/queue/add")
    public String addQueuedPlayer() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference newPlayerRef = database.child("queued_players").push();

        String uuid = newPlayerRef.getKey();

        // Create the player data
        Map<String, Object> player = new HashMap<>();
        player.put("uuid", uuid); 
        player.put("elo", 1500); 
        
        lock.lock();
        try {
            newPlayerRef.setValue(player, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.err.println("Data could not be saved. Error: " + databaseError.getMessage());
                    } else {
                        System.out.println("Player added to queue successfully.");
                    }
                }
            });
        } finally {
            lock.unlock(); 
        }

        return "Player addition to queue initiated.";
    }
}
