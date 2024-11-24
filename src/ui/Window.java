package ui;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
	CardLayout cardLayout;
	JPanel mainPanel;
	JPanel mainMenu;

	static Color tileYellow = new Color(0xEBECD0);
	static Color tileGreen = new Color(0x779556);

	public Window(BoardPanel boardPanel) {
		this.setTitle("Chess");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(500,500);
		
		ImageIcon image = new ImageIcon("src/images/Chess_nlt45.png");
		this.setIconImage(image.getImage());
		this.getContentPane().setBackground(new Color(0x302E2B));
		
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		mainMenu = new MainMenu(this);
		mainPanel.add(mainMenu, "MainMenu");
		mainPanel.add(boardPanel, "BoardPanel");
		add(mainPanel);
		this.setVisible(true);
		
		showPanel("MainMenu");
	}

	public void showPanel(String name) {
		cardLayout.show(mainPanel, name);
	}
}
