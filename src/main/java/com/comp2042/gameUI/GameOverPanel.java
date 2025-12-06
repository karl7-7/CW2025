package com.comp2042.gameUI;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


public class GameOverPanel extends BorderPane {// This class acts as its own container displaying a UI panel when the game ends

    public GameOverPanel() { //constructor that builds the game over screen
        final Label gameOverLabel = new Label("GAME OVER"); //create label to show game over text
        gameOverLabel.getStyleClass().add("gameOverStyle"); // apply css styling from stylesheet
        setCenter(gameOverLabel); //place label at centre of layout
    }

}
