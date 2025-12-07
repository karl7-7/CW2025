package com.comp2042.gameUI;

import com.comp2042.events.MoveEvent;
import com.comp2042.input.InputEventListener;
import com.comp2042.logic.game.DownData;
import com.comp2042.logic.game.ViewData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public class GuiController implements Initializable {

    @FXML
    private GridPane gamePanel;

    @FXML
    private VBox pauseMenu;

    @FXML
    private VBox startMenuContainer;

    @FXML
    private VBox controlsOverlay;

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
    private final SimpleIntegerProperty levelProperty = new SimpleIntegerProperty(1);

    private VBox previousMenu;

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

        // CHANGE 1: Removed visibleProperty().bind(isPause) to allow manual control
        if (pauseMenu != null) {
            pauseMenu.disableProperty().bind(isGameOver);
        }

        if (levelLabel != null) {
            levelLabel.textProperty().bind(
                    new SimpleStringProperty("Level: ").concat(levelProperty.asString())
            );
        }
    }

    public void showControls(ActionEvent actionEvent) {
        if (startMenuContainer.isVisible()) {
            previousMenu = startMenuContainer;
        } else if (pauseMenu.isVisible()) {
            previousMenu = pauseMenu;
        } else {
            return;
        }

        // Now this works because pauseMenu is not bound
        previousMenu.setVisible(false);
        controlsOverlay.setVisible(true);
    }

    public void closeControls(ActionEvent actionEvent) {
        controlsOverlay.setVisible(false);
        if (previousMenu != null) {
            previousMenu.setVisible(true);
        }
    }

    public void onStartGame(ActionEvent actionEvent) {
        startMenuContainer.setVisible(false);
        timelineManager.start();
        gamePanel.requestFocus();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        String code = keyEvent.getCode().toString();

        if (controlsOverlay != null && controlsOverlay.isVisible()) {
            return;
        }

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
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreLabel != null) {
            scoreLabel.textProperty().bind(
                    new SimpleStringProperty("Score: ").concat(integerProperty.asString())
            );
        }
    }

    public void bindScoreAndLevel(IntegerProperty scoreProperty) {
        bindScore(scoreProperty);
        scoreProperty.addListener((obs, oldVal, newVal) -> {
            int score = newVal.intValue();
            int newLevel = Math.min(20, 1 + score / 50);
            if (levelProperty.get() != newLevel) {
                levelProperty.set(newLevel);
                long newPeriod = Math.max(50, 400 - (newLevel - 1) * 18L);
                timelineManager.setPeriodMillis(newPeriod);
            }
        });
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

        // CHANGE 2: Manually hide pauseMenu
        if (pauseMenu != null) pauseMenu.setVisible(false);

        timelineManager.start();
    }

    public void pauseGame(ActionEvent actionEvent) {
        if (controlsOverlay != null && controlsOverlay.isVisible()) return;

        if (isGameOver.get()) {
            return;
        }

        boolean paused = isPause.get();
        if (paused) {
            // Unpause
            timelineManager.start();
            isPause.setValue(false);
            // CHANGE 3: Manually hide pauseMenu
            if (pauseMenu != null) pauseMenu.setVisible(false);
        } else {
            // Pause
            timelineManager.stop();
            isPause.setValue(true);
            // CHANGE 4: Manually show pauseMenu
            if (pauseMenu != null) pauseMenu.setVisible(true);
        }
        gamePanel.requestFocus();
    }
}