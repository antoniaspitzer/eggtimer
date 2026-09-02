package com.eggtimer.ui;

import com.eggtimer.EggTimerApp;

import javafx.scene.layout.AnchorPane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.control.Button;


public class PauseScreen extends AnchorPane {
    public PauseScreen(EggTimerApp app, Runnable onPlay) {

        // BACKGROUND

        Image frameImage = new Image(
                getClass()
                    .getResource("/images/frame.png")
                    .toExternalForm()
        );

        ImageView frame = new ImageView(frameImage);

        frame.setSmooth(false);
        frame.setFitWidth(640);
        frame.setFitHeight(480);

        // label

        Label label = new Label("You did great! Play some Minigames! :D");

        Button playSnake = new Button("Play Snake");

        playSnake.setOnAction(e -> {
            onPlay.run();
        });

        // LAYOUT

        AnchorPane.setTopAnchor(label, 180.0);
        AnchorPane.setLeftAnchor(label, 190.0);

        AnchorPane.setTopAnchor(playSnake, 240.0);
        AnchorPane.setLeftAnchor(playSnake, 270.0);

        getChildren().addAll(
            frame,

            label,
            playSnake
        );
    }
}