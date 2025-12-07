package com.comp2042.logic.game;

import com.comp2042.events.EventSource;
import com.comp2042.events.MoveEvent;
import com.comp2042.gameUI.GuiController;
import com.comp2042.input.InputEventListener;

import com.comp2042.gameUI.GuiController;

public class GameController {

    private final Board board;
    private final GameService service;
    private final GameCoordinator coordinator;

    public GameController(GuiController c) {
        this.board = new SimpleBoard(25, 10);
        this.service = new GameService(board);
        this.coordinator = new GameCoordinator(service, c);

        board.createNewBrick();
        c.setEventListener(coordinator);
        c.initGameView(board.getBoardMatrix(), board.getViewData());
        c.bindScoreAndLevel(service.getScore().scoreProperty());

    }
}