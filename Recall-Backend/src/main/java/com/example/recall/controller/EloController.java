package com.example.recall.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class EloController {

    // Constants
    private static final int INITIAL_ELO = 1000; 
    private static final double K_FACTOR = 32; 

    // Methods
    /**
     * Calculate the expected score for a player A in a match against player B.
     *
     * @param ratingA Elo rating of player A
     * @param ratingB Elo rating of player B
     * @return Expected score of player A
     */
    public double calculateExpectedScore(int ratingA, int ratingB) {
        return 1.0 / (1 + Math.pow(10, (ratingB - ratingA) / 400.0));
    }

    /**
     * Update Elo ratings after a match between two players.
     *
     * @param ratingA Elo rating of player A before the match
     * @param ratingB Elo rating of player B before the match
     * @param scoreA  Actual score of player A (1 for win, 0.5 for draw, 0 for loss)
     * @return Updated Elo ratings for players A and B as an array
     */
    public int[] updateRatings(int ratingA, int ratingB, double scoreA) {
        double expectedA = calculateExpectedScore(ratingA, ratingB);
        double expectedB = 1 - expectedA;

        int newRatingA = (int) Math.round(ratingA + K_FACTOR * (scoreA - expectedA));
        int newRatingB = (int) Math.round(ratingB + K_FACTOR * ((1 - scoreA) - expectedB));

        return new int[]{newRatingA, newRatingB};
    }

    /**
     * Initialize a new player with an initial Elo rating.
     *
     * @return Initial Elo rating for a new player
     */
    public int getInitialEloRating() {
        return INITIAL_ELO;
    }


    public boolean isInEloRange(int eloPlayerA, int eloPlayerB, int range) {
        return Math.abs(eloPlayerA - eloPlayerB) <= range;
    }
}