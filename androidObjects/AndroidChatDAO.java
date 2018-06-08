package androidObjects;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class AndroidChatDAO {
	Connection con; 
	PreparedStatement ps; 
	ResultSet rs; 
	//String url="jdbc:oracle:thin:@localhost:1521:xe";  
	String url="jdbc:oracle:thin:@192.168.0.17:1521:xe";   //데이타베이스 서버 컴퓨터 ip 주소  ---->공인 ip 가 할당된  데이타베이스서버 
	String username="system"; 
	String password="neworacle";
	String msg; 
	final int LoggedIn=1;
	final int LoggedOut=0;
	public AndroidChatDAO(){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			getConnection();
		}catch(Exception e){
			
		}
	}
	public void getConnection() throws SQLException{
			con=DriverManager.getConnection(url,username,password);
	}
	public void dbRegisterUser(AndroidChatDTO dto){
		msg="insert into chatusers values(?,?,?,?,?)";
		try {
			BufferedImage img=null;
			try {
				img=ImageIO.read(dto.getFile());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Blob picture=con.createBlob();
			OutputStream pictureWriter=picture.setBinaryStream(1);
			try {
				ImageIO.write(img, "jpg", pictureWriter);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ps=con.prepareStatement(msg);
			ps.setString(1, dto.getId());
			ps.setString(2, dto.getPassword());
			ps.setString(3, dto.getName());
			ps.setString(4, dto.getEmail());
			ps.setBlob(5, picture);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public boolean loginCheck(String id,String password){
		msg="select * from chatusers where id=?";    //동적 쿼리
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			rs=ps.executeQuery(); 
			rs.next();
			String realpass=rs.getString("password");
			if(realpass.equals(password))
				return true;
			else 
				return false;
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("No DATA");
			return false;
		}
	}
	public String dbSearchFriend(String id){
		msg="select * from chatusers where id=?";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			rs=ps.executeQuery();
			rs.next();//한번은 꼭 호출 해줘야 데이타를 읽어오는게 가능
			String name=rs.getString("name");
			return name;
		} catch (SQLException e) {
			//e.printStackTrace();
			return null;
		}
	}
	public void friendListInsert(String[] info){
		msg="insert into friendlist values(?,?,?)";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, info[0]);
			ps.setString(2, info[1]);
			ps.setString(3, info[2]);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public boolean doubleCheck(String id,String name){
		msg="select * from friendlist where id=? and friendname=?";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			ps.setString(2, name);
			rs=ps.executeQuery();
			rs.next();
			rs.getString("friendid");
			return false;
		} catch (SQLException e) {
			//e.printStackTrace();
			return true;
		}
		
	}
	/**
	public ArrayList<String[]> getFriendList(String id){
			msg="select friendid,friendname from friendlist where id=?";
			ArrayList<String[]> list=new ArrayList<>();
			try {
				ps=con.prepareStatement(msg);
				ps.setString(1, id);
				rs=ps.executeQuery();
				while(rs.next()){
					String[] info={rs.getString("friendid"),rs.getString("friendname")};
					list.add(info);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				list=null;
			}
		
		return list;
	}**/
	
	//  Old Version
	public ArrayList<String[]> getFriendList(String id){
		msg="select friendid,friendname from friendlist where id=?";
		ArrayList<String[]> list=new ArrayList<>();
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			rs=ps.executeQuery();
			while(rs.next()){
				/*
				AndroidFriendListDTO dto=new AndroidFriendListDTO();
				dto.setId(id);
				dto.setFriendID(rs.getString("friendid"));
				dto.setFriendName(rs.getString("friendname"));
				*/
				String[] info=new String[2];
				info[0]=rs.getString("friendid");
				info[1]=rs.getString("friendname");
				list.add(info);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			list=null;
		}
	
	return list;
}
	/*
	public Information getMyProfile(String id){
		msg="select name,picture from chatusers where id=?";
		Information inform=new Information();
		try{
		ps=con.prepareStatement(msg);
		ps.setString(1, id);
		rs=ps.executeQuery();
		rs.next();
		
		Blob picture;
		String []info={null,null,rs.getString("name")};
		picture=rs.getBlob("picture");
		BufferedImage img=null;
		InputStream readPic;
		try {
			readPic=picture.getBinaryStream();
			img=ImageIO.read(readPic);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ImageIcon icon=new ImageIcon(img.getScaledInstance(60, 42, BufferedImage.TYPE_4BYTE_ABGR));
		inform.info=info;
		inform.icon=icon;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return inform;
	}
*/
	public ArrayList<Object> getFriendList_new(String id){
		msg="select friendid,friendname,(select picture from chatusers where id=f.friendid) as picture from friendlist f where id=?";
		ArrayList<Object> list=new ArrayList<>();
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			rs=ps.executeQuery();
			while(rs.next()){
				
				Blob picture;
				String[] info=new String[3];
				info[0]=id;
				info[1]=rs.getString("friendid");
				info[2]=rs.getString("friendname");
				picture=rs.getBlob("picture");
				byte[] pic=new byte[1000000];//1Mb
				//int numberofBytes=0,cnt;
				InputStream readPic;
				
				try {
					readPic=picture.getBinaryStream(); //이미 구현을 해놓은 InputStream 객체값을 반환
					int size=readPic.read(pic);
					System.out.println(size+" bytes read");
					//readPic.read(pic);
					/*
					while( (cnt=readPic.available())!=0)
						numberofBytes+=cnt;
					pic=new byte[numberofBytes];
					readPic.read(pic);*/
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				//FriendListDTO dto=new FriendListDTO();
				//dto.setId(id);
				//dto.setFriendID(rs.getString("friendid"));
				//dto.setFriendName(rs.getString("friendname"));
				Object[] inform={info,pic};
				list.add(inform);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			list=null;
		}
	
	return list;
	}
	public ArrayList<Object> getFriendList2(String id,ArrayList<String> dbinfo){
		msg="select friendid,friendname,(select picture from chatusers where id=f.friendid) as picture from friendlist f where id=?";
		ArrayList<Object> list=new ArrayList<>();
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			rs=ps.executeQuery();
			while(rs.next()){
				
				Blob picture;
				String[] info=new String[3];
				info[0]=id;
				info[1]=rs.getString("friendid");
				info[2]=rs.getString("friendname");
				picture=rs.getBlob("picture");
				byte[] pic=null;
				if(!checkHasID(info[1],dbinfo)){
				//byte[] pic=new byte[1000000];//1Mb
					pic=new byte[1000000];//1Mb
					//int numberofBytes=0,cnt;
				InputStream readPic;
				
				try {
					readPic=picture.getBinaryStream(); //이미 구현을 해놓은 InputStream 객체값을 반환
					int size=readPic.read(pic);
					System.out.println(size+" bytes read");
					//readPic.read(pic);
					/*
					while( (cnt=readPic.available())!=0)
						numberofBytes+=cnt;
					pic=new byte[numberofBytes];
					readPic.read(pic);*/
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
				//FriendListDTO dto=new FriendListDTO();
				//dto.setId(id);
				//dto.setFriendID(rs.getString("friendid"));
				//dto.setFriendName(rs.getString("friendname"));
				
				Object[] inform={info,pic};
				list.add(inform);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			list=null;
		}
	
	return list;
	}
	public boolean checkHasID(String Id,ArrayList<String> dbinfo){
		for(String id:dbinfo){
			if(id==Id)
				return true;
		}
		return false;
	}
	/*  Old Version
	public ArrayList<String> getMsg(ChatMsgDTO dto){
		ArrayList<String> list=new ArrayList<>();
		int num=0;
		msg="select msg,id from chatmsg where id in (?,?) and friendid in (?,?) order by nalja";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1,dto.getId() );
			ps.setString(2,dto.getFriendid() );
			ps.setString(3,dto.getFriendid() );
			ps.setString(4,dto.getId() );
			rs=ps.executeQuery();
			while(rs.next()){
				if(rs.getString("id").equals(dto.getId()))
					list.add(dto.getId()+" : "+rs.getString("msg"));
				else
					list.add(dto.getFriendid()+" : "+rs.getString("msg"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("등록된 되화없음");
			list=null;
		}
		return list;
	}
	*/ /**/
	public ArrayList<String[]> getMsg(String[] dto){
		ArrayList<String[]> list=new ArrayList<>();
		int num=0;
		msg="select msg,id,to_char(nalja,'YY/MM/DD hh:mi:ss') as nalja from chatmsg where id in (?,?) and friendid in (?,?) order by nalja";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1,dto[0] );
			ps.setString(2,dto[1] );
			ps.setString(3,dto[1] );
			ps.setString(4,dto[0] );
			rs=ps.executeQuery();
			while(rs.next()){
				String[] info=new String[2];
				if(rs.getString("id").equals(dto[0]))
					info[0]=dto[0]+" : "+rs.getString("msg");
				else
					info[0]=dto[1]+" : "+rs.getString("msg");
				info[1]=rs.getString("nalja");
				list.add(info);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("등록된 되화없음");
			list=null;
		}
		return list;
	}
	
	public void saveMsg(String[] dto){
		msg="insert into chatmsg values(?,?,?,sysdate)";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, dto[0]);
			ps.setString(2, dto[1]);
			ps.setString(3, dto[2]);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("문자 저장 실패");
		}
	}
	/*
	public ArrayList<String[]> selectAllUser(){
		ArrayList<String[]> list=new ArrayList<>();
		msg="select id,name from chatusers";
		try {
			ps=con.prepareStatement(msg);
			rs=ps.executeQuery();
			while(rs.next()){
				String[] info={rs.getString("id"),rs.getString("name")};
				list.add(info);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return list;
	}
	
	     The Updated Version is down below
	public ArrayList<String[]> selectChatList(String id){
		ArrayList<String[]> list=new ArrayList<>();
		ArrayList<String[]> content=getMessagefromNotAFriend(id);
		for(String[] info:content){
			list.add(info);
		}
		ArrayList<FriendListDTO> friendList=getFriendList(id);
		msg="select msg,nalja,(select picture from chatusers where id=c.friendid) as picture from chatmsg c where id in(?,?)  and friendid in(?,?) order by nalja desc";
		for(FriendListDTO dto:friendList){
			Blob picture;
			try {
				ps=con.prepareStatement(msg);
				ps.setString(1, id);
				ps.setString(2, dto.getFriendID());
				ps.setString(3, id);
				ps.setString(4, dto.getFriendID());
				rs=ps.executeQuery();
				// 처음 시작시 row의 맨 위에 있으므로 한칸 내려가 줘야한다  
				if(!rs.next())
					continue;  //대화를 아직 시작 하지않음  
				String[] info=new String [5];
				info[0]=dto.getFriendName();
				info[1]=rs.getString("msg");
				Date nalja=rs.getDate("nalja");
				
				info[2]=nalja.toString();
				info[3]=dto.getFriendID();
				info[4]=String.valueOf(nalja.getTime());
				list.add(info);
				for(int i=0;i<list.size();i++){
					for(int a=0;a<list.size();a++){
					long time=Long.parseLong(list.get(i)[4]);
					long comparetime=Long.parseLong(list.get(a)[4]);
						if(time>comparetime){
							list.set(i, list.set(a, list.get(i)));
						}
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				System.out.println("No Rows Exist");
			}
			
			
		}
		return list;
	}
	*/
	public ArrayList<String[]> selectChatListText(String id){
		ArrayList<String[]> list=new ArrayList<>();
		//ArrayList<Information> content=getMessagefromNotAFriend(id);
		//for(Information info:content){
			//list.add(info);
		//}
		ArrayList<String[]> friendList=getFriendList(id);
		msg="select msg,nalja from chatmsg c where id in(?,?)  and friendid in(?,?) order by nalja desc";
		for(String[] dto:friendList){
			
			try {
				ps=con.prepareStatement(msg);
				ps.setString(1, id);
				ps.setString(2, dto[0]);
				ps.setString(3, id);
				ps.setString(4, dto[0]);
				rs=ps.executeQuery();
				// 처음 시작시 row의 맨 위에 있으므로 한칸 내려가 줘야한다  
				if(!rs.next())
					continue;  //대화를 아직 시작 하지않음  
				
				String[] info=new String [6];
				info[0]=dto[1];
				info[1]=rs.getString("msg");
				Date nalja=rs.getDate("nalja");
				
				
				info[2]=nalja.toString();
				info[3]=dto[0];
				info[4]=String.valueOf(nalja.getTime());
				//System.out.println(selectNotSeenCount(id, dto.getFriendID()));
				//info[5]=String.valueOf(selectNotSeenCount(id, dto[0]));
				//BufferedImage img=null;
				
				//ImageIcon icon=new ImageIcon(img.getScaledInstance(87, 86, BufferedImage.TYPE_4BYTE_ABGR));
				
				
				//inform.info=info;
				//inform.icon=icon;
				list.add(info);
				for(int i=0;i<list.size();i++){
					for(int a=0;a<list.size();a++){
					long time=Long.parseLong(list.get(i)[4]);
					long comparetime=Long.parseLong(list.get(a)[4]);
						if(time>comparetime){
							list.set(i, list.set(a, list.get(i)));
						}
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("No Rows Exist");
			}
			
			
		}
		return list;
	}
	public ArrayList<Object> selectChatList(String id){
		ArrayList<Object> list=new ArrayList<>();
		//ArrayList<Information> content=getMessagefromNotAFriend(id);
		//for(Information info:content){
			//list.add(info);
		//}
		ArrayList<String[]> friendList=getFriendList(id);
		msg="select msg,nalja,(select picture from chatusers where id=c.friendid) as picture from chatmsg c where id in(?,?)  and friendid in(?,?) order by nalja desc";
		for(String[] dto:friendList){
			Blob picture;
			try {
				ps=con.prepareStatement(msg);
				ps.setString(1, id);
				ps.setString(2, dto[0]);
				ps.setString(3, id);
				ps.setString(4, dto[0]);
				rs=ps.executeQuery();
				// 처음 시작시 row의 맨 위에 있으므로 한칸 내려가 줘야한다  
				if(!rs.next())
					continue;  //대화를 아직 시작 하지않음  
				Object[] collect=new Object[2];
				String[] info=new String [6];
				info[0]=dto[1];
				info[1]=rs.getString("msg");
				Date nalja=rs.getDate("nalja");
				picture=rs.getBlob("picture");
				
				info[2]=nalja.toString();
				info[3]=dto[0];
				info[4]=String.valueOf(nalja.getTime());
				//System.out.println(selectNotSeenCount(id, dto.getFriendID()));
				//info[5]=String.valueOf(selectNotSeenCount(id, dto[0]));
				//BufferedImage img=null;
				byte[] pic=new byte[1000000];
				InputStream readPic;
				try {
					readPic=picture.getBinaryStream();
					//img=ImageIO.read(readPic);
					readPic.read(pic);
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//ImageIcon icon=new ImageIcon(img.getScaledInstance(87, 86, BufferedImage.TYPE_4BYTE_ABGR));
				collect[0]=info;
				collect[1]=pic;
				//inform.info=info;
				//inform.icon=icon;
				list.add(collect);
				for(int i=0;i<list.size();i++){
					for(int a=0;a<list.size();a++){
					long time=Long.parseLong(((String[])(((Object[])list.get(i))[0]))[4]);
					long comparetime=Long.parseLong(((String[])(((Object[])list.get(a))[0]))[4]);
						if(time>comparetime){
							list.set(i, list.set(a, list.get(i)));
						}
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("No Rows Exist");
			}
			
			
		}
		return list;
	}/*
	public ArrayList<String[]> checkFriendRequestSentID(String id){  //친구 요청 이 왔는지 확인할 아이디
		// 레퍼런스 선언 
		ArrayList<String[]> list=new ArrayList<>(); //동적할당 초기화
		msg="select distinct id,(select name from chatusers where id=c.id) as name from chatmsg c where friendid=? and id not in(select friendid from friendlist where id=?)";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			ps.setString(2, id);
			rs=ps.executeQuery();
			String[] info;
			while(rs.next()){
				info=new String[2];
				info[0]=rs.getString("id");
				//System.out.println(info[0]);
				info[1]=rs.getString("name");
				list.add(info);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public ArrayList<Information> getMessagefromNotAFriend(String id){
		ArrayList<Information> sendContent=new ArrayList<>();
		ArrayList<String[]> list=checkFriendRequestSentID(id);
		for(int i=0;i<list.size();i++){
			msg="select msg,nalja,(select picture from chatusers where id=c.friendid) as picture from chatmsg c where friendid=? and id=? order by nalja desc";
			Blob picture;
			try {
				ps=con.prepareStatement(msg);
				ps.setString(1, id);
				ps.setString(2, list.get(i)[0]);
				rs=ps.executeQuery();
				rs.next();
				Information inform=new Information();
				String[] info=new String[5];
				info[0]=list.get(i)[1];
				info[1]=rs.getString("msg");
				//System.out.println(info[1]);
				Date nalja=rs.getDate("nalja");
				picture=rs.getBlob("picture");
				info[2]=nalja.toString();
				info[3]=list.get(i)[0];
				info[4]=String.valueOf(nalja.getTime());
				BufferedImage img=null;
				InputStream readPic;
				try {
					readPic=picture.getBinaryStream();
					img=ImageIO.read(readPic);
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ImageIcon icon=new ImageIcon(img.getScaledInstance(700, 700, BufferedImage.TYPE_4BYTE_ABGR));
				
				inform.info=info;
				inform.icon=icon;
				sendContent.add(inform);
				
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sendContent;
	}
	public ArrayList<String> getMsgfromUnknown(ChatMsgDTO dto){
		ArrayList<String> list=new ArrayList<>();
		msg="select msg,id from chatmsg where friendid=? and id=? order by nalja";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, dto.getId());
			ps.setString(2, dto.getFriendid());
			rs=ps.executeQuery();
			while(rs.next()){
			
			if(rs.getString("id").equals(dto.getId()))
				list.add(dto.getId()+" : "+rs.getString("msg"));
			else
				list.add(dto.getFriendid()+" : "+rs.getString("msg"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			list=null;
		}
		return list;
	}
	
	public boolean checkFriended(String id,String friendid){
		msg="select id from friendlist where friendid=? and id=?";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, friendid);
			ps.setString(2, id);
			rs=ps.executeQuery();
			if(rs.next())
				return true;
			else
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}
	public void updateLastSeenMsg(ChatMsgDTO dto){
		if(checkLastSeenMsg(dto.getId(), dto.getFriendid())){
				msg="update lastseenmsg set msg=?, nalja=to_date(?,'yy/mm/dd hh:mi:ss') where id=? and friendid=?";
				try {
					ps=con.prepareStatement(msg);
					ps.setString(1, dto.getMsg());
					ps.setString(2, dto.getDate());
					ps.setString(3, dto.getId());
					ps.setString(4, dto.getFriendid());
					ps.executeUpdate();
				}catch(SQLException e){
					e.printStackTrace();
				}
		}
		else {
			msg="insert into lastseenmsg values(?,?,?,to_date(?,'yy/mm/dd hh:mi:ss'))";
			try {
				ps=con.prepareStatement(msg);
				ps.setString(1, dto.getId());
				ps.setString(2,dto.getFriendid() );
				ps.setString(3, dto.getMsg());
				ps.setString(4, dto.getDate());
				ps.executeUpdate();
			}catch(SQLException e){
				e.printStackTrace();
			}	
		}
	}
	public boolean checkLastSeenMsg(String id,String friendid){
		msg="select id from lastseenmsg where id=? and friendid=?";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			ps.setString(2, friendid);
			rs=ps.executeQuery();
			if(rs.next())
				return true;
			else 
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	public int selectNotSeenCount(String id,String friendid){
		String msg="select count(msg) cnt from chatmsg where id in(?,?) "
				+ "and friendid in(?,?) and to_char(nalja,'yy/mm/dd hh:mi:ss')>"
				+ "(select to_char(nalja,'yy/mm/dd hh:mi:ss') from lastseenmsg where id=? and friendid=?)";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1,id );
			ps.setString(2, friendid);
			ps.setString(3, id);
			ps.setString(4, friendid);
			ps.setString(5, id);
			ps.setString(6, friendid);
			rs=ps.executeQuery();
			if(rs.next())
				return rs.getInt("cnt");
			else 
				return 0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	public void logIn(String id){
		msg="update chatusers set loggedin=1 where id=?";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void logOut(String id){
		msg="update chatusers set loggedin=0 where id=?";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isLoggedIn(String id){
		msg="select loggedin from chatusers where id=?";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, id);
			rs=ps.executeQuery();
			rs.next();
			if(rs.getInt("loggedin")==1)
				return true;
			else 
				return false;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	*/
	
}
