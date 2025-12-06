package com.comp2042.logic.game;

public final class ClearRow { //This class is used to return the results of clearing rows

    private final int linesRemoved; //number of rows removed
    private final int[][] newMatrix; //updated game matrix after removed rows have been cleared and rows shifted
    private final int scoreBonus; // bonus score awarded for clearing rows

    public ClearRow(int linesRemoved, int[][] newMatrix, int scoreBonus) {
        this.linesRemoved = linesRemoved;
        this.newMatrix = newMatrix;
        this.scoreBonus = scoreBonus;
    } //constructs a ClearRow result object

    public int getLinesRemoved() {
        return linesRemoved; //returns how many rows were cleared in this operation
    }

    public int[][] getNewMatrix() {
        return MatrixOperations.copy(newMatrix); //returns a copy of the updated matric
    }

    public int getScoreBonus() {
        return scoreBonus; //returns the score bonus for clearing the rows
    }
}
