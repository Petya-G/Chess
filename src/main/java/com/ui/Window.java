package main.java.com.ui;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * A főablakot reprezentáló osztály.
 */
public class Window extends JFrame {
  CardLayout cardLayout;
  JPanel mainPanel;
  static MainMenu mainMenu;
  BoardPanel boardPanel;
  HighScorePanel hsPanel;
  public String[] playerNames;

  public static Color tileYellow = new Color(0xEBECD0);
  public static Color tileGreen = new Color(0x779556);

  /**
   * A főablak konstruktora.
   */
  public Window() {
    this.setTitle("Chess");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.setSize(750, 500);
    this.playerNames = new String[] { "Player1", "Player2" };

    ImageIcon image = new ImageIcon("src/main/java/com/images/Chess_nlt45.png");
    this.setIconImage(image.getImage());
    this.getContentPane().setBackground(new Color(0x302E2B));

    cardLayout = new CardLayout();
    mainPanel = new JPanel(cardLayout);

    mainMenu = new MainMenu(this);
    mainPanel.add(mainMenu, "MainMenu");

    hsPanel = new HighScorePanel(this);
    mainPanel.add(hsPanel, "HighScores");

    add(mainPanel);
    this.setVisible(true);

    showPanel("MainMenu");
  }

  /**
   * A panel váltásáért felelős metódus.
   * 
   * @param name A panel neve
   */
  public void showPanel(String name) {
    cardLayout.show(mainPanel, name);
  }

  /**
   * A játéktábla paneljének létrehozása.
   */
  public void createBoardPanel() {
    playerNames = mainMenu.getPlayerNames();
    boardPanel = new BoardPanel(this);
    mainPanel.add(boardPanel, "BoardPanel");
  }

  
  /** 
   * @param args
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    Window window = new Window();
  }
}
