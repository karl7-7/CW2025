package com.comp2042.logic.game;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class with static helpers for common operations on matrix representations
 * of the board and bricks.
 *
 * <p>Provided operations include: intersection checks, deep copy, merge a brick into
 * the board, row removal detection and deep-copying lists of matrices.
 */

public class MatrixOperations { //This is a utility class that performs operations on 2d integer arrays



    private MatrixOperations(){

    }

    /**
     * Check whether the given brick placed at (x,y) intersects the board or is out-of-bounds.
     *
     * @param matrix board matrix (rows x cols)
     * @param brick  brick matrix
     * @param x      x offset (column)
     * @param y      y offset (row)
     * @return true if a collision or out-of-bounds would occur
     */

    public static boolean intersect(final int[][] matrix, final int[][] brick, int x, int y) { //checks if a brick placed at (x,y) intersects with the board of goes out of bounds
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0 && (checkOutOfBound(matrix, targetX, targetY) || matrix[targetY][targetX] != 0)) {
                    return true; //collision detected
                }
            }
        }
        return false;
    }

    private static boolean checkOutOfBound(int[][] matrix, int targetX, int targetY) { //checks whether a given position is outside the board
        boolean returnValue = true;
        if (targetX >= 0 && targetY < matrix.length && targetX < matrix[targetY].length) {
            returnValue = false; //inside bounds so set flag to false
        }
        return returnValue;
    }

    /**
     * Deep copy a 2D integer array.
     *
     * @param original source matrix
     * @return new independent copy
     */

    public static int[][] copy(int[][] original) { //deep copies a 2d array
        int[][] myInt = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            int[] aMatrix = original[i];
            int aLength = aMatrix.length;
            myInt[i] = new int[aLength];
            System.arraycopy(aMatrix, 0, myInt[i], 0, aLength);
        }
        return myInt;
    }

    /**
     * Merge a brick into a copy of the filledFields matrix at the given offset,
     * returning the merged matrix.
     *
     * @param filledFields background matrix
     * @param brick        brick matrix
     * @param x            x offset (column)
     * @param y            y offset (row)
     * @return new matrix with the brick merged
     */

    public static int[][] merge(int[][] filledFields, int[][] brick, int x, int y) { //merges a brick into the board at (x,y) and returns a new matrix
        int[][] copy = copy(filledFields);
        for (int i = 0; i < brick.length; i++) {
            for (int j = 0; j < brick[i].length; j++) {
                int targetX = x + i;
                int targetY = y + j;
                if (brick[j][i] != 0) {
                    copy[targetY][targetX] = brick[j][i];
                }
            }
        }
        return copy;
    }

    /**
     * Detect and remove full rows from the matrix. Returns a {@link ClearRow}
     * containing the number of removed lines, the new matrix and the score bonus.
     *
     * @param matrix source board matrix
     * @return ClearRow describing the removal result
     */

    public static ClearRow checkRemoving(final int[][] matrix) {
        int[][] tmp = new int[matrix.length][matrix[0].length];
        Deque<int[]> newRows = new ArrayDeque<>();
        List<Integer> clearedRows = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) { //detects which rows are full
            int[] tmpRow = new int[matrix[i].length];
            boolean rowToClear = true;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    rowToClear = false;
                }
                tmpRow[j] = matrix[i][j];
            }
            if (rowToClear) { //if full, mark for deletion
                clearedRows.add(i);
            } else {
                newRows.add(tmpRow); //keep the row
            }
        }
        for (int i = matrix.length - 1; i >= 0; i--) { //fill new matrix from bottom upwards with remaining rows
            int[] row = newRows.pollLast();
            if (row != null) {
                tmp[i] = row;
            } else {
                break;
            }
        }
        int scoreBonus = 50 * clearedRows.size() * clearedRows.size(); //score bonus grows quadratically with number of lines cleared
        return new ClearRow(clearedRows.size(), tmp, scoreBonus);
    }

    /**
     * Deep copy a list of 2D integer arrays.
     *
     * @param list source list
     * @return new list with copied matrices
     */

    public static List<int[][]> deepCopyList(List<int[][]> list){
        return list.stream().map(MatrixOperations::copy).collect(Collectors.toList()); //deep copies a list of int matrices (useful when storing preview shapes or history)
    }

}
