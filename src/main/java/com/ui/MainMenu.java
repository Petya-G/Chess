package main.java.com.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainMenu extends JPanel {
  private String[] playerNames;

  class MenuButton extends JButton {
    public MenuButton(String name, Window window) {
      super(name);
      this.setPreferredSize(new Dimension(150, 50));
      this.setFocusable(false);
      this.setBackground(new Color(0x81B64C));
      this.setForeground(Color.white);
    }
  }

  public MainMenu(Window window) {
    GridLayout gridLayout = new GridLayout(3, 3, 20, 20);
    setLayout(gridLayout);

    JButton startButton = new MenuButton("Start New Game", window);
    startButton.addActionListener(e -> {
      playerNames = promptForPlayerNames();
      if (playerNames != null) {
        window.createBoardPanel();
        window.showPanel("BoardPanel");
      }
    });

    add(startButton);

    JButton loadButton = new MenuButton("Load Game", window);
    loadButton.addActionListener(e -> window.showPanel("LoadPanel"));
    add(loadButton);

    JButton exitButton = new MenuButton("Load Game", window);
    exitButton.addActionListener(e -> System.exit(0));
    add(exitButton);
  }

  public String[] getPlayerNames() { return playerNames; }

  private String[] promptForPlayerNames() {
    JTextField player1Field = new JTextField();
    JTextField player2Field = new JTextField();

    JPanel panel = new JPanel(new GridLayout(0, 1)); // Vertical layout
    panel.add(new JLabel("Enter Player 1's name:"));
    panel.add(player1Field);
    panel.add(new JLabel("Enter Player 2's name:"));
    panel.add(player2Field);

    int result = JOptionPane.showConfirmDialog(this, panel, "Player Names",
                                               JOptionPane.OK_CANCEL_OPTION,
                                               JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      String player1 = player1Field.getText().trim();
      String player2 = player2Field.getText().trim();

      if (player1.isEmpty()) {
        player1 = "Player 1";
      }
      if (player2.isEmpty()) {
        player2 = "Player 2";
      }

      return new String[] {player1, player2};
    } else if (result == JOptionPane.CANCEL_OPTION) {
      return null;
    } else {
      return new String[] {"Player 1", "Player 2"};
    }
  }
}