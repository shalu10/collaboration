package com.niit.junittestcases;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.niit.dao.UserDAO;
import com.niit.model.User;

public class UserDAOTestCase {

	@Autowired
	static AnnotationConfigApplicationContext context;

	@Autowired
	static User user;

	@Autowired
	static UserDAO userDAO;

	@BeforeClass
	public static void initialize() {
		context = new AnnotationConfigApplicationContext();
		context.scan("com.niit");
		context.refresh();

		// get the categoryDAO from context
		userDAO = (UserDAO) context.getBean("userDAO");

		// get the category from context
		user = (User) context.getBean("user");

	}

	// @Test
	public void saveUserTestCase() {
		user.setUserid("suveen");
		user.setEmail("suveenkumar.vundavalli@gmail.com");
		user.setFirstname("Suveen Kumar");
		user.setLastname("Vundavalli");
		user.setIsOnline("true");
		user.setPassword("suveen");
		user.setRole("ROLE_USER");
		user.setStatus("NA");
		boolean flag = userDAO.save(user);
		assertEquals("saveUserTestCase", true, flag);
	}

	// @Test
	public void updateUserTestCase() {
		user.setUserid("Suveen");
		user.setEmail("suveenkumar.vundavalli@gmail.com");
		user.setFirstname("Suveen Kumar");
		user.setLastname("Vundavalli");
		user.setIsOnline("true");
		user.setPassword("Suveen");
		user.setRole("ROLE_ADMIN");
		user.setStatus("NA");
		boolean flag = userDAO.update(user);
		assertEquals("saveUserTestCase", true, flag);
	}

	// @Test
	public void listUserTestCase() {
		int size = userDAO.list().size();
		assertEquals("listUserTestCase", 2, size);
	}

	// @Test
	public void validateTestCase() {
		User user = userDAO.validate("Suveen", "Suveen");
		String firstname = user.getFirstname();
		assertEquals("validateTestCase", "Suveen Kumar", firstname);
	}

	@Test
	public void getUserByIdTestCase() {
		User user = userDAO.getUserById("suveen");
		String firstname = user.getFirstname();
		assertEquals("validateTestCase", "Suveen Kumar", firstname);
	}
	@Test
	public void getNotMyFriendsListTestCase() {
		List<User> users123 = userDAO.notMyFriendList("Suveen");
		int size = users123.size();
		assertEquals("getNotMyFriendsListTestCase", 9, size);
	}

}
