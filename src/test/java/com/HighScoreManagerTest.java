package test.java.com;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.java.com.highscore.HighScoreManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class HighScoreManagerTest {
    private static final String TEST_FILE_PATH = "test_highscores.json";
    private HighScoreManager highScoreManager;

    @Before
    public void setUp() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        highScoreManager = new HighScoreManager(TEST_FILE_PATH);
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
    }

    @Test
    public void testIncrementHighScore_NewPlayer() {
        highScoreManager.incrementHighScore("Player1", 10.0f);
        Map<String, Float> highScores = highScoreManager.getHighScores();
        assertEquals(1, highScores.size());
        assertEquals(10.0f, highScores.get("Player1"), 0.001);
    }

    @Test
    public void testIncrementHighScore_ExistingPlayer() {
        highScoreManager.incrementHighScore("Player1", 10.0f);
        highScoreManager.incrementHighScore("Player1", 5.0f);
        Map<String, Float> highScores = highScoreManager.getHighScores();
        assertEquals(1, highScores.size());
        assertEquals(15.0f, highScores.get("Player1"), 0.001);
    }

    @Test
    public void testLoadHighScores() throws IOException {
        highScoreManager.incrementHighScore("Player1", 10.0f);
        highScoreManager.incrementHighScore("Player2", 20.0f);
        highScoreManager = new HighScoreManager(TEST_FILE_PATH);
        Map<String, Float> highScores = highScoreManager.getHighScores();
        assertEquals(2, highScores.size());
        assertEquals(10.0f, highScores.get("Player1"), 0.001);
        assertEquals(20.0f, highScores.get("Player2"), 0.001);
    }

    @Test
    public void testCreateNewFile() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        highScoreManager = new HighScoreManager(TEST_FILE_PATH);
        assertTrue(Files.exists(Paths.get(TEST_FILE_PATH)));
    }
}
