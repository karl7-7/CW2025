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

public class InputHandler { //This class handles user keyboard input events
    private InputEventListener eventListener;
    private final GameViewRenderer renderer;
    private final NotificationManager notificationManager;
    private final BooleanSupplier isPaused;
    private final BooleanSupplier isGameOver;
    private final Function<MoveEvent, DownData> downProcessor;

    public InputHandler(GameViewRenderer renderer,
                        NotificationManager notificationManager,
                        BooleanSupplier isPaused,
                        BooleanSupplier isGameOver,
                        Function<MoveEvent, DownData> downProcessor) { // Constructor initialises the InputHandler with necessary components
        this.renderer = renderer;
        this.notificationManager = notificationManager;
        this.isPaused = isPaused;
        this.isGameOver = isGameOver;
        this.downProcessor = downProcessor;
    }

    public void setEventListener(InputEventListener listener) { // Sets the event listener that will handle input events
        this.eventListener = listener;
    }

    public void handleKey(KeyEvent keyEvent) { // Processes a key event from the user
        if (isPaused.getAsBoolean() || isGameOver.getAsBoolean()) {
            return;
        }

        KeyCode code = keyEvent.getCode(); // Get the key code from the key event
        if (eventListener == null) return;

        if (code == KeyCode.LEFT || code == KeyCode.A) { // If left arrow or 'A' key is pressed
            ViewData v = eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
            renderer.refreshBrick(v);
            keyEvent.consume();
            return;
        }
        if (code == KeyCode.RIGHT || code == KeyCode.D) { // If right arrow or 'D' key is pressed
            ViewData v = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
            renderer.refreshBrick(v);
            keyEvent.consume();
            return;
        }
        if (code == KeyCode.UP || code == KeyCode.W) { // If up arrow or 'W' key is pressed
            ViewData v = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
            renderer.refreshBrick(v);
            keyEvent.consume();
            return;
        }
        if (code == KeyCode.DOWN || code == KeyCode.S) { // If down arrow or 'S' key is pressed
            DownData dd = downProcessor.apply(new MoveEvent(EventType.DOWN, EventSource.USER));
            if (dd != null && dd.getClearRow() != null && dd.getClearRow().getLinesRemoved() > 0) {
                notificationManager.showScorePopup(dd.getClearRow().getScoreBonus());
            }
            renderer.refreshBrick(dd.getViewData()); // Refresh the brick view based on the down event
            keyEvent.consume();
        }
    }
}
