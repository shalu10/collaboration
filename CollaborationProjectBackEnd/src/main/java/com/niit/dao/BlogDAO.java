package com.niit.dao;

import java.util.List;

import com.niit.model.Blog;

public interface BlogDAO {

	public boolean save(Blog blog);

	public boolean update(Blog blog);

	public boolean delete(int id);

	public Blog getBlogById(int id);

	public List<Blog> list();

	public List<Blog> list(String status);

	public int getMaxBlogId();

}
