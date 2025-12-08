package com.tetris;

import com.comp2042.logic.game.Score;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    private Score score;

    @BeforeEach
    void setUp() {
        score = new Score();
    }

    @Test
    void testInitialScoreIsZero() {
        assertEquals(0, score.scoreProperty().get(), "Score should start at 0");
    }

    @Test
    void testAddScore() {
        score.add(100);
        assertEquals(100, score.scoreProperty().get(), "Score should be 100 after adding 100");

        score.add(50);
        assertEquals(150, score.scoreProperty().get(), "Score should accumulate to 150");
    }

    @Test
    void testResetScore() {
        score.add(500);
        score.reset();
        assertEquals(0, score.scoreProperty().get(), "Score should be 0 after reset");
    }
}