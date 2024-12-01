package main.java.com.ui;

import java.awt.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import main.java.com.highscore.HighScoreManager;

/**
 * Toplista megjelnítéséért felelős panel.
 */
public class HighScorePanel extends JPanel {
  public HighScoreManager highScoreManager;

  /**
   * HighScorePanel konstruktora.
   */
  public HighScorePanel() {
    this.highScoreManager = new HighScoreManager("highscores.json");
    setLayout(new BorderLayout());
    updateDisplay();
  }

  /**
   * Frissíti a toplista panelt
   */
  public void updateDisplay() {
    removeAll();
    Map<String, Float> scores = highScoreManager.getHighScores();

    DefaultTableModel model = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    model.addColumn("Player");
    model.addColumn("Score");

    for (Map.Entry<String, Float> entry : scores.entrySet()) {
      model.addRow(new Object[] { entry.getKey(), entry.getValue() });
    }

    JTable scoresTable = new JTable(model);
    scoresTable.setFillsViewportHeight(true);

    JScrollPane scrollPane = new JScrollPane(scoresTable);
    add(scrollPane, BorderLayout.CENTER);

    revalidate();
    repaint();
  }
}