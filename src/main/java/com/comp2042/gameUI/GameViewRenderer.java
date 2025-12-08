package com.comp2042.gameUI;

import com.comp2042.logic.game.ViewData;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GameViewRenderer {
    private static final int BRICK_SIZE = 20;

    private final GridPane gamePanel;
    private final GridPane brickPanel;

    // NEW: Fields for Side Panels
    private GridPane nextBrickPanel;
    private GridPane holdBrickPanel;

    private Rectangle[][] displayMatrix;
    private Rectangle[][] rectangles;
    private Rectangle[][] ghostRectangles;

    public GameViewRenderer(GridPane gamePanel, GridPane brickPanel) {
        this.gamePanel = gamePanel;
        this.brickPanel = brickPanel;
    }

    // NEW: Setter for Side Panels
    public void setSidePanels(GridPane nextBrickPanel, GridPane holdBrickPanel) {
        this.nextBrickPanel = nextBrickPanel;
        this.holdBrickPanel = holdBrickPanel;
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        int rows = brick.getBrickData().length;
        int cols = brick.getBrickData()[0].length;
        rectangles = new Rectangle[rows][cols];
        ghostRectangles = new Rectangle[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Ghost Brick
                Rectangle ghostRect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                ghostRect.getStyleClass().add("brick-ghost");
                ghostRectangles[i][j] = ghostRect;
                brickPanel.add(ghostRect, j, i);

                // Real Brick
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                setRectangleData(brick.getBrickData()[i][j], rectangle);
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        updateBrickPanelPosition(brick);

        // NEW: Draw side panels initially
        updateSidePanels(brick);
    }

    public void refreshBrick(ViewData brick) {
        updateBrickPanelPosition(brick);
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                int brickColor = brick.getBrickData()[i][j];
                setRectangleData(brickColor, rectangles[i][j]);

                if (brickColor != 0) {
                    ghostRectangles[i][j].setVisible(true);
                    int yOffset = (brick.getGhostY() - brick.getyPosition()) * (BRICK_SIZE + 1);
                    ghostRectangles[i][j].setTranslateY(yOffset);
                } else {
                    ghostRectangles[i][j].setVisible(false);
                }
            }
        }
        // NEW: Refresh side panels every move
        updateSidePanels(brick);
    }

    // NEW: Logic to draw mini grids
    private void updateSidePanels(ViewData brick) {
        if (nextBrickPanel != null && brick.getNextBrickData() != null) {
            drawSmallGrid(nextBrickPanel, brick.getNextBrickData());
        }
        if (holdBrickPanel != null) {
            if (brick.getHeldBrickData() != null) {
                drawSmallGrid(holdBrickPanel, brick.getHeldBrickData());
            } else {
                holdBrickPanel.getChildren().clear();
            }
        }
    }

    private void drawSmallGrid(GridPane pane, int[][] data) {
        pane.getChildren().clear();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != 0) {
                    Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    setRectangleData(data[i][j], rect);
                    pane.add(rect, j, i);
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void updateBrickPanelPosition(ViewData brick) {
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private Paint getFillColor(int i) {
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