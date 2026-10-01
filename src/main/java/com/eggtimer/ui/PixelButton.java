package com.eggtimer.ui;

import javafx.scene.control.Button;
import javafx.scene.image.Image;

public class PixelButton extends Button {

    private final Image standardButton = new Image(
        getClass()
            .getResource("/images/functional/timer_background.png")
            .toExternalForm()
    );

    private final Image pressedButton = new Image(
        getClass()
            .getResource("/images/functional/button.png")
            .toExternalForm()
    );

    public PixelButton(String text) {
        super(text);

        setPrefWidth(100);
        setPrefHeight(40);

        // Normaler Button
        setStyle(
            "-fx-background-image: url('" + standardButton.getUrl() + "');" +
            "-fx-background-size: 100% 100%;" +
            "-fx-background-repeat: no-repeat;" +
            "-fx-background-color: transparent;" +
            "-fx-text-fill: white;"
        );

        // Wenn der Button gedrückt wird
        setOnMousePressed(event -> {

            setStyle(
                "-fx-background-image: url('" + pressedButton.getUrl() + "');" +
                "-fx-background-size: 100% 100%;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;"
            );
        });

        // Wenn man loslässt
        setOnMouseReleased(event -> {

            setStyle(
                "-fx-background-image: url('" + standardButton.getUrl() + "');" +
                "-fx-background-size: 100% 100%;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-color: transparent;" +
                "-fx-text-fill: white;"
            );
        });
    }
}