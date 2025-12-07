package com.comp2042.gameUI;

import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.MoveEvent;
import com.comp2042.input.InputEventListener;
import com.comp2042.logic.game.DownData;
import com.comp2042.logic.game.Score;
import com.comp2042.logic.game.ViewData;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.VBox;

public class GuiController implements Initializable {

    @FXML
    private GridPane gamePanel;

    @FXML
    private VBox pauseMenu;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label levelLabel;

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

    // internal level property
    private final SimpleIntegerProperty levelProperty = new SimpleIntegerProperty(1);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        renderer = new GameViewRenderer(gamePanel, brickPanel);
        notificationManager = new NotificationManager(groupNotification);

        Function<MoveEvent, DownData> downProcessor = (moveEvent) -> {
            if (eventListener == null) return null;
            return eventListener.onDownEvent(moveEvent);
        };

        inputHandler = new InputHandler(renderer, notificationManager,
                (BooleanSupplier) isPause::get,
                (BooleanSupplier) isGameOver::get,
                downProcessor);

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

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gamePanel.setOnKeyPressed(this::onKeyPressed);

        gameOverPanel.setVisible(false);

        if (pauseMenu != null) {
            // Menu is visible when isPause is TRUE
            pauseMenu.visibleProperty().bind(isPause);
            // Menu is only manageable when game is NOT over
            pauseMenu.disableProperty().bind(isGameOver);
        }

        // bind level label text to internal property
        if (levelLabel != null) {
            levelLabel.textProperty().bind(
                    new SimpleStringProperty("Level: ").concat(levelProperty.asString())
            );
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        String code = keyEvent.getCode().toString();
        if (code.equals("N")) {
            newGame(null);
            keyEvent.consume();
            return;
        }
        if (code.equals("P")) {
            pauseGame(null);
            keyEvent.consume();
            return;
        }

        inputHandler.handleKey(keyEvent);
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
        this.inputHandler.setEventListener(eventListener);
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        renderer.initGameView(boardMatrix, brick);
        timelineManager.start();
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(
                    new SimpleStringProperty("Score: ").concat(integerProperty.asString())
            );
        }
    }

    // new combined binder: binds score label, computes level from score and adjusts timeline speed
    public void bindScoreAndLevel(IntegerProperty scoreProperty) {
        bindScore(scoreProperty);

        // initial timeline period uses TimelineManager default (400ms)
        scoreProperty.addListener((obs, oldVal, newVal) -> {
            int score = newVal.intValue();
            int newLevel = Math.min(20, 1 + score / 50);
            if (levelProperty.get() != newLevel) {
                levelProperty.set(newLevel);
                // arithmetic speed increase: reduce period by 18ms per level (from 400ms)
                long newPeriod = Math.max(50, 400 - (newLevel - 1) * 18L);
                timelineManager.setPeriodMillis(newPeriod);
            }
        });

        // initialize level from current score value (in case score != 0)
        int initialLevel = Math.min(20, 1 + scoreProperty.get() / 50);
        levelProperty.set(initialLevel);
        long initialPeriod = Math.max(50, 400 - (initialLevel - 1) * 18L);
        timelineManager.setPeriodMillis(initialPeriod);
    }

    public void refreshGameBackground(int[][] boardMatrix) {
        if (renderer != null) {
            renderer.refreshGameBackground(boardMatrix);
        }
    }

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
        if (isGameOver.get()) {
            return;
        }

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