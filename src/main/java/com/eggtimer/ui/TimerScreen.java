package com.eggtimer.ui;

import com.eggtimer.EggTimerApp;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TimerScreen extends VBox {

    public TimerScreen(EggTimerApp app) {

        Label timerLabel = new Label("25:00");

        getChildren().add(timerLabel);
    }
}