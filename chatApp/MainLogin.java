package chatApp;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dbManager.ChatDAO;
import dbManager.ChatDTO;

public class MainLogin {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainLogin window = new MainLogin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainLogin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 255, 204));
		frame.setBounds(100, 100, 616, 483);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JLabel lblLogin = new JLabel("ID");
		lblLogin.setFont(new Font("±¼¸²", Font.PLAIN, 24));
		lblLogin.setBounds(92, 232, 104, 48);
		frame.getContentPane().add(lblLogin);
		
		JLabel lblPassword = new JLabel("PassWord");
		lblPassword.setFont(new Font("±¼¸²", Font.PLAIN, 24));
		lblPassword.setBounds(59, 303, 112, 48);
		frame.getContentPane().add(lblPassword);
		
		textField = new JTextField();
		textField.setFont(new Font("±¼¸²", Font.PLAIN, 22));
		textField.setBounds(208, 237, 197, 41);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(208, 311, 197, 41);
		frame.getContentPane().add(passwordField);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.setBackground(new Color(0, 204, 255));
		btnNewButton.setFont(new Font("±¼¸²", Font.PLAIN, 20));
		btnNewButton.setBounds(453, 278, 118, 73);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener(){
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				ChatDAO dao=new ChatDAO();
				if(dao.loginCheck(textField.getText(), passwordField.getText())){
					String id=textField.getText();
					frame.dispose();
					new UserPage(id);
				}else
					JOptionPane.showMessageDialog(frame, "ID and Password mismatch or not an existing ID");
					passwordField.setText("");
			}
		});
		
		JButton btnNewButton_1 = new JButton("Register");
		btnNewButton_1.setBackground(new Color(0, 255, 102));
		btnNewButton_1.setForeground(new Color(0, 0, 0));
		btnNewButton_1.setFont(new Font("±¼¸²", Font.PLAIN, 20));
		btnNewButton_1.setBounds(59, 361, 104, 50);
		frame.getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int a=JOptionPane.showConfirmDialog(frame, "Would you like to register?","Register" ,JOptionPane.OK_CANCEL_OPTION);
				if(a==0){
					frame.dispose();
					new Register();
				}
			}
		});
		
		JLabel label = new JLabel("\uC544\uB974\uCC57");
		label.setForeground(new Color(0, 153, 255));
		label.setBackground(new Color(255, 255, 153));
		label.setFont(new Font("±¼¸²", Font.BOLD, 31));
		label.setBounds(74, 27, 255, 73);
		frame.getContentPane().add(label);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(MainLogin.class.getResource("/img/Ghibli1.jpg")));
		lblNewLabel.setBounds(253, 27, 322, 164);
		frame.getContentPane().add(lblNewLabel);
		
		
	}
}
