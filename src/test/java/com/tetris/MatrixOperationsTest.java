package com.tetris;

import com.comp2042.logic.game.MatrixOperations;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void testIntersectTrue() {
        // Create a 5x5 board with a block at (2,2)
        int[][] matrix = new int[5][5];
        matrix[2][2] = 1;

        // Create a shape that hits that block
        int[][] shape = {{1}};

        // Check intersection at (2,2)
        assertTrue(MatrixOperations.intersect(matrix, shape, 2, 2), "Should return true when shapes overlap");
    }

    @Test
    void testIntersectFalse() {
        int[][] matrix = new int[5][5];
        int[][] shape = {{1}};
        // Check intersection at empty spot (0,0)
        assertFalse(MatrixOperations.intersect(matrix, shape, 0, 0), "Should return false for no overlap");
    }

    @Test
    void testIntersectOutOfBounds() {
        int[][] matrix = new int[5][5];
        int[][] shape = {{1}};
        // Check intersection outside the board
        assertTrue(MatrixOperations.intersect(matrix, shape, 5, 0), "Should return true (collision) when out of bounds");
    }

    @Test
    void testMerge() {
        int[][] matrix = new int[5][5];
        int[][] shape = {{2}}; // Shape with value 2

        int[][] result = MatrixOperations.merge(matrix, shape, 1, 1);

        assertEquals(2, result[1][1], "Matrix should contain the merged value at (1,1)");
    }


}
