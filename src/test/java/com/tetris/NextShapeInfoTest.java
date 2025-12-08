package com.tetris;

import com.comp2042.logic.game.NextShapeInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NextShapeInfoTest {

    @Test
    void testNextShapeInfo() {
        int[][] shape = {{1, 0}, {0, 1}};
        int position = 2;

        NextShapeInfo info = new NextShapeInfo(shape, position);

        assertArrayEquals(shape, info.getShape());
        assertEquals(position, info.getPosition());
    }
}
