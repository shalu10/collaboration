package com.niit.junittestcases;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.niit.dao.JobDAO;
import com.niit.model.Job;
import com.niit.model.JobApplied;

public class JobDAOTestCase {

	@Autowired
	static AnnotationConfigApplicationContext context;

	@Autowired
	static Job job;
	@Autowired
	static JobApplied jobApplied;

	@Autowired
	static JobDAO jobDAO;

	@BeforeClass
	public static void initialize() {
		context = new AnnotationConfigApplicationContext();
		context.scan("com.niit");
		context.refresh();

		jobDAO = (JobDAO) context.getBean("jobDAO");

		job = (Job) context.getBean("job");
		jobApplied = (JobApplied) context.getBean("jobApplied");

	}
	
	@Test
	public void saveJobTestCase(){
		job.setJobId("SoftwareDeveloper2");
		job.setCreateDate(new Date(System.currentTimeMillis()));
		job.setJobDescription("Software Developer in a leading software company");
		job.setJobQualification("B. Tech, BE, BS, M. Tech, ME, MS");
		job.setJobStatus("O");
		job.setJobTitle("Urgent opening for Java Developer");
		
		boolean flag = jobDAO.save(job);
		assertEquals("saveJobTestCase", true, flag);
	}
	
	@Test
	public void saveJobAppliedTestCase(){
		//int id = 0;
		int id = jobDAO.getMaxJobApplicationId();
		jobApplied.setJobAppliedId(id+1);
		jobApplied.setJobId("SoftwareDeveloper2");
		jobApplied.setUserId("suveen");
		jobApplied.setDateApplied(new Date(System.currentTimeMillis()));
		jobApplied.setStatus("N");
		jobApplied.setRemarks("Under Verification");
		
		boolean flag = jobDAO.save(jobApplied);
		assertEquals("saveJobAppliedTestCase", true, flag);
		
	}
	
	@Test
	public void listAllJobsTestCase(){
		int size = jobDAO.list().size();
		assertEquals("listAllJobsTestCase", 2, size);
	}
	
	@Test
	public void listJobsAppliedTestCase(){
		int size = jobDAO.listAllJobsAppliedByMe("suveen").size();
		assertEquals("listAllJobsTestCase", 2, size);
	}
	
	@Test
	public void listOpenJobsTestCase(){
		int size = jobDAO.list("O").size();
		assertEquals("listAllJobsTestCase", 2, size);
	}
	
	

}
