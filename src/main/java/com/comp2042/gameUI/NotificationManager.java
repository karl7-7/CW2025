package com.comp2042.gameUI;

import javafx.scene.Group;

public class NotificationManager { //This class manages the display of score pop-up notifications
    private final Group notificationGroup;

    public NotificationManager(Group notificationGroup) { //constructor that takes in a group to hold notification panels
        this.notificationGroup = notificationGroup;
    }

    public void showScorePopup(int scoreBonus) { //method to display a score popup notification
        if (scoreBonus <= 0) return;
        NotificationPanel notificationPanel = new NotificationPanel("+" + scoreBonus);
        notificationGroup.getChildren().add(notificationPanel);
        notificationPanel.showScore(notificationGroup.getChildren());
    }
}
