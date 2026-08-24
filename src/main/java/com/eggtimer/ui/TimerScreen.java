package com.eggtimer.ui;

import com.eggtimer.EggTimerApp;
import com.eggtimer.model.Timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class TimerScreen extends VBox {

    private Timer timer;
    private Label timerLabel;
    private Timeline timeline;

    public TimerScreen(EggTimerApp app, int minutes) {

        timer = new Timer(minutes);

        timerLabel = new Label(
            formatTime(timer.getRemainingSeconds())
        );

        Button startButton = new Button("START");
        Button stopButton = new Button ("PAUSE");

        startButton.setOnAction(e -> {
            startTimer();
            startButton.setText("CONTINUE");
        });

        stopButton.setOnAction(e -> {
            stopTimer();
        });

        getChildren().addAll(
            timerLabel,
            startButton,
            stopButton
        );
    }

    private void startTimer() {

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