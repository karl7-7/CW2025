package com.tetris;

import com.comp2042.logic.game.ClearRow;
import com.comp2042.logic.game.DownData;
import com.comp2042.logic.game.ViewData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DownDataTest {

    @Test
    void testDownDataConstruction() {
        ClearRow clearRow = new ClearRow(1, new int[10][20], 100);
        ViewData viewData = new ViewData(new int[1][1], 0, 0, null, null, 0);

        DownData downData = new DownData(clearRow, viewData);

        assertEquals(clearRow, downData.getClearRow());
        assertEquals(viewData, downData.getViewData());
    }
}