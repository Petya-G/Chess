package main.java.com.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * A főmenüt megjelenítő panel.
 */
public class MainMenu extends JPanel {
  private String[] playerNames;

  /**
   * A menü gombot reprezentáló osztály.
   */
  public class MenuButton extends JButton {
    public MenuButton(String name, Window window) {
      super(name);
      this.setPreferredSize(new Dimension(150, 50));
      this.setFocusable(false);
      this.setBackground(new Color(0x81B64C));
      this.setForeground(Color.white);
    }
  }

  /**
   * A főmenü panel konstruktora.
   * 
   * @param window A főablak
   */
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
    loadButton.addActionListener(e -> {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(new File("."));
      fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PGN Files", "pgn"));

      int result = fileChooser.showOpenDialog(window);
      if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();
        String filePath = selectedFile.getAbsolutePath();
        System.out.println("Loading game from: " + filePath);

        window.createBoardPanel();
        window.boardPanel.boardCtrl.loadBoardFrom(selectedFile);
        window.showPanel("BoardPanel");
      } else {
        System.out.println("Load game canceled.");
      }
    });
    add(loadButton);

    JButton exitButton = new MenuButton("Load Game", window);
    exitButton.addActionListener(e -> System.exit(0));
    add(exitButton);
  }

  /**
   * Visszaadja a játékosok neveit.
   * 
   * @return A játékosok nevei
   */
  public String[] getPlayerNames() {
    return playerNames;
  }

  /**
   * A játékosok neveinek bekérését megvalósító metódus.
   * 
   * @return A játékosok nevei
   */
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

      return new String[] { player1, player2 };
    } else if (result == JOptionPane.CANCEL_OPTION) {
      return null;
    } else {
      return new String[] { "Player 1", "Player 2" };
    }
  }
}