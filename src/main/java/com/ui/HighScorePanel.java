package main.java.com.ui;

import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.java.com.highscore.HighScoreManager;

public class HighScorePanel extends JPanel {
  public HighScoreManager highScoreManager;

  public HighScorePanel() {
    this.highScoreManager = new HighScoreManager();
    setLayout(new BorderLayout());
    updateDisplay();
  }

  public void updateDisplay() {
    removeAll();
    Map<String, Integer> scores = highScoreManager.getHighScores();
    StringBuilder scoreText = new StringBuilder("<html><h1>High Scores</h1>");

    for (Map.Entry<String, Integer> entry : scores.entrySet()) {
      scoreText.append(entry.getKey())
          .append(": ")
          .append(entry.getValue())
          .append("<br>");
    }

    scoreText.append("</html>");
    JLabel scoresLabel = new JLabel(scoreText.toString());
    add(scoresLabel, BorderLayout.CENTER);
    revalidate();
    repaint();
  }
}