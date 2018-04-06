package com.niit.junittestcases;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.niit.dao.BlogCommentDAO;
import com.niit.model.BlogComment;

public class BlogCommentDAOTestCase {

	@Autowired
	static AnnotationConfigApplicationContext context;

	@Autowired
	static BlogComment blogComment;

	@Autowired
	static BlogCommentDAO blogCommentDAO;

	@BeforeClass
	public static void initialize() {
		context = new AnnotationConfigApplicationContext();
		context.scan("com.niit");
		context.refresh();

		// get the categoryDAO from context
		blogCommentDAO = (BlogCommentDAO) context.getBean("blogCommentDAO");

		// get the category from context
		blogComment = (BlogComment) context.getBean("blogComment");

	}
	
	@Test
	public void createBlogCommentTestCase(){
		//int id=0;
		int id=blogCommentDAO.getMaxBlogCommentId();
		blogComment.setBlogCommentId(id+1);
		blogComment.setBlogId(1);
		blogComment.setBlogComment("Awesome blog content sir. Huge fan of you.");
		blogComment.setCommentDate(new Date(System.currentTimeMillis()));
		blogComment.setUserId("Suveen");
		blogComment.setUsername("Suveen Kumar Vundavalli");
		boolean flag = blogCommentDAO.save(blogComment);
		assertEquals("createBlogCommentTestCase", true, flag);
	}
	
	@Test
	public void updateBlogCommentTestCase(){
		int id=0;
		//int id=blogCommentDAO.getMaxBlogCommentId();
		blogComment.setBlogCommentId(id+1);
		blogComment.setBlogId(1);
		blogComment.setBlogComment("Awesome blog content sir. Huge fan of you.");
		blogComment.setCommentDate(new Date(System.currentTimeMillis()));
		blogComment.setUserId("Suveen");
		blogComment.setUsername("Suveen Kumar Vundavalli");
		boolean flag = blogCommentDAO.update(blogComment);
		assertEquals("createBlogCommentTestCase", true, flag);
	}
	
	@Test
	public void deleteBlogCommentTestCase(){
		boolean flag = blogCommentDAO.delete(1);
		assertEquals("deleteBlogCommentTestCase", true, flag);

	}

	@Test
	public void listBlogCommentTestCase(){
		int size = blogCommentDAO.list().size();
		assertEquals("listBlogTestCase", 1, size);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
