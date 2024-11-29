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
    Map<String, Integer> scores = highScoreManager.getHighScores();

    // Custom TableModel that makes cells uneditable
    DefaultTableModel model = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false; // All cells are non-editable
      }
    };

    model.addColumn("Player");
    model.addColumn("Score");

    // Populate the table with high score data
    for (Map.Entry<String, Integer> entry : scores.entrySet()) {
      model.addRow(new Object[] {entry.getKey(), entry.getValue()});
    }

    // Create the table with the model
    JTable scoresTable = new JTable(model);
    scoresTable.setFillsViewportHeight(
        true); // Make the table fill the viewport

    // Add the table to a JScrollPane for better usability
    JScrollPane scrollPane = new JScrollPane(scoresTable);
    add(scrollPane, BorderLayout.CENTER);

    revalidate(); // Revalidate the panel to reflect changes
    repaint();    // Repaint the panel
  }
}