package chatApp;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.ScrollPane;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dbManager.ChatDAO;
import dbManager.FriendListDTO;
import dbManager.Information;
import design.ChatListItem;
import design.FriendListItem;
import net.sf.jcarrierpigeon.WindowPosition;
import net.sf.jtelegraph.Telegraph;
import net.sf.jtelegraph.TelegraphQueue;
import net.sf.jtelegraph.TelegraphType;

public class UserPage {

	private JFrame frame;
	private final String signedInAs;
	private JTextField textField;
	private JPanel panel_2;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ChatDAO dao=new ChatDAO();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//UserPage window = new UserPage();
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserPage(String id) {
		signedInAs=id;
		initialize();
		System.out.println("Initialize Complete");
		
		connectSocket();
		new getNotified().start();
	}
	private void connectSocket(){
		try {
			socket=new Socket("192.168.0.17",12346);
			System.out.println("Server Accessed!");
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
			oos.writeObject(signedInAs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		//dao.logIn(signedInAs);
	}
	class getNotified extends Thread{
		public void run(){
			while(ois!=null){
				try {
					String notifymsg=(String)ois.readObject();
					if(!notifymsg.equals("refresh"))
						new Popup(notifymsg).start();
					//JOptionPane.showMessageDialog(new JFrame(), notifymsg);
					refreshChatList();
				} catch (Exception e) {//e.printStackTrace();
					System.out.println("¼ÒÄÏ Á¾·á");
				
				}
			}
		}
	}
	class Popup extends Thread{
		String popup;
		public Popup(String pop){
			popup=pop;
		}
		@Override
		public void run() {
			String []str=popup.split(" : ");
			Telegraph popup=new Telegraph("Message From "+str[0], str[1], TelegraphType.MESSAGE, WindowPosition.TOPRIGHT, 2000);
			TelegraphQueue tq=new TelegraphQueue();
			tq.add(popup);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 679, 580);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosing(e);
				dao.logOut(signedInAs);
				try {
					oos.flush();
					oos.close();
					ois.close();
					oos=null;
					ois=null;
					//socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				super.windowClosed(e);
				//dao.logIn(signedInAs);
			}
		});
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		JButton btnNewButton = new JButton("Friend List");
		btnNewButton.setFont(new Font("Verdana", Font.BOLD, 21));
		btnNewButton.setBackground(new Color(186, 85, 211));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		//btnNewButton.setIcon(new ImageIcon(UserPage.class.getResource("/img/friendlistEdited.png")));
		btnNewButton.setBounds(0, 0, 218, 70);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Chat List");
		btnNewButton_1.setFont(new Font("Verdana", Font.BOLD, 21));
		btnNewButton_1.setBackground(new Color(102, 255, 102));
		//btnNewButton_1.setIcon(new ImageIcon(UserPage.class.getResource("/img/chatedited.png")));
		btnNewButton_1.setBounds(216, 0, 229, 70);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Friend Search");
		btnNewButton_2.setBackground(new Color(135, 206, 235));
		btnNewButton_2.setFont(new Font("Verdana", Font.BOLD, 21));
		//btnNewButton_2.setIcon(new ImageIcon(UserPage.class.getResource("/img/searchfriendEdited.png")));
		btnNewButton_2.setBounds(435, 0, 235, 70);
		frame.getContentPane().add(btnNewButton_2);
		
		CardLayout cl=new CardLayout();
		JPanel panel = new JPanel();
		panel.setBounds(0, 74, 663, 467);
		frame.getContentPane().add(panel);
		panel.setLayout(cl);
		frame.setVisible(true);
		//JPanel panel_2=new JPanel();
		panel_2=new JPanel();
		panel_2.setBackground(new Color(204, 255, 204));
		panel_2.setBounds(0, 112, 663, 429);
		JPanel panel_3=new JPanel();
		panel_3.setBackground(new Color(0, 255, 255));
		panel_3.setBounds(0, 112, 663, 429);
		//JScrollPane scrollpanel=new JScrollPane();
		//panel.add(panel_1, "1");
		
		//panel.add(scrollpanel, "1");
		
		JPanel panel_1=new JPanel();
		//scrollpanel.setColumnHeaderView(panel_1);
		panel_1.setBackground(new Color(204, 255, 51));
		panel_1.setLayout(null);
		panel.add(panel_1, "1");
		
		JLabel lblNewLabel_1 = new JLabel("\uCE5C\uAD6C\uBAA9\uB85D");
		lblNewLabel_1.setForeground(new Color(0, 0, 0));
		lblNewLabel_1.setBackground(Color.BLACK);
		lblNewLabel_1.setFont(new Font("±¼¸²", Font.BOLD, 23));
		lblNewLabel_1.setBounds(12, 10, 299, 51);
		panel_1.add(lblNewLabel_1);
		ScrollPane sp=new ScrollPane();
		sp.add(panel_2);
		
		panel.add(sp, "2");
		//panel.add(panel_2, "2");
		panel_2.setLayout(null);//new BoxLayout(panel_2,BoxLayout.Y_AXIS));
		
		//JLabel lblNewLabel_3 = new JLabel("New label");
		//lblNewLabel_3.setIcon(new ImageIcon(UserPage.class.getResource("/img/pic01.gif")));
		//lblNewLabel_3.setBounds(104, 42, 444, 313);
		//panel_2.add(lblNewLabel_3);
		
		
		panel.add(panel_3, "3");
		panel_3.setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("±¼¸²", Font.PLAIN, 23));
		textField.setBounds(145, 47, 330, 51);
		panel_3.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Search ID");
		lblNewLabel.setForeground(new Color(255, 0, 102));
		lblNewLabel.setFont(new Font("±¼¸²", Font.BOLD, 23));
		lblNewLabel.setBounds(12, 47, 110, 51);
		panel_3.add(lblNewLabel);
		
		JButton btnNewButton_3 = new JButton("Search");
		btnNewButton_3.setForeground(new Color(255, 255, 255));
		btnNewButton_3.setFont(new Font("±¼¸²", Font.BOLD, 23));
		btnNewButton_3.setBackground(new Color(153, 0, 255));
		btnNewButton_3.setBounds(519, 47, 110, 51);
		panel_3.add(btnNewButton_3);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setFont(new Font("MV Boli", Font.BOLD, 24));
		lblNewLabel_2.setBounds(112, 148, 340, 60);
		//panel_3.add(lblNewLabel_2);
		
		JButton btnNewButton_4 = new JButton("Add");
		btnNewButton_4.setBackground(new Color(173, 216, 230));
		btnNewButton_4.setFont(new Font("±¼¸²", Font.BOLD, 24));
		btnNewButton_4.setBounds(488, 148, 97, 60);
		//panel_3.add(btnNewButton_4);
		btnNewButton_3.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String id=textField.getText();
				if(!id.equals(signedInAs)){
				//if(id==null||id.trim()==null)  ¿Ö ¾ÊµÇ´Â Áö Á¤È®È÷ ºÒ¸íÈ®
					if(id.length()==0||id.trim().length()==0){
					JOptionPane.showMessageDialog(frame, "Type in a user's ID");
				}else{
					ChatDAO dao=new ChatDAO();
					String name=dao.dbSearchFriend(id);
					if(name==null){
						JOptionPane.showMessageDialog(frame, "There were no matching IDs");
					}else{
						if(panel_3.getComponentCount()!=3){
							panel_3.remove(lblNewLabel_2);
							panel_3.remove(btnNewButton_4);
							panel_3.repaint();
						}	
						lblNewLabel_2.setText(name);
						panel_3.add(lblNewLabel_2);
						panel_3.add(btnNewButton_4);
						panel_3.repaint();
					}
				}
				}
				else
					JOptionPane.showMessageDialog(frame, "You Cannot add yourself as a friend");
			}			
		});
		btnNewButton_4.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] info={signedInAs,textField.getText(),lblNewLabel_2.getText()};
				ChatDAO dao=new ChatDAO();
				if(dao.doubleCheck(signedInAs, lblNewLabel_2.getText())){
				dao.friendListInsert(info);
				JOptionPane.showMessageDialog(frame, "You have added "+lblNewLabel_2.getText()+" as your friend");
				}else{
					JOptionPane.showMessageDialog(frame, lblNewLabel_2.getText()+" is already your friend");
				}
				panel_3.remove(lblNewLabel_2);
				panel_3.remove(btnNewButton_4);
				panel_3.repaint();
			}			
		});
		cl.show(panel, "2");
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		panel.add(panel_4, "4");
		panel_4.setLayout(null);
		
		JLabel label = new JLabel("");
		label.setBounds(74, 10, 512, 313);
		panel_4.add(label);
		
		JLabel lblNameGoesHere = new JLabel("Name Goes Here");
		lblNameGoesHere.setFont(new Font("±¼¸²", Font.BOLD, 20));
		lblNameGoesHere.setBounds(242, 349, 223, 39);
		panel_4.add(lblNameGoesHere);
		
		JButton btnChat = new JButton("Chat");
		btnChat.setFont(new Font("±¼¸²", Font.BOLD, 18));
		btnChat.setBounds(141, 393, 97, 39);
		panel_4.add(btnChat);
		
		JButton btnEditProfile = new JButton("Edit Profile");
		btnEditProfile.setFont(new Font("±¼¸²", Font.BOLD, 18));
		btnEditProfile.setBounds(390, 393, 151, 39);
		panel_4.add(btnEditProfile);
		btnNewButton.doClick();
		btnNewButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ChatDAO dao=new ChatDAO();
				//ArrayList<FriendListDTO> list=dao.getFriendList(signedInAs);
				ArrayList<Information> list=dao.getFriendList_new(signedInAs);
				if(list==null){
					JLabel lb=new JLabel("No Friends are on your FriendList");
					lb.setFont(new Font("±¼¸²", Font.BOLD, 20));
					lb.setBounds(0, 71, 419, 38);
				}else{
					int y_axis=71;
					Information inform=dao.getMyProfile(signedInAs);
					JPanel myPanel=new FriendListItem(inform);
					myPanel.setBounds(0, y_axis, 580, 64);
					y_axis+=64;
					myPanel.setBackground(new Color(246,247,188));
					panel_1.add(myPanel);
					
					panel.addMouseListener(new MouseAdapter(){
						public void mouseClicked(MouseEvent arg0) {
							cl.show(panel, "4");
							Image img=inform.icon.getImage();
							ImageIcon icon=new ImageIcon(img.getScaledInstance(512, 313, Image.SCALE_DEFAULT));
							label.setIcon(icon);
							lblNameGoesHere.setText(inform.info[2]);
							panel_4.repaint();
						}
					});
					for(int num=0;num<list.size();num++){
						//list.get(num).setFriendName(list.get(num).getFriendName());
						//JButton friendButton = new JButton(list.get(num).getFriendName());
						//friendButton.setForeground(new Color(255, 255, 255));
						//friendButton.setBackground(new Color(153, 153, 255));
						//friendButton.setHorizontalAlignment(SwingConstants.LEFT);
						//friendButton.setFont(new Font("±¼¸²", Font.BOLD, 20));
						JPanel fpanel=new FriendListItem(list.get(num));
						fpanel.setBounds(0, y_axis, 580, 64);
						y_axis+=64;
						panel_1.add(fpanel);
						final int num_copy=num;
						fpanel.addMouseListener(new MouseAdapter(){
							public void mouseClicked(MouseEvent arg0) {
								cl.show(panel, "4");
								Image img=list.get(num_copy).icon.getImage();
								ImageIcon icon=new ImageIcon(img.getScaledInstance(512, 313, Image.SCALE_DEFAULT));
								label.setIcon(icon);
								lblNameGoesHere.setText(list.get(num_copy).info[2]);
								panel_4.repaint();
								btnChat.addActionListener(new ActionListener(){
									@Override
									public void actionPerformed(ActionEvent arg0) {
										FriendListDTO dto=new FriendListDTO();
										dto.setId(signedInAs);
										dto.setFriendName(list.get(num_copy).info[2]);
										dto.setFriendID(list.get(num_copy).info[1]);
										new ChatPage(dto);	
									}
								}
								);
							}
							@Override
							public void mouseEntered(MouseEvent e) {
								fpanel.setBackground(new Color(246,247,188));
							}
							public void mouseExited(MouseEvent e) {
								fpanel.setBackground(new Color(255,255,255));
							}
						});
						/*
						panel.addActionListener(new ActionListener(){
							@Override
							public void actionPerformed(ActionEvent arg0) {
								int pos=0;
								for(int num=0;num<list.size();num++){
									if(list.get(num).getFriendName().equals(friendButton.getText())){
										pos=num;
									}
								}
								new ChatPage(list.get(pos));
							}
						});*/
					}
				}
				panel_1.repaint();
				cl.show(panel, "1");
			}
		});
		btnNewButton_1.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				cl.show(panel, "2");
				panel_2.removeAll();
				ChatDAO dao=new ChatDAO();
		// old Version		ArrayList<String[]> chatList=dao.selectChatList(signedInAs);
				ArrayList<Information> chatList=dao.selectChatList(signedInAs);
				int y=0;//112;
				for(Information info:chatList){
					JPanel jp=new ChatListItem(info);
					jp.setBounds(0, y, 655,106);
					jp.addMouseListener(new MouseAdapter(){
						@Override
						public void mouseClicked(MouseEvent e) {
							FriendListDTO dto=new FriendListDTO();
							dto.setId(signedInAs);
							dto.setFriendID(info.info[3]);
							dto.setFriendName(info.info[0]);
							new ChatPage(dto);
							
							//refreshChatList();
							
						}
						
						public void mouseEntered(MouseEvent arg0) {
							jp.setBackground(Color.GRAY);
						};
						public void mouseExited(MouseEvent e) {
							jp.setBackground(SystemColor.textHighlightText);
						};
						
					});
					panel_2.add(jp);
					y+=106;
				}
				panel_2.setPreferredSize(new Dimension(500,y));
				panel_2.repaint();
				
			}
		});
		btnNewButton_2.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				cl.show(panel, "3");
			}
		});
		refreshChatList();
	}
	private void refreshChatList(){
		panel_2.removeAll();
		ChatDAO dao=new ChatDAO();
// old Version		ArrayList<String[]> chatList=dao.selectChatList(signedInAs);
		ArrayList<Information> chatList=dao.selectChatList(signedInAs);
		int y=0;//112;
		for(Information info:chatList){
			JPanel jp=new ChatListItem(info);
			jp.setBounds(0, y, 655,106);
			jp.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					FriendListDTO dto=new FriendListDTO();
					dto.setId(signedInAs);
					dto.setFriendID(info.info[3]);
					dto.setFriendName(info.info[0]);
					new ChatPage(dto);
					
				}
				
				public void mouseEntered(MouseEvent arg0) {
					jp.setBackground(Color.GRAY);
				};
				public void mouseExited(MouseEvent e) {
					jp.setBackground(SystemColor.textHighlightText);
				};
				
			});
			panel_2.add(jp);
			y+=106;
		}
		panel_2.setPreferredSize(new Dimension(500,y));
		panel_2.repaint();
	}
}
