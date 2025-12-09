package com.comp2042.logic.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Simple score holder using a JavaFX {@link IntegerProperty}.
 *
 * <p>Provides methods to expose the property, to add points and to reset the value.
 */

public final class Score { // Score class manages the player's score using a JavaFx IntegerProperty

    private final IntegerProperty score = new SimpleIntegerProperty(0); //JavaFXX property soring the current score starting at 0

    /**
     * Expose the underlying JavaFX score property for UI binding.
     *
     * @return integer property representing the current score
     */

    public IntegerProperty scoreProperty() {
        return score; // exposes the score property so UI elements can bind to it
    }

    /**
     * Add the given amount to the current score.
     *
     * @param i amount to add (may be zero or positive)
     */

    public void add(int i){ // Adds an "i" amount to the score
        score.setValue(score.getValue() + i); // called when rows are cleared
    }

    /**
     * Reset the score back to zero.
     */

    public void reset() { // Resets score back to 0
        score.setValue(0); // called when starting a new game
    }
}
