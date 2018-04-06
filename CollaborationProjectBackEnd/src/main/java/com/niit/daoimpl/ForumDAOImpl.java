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

import com.niit.dao.ForumDAO;
import com.niit.model.Forum;

@Repository("forumDAO")
@Transactional
public class ForumDAOImpl implements ForumDAO{
	
	private static Logger log = LoggerFactory.getLogger(ForumDAOImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	public ForumDAOImpl(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	public Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	public boolean save(Forum forum) {
		log.debug("---> Starting of method save Forum");
		try {
			getCurrentSession().save(forum);
		} catch (Exception e) {
			log.debug("---> Exception arised while saving forum");
			e.printStackTrace();
			return false;
		}
		log.debug("---> Ending of method save Forum");
		return true;
		
	}

	public boolean update(Forum forum) {
		log.debug("---> Starting of method update Forum");
		try {
			getCurrentSession().update(forum);
		} catch (Exception e) {
			log.debug("---> Exception arised while updating forum");
			e.printStackTrace();
			return false;
		}
		log.debug("---> Ending of method update Forum");
		return true;
		
	}

	public boolean delete(int id) {
		log.debug("---> Starting of method delete Forum");
		try {
			getCurrentSession().delete(getForumById(id));
		} catch (Exception e) {
			log.debug("---> Exception arised while updating forum");
			e.printStackTrace();
			return false;
		}
		log.debug("---> Ending of method delete Forum");
		return true;
		
	}

	public Forum getForumById(int id) {
		log.debug("---> Starting of method getForumById");		
		return (Forum) getCurrentSession().get(Forum.class, id);
	}

	public List<Forum> list() {
		log.debug("---> Starting of method list in Forum");
		return getCurrentSession().createCriteria(Forum.class).list();
		
	}

	public int getMaxForumId() {
		
		int maxNum;
		try {
			log.debug("---> getting max id");
			maxNum = (Integer) getCurrentSession().createQuery("select max(forumId) from Forum").uniqueResult();
			log.debug("---> got maxId = " + maxNum);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("No maxId. Hence returning 0");
			maxNum = 0;
		}
		return maxNum;
	
	}

	public List<Forum> list(String status) {
		return getCurrentSession().createCriteria(Forum.class)
				.add(Restrictions.eq("status", status))
				.list();
	}

	
	
}
