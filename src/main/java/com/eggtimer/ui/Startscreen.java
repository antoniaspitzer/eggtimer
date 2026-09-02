package com.eggtimer.ui;

import com.eggtimer.EggTimerApp;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Spinner;
import javafx.scene.control.Label;

public class StartScreen extends AnchorPane {

    public StartScreen(EggTimerApp app) {

        
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

        // WELCOME

        Label welcome = new Label("Welcome to EggTimer!\n\nChoose your Focus Time");

        Spinner<Integer> minutesSpinner = new Spinner<>(1, 60, 25);
        Button startButton = new Button("OK");

        startButton.setOnAction(e -> {
            app.showTimerScreen(minutesSpinner.getValue());
        });

        // LAYOUT

        AnchorPane.setTopAnchor(welcome, 120.0);
        AnchorPane.setLeftAnchor(welcome, 220.0);

        AnchorPane.setTopAnchor(minutesSpinner, 230.0);
        AnchorPane.setLeftAnchor(minutesSpinner, 270.0);

        AnchorPane.setTopAnchor(startButton, 300.0);
        AnchorPane.setLeftAnchor(startButton, 280.0);


        getChildren().addAll(
            frame,

            welcome,
            minutesSpinner,
            startButton);
    }
}