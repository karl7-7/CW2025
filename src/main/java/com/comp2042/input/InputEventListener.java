package com.comp2042.input;

import com.comp2042.logic.game.DownData;
import com.comp2042.events.MoveEvent;
import com.comp2042.logic.game.ViewData;

public interface InputEventListener {

    DownData onDownEvent(MoveEvent event);

    ViewData onLeftEvent(MoveEvent event);

    ViewData onRightEvent(MoveEvent event);

    ViewData onRotateEvent(MoveEvent event);

    ViewData onHoldEvent(MoveEvent event); // NEW

    void createNewGame();
}
