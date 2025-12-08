package com.tetris;

import com.comp2042.logic.game.ClearRow;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClearRowTest {

    @Test
    void testClearRowData() {
        int lines = 4;
        int[][] matrix = new int[10][20];
        int scoreBonus = 800;

        ClearRow clearRow = new ClearRow(lines, matrix, scoreBonus);

        assertEquals(lines, clearRow.getLinesRemoved());
        assertArrayEquals(matrix, clearRow.getNewMatrix());
        assertEquals(scoreBonus, clearRow.getScoreBonus());
    }
}