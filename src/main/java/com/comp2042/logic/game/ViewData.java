package com.comp2042.logic.game;

/**
 * Immutable DTO that represents the visual state of the currently active brick and related UI data.
 *
 * <p>Contains:
 * <ul>
 *     <li>Current brick matrix and its X/Y offsets.</li>
 *     <li>Next brick matrix used for the small preview.</li>
 *     <li>Held brick matrix (may be null) and computed ghost Y position.</li>
 * </ul>
 */

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;

    // These must be final and initialized in the constructor
    private final int[][] heldBrickData;
    private final int ghostY;


    /**
     * Create a ViewData snapshot.
     *
     * @param brickData     matrix of the active brick
     * @param xPosition     x offset for the active brick
     * @param yPosition     y offset for the active brick
     * @param nextBrickData matrix for the next brick preview
     * @param heldBrickData matrix for the held brick preview (may be null)
     * @param ghostY        Y coordinate for the ghost piece
     */

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int[][] heldBrickData, int ghostY) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;

        this.heldBrickData = heldBrickData;
        this.ghostY = ghostY;
    }

    /**
     * Return a defensive copy of the brick matrix.
     *
     * @return copied matrix
     */

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * X offset of the active brick.
     *
     * @return x position
     */

    public int getxPosition() {
        return xPosition;
    }

    /**
     * Y offset of the active brick.
     *
     * @return y position
     */

    public int getyPosition() {
        return yPosition;
    }


    /**
     * Return a defensive copy of the next-brick matrix.
     *
     * @return next brick data
     */

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Return a defensive copy of the held brick matrix, or null if none.
     *
     * @return held brick data or null
     */

    public int[][] getHeldBrickData() {
        if (heldBrickData == null) {
            return null;
        }
        return MatrixOperations.copy(heldBrickData);
    }


    /**
     * Return the precomputed ghost Y coordinate.
     *
     * @return ghost Y
     */
    public int getGhostY() {
        return ghostY;
    }
}