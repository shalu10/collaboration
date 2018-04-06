package com.niit.junittestcases;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.niit.dao.ForumDAO;
import com.niit.model.Forum;

public class ForumDAOTestCase {

	@Autowired
	static AnnotationConfigApplicationContext context;

	@Autowired
	static Forum forum;

	@Autowired
	static ForumDAO forumDAO;

	@BeforeClass
	public static void initialize() {
		context = new AnnotationConfigApplicationContext();
		context.scan("com.niit");
		context.refresh();

		// get the categoryDAO from context
		forumDAO = (ForumDAO) context.getBean("forumDAO");

		// get the category from context
		forum = (Forum) context.getBean("forum");

	}

	@Test
	public void createForumTestCase() {
		//int id = forumDAO.getMaxForumId();
		int id=0;
		forum.setForumId(id+1);
		forum.setForumName("TestCase2" + (id + 1));
		forum.setForumContent("Test from testcase" + (id + 1));
		forum.setCreateDate(new Date(System.currentTimeMillis()));
		forum.setUserId("Suveen");
		boolean flag = forumDAO.save(forum);
		assertEquals("createForumTestCase", true, flag);
	}

	@Test
	public void updateForumTestCase() {
		forum.setForumId(1);
		forum.setForumName("TestCase2");
		forum.setForumContent("Update Test from testcase2");
		forum.setCreateDate(new Date(System.currentTimeMillis()));
		forum.setUserId("Suveen");
		boolean flag = forumDAO.update(forum);
		assertEquals("updateForumTestCase", true, flag);
	}

	@Test
	public void deleteForumTestCase() {
		boolean flag = forumDAO.delete(1);
		assertEquals("deleteForumTestCase", true, flag);
	}
	
	@Test
	public void listForumTestCase(){
		int size = forumDAO.list().size();
		assertEquals("listForumTestCase", 1, size);
	}

}
