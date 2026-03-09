package database;

import java.util.ArrayList;

import models.User;

public interface IuserDB{
	User getById(int userID); //make this int later as id is in int
	
	int insert(User u);
	
	int update(User u);
	
	User getByName(String name);
	
	ArrayList<User> getAll();	
}