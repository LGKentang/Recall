package com.example.recall.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/hello")
    public String getMethodName() {
        return "hello";
    }

    @GetMapping("/elo")
    public String eloPrint(){
        EloController eloController = new EloController();
        int playerARating = 1000;
        int playerBRating = 1200;
    
        // return Double.valueOf(eloController.calculateExpectedScore(playerARating, playerBRating)).toString();
        int[] newRatings = eloController.updateRatings(playerARating, playerBRating, 1);
        return "Player A: " + newRatings[0] + " " + "Player B: " + newRatings[1];
    }

}
