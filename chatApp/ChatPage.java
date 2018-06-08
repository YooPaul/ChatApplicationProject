package chatApp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import dbManager.ChatDAO;
import dbManager.ChatMsgDTO;
import dbManager.FriendListDTO;
import design.Bubble;
public class ChatPage extends Thread {

	private JFrame frame;
	private String userID;
	private String friendName;
	private FriendListDTO dto;
	private JTextField textField;
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private ArrayList<String[]> list;
	private ChatMsgDTO chatdto=new ChatMsgDTO();
	private ChatMsgDTO lastseenmsg=new ChatMsgDTO();
	JTextArea textArea = new JTextArea();
	private SimpleDateFormat sdf=new SimpleDateFormat("yy/MM/dd hh:mm:ss");
	JTextArea textarea;
	JPanel panel=new JPanel();
	private int y=67;
	boolean isAFriend=true;
	ChatDAO dao=new ChatDAO();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//ChatPage window = new ChatPage();
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	
	public ChatPage(FriendListDTO dto) {
		this.dto=dto;
		chatdto.setId(dto.getId());
		chatdto.setFriendid(dto.getFriendID());
		String ip="192.168.0.17";
		try {
			socket=new Socket(ip, 12345);
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
			oos.writeObject(chatdto);
			list=(ArrayList<String[]>) ois.readObject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("ChatPage 생성자에서 error");
		} 
		isAFriend=new ChatDAO().checkFriended(dto.getId(), dto.getFriendID());
		//if(!isAFriend)
		// -->수정 	list=new ChatDAO().getMsgfromUnknown(chatdto);
		initialize();
		Thread t1=new Thread(this);
		t1.start();
		
	}

	
	/**
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 531, 566);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.setFont(new Font("굴림", Font.BOLD, 23));
		btnNewButton.setBackground(new Color(255, 255, 102));
		btnNewButton.setBounds(371, 402, 144, 125);
		frame.getContentPane().add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				//textArea.append(chatdto.getId()+" : "+textField.getText()+"\n");
				if(!textarea.getText().trim().equals("")){
				Bubble lab=new Bubble(textarea.getText(),textarea.getLineCount(),1);
				//lab.setBounds(10, y,lab.dynamic_width, lab.dynamic_height);
        		lab.setBounds(490-10-lab.dynamic_width, y,lab.dynamic_width, lab.dynamic_height);

				panel.add(lab);
	        	//panel.repaint(10, y,Bubble.dynamic_width, Bubble.dynamic_height);
	        	y+=lab.dynamic_height+10;
	        	panel.setPreferredSize(new Dimension(480,y));
	        	panel.repaint();
	        	lastseenmsg.setChatMsgDTO(dto.getId(), dto.getFriendID(),textarea.getText(),sdf.format(Calendar.getInstance().getTime()));
	        	dao.updateLastSeenMsg(lastseenmsg);
				chatdto.setMsg(textarea.getText());
				//System.out.println(chatdto.getId()+"\t"+chatdto.getFriendid()+"\t"+chatdto.getMsg());
				try {
					oos.writeObject(chatdto.getMsg());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					System.out.println("버튼에서 에러");
				}
				//lastseenmsg.setChatMsgDTO(dto.getId(), dto.getFriendID(),textarea.getText(),sdf.format(Calendar.getInstance().getTime()));
				textarea.setText("");
			}
			}		
		});
		
		textarea=new JTextArea();
		textarea.setFont(new Font("굴림", Font.BOLD, 20));
		textarea.setBounds(0, 402, 371, 125);
		frame.getContentPane().add(textarea);
		//textField = new JTextField();
		//textField.setFont(new Font("굴림", Font.BOLD, 20));
		//textField.setBounds(0, 402, 371, 125);
		//frame.getContentPane().add(textField);
		textarea.setColumns(10);
		textarea.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent arg0) {
				String str=textarea.getText();
				
				//System.out.println(str);
				//System.out.println(str.length());
				if(!str.trim().equals("")){
				if(str.length()%33==0 &&str.length()!=0){
					textarea.append("\n");
					
				}
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					chatdto.setMsg(textarea.getText());
					
					//textArea.append(chatdto.getId()+" : "+textarea.getText()+"\n");
					Bubble lab=new Bubble(textarea.getText(),textarea.getLineCount(),1);
					//lab.setBounds(10, y,lab.dynamic_width, lab.dynamic_height);
	        		lab.setBounds(490-10-lab.dynamic_width, y,lab.dynamic_width, lab.dynamic_height);

					panel.add(lab);
		        	
		        	//panel.repaint(10, y,lab.dynamic_width, lab.dynamic_height);
		        	y+=lab.dynamic_height+10;
		        	//System.out.println(y);
		        	panel.setPreferredSize(new Dimension(480,y));
		        	panel.repaint();
		        	lastseenmsg.setChatMsgDTO(dto.getId(), dto.getFriendID(),textarea.getText(),sdf.format(Calendar.getInstance().getTime()));
					dao.updateLastSeenMsg(lastseenmsg);
		        	//System.out.println(chatdto.getId()+"\t"+chatdto.getFriendid()+"\t"+chatdto.getMsg());
					textarea.setText(null);
					
					
					try {
						oos.writeObject(chatdto.getMsg());
						//oos.writeUTF(chatdto.getMsg());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						System.out.println("텍스트 필드에서 에러");
					}
				}
			}
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER){
					textarea.setText("");
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		/*
		textField.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chatdto.setMsg(textField.getText());
				textArea.append(chatdto.getId()+" : "+textField.getText()+"\n");
				System.out.println(chatdto.getId()+"\t"+chatdto.getFriendid()+"\t"+chatdto.getMsg());
				textField.setText("");
				try {
					oos.writeObject(chatdto.getMsg());
					//oos.writeUTF(chatdto.getMsg());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					System.out.println("텍스트 필드에서 에러");
				}
			}
		});*/
		
		JLabel lblNewLabel = new JLabel(dto.getFriendName()+"님 과의 대화");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setBackground(new Color(0, 204, 255));
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 17));
		lblNewLabel.setBounds(0, 0, 515, 37);
		frame.getContentPane().add(lblNewLabel);
		
		panel.setBackground(new Color(51, 204, 255));
		panel.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 21));
		panel.setBounds(0, 47, 515, 355);
		panel.setLayout(null);
		ScrollPane sp=new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
	        sp.add(panel);
	    panel.setPreferredSize(new Dimension(500,0));    
	    
	    JLabel label = new JLabel("\uB808\uC774\uBE14 \uC785\uB2C8\uB2E4");
	    label.setBounds(200, 10, 120, 15);
	    panel.add(label);
		//textArea.setBackground(new Color(51, 204, 255));
		//textArea.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 21));
		//textArea.setBounds(0, 47, 515, 355);
		//textArea.setEditable(false);
		//JScrollPane sp=new JScrollPane(textArea);
		//frame.getContentPane().add(textArea);
		sp.setBackground(new Color(51, 204, 255));
		sp.setFont(new Font("Monospaced", Font.BOLD | Font.ITALIC, 21));
		sp.setBounds(0, 47, 515, 355);
		
		panel.addComponentListener(new ComponentListener(){
			
			@Override
			public void componentResized(ComponentEvent e) {
				sp.setScrollPosition(0,panel.getPreferredSize().height);
				//System.out.println("JCalled");
			}

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				sp.setScrollPosition(0,panel.getPreferredSize().height);
				System.out.println("Called2");
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				sp.setScrollPosition(0,panel.getPreferredSize().height);
			//	System.out.println("JCalled3");
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				sp.setScrollPosition(0,panel.getPreferredSize().height);
				System.out.println("Called4");
			}
		});
		//JPanel contentPane=new JPanel(null);
		//contentPane.add(sp);
		frame.getContentPane().add(sp);
		//frame.add(contentPane);
		//frame.setContentPane(contentPane);
		frame.setVisible(true);
		if(list!=null){
			 
		for(String[] msg:list){
			//textArea.append(msg+"\n");
			int user=0;
			if(msg[0].substring(0, dto.getFriendID().length()).equals(dto.getFriendID())){
				msg[0]=msg[0].substring(dto.getFriendID().length()+3);
			}else if(msg[0].substring(0, dto.getId().length()).equals(dto.getId())){
				msg[0]=msg[0].substring(dto.getId().length()+3);
				user=1;
			}
			//Bubble lab=new Bubble(msg,msg.length()/33+1,user);   //line count mismatch when the length is exactly 33 or multiples of it 
			Bubble lab=new Bubble(msg[0],msg[0].length()/33+1,user);
			
			JLabel nalja=new JLabel(msg[1].substring(9,14));
			if(user==0){
        		lab.setBounds(10, y,lab.dynamic_width, lab.dynamic_height);
        		nalja.setBounds(12+lab.dynamic_width,y+lab.dynamic_height-17,50,20);
			}else{
        		lab.setBounds(490-10-lab.dynamic_width, y,lab.dynamic_width, lab.dynamic_height);
        		nalja.setBounds(448-lab.dynamic_width,y+lab.dynamic_height-17 , 50, 20);
			}
			panel.add(nalja);
        	panel.add(lab);
        	y+=lab.dynamic_height+10;
        	//System.out.println("width :"+ Bubble.dynamic_width);
        	//System.out.println("msg : " +msg+" length :"+msg.length());
        	//System.out.println("width :"+ Bubble.dynamic_width+" Height : "+Bubble.dynamic_height);
		}
		
		//y-=Bubble.dynamic_height;
		panel.setPreferredSize(new Dimension(480,y));
		lastseenmsg.setChatMsgDTO(dto.getId(), dto.getFriendID(), list.get(list.size()-1)[0], list.get(list.size()-1)[1]);
		dao.updateLastSeenMsg(lastseenmsg);
		//sp.setScrollPosition(0,550);// panel.getPreferredSize().height);

		//panel.repaint(x, y, width, height);i();
		if(!isAFriend){
			JLabel addFriend=new JLabel();
			addFriend.setFont(new Font("MV Boli", Font.BOLD, 18));
			addFriend.setText("Add "+dto.getFriendName()+" as your friend?");
			addFriend.setBounds(10,10,390,24);
			JButton button=new JButton("Add");
			button.setBackground(new Color(173, 216, 230));
			button.setFont(new Font("굴림", Font.BOLD, 18));
			button.setBounds(300, 10, 78, 28);
			panel.add(addFriend);
			panel.add(button);
			button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					String[] info={dto.getId(),dto.getFriendID(),dto.getFriendName()};
					ChatDAO dao=new ChatDAO();
					dao.friendListInsert(info);
					panel.remove(addFriend);
					panel.remove(button);
					panel.repaint();
					
				}
				
			});
		}
		}
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent arg0) {
				//new ChatDAO().updateLastSeenMsg(lastseenmsg);
				try {
					oos.flush();
					ois.close();
					oos.close();
					ois=null;
					oos=null;
					//socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("스트림 닫는데 에러 발생");
				}
				frame.dispose();
				
			}
		});
	}
	public void run(){
		System.out.println("쓰레드 while loop start");
		while(ois!=null){
			try{
				String msg=(String)ois.readObject();
				//ChatMsgDTO dto=(ChatMsgDTO) ois.readObject();
				//String msg=dto.getMsg();
				//System.out.println(msg);
				msg=msg.substring(dto.getFriendID().length()+3);
				Bubble lab=new Bubble(msg,msg.length()/33+1,0);
				lab.setBounds(10, y,lab.dynamic_width, lab.dynamic_height);
	        	panel.add(lab);
	        	y+=lab.dynamic_height+10;
	        	panel.setPreferredSize(new Dimension(480,y));
	        //	sp.setScrollPosition(0, y);
	        	panel.repaint();
	        	//textarea.append("a");      여기 부분 매우 이상함 , 왜 textarea 를 건들게 될시 제대로 띄어지는 건지  
	        	textarea.append(" ");
	        	textarea.setText("");
	        	
	        	lastseenmsg.setChatMsgDTO(dto.getId(), dto.getFriendID(),msg,sdf.format(Calendar.getInstance().getTime()));
	        	dao.updateLastSeenMsg(lastseenmsg);
			}catch(Exception e){
				System.out.println("문자 읽어오지 못함");
				//e.printStackTrace();
			}
			//panel.repaint();
		}
		System.out.println("쓰레드 while loop ended");
	}
}
