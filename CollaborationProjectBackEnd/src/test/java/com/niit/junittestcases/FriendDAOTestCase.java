package com.niit.junittestcases;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.niit.dao.FriendDAO;
import com.niit.model.Friend;

public class FriendDAOTestCase {
	
	@Autowired
	static AnnotationConfigApplicationContext context;

	@Autowired
	static Friend friend;

	@Autowired
	static FriendDAO friendDAO;

	@BeforeClass
	public static void initialize() {
		context = new AnnotationConfigApplicationContext();
		context.scan("com.niit");
		context.refresh();

		// get the categoryDAO from context
		friendDAO = (FriendDAO) context.getBean("friendDAO");

		// get the category from context
		friend = (Friend) context.getBean("friend");

	}
	
	@Test
	public void saveFriendTestCase(){
		friend.setUserId("postman1");
		friend.setFriendId("Suveen");
		friend.setStatus("A");
		boolean flag = friendDAO.save(friend);
		assertEquals("saveFriendTestCase", true, flag);
	}
	
	//@Test
	public void deleteFriendTestCase(){
		boolean flag = friendDAO.delete("postman1", "Suveen");
		assertEquals("deleteFriendTestCase", true, flag);
	}
	
	//@Test
	public void getMyFriendsTestCase(){
		int size = friendDAO.getMyFriends("Suveen").size();
		assertEquals("getMyFriendsTestCase", 2, size);
	}
}
