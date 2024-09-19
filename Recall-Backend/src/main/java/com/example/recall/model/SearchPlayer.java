package com.example.recall.model;

public class SearchPlayer {
    private String uuid;
    private int elo;

    public SearchPlayer(String uuid, int elo) {
        this.uuid = uuid;
        this.elo = elo;
    }

    public String getUuid() {
        return uuid;
    }

    public int getElo() {
        return elo;
    }

    public static SearchPlayer toMap(String uuid, int elo) {
        return new SearchPlayer(uuid, elo);
    }
}
