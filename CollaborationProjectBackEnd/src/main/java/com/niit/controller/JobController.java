package com.niit.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.JobDAO;
import com.niit.model.Job;
import com.niit.model.JobApplied;

@RestController
public class JobController {

	/*
	 *  /getAllJobs													- Get
	 *  /getAllJobApplications										- Get
	 *  /getAllOpenJobs												- Get
	 *  /getJob/{jobId}												- Get
	 *  /getJobApplication/{jobAppliedId}							- Get
	 *  /getAllApprovedJobsApplied									- Get
	 *  /getAllRejectedJobsApplied									- Get
	 *  /getAllInterviewJobsApplied									- Get
	 *  /closeJob/{jobId}											- Put
	 *  /openJob/{jobId}											- Put
	 *  /getMyAppliedJobs											- Get
	 *  /approveJobApplication/{userId}/{jobId}/{remarks}			- Put
	 *  /rejectJobApplication/{userId}/{jobId}/{remarks}			- Put
	 *  /callForInterviewJobApplication/{userId}/{jobId}/{remarks}	- Put
	 *  /applyJob/{jobId}											- Post
	 *  /postNewJob													- Post
	 *  /updateJob/{jobId}											- Put
	 */
	
	private static Logger log = LoggerFactory.getLogger(JobController.class);

	@Autowired
	Job job;
	@Autowired
	JobApplied jobApplied;
	@Autowired
	JobDAO jobDAO;
	
	@Autowired
	HttpSession session;

	@GetMapping("/getAllJobs")
	public ResponseEntity<List<Job>> getAllJobs() {
		log.debug("---> Starting of method getAllJobs");
		return new ResponseEntity<List<Job>>(jobDAO.list(), HttpStatus.OK);
	}
	
	@GetMapping("/getAllJobApplications")
	public ResponseEntity<List<JobApplied>> getAllJobApplications(){
		log.debug("---> Starting of method getAllJobApplications");
		return new ResponseEntity<List<JobApplied>>(jobDAO.listAllJobsApplied(), HttpStatus.OK);
	}

	@GetMapping("/getAllOpenJobs")
	public ResponseEntity<List<Job>> getAllOpenJobs() {
		log.debug("---> Starting of method getAllOpenJobs");
		return new ResponseEntity<List<Job>>(jobDAO.listAllOpenJobs(), HttpStatus.OK);
	}

	@GetMapping("/getJob/{jobId}")
	public ResponseEntity<Job> getJob(@PathVariable("jobId") String jobId) {
		log.debug("---> Starting of method getJob");
		return new ResponseEntity<Job>(jobDAO.getJobById(jobId), HttpStatus.OK);
	}

	@GetMapping("/getJobApplication/{jobAppliedId}")
	public ResponseEntity<JobApplied> getJobApplication(@PathVariable("jobAppliedId") int jobAppliedId) {
		log.debug("---> Starting of method getJobApplication");
		return new ResponseEntity<JobApplied>(jobDAO.getJobApplication(jobAppliedId), HttpStatus.OK);

	}

	@GetMapping("/getAllApprovedJobsApplied")
	public ResponseEntity<List<JobApplied>> getAllApprovedJobsApplied() {
		log.debug("---> Starting of method getAllApprovedJobsApplied");
		return new ResponseEntity<List<JobApplied>>(jobDAO.listJobsAppliedByStatus("A"), HttpStatus.OK);
	}

	@GetMapping("/getAllRejectedJobsApplied")
	public ResponseEntity<List<JobApplied>> getAllRejectedJobsApplied() {
		log.debug("---> Starting of method getAllRejectedJobsApplied");
		return new ResponseEntity<List<JobApplied>>(jobDAO.listJobsAppliedByStatus("R"), HttpStatus.OK);
	}

	@GetMapping("/getAllInterviewJobsApplied")
	public ResponseEntity<List<JobApplied>> getAllInterviewJobsApplied() {
		log.debug("---> Starting of method getAllInterviewJobsApplied");
		return new ResponseEntity<List<JobApplied>>(jobDAO.listJobsAppliedByStatus("I"), HttpStatus.OK);
	}

	@PutMapping("/closeJob/{jobId}")
	public ResponseEntity<Job> closeJob(@PathVariable("jobId") String jobId) {
		log.debug("---> Starting of method closeJob");
		job = jobDAO.getJobById(jobId);
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		if (loggedInUserRole != null && loggedInUserRole.equals("ROLE_ADMIN")) {
			if (job != null) {
				log.debug("---> Job details retrived");
				job.setJobStatus("C");
				if (jobDAO.update(job)) {
					log.debug("---> Job update successfull");
					job.setErrorCode("200");
					job.setErrorMessage("Job status is now closed");
				} else {
					log.debug("---> Job update failed");
					job.setErrorCode("404");
					job.setErrorMessage("Failed to close Job");
				}
			} else {
				log.debug("---> Job doesnot exist");
				job.setErrorCode("404");
				job.setErrorMessage("Job doesnot exist with jobId : " + jobId);
			}
		} else {
			log.debug("---> Not logged in as admin");
			job.setErrorCode("404");
			job.setErrorMessage("Please login as admin to perform this operation!");
		}
		log.debug("---> Starting of method closeJob");
		return new ResponseEntity<Job>(job, HttpStatus.OK);
	}
	@PutMapping("/openJob/{jobId}")
	public ResponseEntity<Job> openJob(@PathVariable("jobId") String jobId) {
		log.debug("---> Starting of method openJob");
		job = jobDAO.getJobById(jobId);
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		if (loggedInUserRole != null && loggedInUserRole.equals("ROLE_ADMIN")) {
			if (job != null) {
				log.debug("---> Job details retrived");
				job.setCreateDate(new Date(System.currentTimeMillis()));
				job.setJobStatus("O");
				if (jobDAO.update(job)) {
					log.debug("---> Job update successfull");
					job.setErrorCode("200");
					job.setErrorMessage("Job status is now opend");
				} else {
					log.debug("---> Job update failed");
					job.setErrorCode("404");
					job.setErrorMessage("Failed to open Job");
				}
			} else {
				log.debug("---> Job doesnot exist");
				job.setErrorCode("404");
				job.setErrorMessage("Job doesnot exist with jobId : " + jobId);
			}
		} else {
			log.debug("---> Not logged in as admin");
			job.setErrorCode("404");
			job.setErrorMessage("Please login as admin to perform this operation!");
		}
		log.debug("---> Starting of method openJob");
		return new ResponseEntity<Job>(job, HttpStatus.OK);
	}

	@GetMapping("/getMyAppliedJobs")
	public ResponseEntity<List<JobApplied>> getMyAppliedJobs() {
		log.debug("---> Starting of method getMyAppliedJobs");
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");

		if (loggedInUserId == null) {
			List<JobApplied> jobsApplied = new LinkedList<JobApplied>();
			jobApplied.setErrorCode("404");
			jobApplied.setErrorMessage("Please login to perform this operation");
			jobsApplied.add(jobApplied);
			return new ResponseEntity<List<JobApplied>>(jobsApplied, HttpStatus.OK);

		}

		log.debug("---> Ending of method getMyAppliedJobs");
		return new ResponseEntity<List<JobApplied>>(jobDAO.listAllJobsAppliedByMe(loggedInUserId), HttpStatus.OK);
	}

	@PutMapping("/approveJobApplication/{userId}/{jobId}/{remarks}")
	public ResponseEntity<JobApplied> approveJobApplication(@PathVariable("userId") String userId,
			@PathVariable("jobId") String jobId, @PathVariable("remarks") String remarks) {
		log.debug("---> ");
		log.debug("---> Starting of method approveJobApplication");
		jobApplied = updateJobAppliedStatus(userId, jobId, remarks, "A");
		log.debug("---> Ending of method approveJobApplication");
		return new ResponseEntity<JobApplied>(jobApplied, HttpStatus.OK);
	}

	@PutMapping("/rejectJobApplication/{userId}/{jobId}/{remarks}")
	public ResponseEntity<JobApplied> rejectJobApplication(@PathVariable("userId") String userId,
			@PathVariable("jobId") String jobId, @PathVariable("remarks") String remarks) {
		log.debug("---> ");
		log.debug("---> Starting of method rejectJobApplication");
		jobApplied = updateJobAppliedStatus(userId, jobId, remarks, "R");
		log.debug("---> Ending of method rejectJobApplication");
		return new ResponseEntity<JobApplied>(jobApplied, HttpStatus.OK);
	}

	@PutMapping("/callForInterviewJobApplication/{userId}/{jobId}/{remarks}")
	public ResponseEntity<JobApplied> callForInterviewJobApplication(@PathVariable("userId") String userId,
			@PathVariable("jobId") String jobId, @PathVariable("remarks") String remarks) {
		log.debug("---> ");
		log.debug("---> Starting of method callForInterviewJobApplication");
		jobApplied = updateJobAppliedStatus(userId, jobId, remarks, "I");
		log.debug("---> Ending of method callForInterviewJobApplication");
		return new ResponseEntity<JobApplied>(jobApplied, HttpStatus.OK);
	}

	@PostMapping("/applyJob/{jobId}")
	public ResponseEntity<JobApplied> applyJob(@PathVariable("jobId") String jobId) {
		log.debug("---> Starting of method getJob");
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		if (loggedInUserRole != null) {
			if (!isUserAppliedForThisJob(loggedInUserId, jobId)) {
				log.debug("---> Setting job application details manually!");
				jobApplied.setDateApplied(new Date(System.currentTimeMillis()));
				jobApplied.setJobId(jobId);
				jobApplied.setJobAppliedId((jobDAO.getMaxJobApplicationId() + 1));
				jobApplied.setUserId(loggedInUserId);
				jobApplied.setStatus("N");
				if (jobDAO.save(jobApplied)) {
					log.debug("---> Applied for job successfully");
					jobApplied.setErrorCode("200");
					jobApplied.setErrorMessage("Applied for job successfully");
				} else {
					log.debug("---> Failed to apply for job");
					jobApplied.setErrorCode("404");
					jobApplied.setErrorMessage("Failed to apply for job. Please try again!");
				}
			} else {
				log.debug("---> User already applied for this job!");
				jobApplied.setErrorCode("404");
				jobApplied.setErrorMessage("User already applied for this job!");
			}
		} else {
			log.debug("---> User not logged in! Please login to apply for job!");
			jobApplied.setErrorCode("404");
			jobApplied.setErrorMessage("Please login as user to apply for this job!");
		}
		log.debug("---> Ending of method getJob");
		return new ResponseEntity<JobApplied>(jobApplied, HttpStatus.OK);
	}

	@PostMapping("/postNewJob")
	public ResponseEntity<Job> postNewJob(@RequestBody Job job) {
		log.debug("---> ");
		log.debug("---> Starting of method postNewJob");
		log.debug("---> Got user details from session");
		// String loggedInUserId = (String)
		// session.getAttribute("loggedInUserId");
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		if (loggedInUserRole != null && loggedInUserRole.equals("ROLE_ADMIN")) {
			if (jobDAO.getJobById(job.getJobId()) == null) {
				log.debug("---> Setting date and status of job");
				job.setJobStatus("O");
				job.setCreateDate(new Date(System.currentTimeMillis()));
				if (jobDAO.save(job)) {
					log.debug("---> Job saved successfully");
					job.setErrorCode("200");
					job.setErrorMessage("Job saved successfully");
				} else {
					log.debug("---> failed to save job");
					job.setErrorCode("404");
					job.setErrorMessage("Failed to save job");
				}
			} else {
				log.debug("---> Job id already exists!");
				job.setErrorCode("404");
				job.setErrorMessage("Job id already exist! Please try again with another id!");
			}
		} else {
			log.debug("---> Not loggedin as admin");
			job.setErrorCode("404");
			job.setErrorMessage("Please login as admin to perform this task!");
		}
		log.debug("---> Ending of method postNewJob");
		return new ResponseEntity<Job>(job, HttpStatus.OK);

	}

	@PutMapping("/updateJob/{jobId}")
	public ResponseEntity<Job> updateJob(@PathVariable("jobId") String jobId, @RequestBody Job jobUpdated) {
		log.debug("---> Starting of method updateJob");
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		if (loggedInUserRole != null && loggedInUserRole.equals("ROLE_ADMIN")) {
			log.debug("---> Logged in as Admin");
			job = jobDAO.getJobById(jobId);
			if (job != null) {
				log.debug("---> Job exists with the job id");
				job.setJobDescription(jobUpdated.getJobDescription());
				job.setJobQualification(jobUpdated.getJobQualification());
				job.setJobTitle(jobUpdated.getJobTitle());
				if (jobDAO.update(job)) {
					log.debug("---> Update of job successfull");
					job.setErrorCode("200");
					job.setErrorMessage("Job updated successfully!");
				} else {
					log.debug("---> Failed to update Job");
					job.setErrorCode("404");
					job.setErrorMessage("Failed to update Job!");
				}
			} else {
				log.debug("---> Job doesnot exist with jobId : " + jobId);
				job = new Job();
				job.setErrorCode("404");
				job.setErrorMessage("Job does not exist with the jobId : " + jobId);
			}
		} else {
			log.debug("---> Not loggedin / as admin to perform");
			job.setErrorCode("404");
			job.setErrorMessage("Please login as admin to perform this operation!");
		}
		log.debug("---> Ending of method updateJob");
		return new ResponseEntity<Job>(job, HttpStatus.OK);

	}

	private boolean isUserAppliedForThisJob(String userId, String jobId) {
		log.debug("---> Starting of method isUserAppliedForThisJob");
		if (jobDAO.getJobApplication(userId, jobId) == null) {
			log.debug("---> User not applied for this job");
			return false;
		}
		log.debug("---> User applied for this job");
		log.debug("---> Ending of method isUserAppliedForThisJob");
		return true;
	}

	private JobApplied updateJobAppliedStatus(String userId, String jobId, String remarks, String status) {
		log.debug("---> Starting of method updateJobAppliedStatus");
		jobApplied = jobDAO.getJobApplication(userId, jobId);
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");

		if (jobApplied != null) {
			if (loggedInUserRole != null && loggedInUserRole.equals("ROLE_ADMIN")) {
				jobApplied.setRemarks(remarks);
				jobApplied.setStatus(status);
				if (jobDAO.update(jobApplied)) {
					jobApplied.setErrorCode("200");
					jobApplied.setErrorMessage("Status changed successfully to : " + status);
				} else {
					jobApplied.setErrorCode("404");
					jobApplied.setErrorMessage("Status changed failed");
				}

			} else {
				jobApplied.setErrorCode("404");
				jobApplied.setErrorMessage("Please login as admin to perform this operation!");

			}
		} else {

			jobApplied = new JobApplied();
			jobApplied.setErrorCode("404");
			jobApplied.setErrorMessage("Job Application doesnot exist!");

		}
		log.debug("---> Ending of method updateJobAppliedStatus");
		return jobApplied;

	}

}
