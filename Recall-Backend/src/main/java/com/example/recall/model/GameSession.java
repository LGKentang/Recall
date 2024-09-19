package com.example.recall.model;

public class GameSession {
    private String player1Uuid;
    private String player2Uuid;
    private int player1Elo;
    private int player2Elo;
    private long timestamp;

    public GameSession(SearchPlayer player1, SearchPlayer player2) {
        this.player1Uuid = player1.getUuid();
        this.player2Uuid = player2.getUuid();
        this.player1Elo = player1.getElo();
        this.player2Elo = player2.getElo();
        this.timestamp = System.currentTimeMillis();
    }

    public String getPlayer1Uuid() {
        return player1Uuid;
    }

    public String getPlayer2Uuid() {
        return player2Uuid;
    }

    public int getPlayer1Elo() {
        return player1Elo;
    }

    public int getPlayer2Elo() {
        return player2Elo;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void start() {
        // Start the game session logic here
    }
}
