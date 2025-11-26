package com.comp2042;

public final class NextShapeInfo { // This immutable data class stores information about the next shape in the game

    private final int[][] shape; //a 2d array representing the block pattern of the next shape
    private final int position; //represents the position of the shape

    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape; // stores reference to the provided shape array
        this.position = position; //stores the shape's position
    }

    public int[][] getShape() {
        return MatrixOperations.copy(shape); //returns a copy of the 2d shape array
    }

    public int getPosition() {
        return position; // returns the position of the shape
    }
}
