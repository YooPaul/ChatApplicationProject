package design;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;

import dbManager.Information;
public class ChatListItem extends JPanel{
	
	public ChatListItem(Information info){   // old Version      String[] info){
		setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		setBackground(SystemColor.textHighlightText);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel(info.info[0]);
		lblNewLabel.setFont(new Font("Segoe UI Semilight", Font.BOLD, 20));
		lblNewLabel.setBounds(111, 10, 153, 40);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel(info.info[1]);
		lblNewLabel_1.setForeground(SystemColor.textInactiveText);
		lblNewLabel_1.setFont(new Font("¸¼Àº °íµñ", Font.PLAIN, 18));
		lblNewLabel_1.setBounds(111, 47, 340, 42);
		add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel(info.info[2]);
		lblNewLabel_2.setBounds(457, 27, 171, 15);
		add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel();
		lblNewLabel_3.setBounds(12, 10, 87, 86);
		lblNewLabel_3.setIcon(info.icon);
		add(lblNewLabel_3);
		
		if(info.info.length==6){
		if(!info.info[5].equals("0")){
			JLabel label = new Alert(info.info[5]);
			label.setBounds(368, 18, 36, 32);
			add(label);
		}
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
