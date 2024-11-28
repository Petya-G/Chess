package main.java.com.ui;

import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import main.java.com.highscore.HighScoreManager;

public class HighScorePanel extends JPanel {
  private final HighScoreManager highScoreManager;
  private final JTextArea highScoreTextArea;

  public HighScorePanel(HighScoreManager highScoreManager) {
    this.highScoreManager = highScoreManager;
    this.highScoreTextArea = new JTextArea();
    this.highScoreTextArea.setEditable(false);
    this.setLayout(new BorderLayout());
    this.add(new JScrollPane(highScoreTextArea), BorderLayout.CENTER);
    displayHighScores();
  }

  private void displayHighScores() {
    StringBuilder builder = new StringBuilder();
    Map<String, Integer> highScores = highScoreManager.getHighScores();
    for (Map.Entry<String, Integer> entry : highScores.entrySet()) {
      builder.append(entry.getKey())
          .append(": ")
          .append(entry.getValue())
          .append("\n");
    }
    highScoreTextArea.setText(builder.toString());
  }
}