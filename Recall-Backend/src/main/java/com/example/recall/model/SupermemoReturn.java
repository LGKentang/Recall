package com.example.recall.model;

public class SupermemoReturn {
    private int repetition;
    private double easinessFactor;
    private int interval;


    public SupermemoReturn(int repetition, double easinessFactor, int interval) {
        this.repetition = repetition;
        this.easinessFactor = easinessFactor;
        this.interval = interval;
    }


    /**
     * @return int return the repetition
     */
    public int getRepetition() {
        return repetition;
    }

    /**
     * @param repetition the repetition to set
     */
    public void setRepetition(int repetition) {
        this.repetition = repetition;
    }

    /**
     * @return double return the easinessFactor
     */
    public double getEasinessFactor() {
        return easinessFactor;
    }

    /**
     * @param easinessFactor the easinessFactor to set
     */
    public void setEasinessFactor(double easinessFactor) {
        this.easinessFactor = easinessFactor;
    }

    /**
     * @return int return the interval
     */
    public int getInterval() {
        return interval;
    }

    /**
     * @param interval the interval to set
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

}
