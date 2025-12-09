package com.comp2042.gameUI;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * UI node used to display an animated score bonus popup.
 *
 * <p>The panel contains a styled label and provides a convenience method to
 * animate itself and remove from a parent children list when the animation ends.
 */

public class NotificationPanel extends BorderPane { //This class creates and animates a popup panel that shows score bonuses when the player clears line

    /**
     * Create a notification panel showing the provided text.
     *
     * @param text text to display (e.g. "+100")
     */

    public NotificationPanel(String text) { //Constructor creates a panel containing a glowing label showing bonus score text
        setMinHeight(200);
        setMinWidth(220); //size of the notification panel
        final Label score = new Label(text); //label that displays the bonus value
        score.getStyleClass().add("bonusStyle"); // apply CSS style defined in stylesheet
        final Effect glow = new Glow(0.6); //glow effect
        score.setEffect(glow);
        score.setTextFill(Color.WHITE); //text colour
        setCenter(score); //label placed in centre of BorderPane

    }

    /**
     * Animate the panel (fade + translate) and remove it from the given children list
     * when the animation completes.
     *
     * @param list observable children list of the parent node
     */

    public void showScore(ObservableList<Node> list) { //displays notification by animating it
        FadeTransition ft = new FadeTransition(Duration.millis(2000), this); //fade-out animation
        TranslateTransition tt = new TranslateTransition(Duration.millis(2500), this); //move panel upwards
        tt.setToY(this.getLayoutY() - 40); // 40px upwards
        ft.setFromValue(1);
        ft.setToValue(0);
        ParallelTransition transition = new ParallelTransition(tt, ft); //play both fade and move at the same time
        transition.setOnFinished(new EventHandler<ActionEvent>() { //when animation ends, remove this panel from the parent list
            @Override
            public void handle(ActionEvent event) {
                list.remove(NotificationPanel.this);
            }
        });
        transition.play(); //start the animation
    }
}
