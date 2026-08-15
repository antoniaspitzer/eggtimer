package com.eggtimer.controller;

import com.eggtimer.model.Egg;

public class GameController {

    private Egg egg;

    public GameController(Egg egg) {
        this.egg = egg;
    }

    public Pet getEgg() {
        return egg;
    }
}