package com.comp2042.gameUI;

import javafx.scene.Group;

/**
 * Manages score popup notifications shown in a {@link Group} node.
 * The manager creates and adds {@link NotificationPanel} instances when a positive score bonus occurs.
 */

public class NotificationManager { //This class manages the display of score pop-up notifications
    private final Group notificationGroup;

    /**
     * Create a NotificationManager that will attach popups into the provided group.
     *
     * @param notificationGroup JavaFX group to contain notification nodes
     */

    public NotificationManager(Group notificationGroup) { //constructor that takes in a group to hold notification panels
        this.notificationGroup = notificationGroup;
    }

    /**
     * Show a score popup with the given bonus. No popup is shown for non-positive bonuses.
     *
     * @param scoreBonus positive score amount to display
     */

    public void showScorePopup(int scoreBonus) { //method to display a score popup notification
        if (scoreBonus <= 0) return;
        NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
        notificationGroup.getChildren().add(notificationPanel);
        notificationPanel.showScore(notificationGroup.getChildren());
    }
}
