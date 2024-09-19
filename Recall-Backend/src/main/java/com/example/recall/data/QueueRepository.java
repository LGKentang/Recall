package com.example.recall.data;

import org.springframework.stereotype.Repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import jakarta.annotation.PostConstruct;

@Repository
public class QueueRepository {

    private static DatabaseReference queuedPlayersRef;

    @PostConstruct
    public void init() {
        QueueRepository.queuedPlayersRef = FirebaseDatabase.getInstance().getReference("queued_players");
    }

    public static void removeUserByUuid(String uuid) {
        queuedPlayersRef.child(uuid).removeValueAsync();
    }
}
