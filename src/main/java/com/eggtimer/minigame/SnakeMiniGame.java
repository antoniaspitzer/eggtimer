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

public class SnakeMiniGame extends StackPane {

    private static final int BOARD_WIDTH = 380;
    private static final int BOARD_HEIGHT = 240;
    private static final int TILE_SIZE = 20;
    private static final int MAX_SCORE = 5;

    private static final Color SNAKE_HEAD_COLOR = Color.web("#dec349");
    private static final Color SNAKE_BODY_COLOR = Color.web("#f5e291");
    private static final Color FOOD_COLOR = Color.web("#867114");

    private class Tile {
        int x, y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private Tile snakeHead;
    private ArrayList<Tile> snakeBody;
    private Tile food;

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


    public SnakeMiniGame(Runnable onGameFinished) {

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
        infoLabel = new Label("Use the arrow keys!");

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

        snakeHead = new Tile(12, 9);
        snakeBody = new ArrayList<>();

        food = new Tile(5, 5);
        random = new Random();

        velocityX = 0;
        velocityY = 0;

        score = 0;
        gameOver = false;
        gameStarted = false;

        placeFood();
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

            if (key == KeyCode.UP && velocityY != 1) {

                velocityX = 0;
                velocityY = -1;

            } else if (key == KeyCode.DOWN && velocityY != -1) {

                velocityX = 0;
                velocityY = 1;

            } else if (key == KeyCode.LEFT && velocityX != 1) {

                velocityX = -1;
                velocityY = 0;

            } else if (key == KeyCode.RIGHT && velocityX != -1) {

                velocityX = 1;
                velocityY = 0;

            } else {
                return;
            }

            gameStarted = true;
            infoLabel.setText("");
        });
    }


    private void move() {

        if (gameOver) {
            return;
        }

        // Food eaten
        if (collision(snakeHead, food)) {

            snakeBody.add(
                    new Tile(food.x, food.y)
            );

            score++;
            updateScore();

            if (score >= MAX_SCORE) {
                finishGame();
                return;
            }

            placeFood();
        }


        // Move body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {

            Tile part = snakeBody.get(i);

            if (i == 0) {

                part.x = snakeHead.x;
                part.y = snakeHead.y;

            } else {

                Tile previous = snakeBody.get(i - 1);

                part.x = previous.x;
                part.y = previous.y;
            }
        }


        // Move head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;


        // Body collision
        for (Tile part : snakeBody) {

            if (collision(snakeHead, part)) {

                gameOver = true;
                stopGame();

                infoLabel.setText("Oops! Try again!");

                restartAfterDelay();

                return;
            }
        }


        // Wall collision
        if (
                snakeHead.x < 0 ||
                snakeHead.x >= BOARD_WIDTH / TILE_SIZE ||
                snakeHead.y < 0 ||
                snakeHead.y >= BOARD_HEIGHT / TILE_SIZE
        ) {

            gameOver = true;
            stopGame();

            infoLabel.setText("Oops! Try again!");

            restartAfterDelay();
        }
    }


    private void placeFood() {

        do {

            food.x = random.nextInt(
                    BOARD_WIDTH / TILE_SIZE
            );

            food.y = random.nextInt(
                    BOARD_HEIGHT / TILE_SIZE
            );

        } while (isSnakePosition(food));
    }


    private boolean isSnakePosition(Tile tile) {

        if (collision(snakeHead, tile)) {
            return true;
        }

        for (Tile part : snakeBody) {

            if (collision(part, tile)) {
                return true;
            }
        }

        return false;
    }


    private boolean collision(Tile tile1, Tile tile2) {

        return tile1.x == tile2.x &&
                tile1.y == tile2.y;
    }


    private void draw() {

        gameBoard.getChildren().clear();


        // Food
        Rectangle foodRect = new Rectangle(
                TILE_SIZE - 4,
                TILE_SIZE - 4
        );

        foodRect.setFill(FOOD_COLOR);
        foodRect.setArcWidth(8);
        foodRect.setArcHeight(8);

        foodRect.setX(
                food.x * TILE_SIZE + 2
        );

        foodRect.setY(
                food.y * TILE_SIZE + 2
        );


        // Snake head
        Rectangle headRect = new Rectangle(
                TILE_SIZE - 4,
                TILE_SIZE - 4
        );

        headRect.setFill(SNAKE_HEAD_COLOR);
        headRect.setArcWidth(6);
        headRect.setArcHeight(6);

        headRect.setX(
                snakeHead.x * TILE_SIZE + 2
        );

        headRect.setY(
                snakeHead.y * TILE_SIZE + 2
        );


        gameBoard.getChildren().addAll(
                foodRect,
                headRect
        );


        // Snake body
        for (Tile part : snakeBody) {

            Rectangle bodyRect = new Rectangle(
                    TILE_SIZE - 4,
                    TILE_SIZE - 4
            );

            bodyRect.setFill(SNAKE_BODY_COLOR);
            bodyRect.setArcWidth(6);
            bodyRect.setArcHeight(6);

            bodyRect.setX(
                    part.x * TILE_SIZE + 2
            );

            bodyRect.setY(
                    part.y * TILE_SIZE + 2
            );

            gameBoard.getChildren().add(bodyRect);
        }
    }


    private void updateScore() {

        scoreLabel.setText(
                score + " / " + MAX_SCORE
        );
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

        snakeHead = new Tile(12, 9);
        snakeBody.clear();

        velocityX = 0;
        velocityY = 0;

        score = 0;
        gameOver = false;
        gameStarted = false;

        placeFood();
        updateScore();

        infoLabel.setText("Use the arrow keys!");

        draw();

        startGameLoop();

        requestFocus();
    }
}