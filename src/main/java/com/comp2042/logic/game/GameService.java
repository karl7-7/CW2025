package com.comp2042.logic.game;

import com.comp2042.events.MoveEvent;

public class GameService {

    private final Board board;

    public GameService(Board board) {
        this.board = board;
    }

    public MoveResult moveDown(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        boolean merged = false;
        boolean gameOver = false;

        if (!canMove) {
            merged = true;
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow != null && clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                gameOver = true;
            }
        } else {
            if (event.getEventSource() == com.comp2042.events.EventSource.USER) {
                board.getScore().add(1);
            }
        }

        DownData dd = new DownData(clearRow, board.getViewData());
        return new MoveResult(dd, merged, gameOver);
    }
    public ViewData holdPiece() {
        board.holdPiece();
        return board.getViewData();
    }

    public ViewData moveLeft(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    public ViewData moveRight(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    public ViewData rotate(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    public void newGame() {
        board.newGame();
        board.createNewBrick();
    }

    public Board getBoard() {
        return board;
    }

    public Score getScore() {
        return board.getScore();
    }

    public static final class MoveResult {
        private final DownData downData;
        private final boolean merged;
        private final boolean gameOver;

        public MoveResult(DownData downData, boolean merged, boolean gameOver) {
            this.downData = downData;
            this.merged = merged;
            this.gameOver = gameOver;
        }

        public DownData getDownData() { return downData; }
        public boolean isMerged() { return merged; }
        public boolean isGameOver() { return gameOver; }
    }
}
