package com.tetris;

import com.comp2042.logic.game.ViewData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ViewDataTest {

    @Test
    void testConstructorAndGetters() {
        int[][] brick = {{1}};
        int[][] next = {{2}};
        int[][] held = {{3}};

        ViewData viewData = new ViewData(brick, 5, 10, next, held, 15);

        assertArrayEquals(brick, viewData.getBrickData());
        assertEquals(5, viewData.getxPosition());
        assertEquals(10, viewData.getyPosition());
        assertArrayEquals(next, viewData.getNextBrickData());
        assertArrayEquals(held, viewData.getHeldBrickData());
        assertEquals(15, viewData.getGhostY());
    }

    @Test
    void testHeldBrickNullSafety() {
        int[][] brick = {{1}};
        // Pass null for held brick
        ViewData viewData = new ViewData(brick, 0, 0, brick, null, 0);

        assertNull(viewData.getHeldBrickData(), "Should return null safely if no brick is held");
    }
}
