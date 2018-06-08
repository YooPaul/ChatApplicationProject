package chatApp;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dbManager.ChatDAO;
import dbManager.ChatDTO;
import javax.swing.UIManager;

public class Register {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_2;
	private JTextField textField_3;
	private JPasswordField passwordField;
	//private boolean photoCheck=true;
	private String defaultPic="/img/defaultPic.jpg";
	private String picture=defaultPic;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Register window = new Register();
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
	public Register() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 872, 524);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblRegister = new JLabel("Register");
		lblRegister.setBackground(new Color(0, 102, 255));
		lblRegister.setForeground(new Color(255, 51, 51));
		lblRegister.setFont(new Font("±¼¸²", Font.BOLD, 29));
		lblRegister.setBounds(81, 47, 238, 99);
		frame.getContentPane().add(lblRegister);
		
		JLabel lblNewLabel = new JLabel("ID");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("±¼¸²", Font.BOLD, 24));
		lblNewLabel.setBounds(81, 135, 177, 59);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("±¼¸²", Font.BOLD, 24));
		lblNewLabel_1.setBounds(81, 208, 173, 59);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Name");
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setFont(new Font("±¼¸²", Font.BOLD, 24));
		lblNewLabel_2.setBounds(81, 277, 173, 59);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Email");
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setFont(new Font("±¼¸²", Font.BOLD, 24));
		lblNewLabel_3.setBounds(81, 346, 173, 59);
		frame.getContentPane().add(lblNewLabel_3);
		
		textField = new JTextField();
		textField.setFont(new Font("±¼¸²", Font.PLAIN, 24));
		textField.setBounds(321, 142, 317, 46);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("±¼¸²", Font.PLAIN, 24));
		textField_2.setBounds(321, 285, 317, 44);
		frame.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setFont(new Font("±¼¸²", Font.PLAIN, 24));
		textField_3.setBounds(321, 353, 317, 46);
		frame.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnNewButton = new JButton("Register");
		btnNewButton.setFont(new Font("±¼¸²", Font.PLAIN, 24));
		btnNewButton.setBackground(new Color(0, 255, 204));
		btnNewButton.setBounds(697, 290, 132, 91);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String t1=textField.getText();
				@SuppressWarnings("deprecation")
				String t2=passwordField.getText();
				String t3=textField_2.getText();
				String t4=textField_3.getText();
				if(t1!=null&&t1.trim().length()>2&&t2!=null&&t2.trim().length()>2&&t3!=null&&t3.trim().length()>2
					&&t4!=null&&t4.trim().length()>2){
					ChatDTO dto=new ChatDTO();
					ChatDAO dao=new ChatDAO();
					dto.setId(t1);
					dto.setPassword(t2);
					dto.setName(t3);
					dto.setEmail(t4);
					dto.setFile(new File(picture));
					dao.dbRegisterUser(dto);
					frame.dispose();
					new MainLogin();
				}
				else{
					//JOptionPane.showConfirmDialog(frame,"Type in Informations Correctly", "Register", JOptionPane.OK_OPTION);
					JOptionPane.showMessageDialog(frame, "Type in Informations Correctly");
					textField.setText("");
					textField_2.setText("");
					textField_3.setText("");
					passwordField.setText("");
				}
			}
		});
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("±¼¸²", Font.PLAIN, 24));
		passwordField.setBounds(321, 216, 317, 44);
		frame.getContentPane().add(passwordField);
		
		//JLabel lblNewLabel_4 = new JLabel("New label");
		//lblNewLabel_4.setIcon(new ImageIcon(Register.class.getResource("/img/Ghibli2.png")));
		//lblNewLabel_4.setBounds(0, -12, 866, 505);
		//frame.getContentPane().add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("Profile Picture");
		lblNewLabel_5.setForeground(new Color(255, 255, 255));
		lblNewLabel_5.setFont(new Font("±¼¸²", Font.BOLD, 24));
		lblNewLabel_5.setBounds(81, 415, 177, 38);
		frame.getContentPane().add(lblNewLabel_5);
		
		
		
		
		
		JButton btnNewButton_1 = new JButton("Upload Photo");
		btnNewButton_1.setBackground(new Color(0, 153, 255));
		btnNewButton_1.setForeground(UIManager.getColor("Button.highlight"));
		btnNewButton_1.setFont(new Font("±¼¸²", Font.BOLD, 20));
		btnNewButton_1.setBounds(354, 417, 226, 38);
		btnNewButton_1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				FileDialog fd=new FileDialog(new JFrame(), "Upload Photo", FileDialog.LOAD);
				fd.setVisible(true);
				//fd.setFile("*.jpg;*.png;*.gif;*.jpeg");
				if(fd.getDirectory()==null||fd.getFile()==null){
					JOptionPane.showMessageDialog(null, "No file was selected\nDefault Profile will be used");
					picture=defaultPic;
				}
				else{
				if(fd.getFile().endsWith("png")||fd.getFile().endsWith("gif")||fd.getFile().endsWith("jpeg")||fd.getFile().endsWith("jpg")){
					JOptionPane.showMessageDialog(null, "File upload Successful");
					picture=fd.getDirectory()+"\\"+fd.getFile();
				}else{
					JOptionPane.showMessageDialog(null, "Not an appropriate file type\nDefault Profile will be used");
					picture=defaultPic;
				}
				}
			}
			
		});
		frame.getContentPane().add(btnNewButton_1);
		
		JLabel lblNewLabel_6 = new JLabel("Upload Your Profile Picture");
		lblNewLabel_6.setForeground(new Color(255, 255, 255));
		lblNewLabel_6.setFont(new Font("±¼¸²", Font.BOLD, 16));
		lblNewLabel_6.setBounds(592, 417, 244, 40);
		frame.getContentPane().add(lblNewLabel_6);
		
		JLabel lblNewLabel_4 = new JLabel("New label");
		lblNewLabel_4.setIcon(new ImageIcon(Register.class.getResource("/img/Ghibli2.png")));
		lblNewLabel_4.setBounds(0, -12, 866, 505);
		frame.getContentPane().add(lblNewLabel_4);
		frame.setVisible(true);
		//frame.repaint();
	}
}
