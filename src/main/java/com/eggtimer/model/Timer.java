package com.eggtimer.model;

public class Timer {

    private int remainingSeconds;

    public Timer(int minutes) {
        this.remainingSeconds = minutes * 60;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
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