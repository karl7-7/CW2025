package com.comp2042;

public final class ViewData { //ViewData is an immutable data container that stores everything

    private final int[][] brickData; // 2D matrix representing the shape of the current falling brick
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData; // 2D matrix representing the next brick (for the preview window)

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) { // constructs a new ViewData object
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
    }

    public int[][] getBrickData() { //returns a copy of the current brick data
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition; // returns the x-coordinate of the brick on the game board
    }

    public int getyPosition() {
        return yPosition; // returns the y-coordinate of the brick on the game board
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData); //returns a copy of the next brick matrix for the preview display
    }
}
