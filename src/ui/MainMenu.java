package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MainMenu extends JPanel {
	class MenuButton extends JButton {
		public MenuButton(String name, Window window, String scene) {
			super(name);
			this.setPreferredSize(new Dimension(150, 50));
			this.setFocusable(false);
			this.setBackground(new Color(0x81B64C));
			this.setForeground(Color.white);
			this.addActionListener(e -> window.showPanel(scene));
		}
	}

	public MainMenu(Window window) {
		GridLayout gridLayout = new GridLayout(3, 3, 20, 20);
		setLayout(gridLayout);

		JButton startButton = new MenuButton("Start New Game", window, "BoardPanel");
		add(startButton);

		JButton loadButton = new MenuButton("Load Game", window, "Load");
		add(loadButton);

		JButton exitButton = new MenuButton("Load Game", window, "Exit");
		add(exitButton);
	}
}
