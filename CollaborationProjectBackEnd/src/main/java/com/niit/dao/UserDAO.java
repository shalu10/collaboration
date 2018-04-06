package com.niit.dao;

import java.util.List;

import com.niit.model.User;

public interface UserDAO {

	public boolean save(User user);

	public boolean update(User user);

	public boolean delete(String userId);

	public User getUserById(String userId);
	
	public User getUserById(String userId, String status);

	public List<User> list();
	
	public List<User> list(String status);
	
	public void setOnline(String userId);

	public void setOffline(String userId);

	public User validate(String userId, String password);
	
	public List<User> notMyFriendList(String userId);
	
	public List<User> notMyFriendList1(String userId);
}
