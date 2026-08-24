package com.eggtimer.ui;

import com.eggtimer.EggTimerApp;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javafx.scene.control.Spinner;
import javafx.scene.control.Label;

public class StartScreen extends VBox {

    public StartScreen(EggTimerApp app) {

        Label welcome = new Label("Welcome to EggTimer!\n\nChoose your Focus Time");

        Spinner<Integer> minutesSpinner = new Spinner<>(1, 60, 25);
        Button startButton = new Button("OK");

        startButton.setOnAction(e -> {
            app.showTimerScreen(minutesSpinner.getValue());
        });

        getChildren().addAll(
            welcome,
            minutesSpinner,
            startButton);
    }
}