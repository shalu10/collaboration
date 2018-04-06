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

import com.niit.dao.BlogCommentDAO;
import com.niit.dao.BlogDAO;
import com.niit.dao.UserDAO;
import com.niit.model.Blog;
import com.niit.model.BlogComment;
import com.niit.model.User;

@RestController
public class BlogCommentController {

	/*
	 * /saveBlogComment						- Post
	 * /updateBlogComment/{blogCommentId}	- Put
	 * /deleteBlogComment/{blogCommentId}	- Delete
	 * /getAllCommentsByBlogId/{blogId}		- Get
	 * /getAllBlogComments					- Get
	 */
	
	private static Logger log = LoggerFactory.getLogger(BlogController.class);
	
	@Autowired Blog blog;
	@Autowired BlogDAO blogDAO;
	@Autowired BlogComment blogComment;
	@Autowired BlogCommentDAO blogCommentDAO;
	@Autowired HttpSession session;
	@Autowired UserDAO userDAO;
	@Autowired User user;
	
	@PostMapping("/saveBlogComment")
	public ResponseEntity<BlogComment> saveBlogComment(@RequestBody BlogComment blogComment){
		log.debug("---> ");
		log.debug("---> Starting of method saveBlogComment");
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		if(loggedInUserId != null){
			log.debug("---> Adding blogcomment with userId : "+loggedInUserId+" for blog : "+blogComment.getBlogId());
			user = userDAO.getUserById(loggedInUserId);
			String username = user.getFirstname()+" "+user.getLastname();
			blogComment.setUserId(loggedInUserId);
			blogComment.setBlogCommentId(blogCommentDAO.getMaxBlogCommentId()+1);
			blogComment.setCommentDate(new Date(System.currentTimeMillis()));
			blogComment.setUsername(username);
			
			if(blogCommentDAO.save(blogComment)){
				log.debug("---> Blog comment save successfull");
				blogComment.setErrorCode("200");
				blogComment.setErrorMessage("Blog comment saved successfully");
			} else{
				log.debug("---> Blog comment save failed");
				blogComment.setErrorCode("404");
				blogComment.setErrorMessage("Failed to save blog comment! please try again");
			}
			
		} else {
			log.debug("---> User need to login to add blog comment");
			blogComment.setErrorCode("404");
			blogComment.setErrorMessage("Please login to add blog comment!");
		}
		log.debug("---> Ending of method saveBlogComment");
		return new ResponseEntity<BlogComment>(blogComment, HttpStatus.OK);
	}
	
	@PutMapping("/updateBlogComment/{blogCommentId}")
	public ResponseEntity<BlogComment> updateBlogComment(@RequestBody BlogComment blogCommentNew, @PathVariable("blogCommentId") int blogCommentId){
		log.debug("---> Starting of method updateBLogComment");
		blogComment = blogCommentDAO.getBlogCommentById(blogCommentId);
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		log.debug("---> Logged in user :"+loggedInUserId);
		if(loggedInUserId != null){
			if(loggedInUserId.equals(blogComment.getUserId())){
				blogComment.setBlogComment(blogCommentNew.getBlogComment());
				if(blogCommentDAO.update(blogComment)){
					log.debug("---> Update blogComment successfull");
					blogComment.setErrorCode("200");
					blogComment.setErrorMessage("Update successfull");
				} else{
					log.debug("---> Update blogComment failed");
					blogComment.setErrorCode("404");
					blogComment.setErrorMessage("Failed to update! Please try again!");
				}
			} else {
				log.debug("---> User tried to delete comment that was not posted by them");
				blogComment.setErrorCode("404");
				blogComment.setErrorMessage("You did not post this comment to update!");
			}
		} else {
			log.debug("---> Usernot logged in to perform updation");
			blogComment.setErrorCode("404");
			blogComment.setErrorMessage("Please login to perform this task!");
		}
		return new ResponseEntity<BlogComment>(blogComment, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteBlogComment/{blogCommentId}")
	public ResponseEntity<BlogComment> deleteBlogComment(@PathVariable("blogCommentId") int blogCommentId){
		log.debug("---> Starting of deleteBlogComment method");
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		blogComment = blogCommentDAO.getBlogCommentById(blogCommentId);
		if(loggedInUserId != null && loggedInUserId.equals(blogComment.getUserId())){
			log.debug("---> Verified if loggedin user posted the comment");
			if(blogCommentDAO.delete(blogCommentId)){
				log.debug("---> Blog comment delete successfull");
				blogComment.setErrorCode("200");
				blogComment.setErrorMessage("Blog Comment Delete successfull");
			} else {
				log.debug("---> blogComment delete failed");
				blogComment.setErrorCode("404");
				blogComment.setErrorMessage("Blog Comment Delete failed");
			}
		}else {
			log.debug("---> User not authorised to perform the deletion");
			blogComment.setErrorCode("404");
			blogComment.setErrorMessage("You are not authorized to perform this task!");
		}
		log.debug("---> Ending of deleteBlogComment method");
		return new ResponseEntity<BlogComment>(blogComment,HttpStatus.OK);
		
	}
	
	@GetMapping("/getAllCommentsByBlogId/{blogId}")
	public ResponseEntity<List<BlogComment>> getAllCommentsByBlogId(@PathVariable("blogId") int blogId){
		log.debug("---> starting of method getAllCommentsByBlogId");
		if(blogDAO.getBlogById(blogId)==null){
			log.debug("---> Blog not found!");
			blogComment = new BlogComment();
			blogComment.setErrorCode("404");
			blogComment.setErrorMessage("Blog Not Found");
			List<BlogComment> blogCommentList = new LinkedList<BlogComment>();
			blogCommentList.add(blogComment);
			return new ResponseEntity<List<BlogComment>>(blogCommentList, HttpStatus.OK);
		}
		
		log.debug("---> Ending of method getAllCommentsByBlogId");
		return new ResponseEntity<List<BlogComment>>(blogCommentDAO.getAllCommentsByBlogId(blogId), HttpStatus.OK);
	}
	
	@GetMapping("/getAllBlogComments")
	public ResponseEntity<List<BlogComment>> getAllBlogComments(){
		log.debug("---> starting of method getAllCommentsByBlogId");
		log.debug("---> Ending of method getAllCommentsByBlogId");
		return new ResponseEntity<List<BlogComment>>(blogCommentDAO.list(), HttpStatus.OK);
	}
	
}
