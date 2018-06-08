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

import androidObjects.AndroidChatDAO;
import dbManager.ChatDAO;
import dbManager.ChatDTO;
import dbManager.ChatMsgDTO;
import dbManager.FriendListDTO;
public class AndroidServer extends Thread {

	private JFrame frame;
	private ServerSocket ss,ss2,ss3;
	private HashMap<String,ObjectOutputStream> client_map;
	private ChatDAO dao=new ChatDAO();
	JTextArea textArea_1 = new JTextArea();
	
	public static void main(String[] args) {
		AndroidServer window = new AndroidServer();
		window.start();
		window.initiate();
	}

	
	public AndroidServer() {
		client_map=new HashMap<String, ObjectOutputStream>();
		Collections.synchronizedMap(client_map);
		try {
			ss=new ServerSocket(9698);
			ss2=new ServerSocket(9999);
			ss3=new ServerSocket(19999);
			System.out.println("서버 실행!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("서버생성자 에러");
		}
		initialize();
	}
	public void initiate(){
		Socket socket,socket2;
		Thread t=new Thread(){
			Socket socket3;
			@Override
			public void run() {
				while(true){
					try {
						socket3=ss3.accept();
						SocketUserInfo sui=new SocketUserInfo(socket3);
						sui.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		Thread t2=new Thread(){
			Socket socket;
			@Override
			public void run() {
				while(true){
					try {
						socket=ss.accept();
						SocketRecieverForMessage sr=new SocketRecieverForMessage(socket);
						sr.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		t2.start();
			try {
				while(true){
					socket2=ss2.accept();
					System.out.println("Socket Accessed!!");
					SocketRecieverForInfo srf=new SocketRecieverForInfo(socket2);
					srf.start();
				//socket=ss.accept();
				//SocketRecieverForMessage sr=new SocketRecieverForMessage(socket);
				//sr.start();
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
	class SocketRecieverForInfo extends Thread{
		
		Socket socket;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		ChatDAO dao=new ChatDAO();
		public SocketRecieverForInfo(Socket socket){
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
			loginCheck();
			
			
		}
		private void loginCheck(){
			Boolean check=new Boolean(false);
			System.out.println("loginCheck called");
			while(!check.booleanValue()){
			try {
				System.out.println("Abour to Read");
				//ChatDTO dto=(ChatDTO)ois.readObject();
				String[] info=(String[])ois.readObject();
				System.out.println("Read!!");
				//check=dao.loginCheck(dto.getId(), dto.getPassword());
				check=dao.loginCheck(info[0],info[1]);
				oos.writeObject(check);
				System.out.println(check);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			}
			System.out.println("while loop ended!!");
			
		}
		
		
	}
	
	class SocketUserInfo extends Thread{
		Socket socket;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		AndroidChatDAO dao=new AndroidChatDAO();
		public SocketUserInfo(Socket socket){
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
		@Override
		public void run() {
			System.out.println("SocketUserInfo Start");
			while(ois!=null && oos!=null){
				try {
					String id=(String)ois.readObject();
					if(id.charAt(0)=='F'){
						sendFriendList(id.substring(1));
					}else if(id.charAt(0)=='C'){
						sendChatList(id.substring(1));
					}else if(id.charAt(0)=='A'){
						sendFriendList2(id.substring(1));
					}else if(id.charAt(0)=='B'){
						sendChatList2(id.substring(1));
					}
						
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("client left");
					break;
				}
				
			}
			System.out.println("client ended");
		}
		
		private void sendFriendList(String id){
			//String id;
			try {
				//id = (String)ois.readObject();
				ArrayList<String> dbinfo=(ArrayList<String>)ois.readObject();
				
				ArrayList<Object> list=dao.getFriendList2(id,dbinfo);
				oos.writeObject(list);
				System.out.println("Object Sent");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		private void sendFriendList2(String id){
			//String id;
			try {
				//id = (String)ois.readObject();
				ArrayList<String[]> list=dao.getFriendList(id);
				oos.writeObject(list);
				System.out.println("Object Sent");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		private void sendChatList2(String id){
			//String id;
			try {
				ArrayList<Object> list=dao.selectChatList(id);
				oos.writeObject(list);
				//id = (String)ois.readObject();
	// 다시수정			ArrayList<String[]> list=dao.selectChatList(id);
	//			oos.writeObject(list);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		private void sendChatList(String id){
			//String id;
			try {
				ArrayList<Object> list=dao.selectChatList(id);
				oos.writeObject(list);
				//id = (String)ois.readObject();
	// 다시수정			ArrayList<String[]> list=dao.selectChatList(id);
	//			oos.writeObject(list);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
	}
	class SocketRecieverForMessage extends Thread{
		Socket socket;
		ObjectInputStream ois;
		ObjectOutputStream oos;
		AndroidChatDAO dao=new AndroidChatDAO();
		public SocketRecieverForMessage(Socket socket){
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
			accessChatPage();
			
		}
		private void accessChatPage(){
			 String id="";
				try {
					String[] dto=(String[])ois.readObject();
					System.out.println(dto[0]+" 접속");
					client_map.put(dto[0],oos);//socket);
					ArrayList<String[]> list=dao.getMsg(dto);
					oos.writeObject(list);
					id=dto[0];
					//ObjectOutputStream out;
					
					while(ois!=null){
						//dto=(ChatMsgDTO)ois.readObject();
						String msg=(String) ois.readObject();
						dto[2]=msg;
						System.out.println(dto[0]+"보낸 문자 "+dto[2]+" to "+dto[1]);
						dao.saveMsg(dto);
						if(client_map.containsKey(dto[1])){
							System.out.println("client_map does contains key!!");
							//out=new ObjectOutputStream(client_map.get(dto.getFriendid()));
							//if(out!=null){
							//out.writeObject(dto);//dto.getId()+" : " + dto.getMsg());
							client_map.get(dto[1]).writeObject(dto[0]+" : " + dto[2]);
							System.out.println("전송성공!!");
							//}
							//out.close();
						}
						System.out.println("끝!!!");
					}
					
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
		
		JLabel lblNewLabel_1 = new JLabel("Currently Active Users on Android");
		lblNewLabel_1.setFont(new Font("굴림", Font.BOLD, 22));
		lblNewLabel_1.setBounds(672, 31, 276, 32);
		frame.getContentPane().add(lblNewLabel_1);
		
		ArrayList<String[]> userInfo=dao.selectAllUser();
		textArea.append("UserID"+"\t\t"+"UserName\n");
		for(String[] arr: userInfo){
			textArea.append(arr[0]+"\t\t"+arr[1]+"\n");
		}
		frame.repaint();  //프레임 이 처음 그려질때 한번에 재데로 paint 되지 않아서
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
				Thread.sleep(45000); //45 초 간격 으로 접속자 갱신 
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
