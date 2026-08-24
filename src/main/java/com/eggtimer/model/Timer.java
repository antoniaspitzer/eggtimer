package com.eggtimer.model;

public class Timer {

    private int remainingSeconds;
    private int minutes;

    public Timer(int minutes) {
        this.remainingSeconds = minutes * 60;
        this.minutes = minutes;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void resetTimer() {
        this.remainingSeconds = minutes * 60;
    }

    public void tick() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
        }
    }

    public boolean isFinished() {
        return remainingSeconds == 0;
    }
}