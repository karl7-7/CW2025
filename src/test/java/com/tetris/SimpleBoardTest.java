package com.tetris;

import com.comp2042.logic.game.SimpleBoard;
import com.comp2042.logic.game.ViewData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SimpleBoardTest {

    private SimpleBoard board;
    private final int WIDTH = 10;
    private final int HEIGHT = 20;

    @BeforeEach
    void setUp() {
        // Initialize a standard 10x20 board
        board = new SimpleBoard(WIDTH, HEIGHT);
        board.createNewBrick();
    }

    @Test
    void testBoardInitialization() {
        int[][] matrix = board.getBoardMatrix();
        assertEquals(WIDTH, matrix.length);
        assertEquals(HEIGHT, matrix[0].length);
    }

    @Test
    void testBrickSpawn() {
        ViewData view = board.getViewData();
        assertNotNull(view.getBrickData(), "Current brick data should not be null");
        // Bricks spawn at x=4
        assertEquals(4, view.getxPosition(), "Brick should spawn at X=4");
    }

    @Test
    void testMoveDown() {
        ViewData initial = board.getViewData();
        int initialY = initial.getyPosition();

        boolean moved = board.moveBrickDown();

        assertTrue(moved, "Brick should move down successfully in empty space");
        assertEquals(initialY + 1, board.getViewData().getyPosition(), "Y position should increase by 1");
    }

    @Test
    void testMoveLeft() {
        ViewData initial = board.getViewData();
        int initialX = initial.getxPosition();

        board.moveBrickLeft();

        assertEquals(initialX - 1, board.getViewData().getxPosition(), "X position should decrease by 1");
    }

    @Test
    void testMoveRight() {
        ViewData initial = board.getViewData();
        int initialX = initial.getxPosition();

        board.moveBrickRight();

        assertEquals(initialX + 1, board.getViewData().getxPosition(), "X position should increase by 1");
    }

    @Test
    void testHardDrop() {
        // Get initial Y
        int initialY = board.getViewData().getyPosition();

        // Perform hard drop
        board.hardDrop();

        // The brick should effectively be at the bottom (or ghost position)
        int newY = board.getViewData().getyPosition();

        assertTrue(newY > initialY, "Hard drop should move the brick downwards significantly");
    }

    @Test
    void testNewGameResetsBoard() {
        // Simulate playing: Move down and merge
        board.moveBrickDown();
        board.mergeBrickToBackground();

        // Score some points
        board.getScore().add(100);

        // Start New Game
        board.newGame();

        // Verify Reset
        assertEquals(0, board.getScore().scoreProperty().get(), "Score should reset to 0");

        // Check that the board matrix is cleared (all zeros)
        int[][] matrix = board.getBoardMatrix();
        for (int[] col : matrix) {
            for (int cell : col) {
                assertEquals(0, cell, "Board should be empty after new game");
            }
        }
    }
}
