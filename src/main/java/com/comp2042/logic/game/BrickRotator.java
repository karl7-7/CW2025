package com.comp2042.logic.game;

import com.comp2042.logic.bricks.Brick;

public class BrickRotator {// This class is responsible for managing rotation of the current brick

    private Brick brick; //currently active brick
    private int currentShape = 0; //index of the brick's current rotation state

    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size(); //move to the next rotation index, wrapping around
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape); //return the next rotation and its index
    }

    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape); // returns the 2d matrix representing the current rotation of the brick
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape; //sets the active rotation of the brick
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0; //sets a new brick as the piece currently falling in the game and rests its rotation to default
    }


}
