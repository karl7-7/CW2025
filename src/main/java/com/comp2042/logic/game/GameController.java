package com.comp2042.logic.game;

import com.comp2042.events.EventSource;
import com.comp2042.events.MoveEvent;
import com.comp2042.gameUI.GuiController;
import com.comp2042.input.InputEventListener;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(25, 10);     // The game board model (logic, brick placement, movement, scoring)

    private final GuiController viewGuiController;     // Reference to the GUI controller (for updating UI)

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick(); // Create the very first brick on the board
        viewGuiController.setEventListener(this);  // Register this GameController so GUI can send input events here
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());         // Tell the GUI to draw the background grid + the first brick
        viewGuiController.bindScore(board.getScore().scoreProperty()); // Bind GUI score label to the board's score property (auto-updates)
    }

    @Override
    public DownData onDownEvent(MoveEvent event) { // Called when timer or user requests to move the brick down
        boolean canMove = board.moveBrickDown();  // Try to move brick down
        ClearRow clearRow = null;
        if (!canMove) {  // Brick reached bottom or landed on another brick → freeze it
            board.mergeBrickToBackground();
            clearRow = board.clearRows();  // Check for completed lines
            if (clearRow.getLinesRemoved() > 0) { // If lines cleared → update score
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) { // Try to spawn a new brick
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());  // Redraw background after merging rows

        } else { // Award small points for user-initiated manual down movement
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData()); // Return information about cleared rows and new brick shape
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) { // Move the falling brick left
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) { // Move the falling brick right
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) { // rotate the following brick
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() { // reset the game entirely
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }
}
