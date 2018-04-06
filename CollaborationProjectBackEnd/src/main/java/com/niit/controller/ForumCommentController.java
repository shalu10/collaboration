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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.ForumCommentDAO;
import com.niit.dao.ForumDAO;
import com.niit.dao.UserDAO;
import com.niit.model.BlogComment;
import com.niit.model.Forum;
import com.niit.model.ForumComment;
import com.niit.model.User;

@RestController
public class ForumCommentController {

	/*
	 * /saveForumComment						- Post
	 * /updateForumComment/{forumCommentId}		- Put
	 * /deleteForumComment/{forumCommentId}		- Delete
	 * /getAllCommentsByForumId/{forumId}		- Get
	 */
	
	
	private static Logger log = LoggerFactory.getLogger(ForumController.class);
	
	@Autowired Forum forum;
	@Autowired ForumDAO forumDAO;
	@Autowired ForumComment forumComment;
	@Autowired ForumCommentDAO forumCommentDAO;
	@Autowired HttpSession session;
	@Autowired UserDAO userDAO;
	@Autowired User user;
	
	
	@GetMapping("/getAllForumComments")
	public ResponseEntity<List<ForumComment>> getAllForumComments(){
		log.debug("---> ");
		log.debug("---> Starting of method getAllForumComments");
		return new ResponseEntity<List<ForumComment>>(forumCommentDAO.list(), HttpStatus.OK);
	}
	
	
	@PostMapping("/saveForumComment")
	public ResponseEntity<ForumComment> saveForumComment(@RequestBody ForumComment forumComment){
		log.debug("---> ");
		log.debug("---> Starting of method saveForumComment");
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		if(loggedInUserId != null){
			log.debug("---> Adding forumcomment with userId : "+loggedInUserId+" for forum : "+forumComment.getForumId());
			user = userDAO.getUserById(loggedInUserId);
			String username = user.getFirstname()+" "+user.getLastname();
			forumComment.setUserId(loggedInUserId);
			forumComment.setForumCommentId(forumCommentDAO.getMaxForumCommentId()+1);
			forumComment.setCommentDate(new Date(System.currentTimeMillis()));
			forumComment.setUsername(username);
			
			if(forumCommentDAO.save(forumComment)){
				log.debug("---> Forum comment save successfull");
				forumComment.setErrorCode("200");
				forumComment.setErrorMessage("Forum comment saved successfully");
			} else{
				log.debug("---> Forum comment save failed");
				forumComment.setErrorCode("404");
				forumComment.setErrorMessage("Failed to save forum comment! please try again");
			}
			
		} else {
			log.debug("---> User need to login to add forum comment");
			forumComment.setErrorCode("404");
			forumComment.setErrorMessage("Please login to add forum comment!");
		}
		log.debug("---> Ending of method saveForumComment");
		return new ResponseEntity<ForumComment>(forumComment, HttpStatus.OK);
	}
	
	@PutMapping("/updateForumComment/{forumCommentId}")
	public ResponseEntity<ForumComment> updateForumComment(@RequestBody ForumComment forumCommentNew, @PathVariable("forumCommentId") int forumCommentId){
		log.debug("---> Starting of method updateBLogComment");
		forumComment = forumCommentDAO.getForumCommentById(forumCommentId);
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		log.debug("---> Logged in user :"+loggedInUserId);
		if(loggedInUserId != null){
			if(loggedInUserId.equals(forumComment.getUserId())){
				forumComment.setForumComment(forumCommentNew.getForumComment());
				if(forumCommentDAO.update(forumComment)){
					log.debug("---> Update forumComment successfull");
					forumComment.setErrorCode("200");
					forumComment.setErrorMessage("Update successfull");
				} else{
					log.debug("---> Update forumComment failed");
					forumComment.setErrorCode("404");
					forumComment.setErrorMessage("Failed to update! Please try again!");
				}
			} else {
				log.debug("---> User tried to delete comment that was not posted by them");
				forumComment.setErrorCode("404");
				forumComment.setErrorMessage("You did not post this comment to update!");
			}
		} else {
			log.debug("---> Usernot logged in to perform updation");
			forumComment.setErrorCode("404");
			forumComment.setErrorMessage("Please login to perform this task!");
		}
		return new ResponseEntity<ForumComment>(forumComment, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteForumComment/{forumCommentId}")
	public ResponseEntity<ForumComment> deleteForumComment(@PathVariable("forumCommentId") int forumCommentId){
		log.debug("---> Starting of deleteForumComment method");
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		forumComment = forumCommentDAO.getForumCommentById(forumCommentId);
		if(loggedInUserId != null && loggedInUserId.equals(forumComment.getUserId())){
			log.debug("---> Verified if loggedin user posted the comment");
			if(forumCommentDAO.delete(forumCommentId)){
				log.debug("---> Forum comment delete successfull");
				forumComment.setErrorCode("200");
				forumComment.setErrorMessage("Forum Comment Delete successfull");
			} else {
				log.debug("---> forumComment delete failed");
				forumComment.setErrorCode("404");
				forumComment.setErrorMessage("Forum Comment Delete failed");
			}
		}else {
			log.debug("---> User not authorised to perform the deletion");
			forumComment.setErrorCode("404");
			forumComment.setErrorMessage("You are not authorized to perform this task!");
		}
		log.debug("---> Ending of deleteForumComment method");
		return new ResponseEntity<ForumComment>(forumComment,HttpStatus.OK);
		
	}
	
	@GetMapping("/getAllCommentsByForumId/{forumId}")
	public ResponseEntity<List<ForumComment>> getAllCommentsByForumId(@PathVariable("forumId") int forumId){
		log.debug("---> starting of method getAllCommentsByForumId");
		if(forumDAO.getForumById(forumId)==null){
			forumComment = new ForumComment();
			forumComment.setErrorCode("404");
			forumComment.setErrorMessage("Forum Not Found");
			List<ForumComment> forumCommentList = new LinkedList<ForumComment>();
			forumCommentList.add(forumComment);
			return new ResponseEntity<List<ForumComment>>(forumCommentList, HttpStatus.OK);
		}
		
		
		return new ResponseEntity<List<ForumComment>>(forumCommentDAO.getAllCommentsByForumId(forumId), HttpStatus.OK);
	}
	
}
