package com.eggtimer.ui;

import com.eggtimer.EggTimerApp;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import javafx.scene.control.Spinner;

public class StartScreen extends VBox {

    public StartScreen(EggTimerApp app) {

        Spinner<Integer> minutesSpinner = new Spinner<>(1, 60, 25);
        Button startButton = new Button("OK");

        startButton.setOnAction(e -> {
            app.showTimerScreen(minutesSpinner.getValue());
        });

        getChildren().addAll(
            minutesSpinner,
            startButton);
    }
}