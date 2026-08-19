package com.eggtimer.ui;

import com.eggtimer.EggTimerApp;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class StartScreen extends VBox {

    public StartScreen(EggTimerApp app) {

        Button startButton = new Button("START");

        startButton.setOnAction(e -> {
            app.showTimerScreen();
        });

        getChildren().add(startButton);
    }
}