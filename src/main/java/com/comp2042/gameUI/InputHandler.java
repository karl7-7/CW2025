package com.comp2042.gameUI;

import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.MoveEvent;
import com.comp2042.input.InputEventListener;
import com.comp2042.logic.game.DownData;
import com.comp2042.logic.game.ViewData;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * Handles keyboard input and forwards corresponding events to the game logic via an
 * {@link InputEventListener}. Also instructs the {@link GameViewRenderer} to refresh visuals
 * and uses {@link NotificationManager} for score popups.
 */

public class InputHandler {
    private InputEventListener eventListener;
    private final GameViewRenderer renderer;
    private final NotificationManager notificationManager;
    private final BooleanSupplier isPaused;
    private final BooleanSupplier isGameOver;
    private final Function<MoveEvent, DownData> downProcessor;



    /**
     * Create a new InputHandler.
     *
     * @param renderer            renderer used to update brick visuals
     * @param notificationManager manager used to show score notifications
     * @param isPaused            supplier returning whether the game is paused
     * @param isGameOver          supplier returning whether the game is over
     * @param downProcessor       function to process DOWN/HARD_DROP events and return {@link DownData}
     */

    public InputHandler(GameViewRenderer renderer,
                        NotificationManager notificationManager,
                        BooleanSupplier isPaused,
                        BooleanSupplier isGameOver,
                        Function<MoveEvent, DownData> downProcessor) {
        this.renderer = renderer;
        this.notificationManager = notificationManager;
        this.isPaused = isPaused;
        this.isGameOver = isGameOver;
        this.downProcessor = downProcessor;
    }

    /**
     * Set the listener that will receive translated input events.
     *
     * @param listener listener implementation
     */
    public void setEventListener(InputEventListener listener) {
        this.eventListener = listener;
    }


    /**
     * Handle a key event coming from the UI. This will translate keys to game actions,
     * call the appropriate method on the {@link InputEventListener}, update the renderer,
     * and show score popups when rows are cleared.
     *
     * @param keyEvent the JavaFX key event
     */

    public void handleKey(KeyEvent keyEvent) {
        if (isPaused.getAsBoolean() || isGameOver.getAsBoolean()) {
            return;
        }

        KeyCode code = keyEvent.getCode();
        if (eventListener == null) return;

        if (code == KeyCode.LEFT || code == KeyCode.A) {
            ViewData v = eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
            renderer.refreshBrick(v);
            keyEvent.consume();
            return;
        }
        if (code == KeyCode.RIGHT || code == KeyCode.D) {
            ViewData v = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
            renderer.refreshBrick(v);
            keyEvent.consume();
            return;
        }
        if (code == KeyCode.UP || code == KeyCode.W) {
            ViewData v = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
            renderer.refreshBrick(v);
            keyEvent.consume();
            return;
        }
        if (code == KeyCode.DOWN || code == KeyCode.S) {
            DownData dd = downProcessor.apply(new MoveEvent(EventType.DOWN, EventSource.USER));
            handleScorePopup(dd);
            renderer.refreshBrick(dd.getViewData());
            keyEvent.consume();
            return;
        }

        // NEW: Space Bar for Hard Drop
        if (code == KeyCode.SPACE) {
            DownData dd = downProcessor.apply(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
            handleScorePopup(dd);
            renderer.refreshBrick(dd.getViewData());
            keyEvent.consume();
        }

        if (code == KeyCode.C) {
            ViewData v = eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
            renderer.refreshBrick(v);
            keyEvent.consume();
            return;
        }
    }

    private void handleScorePopup(DownData dd) {
        if (dd != null && dd.getClearRow() != null && dd.getClearRow().getLinesRemoved() > 0) {
            notificationManager.showScorePopup(dd.getClearRow().getScoreBonus());
        }
    }
}