package dbManager;
import java.sql.*;

public class DBTest {
	Connection con; 
	PreparedStatement ps; 
	ResultSet rs; 
	//String url="jdbc:oracle:thin:@localhost:1521:xe";
	String url="jdbc:oracle:thin:@192.168.0.17:1521:xe";
	String username="system"; 
	String password="neworacle";  // Oracle system user's password has been expired so a new password was set 
	String msg; 
	public DBTest(){
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
	public static void main(String[] args){
		DBTest db=new DBTest();
		db.getConnection();
		db.dbInsert();
		//db.dbDelete();
		db.dbSelect();
		
		
	}
}
