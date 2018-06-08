package design;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JLabel;

public class Bubble extends JLabel {

	public final int default_height=30; 
	public final int default_arc_height=40;
	public final int default_arc_width=40;
	public String text;
	public final int margin=20;
	public final int padding=19;
	//public final int max_width;
	//public static int dynamic_width;
	//public static int dynamic_height;
	public int dynamic_width;
	public int dynamic_height;
	private final int char_to_pix_en=7;
	private final int char_to_pix_kr=10;
	int user;
	String[] multitext;
	int lineCnt;
	public Bubble(String text,int lineCnt,int user){
		this.text=text;
		multitext=text.split("\n");
		/*
		int a=text.charAt(0);
		if(a<127)
			dynamic_width=multitext[0].length()*char_to_pix_en+margin*2;
		else 
			dynamic_width=multitext[0].length()*char_to_pix_kr+margin*2;
			*/
		JLabel lab=new JLabel();
		FontMetrics metrics=lab.getFontMetrics(lab.getFont());//getFontMetrics(getGraphics().getFont());
		int height=metrics.getHeight();
		int width=metrics.stringWidth(multitext[0]);
		dynamic_width=width+margin*2;
		//System.out.println(dynamic_width);
		
		dynamic_height=lineCnt*default_height;
		if(lineCnt>1)
			dynamic_height=lineCnt*height+8*2;
			//dynamic_height=(int)(lineCnt*default_height*0.8);
			  
			this.lineCnt=lineCnt; 
		this.user=user;
	}
	
	public void calcMetrics(FontMetrics metrics){
		int height=metrics.getHeight();
		int width=metrics.stringWidth(multitext[0]);
		System.out.println(height+"  and  " +width);
		dynamic_width=width+(int)(margin*2.5);
		//System.out.println(dynamic_width);
		
		dynamic_height=lineCnt*default_height;
		if(lineCnt>1)
			dynamic_height=lineCnt*height+8*2;
	}
	/*
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		g.setColor(Color.white);
		g.drawRoundRect(0, 0,200 , 30, 40, 40);
		g.fillRoundRect(0, 0,200 , 30, 40, 40);
		g.setColor(Color.BLACK);
		g.drawString(text,20 ,19 );
		
	}*/
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//calcMetrics(g.getFontMetrics(g.getFont()));
		if(user==0)
			g.setColor(Color.white);
		else
			g.setColor(Color.YELLOW);
		g.drawRoundRect(0, 0,dynamic_width , dynamic_height, 40, 40);
		g.fillRoundRect(0, 0,dynamic_width , dynamic_height, 40, 40);
		g.setColor(Color.BLACK);
		for(int i=0;i<multitext.length;i=i+1){
			g.drawString(multitext[i],20 ,19+(i*15) );
		}
		
	}

}
