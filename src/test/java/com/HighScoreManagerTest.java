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
    private HighScoreManager highScoreManager;
    private static final String TEST_FILE_PATH = "test_highscores.json";

    @Before
    public void setUp() {
        System.setProperty("highscores.file", TEST_FILE_PATH);
        highScoreManager = new HighScoreManager(TEST_FILE_PATH);
    }

    @After
    public void tearDown() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadHighScores_InitialLoad() {
        Map<String, Float> scores = highScoreManager.getHighScores();
        assertTrue(scores.isEmpty());
    }

    @Test
    public void testIncrementHighScore() {
        highScoreManager.incrementHighScore("Player1", 10.0f);
        Map<String, Float> scores = highScoreManager.getHighScores();
        assertEquals(Float.valueOf(10.0f), scores.get("Player1"));
        
        highScoreManager.incrementHighScore("Player1", 5.0f);
        scores = highScoreManager.getHighScores();
        assertEquals(Float.valueOf(15.0f), scores.get("Player1"));
    }

    @Test
    public void testSaveHighScores() throws IOException {
        highScoreManager.incrementHighScore("Player1", 20.0f);
        highScoreManager.incrementHighScore("Player2", 30.0f);

        String content = new String(Files.readAllBytes(Paths.get(TEST_FILE_PATH)));
        
        assertTrue(content.contains("Player1"));
        assertTrue(content.contains("20.0"));
        assertTrue(content.contains("Player2"));
        assertTrue(content.contains("30.0"));
    }
}