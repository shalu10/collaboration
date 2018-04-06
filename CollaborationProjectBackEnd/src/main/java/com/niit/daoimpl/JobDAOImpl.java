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

import com.niit.dao.JobDAO;
import com.niit.model.Job;
import com.niit.model.JobApplied;

@Repository("jobDAO")
@Transactional
public class JobDAOImpl implements JobDAO{
	
	private static Logger log = LoggerFactory.getLogger(JobDAOImpl.class);
	
	@Autowired
	SessionFactory sessionFactory;
	
	public JobDAOImpl(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	public Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	public boolean save(Job job) {
		log.debug("---> Starting of save job method");
		try {
			getCurrentSession().save(job);
		} catch (Exception e) {
			log.debug("---> Exception arised during save job method");
			e.printStackTrace();
			return false;
		}
		log.debug("---> Ending of save job method");
		return true;
	}

	public boolean update(Job job) {
		log.debug("---> Starting of update job method");
		try {
			getCurrentSession().update(job);
		} catch (Exception e) {
			log.debug("---> Exception arised during update job method");
			e.printStackTrace();
			return false;
		}
		log.debug("---> Ending of update job method");
		return true;
	}
	
	public List<Job> list() {
		return getCurrentSession().createCriteria(Job.class).list();
	}

	public List<Job> list(String status) {
		log.debug("---> Starting of list jobs method by status");
		return getCurrentSession().createCriteria(Job.class)
		.add(Restrictions.eq("jobStatus", status))
		.list();
	}

	public boolean save(JobApplied jobApplied) {
		log.debug("---> Starting of save jobApplied method");
		try {
			getCurrentSession().save(jobApplied);
		} catch (Exception e) {
			log.debug("---> Exception arised during save jobApplied method");
			e.printStackTrace();
			return false;
		}
		log.debug("---> Ending of save jobApplied method");
		return true;
	}

	public boolean update(JobApplied jobApplied) {
		log.debug("---> Starting of update jobApplied method");
		try {
			getCurrentSession().update(jobApplied);
		} catch (Exception e) {
			log.debug("---> Exception arised during update jobApplied method");
			e.printStackTrace();
			return false;
		}
		log.debug("---> Ending of update jobApplied method");
		return true;
	}

	public List<JobApplied> listAllJobsAppliedByMe(String userId) {
		log.debug("---> Starting of list jobApplied method");
		return getCurrentSession().createCriteria(JobApplied.class)
		.add(Restrictions.eq("userId", userId))
		.list();
	}

	public int getMaxJobApplicationId() {
		
		int maxNum;
		try {
			log.debug("---> getting max id");
			maxNum = (Integer) getCurrentSession().createQuery("select max(jobAppliedId) from JobApplied").uniqueResult();
			log.debug("---> got maxId = " + maxNum);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("No maxId. Hence returning 0");
			maxNum = 0;
		}
		return maxNum;
		
	}

	public JobApplied getJobApplication(String userId, String jobId) {
		log.debug("---> Starting of getJobApplication with userId and jobId");
		return (JobApplied) getCurrentSession().createCriteria(JobApplied.class)
		.add(Restrictions.eq("userId", userId))
		.add(Restrictions.eq("jobId", jobId))
		.uniqueResult();
	}

	public JobApplied getJobApplication(int jobAppliedId) {
		log.debug("---> Starting of getJobApplication with userId and jobId");
		return (JobApplied) getCurrentSession().get(JobApplied.class, jobAppliedId);
	}

	public List<Job> listAllOpenJobs() {
		log.debug("Starting of listAllOpenJobs method");
		return getCurrentSession().createCriteria(Job.class)
		.add(Restrictions.eq("jobStatus", "O"))
		.list();
	}

	public Job getJobById(String id) {
		return (Job) getCurrentSession().createCriteria(Job.class)
		.add(Restrictions.eq("jobId", id))
		.uniqueResult();
	}

	public List<JobApplied> listJobsAppliedByStatus(String status) {
		return getCurrentSession().createCriteria(JobApplied.class)
		.add(Restrictions.eq("status", status))
		.list();
	}

	public List<JobApplied> listAllJobsApplied() {
		return getCurrentSession().createCriteria(JobApplied.class)
				.list();
	}

	
	

}
