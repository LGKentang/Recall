package com.example.recall.controller;

import com.example.recall.model.SupermemoReturn;

public class AnkiController {
    // TODO
    // Anki SRS

    public SupermemoReturn supermemo2(Grade grade, int repetition, double easinessFactor, int interval){
        // https://www.youtube.com/watch?v=dF5rY3xQeAQ
        // https://en.wikipedia.org/wiki/SuperMemo#:~:text=released%20in%202019.-,Description%20of%20SM%2D2%20algorithm,last%20time%20it%20was%20not.
        // input :
        // - user grade q
        // - repetition number n
        // - easiness factor EF
        // - interval I (days time)

        // output :
        // updated n, EF, I

        if (grade.ordinal() >= 3){
            if (repetition == 0){
                interval = 1;
            }
            else if (repetition == 1){
                interval = 6;
            }
            else{
                interval = (int) Math.round(interval * easinessFactor);
            }
            repetition++;
        }

        else{
            repetition = 0;
            interval = 1;
        }

        easinessFactor = easinessFactor + (0.1 - (5 - grade.ordinal()) * (0.08 + (5 - grade.ordinal()) * 0.02));

        if (easinessFactor < 1.3){
            easinessFactor = 1.3;   
        }

        return new SupermemoReturn(repetition, easinessFactor, interval);
    }

    public enum Grade{
        Bad,
        Poor,
        Fair,
        Good,
        Excellent,
    }
}


