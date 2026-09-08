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

    private class Wall {
        int x;
        int gapY;
        boolean scored;

        Wall(int x, int gapY) {
            this.x = x;
            this.gapY = gapY;
            this.scored = false;
        }
    }

    private Tile flappyBird;
    private ArrayList<Wall> walls;

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
        random = new Random();

        flappyBird = new Tile(12, 9);
        walls = new ArrayList<>();

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
                Duration.millis(200),
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

            if (key == KeyCode.SPACE) {
                if (!gameStarted) {
                    gameStarted = true;
                    infoLabel.setText("");
                }
                jump();
            }
        });
    }

    private void move() {
        if (gameOver) {
            return;
        }

        // gravity
        velocityY += 1;

        // move birdb
        flappyBird.y += velocityY;

        // jump
        if (flappyBird.y < 0) {
            flappyBird.y = 0;
            velocityY = 0;
        }

        // bottom collision 
        if (flappyBird.y >= BOARD_HEIGHT / TILE_SIZE - 1) {
            gameOver = true;

            stopGame();

            infoLabel.setText("Ooops! Try again!");

            restartAfterDelay();

            return;
        }

        // move the wallls
        for (Wall wall : walls) {
            wall.x--;
        }

        checkScore();

        if (walls.isEmpty() || walls.get(walls.size() - 1).x < 15) {
            placeWall();
        }

        walls.removeIf(wall -> wall.x < -2);

        checkCollision();
    }

    private void jump() {

        if (gameOver) {
            return;
        }

        velocityY = -2;
    }

    private void placeWall() {
        int gapY = 3 + random.nextInt(5);

        walls.add(new Wall(
            BOARD_WIDTH / TILE_SIZE,
            gapY
        ));
    }

    private void draw() {
        gameBoard.getChildren().clear();

        // PET
        Rectangle bird = new Rectangle(
            TILE_SIZE,
            TILE_SIZE,
            FLAPPY_BIRD_COLOR
        );

        bird.setX(flappyBird.x * TILE_SIZE);
        bird.setY(flappyBird.y * TILE_SIZE);

        gameBoard.getChildren().add(bird);

        // WALLSS
        for (Wall wall : walls) {
            // top wall
            for (int y = 0; y < wall.gapY; y++) {

                Rectangle block = new Rectangle(
                    TILE_SIZE,
                    TILE_SIZE,
                    WALL_COLOR
                );

                block.setX(wall.x * TILE_SIZE);
                block.setY(y * TILE_SIZE);

                gameBoard.getChildren().add(block);
            }

            // botttom wall
            for (int y = wall.gapY + 3; y < BOARD_HEIGHT / TILE_SIZE; y++) {

                Rectangle block = new Rectangle(TILE_SIZE, TILE_SIZE, WALL_COLOR);

                block.setX(wall.x * TILE_SIZE);
                block.setY(y * TILE_SIZE);

                gameBoard.getChildren().add(block);
            }
        }
    }
    
    private void checkCollision() {
        for (Wall wall : walls) {
            if (flappyBird.x == wall.x) {
                if(
                    flappyBird.y < wall.gapY ||
                    flappyBird.y > wall.gapY + 2
                ) {
                    gameOver = true;
                    stopGame();

                    infoLabel.setText("Ooops! Try again!");

                    restartAfterDelay();

                    return;
                }
            }
        }
    }

    private void updateScore() {
        scoreLabel.setText(score + " / " + MAX_SCORE);
    }

    private void checkScore() {

        for (Wall wall : walls) {

            if (!wall.scored && wall.x < flappyBird.x) {

                wall.scored = true;

                score++;
                updateScore();

                // Highscore erreicht
                if (score >= MAX_SCORE) {
                    finishGame();
                    return;
                }
            }
        }
    }

    private void finishGame() {

        stopGame();

        gameOver = true;

        if (onGameFinished != null) {
            onGameFinished.run();
        }
    }


    private void stopGame() {

        if (gameLoop != null) {
            gameLoop.stop();
        }
    }


    private void restartAfterDelay() {

        Timeline restartTimer = new Timeline(
                new KeyFrame(
                        Duration.seconds(1.5),
                        event -> restartGame()
                )
        );

        restartTimer.setCycleCount(1);
        restartTimer.play();
    }

    private void restartGame() {

        flappyBird = new Tile(12, 9);
        walls.clear();

        velocityY = 0;

        score = 0;
        gameOver = false;
        gameStarted = false;

        placeWall();
        updateScore();

        infoLabel.setText("Use the space bar!");

        draw();

        startGameLoop();

        requestFocus();
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