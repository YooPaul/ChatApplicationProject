package design;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

public class Alert extends JLabel {

	String num;
	public Alert(String num){
		this.num=num;
	}
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.setColor(Color.RED);
		g.drawOval(0, 0, 36, 32);
		g.fillOval(0, 0, 36,32);
		g.setColor(Color.WHITE);
		g.drawString(num, 15, 21);
	}
}
