package com.niit.daoimpl;

import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.niit.dao.FriendDAO;
import com.niit.dao.UserDAO;
import com.niit.model.Friend;
import com.niit.model.User;

@Repository("userDAO")
@Transactional
public class UserDAOImpl implements UserDAO {

	private static Logger log = LoggerFactory.getLogger(UserDAOImpl.class);

	@Autowired
	FriendDAO friendDAO;
	
	@Autowired
	Friend friend;
	
	@Autowired
	SessionFactory sessionFactory;

	public UserDAOImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	public boolean save(User user) {
		log.debug("---> Starting of User Save method");

		try {
			getCurrentSession().save(user);
		} catch (Exception e) {
			log.debug("---> Exception arised in User Save method");
			e.printStackTrace();
			return false;
		}

		log.debug("---> Ending of User Save method");
		return true;
	}

	public boolean update(User user) {
		log.debug("---> Starting of User update method");

		try {
			getCurrentSession().update(user);
		} catch (Exception e) {
			log.debug("---> Exception arised in User update method");
			e.printStackTrace();
			return false;
		}

		log.debug("---> Ending of User update method");
		return true;
	}

	public boolean delete(String userId) {
		log.debug("---> Starting of User delete method");

		try {
			getCurrentSession().delete(getUserById(userId));
		} catch (Exception e) {
			log.debug("---> Exception arised in User delete method");
			e.printStackTrace();
			return false;
		}

		log.debug("---> Ending of User delete method");
		return true;
	}

	public User getUserById(String userId) {
		log.debug("---> Starting of getUserById method");
		return (User) getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("userId", userId))
				.uniqueResult();
	}
	
	public User getUserById(String userId, String status) {
		log.debug("---> Starting of getUserById method");
		return (User) getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("userId", userId))
				.add(Restrictions.eq("status", status))
				.uniqueResult();
	}


	public List<User> list() {
		log.debug("---> Starting of list method in User");
		return getCurrentSession().createCriteria(User.class).list();
	}
	
	public List<User> list(String status) {
		log.debug("---> Starting of list by status method in User");
		return getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("status", status))
				.list();
	}

	public User validate(String userId, String password) {
		log.debug("---> Starting of method User Validate");
		return (User) getCurrentSession().createCriteria(User.class)
				.add(Restrictions.eq("userId", userId))
				.add(Restrictions.eq("password", password))
				.uniqueResult();
		

	}
	
	public void setOnline(String userId) {
		getCurrentSession().createQuery("UPDATE User SET isOnline = 'Y' where userId = ?").setString(0, userId).executeUpdate();
	}

	public void setOffline(String userId) {
		getCurrentSession().createQuery("UPDATE User SET isOnline = 'N' where userId = ?").setString(0, userId).executeUpdate();
		}


	public List<User> notMyFriendList(String userId) {
		String hql = "FROM User WHERE userId NOT IN (SELECT friendId FROM Friend WHERE userId='" + userId
				+ "' OR friendId='" + userId + "')";
		log.debug("Query : "+hql);
		return getCurrentSession().createQuery(hql).list();

	}
	public List<User> notMyFriendList1(String userId) {
		List<Friend> friends = new LinkedList<Friend>();
		friends = friendDAO.getFriendForNotCondition(userId);
		
		
		//Query query1 = getCurrentSession().createQuery("select s.id from Salary s where s.salary < 50000 AND s.salary > 49980");
		Query query2 = getCurrentSession().createQuery("from User where userId not in (:ids)").setParameterList("ids", friends);
		return query2.list();
		
	}

	
}
