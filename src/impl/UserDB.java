package impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import database.ConnectionFactory;
import database.IuserDB;
import models.User;
public class UserDB implements IuserDB{

	private Connection conn;
	public UserDB(){
		conn=ConnectionFactory.getConnection();
	}
	
	@Override
	public int insert(User u){
		String sql= "insert into users(name,gender,age,password) values(?,?,?,?)";
		try{
			PreparedStatement pstmt =conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, u.getName());
			pstmt.setString(2, u.getGender());
			pstmt.setInt(3, u.getAge());
			pstmt.setString(4, u.getPassword());
			
			if(pstmt.executeUpdate() > 0){
				ResultSet rs=pstmt.getGeneratedKeys();
				if(rs.next())
					return rs.getInt(1);

					
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public int update(User u) {
		String sql="update users set name=?,gender=?,age=?,password=? where id=?";
		try{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(5, u.getId());
			pstmt.setString(1, u.getName());
			pstmt.setString(2, u.getGender());
			pstmt.setInt(3, u.getAge());
			pstmt.setString(4, u.getPassword());
			
			return pstmt.executeUpdate();
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public User getById(int userID) {
		String sql="select * from users where id=?";
		User u =new User();
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, userID);  //make this int later
			ResultSet rs=pstmt.executeQuery();
			
			if(rs.next()){
				u.setId(rs.getInt("id"));
				u.setName(rs.getString("name"));
				u.setGender(rs.getString("gender"));
				u.setAge(rs.getInt("age"));
				
			}
			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return u;
	}
	
	@Override
	public ArrayList<User> getAll() {
		ArrayList<User> users=new ArrayList<>();
		String sql="select * from users";
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			ResultSet rs=pstmt.executeQuery();
			
			while(rs.next()){
				User u =new User();
				u.setId(rs.getInt("id"));
				u.setName(rs.getString("name"));
				u.setGender(rs.getString("gender"));
				u.setAge(rs.getInt("age"));
				users.add(u);
				
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public User getByName(String name) {
		String sql="select * from users where name=?";
		User u =new User();
		try{
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, name);  //make this int later
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()){
				u.setId(rs.getInt("id"));
				u.setName(rs.getString("name"));
				u.setGender(rs.getString("gender"));
				u.setAge(rs.getInt("age"));
				u.setPassword(rs.getString("password"));	
			}	
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		return u;
	}	
}
