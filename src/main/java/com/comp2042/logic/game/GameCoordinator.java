package com.comp2042.logic.game;

import com.comp2042.events.MoveEvent;
import com.comp2042.input.InputEventListener;
import com.comp2042.gameUI.GuiController;

public class GameCoordinator implements InputEventListener {

    private final GameService service;
    private final GuiController gui;

    public GameCoordinator(GameService service, GuiController gui) {
        this.service = service;
        this.gui = gui;
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        GameService.MoveResult r = service.moveDown(event);
        if (r.isMerged()) {
            gui.refreshGameBackground(service.getBoard().getBoardMatrix());
        }
        if (r.isGameOver()) {
            gui.gameOver();
        }
        return r.getDownData();
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        return service.moveLeft(event);
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        return service.moveRight(event);
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        return service.rotate(event);
    }

    @Override
    public void createNewGame() {
        service.newGame();
        gui.refreshGameBackground(service.getBoard().getBoardMatrix());
    }
}