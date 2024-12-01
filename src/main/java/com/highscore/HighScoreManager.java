package main.java.com.highscore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

/**
 * Osztály a topplista kezelésére.
 */
public class HighScoreManager {
  private String FILE_PATH;
  private Map<String, Float> highScores;

  /**
   * HighScoreManager konstruktora.
   *
   * @param FILE_PATH A fájl elérési útvonala.
   */
  public HighScoreManager(String FILE_PATH) {
    this.FILE_PATH = FILE_PATH;
    highScores = new HashMap<>();
    loadHighScores();
  }

  /**
   * Betölti a topplista adatait a megadott fájlból.
   */
  private void loadHighScores() {
    File file = new File(FILE_PATH);
    if (!file.exists()) {
      createNewFile();
    }

    try (JsonReader reader = Json.createReader(new FileInputStream(file))) {
      JsonArray jsonArray = reader.readArray();
      for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
        String name = jsonObject.getString("name");
        float score = jsonObject.getInt("highscore");
        highScores.put(name, score);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void createNewFile() {
    try {
      Files.write(Paths.get(FILE_PATH),
          "[]".getBytes()); // Create an empty JSON array
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Növeli a megadott játékos pontszámát a megadott értékkel.
   *
   * @param name  A játékos neve.
   * @param score A növelendő pontszám.
   */
  public void incrementHighScore(String name, float score) {
    highScores.put(name, highScores.getOrDefault(name, (float) 0) + score);
    saveHighScores();
  }

  /**
   * Elmenti a toplista adatait a megadott fájlba.
   */
  private void saveHighScores() {
    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
    for (Map.Entry<String, Float> entry : highScores.entrySet()) {
      JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
      objectBuilder.add("name", entry.getKey());
      objectBuilder.add("highscore", entry.getValue());
      arrayBuilder.add(objectBuilder.build());
    }

    try (JsonWriter writer = Json.createWriter(new FileOutputStream(FILE_PATH))) {
      writer.writeArray(arrayBuilder.build());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Visszaadja a toplista adatait.
   *
   * @return A toplista adatai.
   */
  public Map<String, Float> getHighScores() {
    return highScores;
  }
}