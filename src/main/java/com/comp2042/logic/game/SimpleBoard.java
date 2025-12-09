package com.comp2042.logic.game;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

/**
 * Simple implementation of the {@link Board} interface representing the Tetris game board.
 *
 * <p>Responsibilities:
 * <ul>
 *     <li>Manage the board matrix and active brick position/rotation via {@link BrickRotator}.</li>
 *     <li>Provide move/rotate/hold/hard-drop operations and merging/clearing rows.</li>
 *     <li>Track score via {@link Score}.</li>
 * </ul>
 */

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    private Brick heldBrick;
    private Brick currentBrickObj;
    private boolean canHold = true;

    /**
     * Create a SimpleBoard with the given dimensions.
     *
     * @param width  board width (columns)
     * @param height board height (rows)
     */

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    /**
     * Attempt to move the active brick one cell down. Returns false if movement would collide.
     *
     * @return true when movement succeeded, false when blocked
     */

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /**
     * Attempt to move the active brick one cell left.
     *
     * @return true when movement succeeded, false when blocked
     */

    @Override
    public boolean moveBrickLeft() {
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

    /**
     * Attempt to move the active brick one cell right.
     *
     * @return true when movement succeeded, false when blocked
     */

    @Override
    public boolean moveBrickRight() {
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

    /**
     * Rotate the active brick to its next rotation if possible.
     *
     * @return true when rotation succeeded, false when blocked
     */

    @Override
    public boolean rotateLeftBrick() {
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

    /**
     * Create a new active brick and reset hold permission.
     *
     * @return true if the new brick immediately collides (game over), false otherwise
     */

    @Override
    public boolean createNewBrick() {
        canHold = true; // Reset hold permission
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 0); // Changed to 0 to spawn at very top
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }


    /**
     * Get the current background board matrix.
     *
     * @return 2D int matrix representing placed blocks
     */

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    /**
     * Build a {@link ViewData} object representing current view state including
     * current brick shape, offsets, next brick, held brick and ghost Y.
     *
     * @return view data snapshot
     */

    @Override
    public ViewData getViewData() {
        return new ViewData(
                brickRotator.getCurrentShape(),
                (int) currentOffset.getX(),
                (int) currentOffset.getY(),
                brickGenerator.getNextBrick().getShapeMatrix().get(0),
                heldBrick != null ? heldBrick.getShapeMatrix().get(0) : null, // Pass held brick data
                getGhostY() // Pass ghost Y
        );
    }

    /**
     * Hold the current piece. Only allowed when canHold is true; after holding,
     * canHold is set to false until a new piece is spawned.
     */

    @Override
    public void holdPiece() {
        if (!canHold) return;

        if (heldBrick == null) {
            heldBrick = currentBrickObj;
            createNewBrick();
        } else {
            Brick temp = currentBrickObj;
            currentBrickObj = heldBrick;
            heldBrick = temp;
            brickRotator.setBrick(currentBrickObj);
            currentOffset = new Point(4, 0);
        }
        canHold = false;
    }

    /**
     * Merge the active brick into the background matrix.
     */

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /**
     * Remove full rows from the background matrix and return information describing the removal.
     *
     * @return ClearRow instance with new matrix and lines removed information
     */

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;
    }

    /**
     * Get the {@link Score} instance used by this board.
     *
     * @return score object
     */

    @Override
    public Score getScore() {
        return score;
    }

    /**
     * Reset the board to an initial empty state and reset score.
     */

    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }

    // NEW: Calculate where the ghost piece should be

    /**
     * Compute the Y coordinate of the ghost (the row where the active piece would land).
     *
     * @return Y coordinate for ghost position
     */

    private int getGhostY() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        int[][] shape = brickRotator.getCurrentShape();
        int x = (int) currentOffset.getX();
        int y = (int) currentOffset.getY();

        while (true) {
            y++;
            if (MatrixOperations.intersect(currentMatrix, shape, x, y)) {
                return y - 1; // Found collision, return previous valid Y
            }
        }
    }

    // NEW: Instantly drop piece to ghost position

    /**
     * Instantly move the active piece to the ghost Y position.
     */

    @Override
    public void hardDrop() {
        int y = getGhostY();
        currentOffset.setLocation(currentOffset.getX(), y);
    }


}
