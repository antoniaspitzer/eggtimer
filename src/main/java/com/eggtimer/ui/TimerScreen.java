package com.eggtimer.ui;

import com.eggtimer.EggTimerApp;
import com.eggtimer.model.Timer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class TimerScreen extends AnchorPane {

    private Timer timer;
    private Label timerLabel;
    private Timeline timeline;

    public TimerScreen(EggTimerApp app, int minutes) {

        
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

        // TIMER

        timer = new Timer(minutes);

        timerLabel = new Label(
            formatTime(timer.getRemainingSeconds())
        );

        Button startButton = new Button("START");
        Button stopButton = new Button ("PAUSE");
        Button resetButton = new Button ("RESET");

        startButton.setOnAction(e -> {
            startTimer(app);
            startButton.setText("CONTINUE");
        });

        stopButton.setOnAction(e -> {
            stopTimer();
        });

        resetButton.setOnAction(e -> {
            stopTimer();
            timer.resetTimer();
            timerLabel.setText(formatTime(timer.getRemainingSeconds()));
        });

        AnchorPane.setTopAnchor(timerLabel, 150.0);
        AnchorPane.setLeftAnchor(timerLabel, 270.0);

        AnchorPane.setTopAnchor(startButton, 250.0);
        AnchorPane.setLeftAnchor(startButton, 270.0);

        AnchorPane.setTopAnchor(stopButton, 300.0);
        AnchorPane.setLeftAnchor(stopButton, 270.0);

        AnchorPane.setTopAnchor(resetButton, 350.0);
        AnchorPane.setLeftAnchor(resetButton, 270.0);

        getChildren().addAll(
            frame,

            timerLabel,
            startButton,
            stopButton,
            resetButton
        );
    }

    private void startTimer(EggTimerApp app) {

        timeline = new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                e -> {

                    timer.tick();

                    timerLabel.setText(
                        formatTime(timer.getRemainingSeconds())
                    );

                    if (timer.isFinished()) {
                        timeline.stop();
                        app.showPauseScreen();
                    }
                }
            )
        );

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    private void stopTimer() {
        timeline.stop();
    }

    private String formatTime(int seconds) {

        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;

        return String.format(
            "%02d:%02d",
            minutes,
            remainingSeconds
        );
    }
}