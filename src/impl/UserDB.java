package impl;

import database.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import models.User;

public class UserDB {

    public void insert(User user) {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "INSERT INTO users (name, gender, age, password) VALUES (?, ?, ?, ?)"
            );
            ps.setString(1, user.getName());
            ps.setString(2, user.getGender());
            ps.setInt(3, user.getAge());
            ps.setString(4, user.getPassword());
            ps.execute();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM users");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setGender(rs.getString("gender"));
                user.setAge(rs.getInt("age"));
                user.setPassword(rs.getString("password"));
                list.add(user);
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public User getById(int id) {
        User user = null;
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setGender(rs.getString("gender"));
                user.setAge(rs.getInt("age"));
                user.setPassword(rs.getString("password"));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getByName(String name) {
        User user = null;
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM users WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setGender(rs.getString("gender"));
                user.setAge(rs.getInt("age"));
                user.setPassword(rs.getString("password"));
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void update(User user) {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "UPDATE users SET name = ?, gender = ?, age = ?, password = ? WHERE id = ?"
            );
            ps.setString(1, user.getName());
            ps.setString(2, user.getGender());
            ps.setInt(3, user.getAge());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getId());
            ps.execute();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        try {
            Connection c = ConnectionFactory.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE FROM users WHERE id = ?");
            ps.setInt(1, id);
            ps.execute();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
