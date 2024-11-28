package main.java.com.ui;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
  CardLayout cardLayout;
  JPanel mainPanel;
  static MainMenu mainMenu;
  BoardPanel boardPanel;
  HighScorePanel hsPanel;

  static Color tileYellow = new Color(0xEBECD0);
  static Color tileGreen = new Color(0x779556);

  public Window() {
    this.setTitle("Chess");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.setSize(750, 500);

    ImageIcon image = new ImageIcon("src/images/Chess_nlt45.png");
    this.setIconImage(image.getImage());
    this.getContentPane().setBackground(new Color(0x302E2B));

    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    mainMenu = new MainMenu(this);
    mainPanel.add(mainMenu, "MainMenu");

    //    hsPanel = new HighScorePanel();
    //   mainPanel.add(hsPanel, "HighScores");

    add(mainPanel);
    this.setVisible(true);

    showPanel("MainMenu");
  }

  public static String[] playerNames;

  public void showPanel(String name) { cardLayout.show(mainPanel, name); }

  public void createBoardPanel() {
    playerNames = mainMenu.getPlayerNames();
    if (playerNames != null) {
      boardPanel = new BoardPanel(this);
      mainPanel.add(boardPanel, "BoardPanel");
    }
  }

  public static void main(String[] args) { Window window = new Window(); }
}
