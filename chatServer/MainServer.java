package chatServer;

import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dbManager.ChatDAO;
import dbManager.ChatMsgDTO;
public class MainServer extends Thread {

	private JFrame frame;
	private ServerSocket ss;
	private ServerSocket chatNotification;
	private HashMap<String,ObjectOutputStream> client_map;
	private HashMap<String,ObjectOutputStream> notification_map;
	private ChatDAO dao=new ChatDAO();
	JTextArea textArea_1 = new JTextArea();
	
	public static void main(String[] args) {
		MainServer window = new MainServer();
		window.start();
		window.initiate();
	}

	
	public MainServer() {
		client_map=new HashMap<String, ObjectOutputStream>();
		notification_map=new HashMap<>();
		Collections.synchronizedMap(client_map);
		Collections.synchronizedMap(notification_map);
		try {
			ss=new ServerSocket(12345);
			chatNotification=new ServerSocket(12346);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("서버생성자 에러");
		}
		initialize();
	}
	public void initiate(){
		Socket socket;
		Thread notification=new Thread(){
			@Override
			public void run() {
				Socket socket;
				while(true){
					try {
						socket=chatNotification.accept();
						System.out.println("Client Accessed");
						NotificationSocket notificationSocket=new NotificationSocket(socket);
						notificationSocket.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		};
		notification.start();
			try {
				while(true){
				socket=ss.accept();
				SocketReciever sr=new SocketReciever(socket);
				sr.start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("initiate() 에서 에러");
			}
			
	}
	//---------------------------------------------------------------------
	/**public void run(){
		Socket socket;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		try{
		while(true){
			socket=ss.accept();
			ois=new ObjectInputStream(socket.getInputStream());
			oos=new ObjectOutputStream(socket.getOutputStream());
			ChatMsgDTO dto=(ChatMsgDTO) ois.readObject();
			client_map.put(dto.getId(),socket);
			ArrayList<String> list=dao.getMsg(dto);
			oos.writeObject(list);
		}
		}catch(Exception e){
			
		}
	}**/
	//---------------------------------------------------------------------
	class NotificationSocket extends Thread{
		Socket socket;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		public NotificationSocket(Socket socket){
			this.socket=socket;
			try {
				ois=new ObjectInputStream(socket.getInputStream());
				oos=new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("NotificationSocket InnerClass error");
			}
		}
		public void run(){
			try {
				String id=(String) ois.readObject();
				System.out.println("아이디 받아옴");
				dao.logIn(id);
				System.out.println("Login Successful");
				notification_map.put(id, oos);
				while(true){
					if(oos==null){
						notification_map.remove(id);
						//dao.logOut(id);
						System.out.println("logOut Successful");
						break;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}
	}
	class SocketReciever extends Thread{
		Socket socket;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		public SocketReciever(Socket socket){
			this.socket=socket;
			try {
				ois=new ObjectInputStream(socket.getInputStream());
				oos=new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("SocketReciever InnerClass error");
			}
			
		}
		public void run(){
			 String id="";
			try {
				ChatMsgDTO dto=(ChatMsgDTO) ois.readObject();
				System.out.println(dto.getId()+" 접속");
				client_map.put(dto.getId().concat(dto.getFriendid()),oos);//socket);
				ArrayList<String[]> list=dao.getMsg(dto);
				oos.writeObject(list);
				id=dto.getId().concat(dto.getFriendid());
				notification_map.get(dto.getId()).writeObject("refresh");
				//ObjectOutputStream out;
				
				while(ois!=null){
					//dto=(ChatMsgDTO)ois.readObject();
					String msg=(String) ois.readObject();
					dto.setMsg(msg);
					//System.out.println(dto.getId()+"보낸 문자 "+dto.getMsg()+" to "+dto.getFriendid());
					dao.saveMsg(dto);
					notification_map.get(dto.getId()).writeObject("refresh");
					if(client_map.containsKey(dto.getFriendid().concat(dto.getId()))){
						System.out.println("client_map does contains key!!");
						notification_map.get(dto.getFriendid()).writeObject("refresh");
						//out=new ObjectOutputStream(client_map.get(dto.getFriendid()));
						//if(out!=null){
						//out.writeObject(dto);//dto.getId()+" : " + dto.getMsg());
						client_map.get(dto.getFriendid().concat(dto.getId())).writeObject(dto.getId()+" : " + dto.getMsg());
						System.out.println("전송성공!!");
						//}
						//out.close();
					}else if(dao.isLoggedIn(dto.getFriendid())){
						notification_map.get(dto.getFriendid()).writeObject(dto.getId()+" : "+dto.getMsg());
					}
					//System.out.println("끝!!!");
				}
				System.out.println("끝!!!");
				//client_map.remove(dto.getId());
				//System.out.println("the client Left");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("문제 에러에러");
				client_map.remove(id);
				System.out.println("the client Left");
			}finally{
				//client_map.remove();
				 //System.out.println("the client Left");
			}
			
		}
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 994, 512);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		JTextArea textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.BOLD, 20));
		textArea.setEditable(false);
		textArea.setBounds(33, 73, 536, 351);
		JScrollPane sp=new JScrollPane(textArea);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setBounds(33, 73, 536, 351);
		frame.getContentPane().add(sp);
		
		
		textArea_1.setFont(new Font("Monospaced", Font.BOLD, 20));
		textArea_1.setEditable(false);
		textArea_1.setBounds(672, 73, 276, 351);
		JScrollPane sp2=new JScrollPane(textArea_1);
		sp2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp2.setBounds(672, 73, 276, 351);
		frame.getContentPane().add(sp2);
		textArea_1.append("Active UserID\n");
		
		
		JLabel lblNewLabel = new JLabel("All Registered Users");
		lblNewLabel.setFont(new Font("굴림", Font.BOLD, 21));
		lblNewLabel.setBounds(33, 31, 217, 32);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Currently Active Users");
		lblNewLabel_1.setFont(new Font("굴림", Font.BOLD, 22));
		lblNewLabel_1.setBounds(672, 31, 276, 32);
		frame.getContentPane().add(lblNewLabel_1);
		ArrayList<String[]> userInfo=dao.selectAllUser();
		textArea.append("UserID"+"\t\t"+"UserName\n");
		for(String[] arr: userInfo){
			textArea.append(arr[0]+"\t\t"+arr[1]+"\n");
		}
		frame.repaint();
	}
	public void run(){
		while(true){
			textArea_1.setText(null);
			Iterator<String> it=client_map.keySet().iterator();
			while(it.hasNext()){
				String id=it.next();
				textArea_1.append(id+"\n");
				System.out.println(id +"from Client_map");
			}
			try {
				Thread.sleep(45000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
