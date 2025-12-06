package com.comp2042.gameUI;

import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.MoveEvent;
import com.comp2042.input.InputEventListener;
import com.comp2042.logic.game.DownData;
import com.comp2042.logic.game.ViewData;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class GuiController implements Initializable { // This class is the controller for the JavaFX GUI

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    private InputEventListener eventListener;

    private GameViewRenderer renderer;
    private NotificationManager notificationManager;
    private TimelineManager timelineManager;
    private InputHandler inputHandler;

    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderer = new GameViewRenderer(gamePanel, brickPanel);
        notificationManager = new NotificationManager(groupNotification);

        // down processor uses the current eventListener and returns DownData (delegated to GameController)
        Function<MoveEvent, DownData> downProcessor = (moveEvent) -> {
            if (eventListener == null) return null;
            return eventListener.onDownEvent(moveEvent);
        };

        inputHandler = new InputHandler(renderer, notificationManager,
                (BooleanSupplier) isPause::get,
                (BooleanSupplier) isGameOver::get,
                downProcessor);

        // timeline drives DOWN events via same processor
        timelineManager = new TimelineManager(moveEvent -> {
            if (!isPause.get() && !isGameOver.get()) {
                DownData dd = downProcessor.apply(moveEvent);
                if (dd != null && dd.getClearRow() != null && dd.getClearRow().getLinesRemoved() > 0) {
                    notificationManager.showScorePopup(dd.getClearRow().getScoreBonus());
                }
                if (dd != null) renderer.refreshBrick(dd.getViewData());
            }
            gamePanel.requestFocus();
        });

        // key handling
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::onKeyPressed);

        gameOverPanel.setVisible(false);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        // 'N' handled here to always start new game
        if (keyEvent.getCode().toString().equals("N")) {
            newGame(null);
            keyEvent.consume();
            return;
        }

        inputHandler.handleKey(keyEvent);
    }

    // Called by GameController on construction
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        this.inputHandler.setEventListener(eventListener);
    }

    // Called by GameController to initialize visuals
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        renderer.initGameView(boardMatrix, brick);
        timelineManager.start();
    }

    public void bindScore(IntegerProperty integerProperty) {

    }

    public void refreshGameBackground(int[][] boardMatrix) {
        if (renderer != null) {
            renderer.refreshGameBackground(boardMatrix);
        }
    }
    //

    public void gameOver() {
        timelineManager.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    public void newGame(ActionEvent actionEvent) {
        timelineManager.stop();
        gameOverPanel.setVisible(false);
        if (eventListener != null) {
            eventListener.createNewGame();
        }
        gamePanel.requestFocus();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        timelineManager.start();
    }

    public void pauseGame(ActionEvent actionEvent) {
        boolean paused = isPause.get();
        if (paused) {
            timelineManager.start();
            isPause.setValue(false);
        } else {
            timelineManager.stop();
            isPause.setValue(true);
        }
        gamePanel.requestFocus();
    }
}
