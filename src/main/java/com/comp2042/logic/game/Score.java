package com.comp2042.logic.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public final class Score { // Score class manages the player's score using a JavaFx IntegerProperty

    private final IntegerProperty score = new SimpleIntegerProperty(0); //JavaFXX property soring the current score starting at 0

    public IntegerProperty scoreProperty() {
        return score; // exposes the score property so UI elements can bind to it
    }

    public void add(int i){ // Adds an "i" amount to the score
        score.setValue(score.getValue() + i); // called when rows are cleared
    }

    public void reset() { // Resets score back to 0
        score.setValue(0); // called when starting a new game
    }
}
