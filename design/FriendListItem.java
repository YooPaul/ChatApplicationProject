package design;

import javax.swing.JPanel;

import dbManager.Information;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.LineBorder;

public class FriendListItem extends JPanel {

	public FriendListItem(Information info){
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBackground(Color.WHITE);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel();
		lblNewLabel.setBounds(12, 10, 60, 42);
		lblNewLabel.setIcon(info.icon);
		add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel(info.info[2]);
		lblNewLabel_1.setFont(new Font("±¼¸²", Font.BOLD, 17));
		lblNewLabel_1.setBounds(111, 10, 171, 30);
		add(lblNewLabel_1);
		
	}

}
