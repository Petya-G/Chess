package main.java.com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class LoadPanel extends JPanel {
	public LoadPanel(Window window) {
		GridLayout gridLayout = new GridLayout(3, 3, 50, 50);
		setLayout(gridLayout);

        JButton startButton = new JButton("Load Game");
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.setFocusable(false);
        startButton.setBackground(new Color(0x81B64C));
        startButton.setForeground(Color.white);
        startButton.addActionListener(e -> window.showPanel("Board"));

        add(startButton, BorderLayout.CENTER);
	}
}
