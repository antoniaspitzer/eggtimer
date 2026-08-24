package com.eggtimer.ui;

import com.eggtimer.EggTimerApp;

import javafx.scene.layout.VBox;

import javafx.scene.control.Label;
import javafx.scene.control.Button;


public class PauseScreen extends VBox {
    public PauseScreen(EggTimerApp app, Runnable onPlay) {

        Label label = new Label("You did great! Play some Minigames! :D");

        Button playSnake = new Button("Play Snake");

        playSnake.setOnAction(e -> {
            onPlay.run();
        });

        getChildren().addAll(
            label,
            playSnake
        );
    }
}