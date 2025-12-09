package com.comp2042.logic.game;

import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.MoveEvent;
import com.comp2042.gameUI.GuiController;
import com.comp2042.input.InputEventListener;

/**
 * Coordinates game logic and the GUI. Implements {@link InputEventListener}
 * to translate UI events into board operations and to update the view.
 */

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);
    private final GuiController viewGuiController;

    /**
     * Create a GameController and initialize a new game.
     *
     * @param c GUI controller used to update and receive input
     */

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScoreAndLevel(board.getScore().scoreProperty());
    }


    /**
     * Handle DOWN / HARD_DROP events. For HARD_DROP the piece is dropped,
     * merged and scoring is applied. For normal DOWN attempts the piece moves
     * or, if blocked, is merged and rows cleared.
     *
     * @param event move event representing a down action
     * @return DownData containing any clear information and the new view snapshot
     */

    @Override
    public DownData onDownEvent(MoveEvent event) {
        // NEW: Handle Hard Drop
        if (event.getEventType() == EventType.HARD_DROP) {
            board.hardDrop();
            board.mergeBrickToBackground();
            ClearRow clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            // Add extra score for hard drop
            board.getScore().add(2);

            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
            return new DownData(clearRow, board.getViewData());
        }

        // Existing logic for Normal Drop
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }
            viewGuiController.refreshGameBackground(board.getBoardMatrix());
        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    /**
     * Handle left move from UI.
     *
     * @param event move event
     * @return new view snapshot
     */

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    /**
     * Handle right move from UI.
     *
     * @param event move event
     * @return new view snapshot
     */

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    /**
     * Handle rotate event from UI.
     *
     * @param event move event
     * @return new view snapshot
     */

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /**
     * Start a new game: reset board and refresh background view.
     */

    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    /**
     * Handle hold piece request from UI.
     *
     * @param event move event
     * @return new view snapshot after holding
     */

    @Override
    public ViewData onHoldEvent(MoveEvent event) {
        board.holdPiece();
        return board.getViewData();
    }
}