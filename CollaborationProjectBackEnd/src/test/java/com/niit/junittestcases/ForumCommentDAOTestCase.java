package com.niit.junittestcases;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.niit.dao.ForumCommentDAO;
import com.niit.model.ForumComment;

public class ForumCommentDAOTestCase {

	@Autowired
	static AnnotationConfigApplicationContext context;

	@Autowired
	static ForumComment forumComment;

	@Autowired
	static ForumCommentDAO forumCommentDAO;

	@BeforeClass
	public static void initialize() {
		context = new AnnotationConfigApplicationContext();
		context.scan("com.niit");
		context.refresh();

		// get the categoryDAO from context
		forumCommentDAO = (ForumCommentDAO) context.getBean("forumCommentDAO");

		// get the category from context
		forumComment = (ForumComment) context.getBean("forumComment");

	}
	
	@Test
	public void createForumCommentTestCase(){
		//int id=0;
		int id=forumCommentDAO.getMaxForumCommentId();
		forumComment.setForumCommentId(id+1);
		forumComment.setForumId(1);
		forumComment.setForumComment("Awesome forum content sir. Huge fan of you.");
		forumComment.setCommentDate(new Date(System.currentTimeMillis()));
		forumComment.setUserId("Suveen");
		forumComment.setUsername("Suveen Kumar Vundavalli");
		boolean flag = forumCommentDAO.save(forumComment);
		assertEquals("createForumCommentTestCase", true, flag);
	}
	
	@Test
	public void updateForumCommentTestCase(){
		int id=0;
		//int id=forumCommentDAO.getMaxForumCommentId();
		forumComment.setForumCommentId(id+1);
		forumComment.setForumId(1);
		forumComment.setForumComment("Awesome forum content sir. Huge fan of you.");
		forumComment.setCommentDate(new Date(System.currentTimeMillis()));
		forumComment.setUserId("Suveen");
		forumComment.setUsername("Suveen Kumar Vundavalli");
		boolean flag = forumCommentDAO.update(forumComment);
		assertEquals("createForumCommentTestCase", true, flag);
	}
	
	@Test
	public void deleteForumCommentTestCase(){
		boolean flag = forumCommentDAO.delete(2);
		assertEquals("deleteForumCommentTestCase", true, flag);

	}

	@Test
	public void listForumCommentTestCase(){
		int size = forumCommentDAO.list().size();
		assertEquals("listForumTestCase", 1, size);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
