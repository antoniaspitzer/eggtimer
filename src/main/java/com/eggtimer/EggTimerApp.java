package com.eggtimer;

import com.eggtimer.ui.StartScreen;
import com.eggtimer.ui.TimerScreen;
import com.eggtimer.ui.PauseScreen;

import com.eggtimer.minigame.SnakeMiniGame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EggTimerApp extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        showStartScreen();

        stage.show();
    }

    public void showStartScreen() {
        StartScreen startScreen = new StartScreen(this);

        Scene scene = new Scene(startScreen, 640, 480);

        stage.setScene(scene);
    }

    public void showTimerScreen(int minutes) {
        TimerScreen timerScreen = new TimerScreen(this, minutes);

        Scene scene = new Scene(timerScreen, 640, 480);

        stage.setScene(scene);
    }

    public void showPauseScreen() {
        PauseScreen pauseScreen = new PauseScreen(this, () -> {showSnakeMiniGame();});

        Scene scene = new Scene(pauseScreen, 640, 480);
        
        stage.setScene(scene);
    }

    public void showSnakeMiniGame() {
        SnakeMiniGame snakeMiniGame = 
                    new SnakeMiniGame(() -> {
                        showPauseScreen();
                    });
    
        Scene scene = new Scene(
                snakeMiniGame,
                640,
                480
        );

        stage.setScene(scene);

        // Wichtig für die Pfeiltasten!
        snakeMiniGame.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}