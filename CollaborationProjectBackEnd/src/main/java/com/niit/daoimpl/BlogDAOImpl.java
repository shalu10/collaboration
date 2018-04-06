package com.niit.daoimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.niit.dao.BlogDAO;
import com.niit.model.Blog;

@Repository("blogDAO")
@Transactional
public class BlogDAOImpl implements BlogDAO{
	
	private static Logger log = LoggerFactory.getLogger(BlogDAOImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	public BlogDAOImpl(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	public Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	public boolean save(Blog blog) {
		log.debug("---> Starting of method save Blog");
		try {
			getCurrentSession().save(blog);
		} catch (Exception e) {
			log.debug("---> Exception arised while saving blog");
			e.printStackTrace();
			return false;
		}
		log.debug("---> Ending of method save Blog");
		return true;
		
	}

	public boolean update(Blog blog) {
		log.debug("---> Starting of method update Blog");
		try {
			getCurrentSession().update(blog);
		} catch (Exception e) {
			log.debug("---> Exception arised while updating blog");
			e.printStackTrace();
			return false;
		}
		log.debug("---> Ending of method update Blog");
		return true;
		
	}

	public boolean delete(int id) {
		log.debug("---> Starting of method delete Blog");
		try {
			getCurrentSession().delete(getBlogById(id));
		} catch (Exception e) {
			log.debug("---> Exception arised while updating blog");
			e.printStackTrace();
			return false;
		}
		log.debug("---> Ending of method delete Blog");
		return true;
		
	}

	public Blog getBlogById(int id) {
		log.debug("---> Starting of method getBlogById");		
		return (Blog) getCurrentSession().get(Blog.class, id);
	}

	public List<Blog> list() {
		log.debug("---> Starting of method list in Blog");
		return getCurrentSession().createCriteria(Blog.class)
				.list();
		
	}

	public int getMaxBlogId() {
		int maxNum;
		try {
			log.debug("---> getting max id");
			maxNum = (Integer) getCurrentSession().createQuery("select max(blogId) from Blog").uniqueResult();
			log.debug("---> got maxId = "+maxNum);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("No maxId. Hence returning 0");
			maxNum=0;
		}
		return maxNum;
	}

	public List<Blog> list(String status) {
		log.debug("---> Starting of method list in Blog");
		return getCurrentSession().createCriteria(Blog.class)
				.add(Restrictions.eq("status", status))
				.list();
		
	}

	
}
