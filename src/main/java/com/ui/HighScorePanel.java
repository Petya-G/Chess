package main.java.com.ui;

import java.awt.*;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
      model.addRow(new Object[] {entry.getKey(), entry.getValue()});
    }

    JTable scoresTable = new JTable(model);
    scoresTable.setFillsViewportHeight(true);

    JScrollPane scrollPane = new JScrollPane(scoresTable);
    add(scrollPane, BorderLayout.CENTER);

    revalidate();
    repaint();
  }
}