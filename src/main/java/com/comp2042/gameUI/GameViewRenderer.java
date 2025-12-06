package com.comp2042.gameUI;

import com.comp2042.logic.game.ViewData;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GameViewRenderer
{ // This class is responsible for rendering the game view including the game board and the current brick
    private static final int BRICK_SIZE = 20;

    private final GridPane gamePanel;
    private final GridPane brickPanel;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;

    public GameViewRenderer(GridPane gamePanel, GridPane brickPanel) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) { // Initializes the game view with the board matrix and the current brick
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length]; // Initialize display matrix for the game board
        for (int i = 2; i < boardMatrix.length; i++) { // Iterate through the board matrix starting from row 2
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2); // Add rectangle to the game panel at the correct position
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length]; // Initialize rectangles for the current brick

        for (int i = 0; i < brick.getBrickData().length; i++) { // Iterate through the brick data to create rectangles
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i); // Add rectangle to the brick panel at the correct position
            }
        }

        updateBrickPanelPosition(brick);
    }

    public void refreshBrick(ViewData brick) { // Refreshes the current brick's position and appearance
        updateBrickPanelPosition(brick);
        for (int i = 0; i < brick.getBrickData().length; i++) { // Iterate through the brick data to update rectangles
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
            }
        }
    }

    public void refreshGameBackground(int[][] board) { // Refreshes the game board's appearance based on the current board matrix
        for (int i = 2; i < board.length; i++) { // Iterate through the board matrix starting from row 2
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void updateBrickPanelPosition(ViewData brick) { // Updates the position of the brick panel based on the brick's position
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
    }

    private void setRectangleData(int color, Rectangle rectangle) { // Sets the color and appearance of a rectangle based on the provided color code
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private Paint getFillColor(int i) { // Maps integer color codes to actual Color objects
        switch (i) {
            case 0: return Color.TRANSPARENT;
            case 1: return Color.AQUA;
            case 2: return Color.BLUEVIOLET;
            case 3: return Color.DARKGREEN;
            case 4: return Color.YELLOW;
            case 5: return Color.RED;
            case 6: return Color.BEIGE;
            case 7: return Color.BURLYWOOD;
            default: return Color.WHITE;
        }
    }
}
