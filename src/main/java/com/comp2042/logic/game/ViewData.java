package com.comp2042.logic.game;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    // These must be final and initialized in the constructor
    private final int[][] heldBrickData;
    private final int ghostY;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] heldBrickData, int ghostY) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;

        this.heldBrickData = heldBrickData;
        this.ghostY = ghostY;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    // Getter for Held Brick
    public int[][] getHeldBrickData() {
        if (heldBrickData == null) {
            return null;
        }
        return MatrixOperations.copy(heldBrickData);
    }

    // Getter for Ghost Y
    public int getGhostY() {
        return ghostY;
    }
}