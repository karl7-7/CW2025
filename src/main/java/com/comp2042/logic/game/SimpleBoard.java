package com.comp2042.logic.game;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board { //SimpleBoard implements the core logic of the Tetris game board

    private final int width; //number of rows in the game grid
    private final int height; // number of columns in the game grid
    private final BrickGenerator brickGenerator; //generates random bricks
    private final BrickRotator brickRotator; // handles brick rotation
    private int[][] currentGameMatrix; //the background grid storing merged bricks
    private Point currentOffset; // current brick's x/y position on the board
    private final Score score; //player score object

    public SimpleBoard(int width, int height) { //initialise the board with given dimensions
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height]; // initialise empty board
        brickGenerator = new RandomBrickGenerator(); //create brick generator
        brickRotator = new BrickRotator(); //create rotation handler
        score = new Score(); //initialise score counter
    }

    @Override
    public boolean moveBrickDown() { //moves the current brick down by 1 cell
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        } //returns true if move is successful, false if collision occurs
    }


    @Override
    public boolean moveBrickLeft() { //moves the brick 1 cell to the left
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() { // moves the brick 1 cell to the right
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() { // attempts to rotate the current brick anti-clockwise
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() { // creates a new falling brick at the top of the board
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 10); // initial spawn position
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    } // returns the current background game matrix

    @Override
    public ViewData getViewData() { //packages everything needed by the Gui into a ViewData object
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
    }

    @Override
    public void mergeBrickToBackground() { //merges the current falling brick into the background matrix
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public ClearRow clearRows() { //clears completed rows, updates the matrix, and returns clear info
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    @Override
    public Score getScore() {
        return score;
    } //returns the score object


    @Override
    public void newGame() { //resets the board for a new game
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }
}
