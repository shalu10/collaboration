package com.niit.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.ForumDAO;
import com.niit.model.Forum;
import com.sun.corba.se.pept.transport.ContactInfo;

@RestController
public class ForumController {

	/*
	 * /getApprovedForums		- Get
	 * /getAllForums			- Get
	 * /getNewForums			- Get
	 * /getRejectedForums		- Get
	 * /getUpdatedForums		- Get
	 * /insertForum				- Post
	 * /updateForum/{forumId}	- Put
	 * /deleteForum/{forumId}	- Delete
	 * /approveForum/{forumId}	- Put
	 * /rejectForum/{forumId}	- Put
	 */
	
	private static Logger log = LoggerFactory.getLogger(ForumController.class);

	@Autowired
	ForumDAO forumDAO;
	@Autowired
	Forum forum;
	@Autowired
	HttpSession session;

	@GetMapping("/getApprovedForums")
	public ResponseEntity<List<Forum>> getApprovedForums() {
		log.info("---> ");
		log.debug("---> in /getApprovedForums");
		log.debug("---> Starting getForums method");
		return new ResponseEntity<List<Forum>>(forumDAO.list("A"), HttpStatus.OK);
	}

	@GetMapping("/getAllForums")
	public ResponseEntity<List<Forum>> getAllForums() {
		log.info("---> ");
		log.debug("---> in /getAllForums");
		log.debug("---> Starting getAllForums method");
		return new ResponseEntity<List<Forum>>(forumDAO.list(), HttpStatus.OK);
	}

	@GetMapping("/getNewForums")
	public ResponseEntity<List<Forum>> getNewForums() {
		log.info("---> ");
		log.debug("---> in /getNewForums");
		log.debug("---> Starting getNewForums method");
		return new ResponseEntity<List<Forum>>(forumDAO.list("N"), HttpStatus.OK);
	}

	@GetMapping("/getUpdatedForums")
	public ResponseEntity<List<Forum>> getUpdatedForums() {
		log.info("---> ");
		log.debug("---> in /getUpdatedForums");
		log.debug("---> Starting getUpdatedForums method");
		return new ResponseEntity<List<Forum>>(forumDAO.list("U"), HttpStatus.OK);
	}
	@GetMapping("/getRejectedForums")
	public ResponseEntity<List<Forum>> getRejectedForums() {
		log.info("---> ");
		log.debug("---> in /getRejectedForums");
		log.debug("---> Starting getRejectedForums method");
		return new ResponseEntity<List<Forum>>(forumDAO.list("R"), HttpStatus.OK);
	}

	@PostMapping("/insertForum")
	public ResponseEntity<Forum> insertForum(@RequestBody Forum forum) {
		log.debug("---> Starting insert forum method");
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		forum.setForumId(getMaxId() + 1);
		forum.setCreateDate(new Date(System.currentTimeMillis()));
		if (loggedInUserId != null && loggedInUserRole.equals("ROLE_ADMIN")) {
			forum.setStatus("A");
		} else {
			forum.setStatus("N");
		}
		forum.setUserId(loggedInUserId);
		if (loggedInUserId == null) {
			log.debug("---> UserId is null. User need to login");
			forum.setErrorCode("404");
			forum.setErrorMessage("Please Login to Continue!");
			log.debug("---> Returning forum with 404 error code!");
			
		} else {
			log.debug("---> User data present in session with userId : " + loggedInUserId);
			if (forumDAO.save(forum)) {
				log.debug("---> Save forum successfull");
				forum.setErrorCode("200");
				forum.setErrorMessage("Successfully Saved Forum");
				
			} else {
				log.debug("---> Save forum failed.");
				forum.setErrorCode("404");
				forum.setErrorMessage("Failed to save Forum");
				
			}
		}
		return new ResponseEntity<Forum>(forum, HttpStatus.OK);
	}

	@PutMapping("/updateForum/{forumId}")
	public ResponseEntity<Forum> updateForum(@PathVariable("forumId") int forumId, @RequestBody Forum forum) {
		log.debug("---> in updateForum method");
		log.debug("---> Getting actual forum with forum id");
		Forum actualForum = forumDAO.getForumById(forumId);
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		if (loggedInUserRole != null) {
			if (loggedInUserId.equals(actualForum.getUserId()) || loggedInUserRole.equals("ROLE_ADMIN")) {
				log.debug("You are the author of the forum...");
				actualForum.setForumName(forum.getForumName());
				actualForum.setForumContent(forum.getForumContent());
				log.debug("---> Setting status to NA after edit");
				if (loggedInUserRole.equals("ROLE_ADMIN")) {
					actualForum.setStatus("A");
				} else {
					actualForum.setStatus("U");
				}
				if (forumDAO.update(actualForum)) {
					log.debug("---> Update forum successfull");
					actualForum.setErrorCode("200");
					actualForum.setErrorMessage("Update successfull!");
				} else {
					log.debug("---> Update forum failed");
					actualForum.setErrorCode("404");
					actualForum.setErrorMessage("Update failed! Please try again.");
				}
			} else {
				log.debug("You are not the author of the forum");
				actualForum.setErrorCode("404");
				actualForum.setErrorMessage("You are not authorised to update since you are not the author of the forum!");
			}
		} else {
			log.debug("user not logged in!");
			actualForum.setErrorCode("404");
			actualForum.setErrorMessage("Please login to perform this operation");

		}
		return new ResponseEntity<Forum>(actualForum, HttpStatus.OK);
	}

	@DeleteMapping("/deleteForum/{forumId}")
	public ResponseEntity<Forum> deleteForum(@PathVariable("forumId") int forumId) {
		log.debug("---> In delete forum method");
		log.warn("You are deleteing forum with id : " + forumId);
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		forum = forumDAO.getForumById(forumId);
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		if (loggedInUserRole != null) {

			if (forum.getUserId() == loggedInUserId || loggedInUserRole.equals("ROLE_ADMIN")) {
				if (forumDAO.delete(forumId)) {
					log.debug("---> delete forum successfull");
					forum.setErrorCode("200");
					forum.setErrorMessage("Delete Forum successfull");
				} else {
					forum.setErrorCode("404");
					log.debug("---> delete forum failed");
					forum.setErrorMessage("Delete forum failed");
				}
			} else {
				forum.setErrorCode("404");
				forum.setErrorMessage("You are not authorised to delete this forum!");
			}
		} else {
			log.debug("user not logged in!");
			forum.setErrorCode("404");
			forum.setErrorMessage("Please login to perform this operation");

		}
		log.debug("---> Returning new forum with error codes and messages");
		return new ResponseEntity<Forum>(forum, HttpStatus.OK);
	}

	@PutMapping("/approveForum/{forumId}")
	public ResponseEntity<Forum> approveForum(@PathVariable("forumId") int forumId) {
		log.debug("---> in approveForum");
		log.debug("---> Getting forum with id : " + forumId);
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		Forum forum = forumDAO.getForumById(forumId);
		if (loggedInUserRole != null) {
			if (loggedInUserRole.equals("ROLE_ADMIN")) {
				String loggedInUserId = (String) session.getAttribute("loggedInUserId");
				forum.setRemarks("Approved by :"+loggedInUserId);
				if (changeStatus(forum, "A")) {
					log.debug("---> Forum approved");
					forum.setErrorCode("200");
					forum.setErrorMessage("Forum Approved Successfully!");
				} else {
					log.debug("---> Failed to approve forum");
					forum.setErrorCode("404");
					forum.setErrorMessage("Forum Approve Failed!");
				}

			} else {
				log.debug("You are not authorised to approve this task since your role is : " + loggedInUserRole);
				forum.setErrorCode("404");
				forum.setErrorMessage(
						"You are not authorised to approve this task since your role is : " + loggedInUserRole);
			}
		} else {
			log.debug("user not logged in!");
			forum.setErrorCode("404");
			forum.setErrorMessage("Please login to perform this operation");

		}
		
		log.debug("---> Returning forum");
		return new ResponseEntity<Forum>(forum, HttpStatus.OK);

	}

	@PutMapping("/rejectForum/{forumId}")
	public ResponseEntity<Forum> rejectForum(@PathVariable("forumId") int forumId) {
		log.debug("---> In reject Forum");
		log.debug("---> Gettng forum details with forum id : " + forumId);
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		Forum forum = forumDAO.getForumById(forumId);
		if (loggedInUserRole != null) {
			if (loggedInUserRole.equals("ROLE_ADMIN")) {
				String loggedInUserId = (String) session.getAttribute("loggedInUserId");
				forum.setRemarks("Rejected by :"+loggedInUserId+". Contact admin if unsatisfied!");
				if (changeStatus(forum, "R")) {
					log.debug("---> Forum rejected successfully");
					forum.setErrorCode("200");
					forum.setErrorMessage("Forum Rejected Successfully!");
				} else {
					log.debug("---> Failed to reject forum");
					forum.setErrorCode("404");
					forum.setErrorMessage("Forum Rejection Failed!");

				}
			} else {
				log.debug("You are not authorised to reject this task since your role is : " + loggedInUserRole);
				forum.setErrorCode("404");
				forum.setErrorMessage(
						"You are not authorised to reject this task since your role is : " + loggedInUserRole);
			}
		} else {
			log.debug("user not logged in!");
			forum.setErrorCode("404");
			forum.setErrorMessage("Please login to perform this operation");

		}
		log.debug("---> Returning updated forum");
		return new ResponseEntity<Forum>(forum, HttpStatus.OK);

	}

	private boolean changeStatus(Forum forum, String status) {
		log.debug("---> In changeStatus private method");
		forum.setStatus(status);
		return forumDAO.update(forum);
	}

	private int getMaxId() {
		log.debug("---> In getMaxId private method");
		return forumDAO.getMaxForumId();
	}
}
