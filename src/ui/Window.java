package ui;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Window extends JFrame {
	public Window() {
		this.setTitle("Chess");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(500,500);
		this.setVisible(true);
		
		ImageIcon image = new ImageIcon("src/images/Chess_nlt45.png");
		this.setIconImage(image.getImage());
		this.getContentPane().setBackground(new Color(0x302E2B));
	}
	
	public static void main(String[] args) {
		Window window = new Window();
	}
}
