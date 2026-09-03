package com.eggtimer.minigame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class FlappyPetGame extends StackPane {

    private static final int BOARD_WIDTH = 380;
    private static final int BOARD_HEIGHT = 240;
    private static final int TILE_SIZE = 20;
    private static final int MAX_SCORE = 5;

    private static final Color FLAPPY_BIRD_COLOR = Color.web("#E88FB3");
    private static final Color WALL_COLOR = Color.web("#E85D8C");

    private class Tile {
        int x, y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private Tile flappyBird;
    private ArrayList<Tile> wall;

    private Random random;
    private Timeline gameLoop;

    private int velocityX;
    private int velocityY;
    private int score;

    private boolean gameOver;
    private boolean gameStarted;

    private Pane gameBoard;
    private Label scoreLabel;
    private Label infoLabel;

    private Runnable onGameFinished;

    public FlappyPetGame(Runnable onGameFinished) {
        
        this.onGameFinished = onGameFinished;

        setPrefSize(640, 480);
        setFocusTraversable(true);

        createUI();
        initializeGame();
        setupKeyboard();
        startGameLoop();

        Platform.runLater(this::requestFocus);
    }

    private void createUI() {

        // Game board
        gameBoard = new Pane();

        gameBoard.setPrefSize(
                BOARD_WIDTH,
                BOARD_HEIGHT
        );

        gameBoard.setMinSize(
                BOARD_WIDTH,
                BOARD_HEIGHT
        );

        gameBoard.setMaxSize(
                BOARD_WIDTH,
                BOARD_HEIGHT
        );

        gameBoard.setStyle(
                "-fx-background-color: #fff3bb;" +
                "-fx-border-color: #8c6d0f;" +
                "-fx-border-width: 3px;"
        );

        // Score
        scoreLabel = new Label("0 / " + MAX_SCORE);

        scoreLabel.setStyle("""
                -fx-text-fill: #8c6d0f;
                -fx-font-size: 22px;
                -fx-font-weight: bold;
                -fx-background-color: #fff3bb;
                -fx-padding: 5px 15px;
                -fx-border-color: #8c6d0f;
                -fx-border-width: 2px;
                """);
        
        StackPane.setAlignment(
                scoreLabel,
                Pos.TOP_CENTER
        );

        // Info
        infoLabel = new Label("Use the space bar!");

        infoLabel.setStyle("""
                -fx-text-fill: #8c6d0f;
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                """);

        StackPane.setAlignment(
                infoLabel,
                Pos.BOTTOM_CENTER
        );

        getChildren().addAll(
            gameBoard,
            scoreLabel,
            infoLabel
        );
    }

    private void initializeGame() {

        flappyBird = new Tile(12, 9);

        wall = new ArrayList<>(5,5);

        velocityX = 0;
        velocityY = 0;

        score = 0;
        gameOver = false;
        gameStarted = false;

        placeWall();
        updateScore();
        draw();
    }

    private void startGameLoop() {

        gameLoop = new Timeline(
            new KeyFrame(
                Duration.millis(120),
                event -> {
                    
                    if (gameStarted) {
                        move();
                        draw();
                    }
                }
            )
        );

        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();
    }

    private void setupKeyboard() {

        setOnKeyPressed(event -> {
            
            KeyCode key = event.getCode();

            if (key == KeyCode.space) {
                jump();
            }

            gameStarted = true;
            infoLabel.setText("");
        });
    }

}


// first we have to get a single tile (player)
// everytime you tap/you press the space bar
// it will flap its arms down and back up

// we have to get kind of a small physics engine 
// bzw the pet/image should fall always 
// the same (using a graph?)

// there will be vertical 10 "blocks"
// and there will be a random number
// divided through 10 to get one of the
// blocks not filled up, so the pet 
// can get through it!

// every zb 50 px there will be an
// vertical block thing

// for the game loop there will be some
// kind of method so the "camera" will keep
// continously move right