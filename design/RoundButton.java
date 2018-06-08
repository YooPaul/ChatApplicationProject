package design;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JFrame;


public class RoundButton extends JButton {

	
	public RoundButton() {
		setSize(60,60);
		
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		//g.clearRect(0, 0, 60, 60);
		g.drawOval(0, 0, 60, 60);
		g.setColor(Color.blue);
		g.fillOval(0, 0, 60, 60);
		
	}
	public static void main(String[] args){
		JFrame f=new JFrame();
		f.setSize(100, 100);
		f.setLayout(null);
		RoundButton rb=new RoundButton();
		rb.setBounds(10, 10,60,60);
		f.add(rb);
		f.setVisible(true);
	}

	
	
}
