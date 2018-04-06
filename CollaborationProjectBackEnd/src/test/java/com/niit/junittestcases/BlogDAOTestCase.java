package com.niit.junittestcases;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.niit.dao.BlogDAO;
import com.niit.model.Blog;

public class BlogDAOTestCase {

	@Autowired
	static AnnotationConfigApplicationContext context;

	@Autowired
	static Blog blog;

	@Autowired
	static BlogDAO blogDAO;

	@BeforeClass
	public static void initialize() {
		context = new AnnotationConfigApplicationContext();
		context.scan("com.niit");
		context.refresh();

		// get the categoryDAO from context
		blogDAO = (BlogDAO) context.getBean("blogDAO");

		// get the category from context
		blog = (Blog) context.getBean("blog");

	}

	@Test
	public void createBlogTestCase() {
		int id = blogDAO.getMaxBlogId();
		blog.setBlogId(id + 1);
		blog.setBlogName("TestCase2" + (id + 1));
		blog.setBlogContent("Test from testcase" + (id + 1));
		blog.setCreateDate(new Date(System.currentTimeMillis()));
		blog.setLikes(0);
		blog.setStatus("NA");
		blog.setUserId("Suveen");
		boolean flag = blogDAO.save(blog);
		assertEquals("createBlogTestCase", true, flag);
	}

	@Test
	public void updateBlogTestCase() {
		blog.setBlogId(2);
		blog.setBlogName("TestCase2");
		blog.setBlogContent("Update Test from testcase2");
		blog.setCreateDate(new Date(System.currentTimeMillis()));
		blog.setLikes(0);
		blog.setStatus("NA");
		blog.setUserId("Suveen");
		boolean flag = blogDAO.update(blog);
		assertEquals("updateBlogTestCase", true, flag);
	}

	@Test
	public void deleteBlogTestCase() {
		boolean flag = blogDAO.delete(5);
		assertEquals("deleteBlogTestCase", true, flag);
	}
	
	@Test
	public void listBlogTestCase(){
		int size = blogDAO.list().size();
		assertEquals("listBlogTestCase", 4, size);
	}

}
