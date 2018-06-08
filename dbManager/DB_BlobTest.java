package dbManager;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.sql.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DB_BlobTest {
	Connection con; 
	PreparedStatement ps; 
	ResultSet rs; 
	//String url="jdbc:oracle:thin:@localhost:1521:xe";
	String url="jdbc:oracle:thin:@192.168.0.17:1521:xe";
	String username="system"; 
	String password="neworacle";  // Oracle system user's password has been expired so a new password was set 
	String msg; 
	public DB_BlobTest(){
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
		}catch(Exception e){
			
		}
	}
	public void getConnection(){
		try {
			con=DriverManager.getConnection(url,username,password);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void dbSelect(){
		msg="select * from test";
		try {
			ps=con.prepareStatement(msg);
			rs=ps.executeQuery();
			while(rs.next()){
				System.out.println(rs.getString("name"));
				System.out.println(rs.getString("title"));
				System.out.println(rs.getInt("age"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dbInsert(){
		msg="insert into test values(?,?,?)";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, "Maou");
			ps.setString(2, "Devil");
			ps.setInt(3, 23);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void dbDelete(){
		msg="delete from test where name=?";
		try {
			ps=con.prepareStatement(msg);
			ps.setString(1, "Maou");
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void testBlob(){
		//msg="update chatusers set picture=? where id='levi11'";
		msg="update chatusers set picture=? where id!='levi11'";
		//byte[] pic;
		BufferedImage img=null;
		try {
			img=ImageIO.read(new File("C:\\Users\\User7\\Pictures\\defaultPic.jpg"));
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ps=con.prepareStatement(msg);
			Blob picture=con.createBlob();
			//Writer picWriter=picture.setBinaryStream(1);
			OutputStream picWriter=picture.setBinaryStream(1);
			ImageIO.write(img, "jpg", picWriter);
			
			ps.setBlob(1, picture);
			ps.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getBlob(){
		msg="select picture from chatusers where id='levi11'";
		Blob picture=null;
		try {
			ps=con.prepareStatement(msg);
		
			//Writer picWriter=picture.setBinaryStream(1);
			
			rs=ps.executeQuery();
			rs.next();
			picture=rs.getBlob("picture");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedImage img=null;
		InputStream readPic;
		try {
			readPic=picture.getBinaryStream();
			img=ImageIO.read(readPic);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JFrame f=new JFrame();
		f.setBounds(200,200,400,400);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//img=(BufferedImage)img.getScaledInstance(300, 300, BufferedImage.TYPE_INT_ARGB);
		//ImageIcon icon=new ImageIcon(img);
		ImageIcon icon=new ImageIcon(img.getScaledInstance(700, 700, BufferedImage.TYPE_4BYTE_ABGR));
		JLabel lab=new JLabel();
		lab.setIcon(icon);
		
		//Graphics g=lab.getGraphics();
		//g.drawImage(img,0 , 0, icon.getIconWidth(), icon.getIconHeight(), 0, 0, 300, 300, null);
		f.getContentPane().add(lab);
		f.getContentPane().repaint();
		f.repaint();
	
	}
	public static void main(String[] args){
		DB_BlobTest db=new DB_BlobTest();
		db.getConnection();
		//db.dbInsert();
		//db.dbDelete();
		//db.dbSelect();
		db.testBlob();
		//db.getBlob();
	}
}
