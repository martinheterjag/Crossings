package se.kth.martine7;

import java.awt.Dimension;

import javax.swing.JFrame;

import se.kth.martine7.JunctionPanel;

public class Junction {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame frame = new JFrame("Junction");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		JunctionPanel junction = new JunctionPanel(frame);
		frame.add(junction);
		frame.pack();
		frame.setVisible(true);
		junction.startAnimation();
	}

}
