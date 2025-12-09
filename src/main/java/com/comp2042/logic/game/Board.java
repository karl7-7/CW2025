package com.comp2042.logic.game;

/**
 * Represents the game board API used by the game controller.
 *
 * <p>Implementations manage the board matrix, the currently active brick,
 * possible moves (left/right/down/rotate), spawning new bricks, merging the
 * active brick into the background, clearing full rows and tracking score.
 */

public interface Board {
    boolean moveBrickDown();
    boolean moveBrickLeft();
    boolean moveBrickRight();
    boolean rotateLeftBrick();
    boolean createNewBrick();
    int[][] getBoardMatrix();
    ViewData getViewData();
    void mergeBrickToBackground();
    ClearRow clearRows();
    Score getScore();
    void newGame();

    /**
     * Instantly drop the active piece to its landing position.
     */
    void hardDrop();

    /**
     * Hold the current active piece (swap with held piece). Implementations
     * typically restrict holding to once per spawned piece.
     */

    void holdPiece(); // NEW;
}