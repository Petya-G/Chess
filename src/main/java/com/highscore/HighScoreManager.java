package main.java.com.highscore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HighScoreManager {
  private final String filePath;
  private Map<String, Integer> highScores;
  private final ObjectMapper objectMapper;

  public HighScoreManager(String filePath) {
    this.filePath = filePath;
    this.highScores = new HashMap<>();
    this.objectMapper = new ObjectMapper();
    loadHighScores();
  }

  private void loadHighScores() {
    File file = new File(filePath);
    if (!file.exists()) {
      // Create an empty high score JSON
      saveHighScores();
      return;
    }

    try {
      String content = new String(Files.readAllBytes(Paths.get(filePath)));
      highScores = objectMapper.readValue(content, Map.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void incrementScore(String name) {
    highScores.put(name, highScores.getOrDefault(name, 0) + 1);
    saveHighScores();
  }

  private void saveHighScores() {
    try {
      String jsonString =
          objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
              highScores);
      Files.write(Paths.get(filePath), jsonString.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Map<String, Integer> getHighScores() { return highScores; }
}