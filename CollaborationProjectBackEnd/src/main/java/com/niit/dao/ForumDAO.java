package com.niit.dao;

import java.util.List;

import com.niit.model.Forum;

public interface ForumDAO {

	public boolean save(Forum forum);

	public boolean update(Forum forum);

	public boolean delete(int id);

	public Forum getForumById(int id);

	public List<Forum> list();
	
	public List<Forum> list(String status);
	
	public int getMaxForumId();
}
