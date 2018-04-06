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

import com.niit.dao.BlogDAO;
import com.niit.model.Blog;

@RestController
public class BlogController {
	
	/*
	 * /getApprovedBlogs		- Get
	 * /getAllBlogs				- Get
	 * /getNewBlogs				- Get
	 * /getRejectedBlogs		- Get
	 * /getUpdatedBlogs			- Get
	 * /insertBlog				- Post
	 * /updateBlog/{blogId}		- Put
	 * /deleteBlog/{blogId}		- Delete
	 * /approveBlog/{blogId}	- Put
	 * /rejectBlog/{blogId}		- Put
	 */

	private static Logger log = LoggerFactory.getLogger(BlogController.class);

	@Autowired
	BlogDAO blogDAO;
	@Autowired
	Blog blog;
	@Autowired
	HttpSession session;

	@GetMapping("/getApprovedBlogs")
	public ResponseEntity<List<Blog>> getApprovedBlogs() {
		log.info("---> ");
		log.debug("---> in /getApprovedBlogs");
		log.debug("---> Starting getBlogs method");
		return new ResponseEntity<List<Blog>>(blogDAO.list("A"), HttpStatus.OK);
	}

	@GetMapping("/getAllBlogs")
	public ResponseEntity<List<Blog>> getAllBlogs() {
		log.info("---> ");
		log.debug("---> in /getAllBlogs");
		log.debug("---> Starting getAllBlogs method");
		return new ResponseEntity<List<Blog>>(blogDAO.list(), HttpStatus.OK);
	}

	@GetMapping("/getNewBlogs")
	public ResponseEntity<List<Blog>> getNewBlogs() {
		log.info("---> ");
		log.debug("---> in /getNewBlogs");
		log.debug("---> Starting getNewBlogs method");
		return new ResponseEntity<List<Blog>>(blogDAO.list("N"), HttpStatus.OK);
	}
	@GetMapping("/getRejectedBlogs")
	public ResponseEntity<List<Blog>> getRejectedBlogs() {
		log.info("---> ");
		log.debug("---> in /getRejectedBlogs");
		log.debug("---> Starting getRejectedBlogs method");
		return new ResponseEntity<List<Blog>>(blogDAO.list("R"), HttpStatus.OK);
	}

	@GetMapping("/getUpdatedBlogs")
	public ResponseEntity<List<Blog>> getUpdatedBlogs() {
		log.info("---> ");
		log.debug("---> in /getApprovedBlogs");
		log.debug("---> Starting getBlogs method");
		return new ResponseEntity<List<Blog>>(blogDAO.list("U"), HttpStatus.OK);
	}

	@PostMapping("/insertBlog")
	public ResponseEntity<Blog> insertBlog(@RequestBody Blog blog) {
		log.debug("---> Starting insert blog method");
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		blog.setBlogId(getMaxId() + 1);
		blog.setCreateDate(new Date(System.currentTimeMillis()));
		blog.setLikes(0);
		if (loggedInUserId != null && loggedInUserRole.equals("ROLE_ADMIN")) {
			blog.setStatus("A");
		} else {
			blog.setStatus("N");
		}
		blog.setUserId(loggedInUserId);
		if (loggedInUserId == null) {
			log.debug("---> UserId is null. User need to login");
			blog.setErrorCode("404");
			blog.setErrorMessage("Please Login to Continue!");
			log.debug("---> Returning blog with 404 error code!");
			
		} else {
			log.debug("---> User data present in session with userId : " + loggedInUserId);
			if (blogDAO.save(blog)) {
				log.debug("---> Save blog successfull");
				blog.setErrorCode("200");
				blog.setErrorMessage("Successfully Saved Blog");
				
			} else {
				log.debug("---> Save blog failed.");
				blog.setErrorCode("404");
				blog.setErrorMessage("Failed to save Blog");
				
			}
		}
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}

	@PutMapping("/updateBlog/{blogId}")
	public ResponseEntity<Blog> updateBlog(@PathVariable("blogId") int blogId, @RequestBody Blog blog) {
		log.debug("---> in updateBlog method");
		log.debug("---> Getting actual blog with blog id");
		Blog actualBlog = blogDAO.getBlogById(blogId);
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		if (loggedInUserRole != null) {
			if (loggedInUserId.equals(actualBlog.getUserId()) || loggedInUserRole.equals("ROLE_ADMIN")) {
				log.debug("You are the author of the blog...");
				actualBlog.setBlogName(blog.getBlogName());
				actualBlog.setBlogContent(blog.getBlogContent());
				log.debug("---> Setting status to U after edit");
				if (loggedInUserRole.equals("ROLE_ADMIN")) {
					actualBlog.setStatus("A");
				} else {
					actualBlog.setStatus("U");
				}
				if (blogDAO.update(actualBlog)) {
					log.debug("---> Update blog successfull");
					actualBlog.setErrorCode("200");
					actualBlog.setErrorMessage("Update successfull!");
				} else {
					log.debug("---> Update blog failed");
					actualBlog.setErrorCode("404");
					actualBlog.setErrorMessage("Update failed! Please try again.");
				}
			} else {
				log.debug("You are not the author of the blog");
				actualBlog.setErrorCode("404");
				actualBlog.setErrorMessage("You are not authorised to update since you are not the author of the blog!");
			}
		} else {
			log.debug("user not logged in!");
			actualBlog.setErrorCode("404");
			actualBlog.setErrorMessage("Please login to perform this operation");

		}
		return new ResponseEntity<Blog>(actualBlog, HttpStatus.OK);
	}

	@DeleteMapping("/deleteBlog/{blogId}")
	public ResponseEntity<Blog> deleteBlog(@PathVariable("blogId") int blogId) {
		log.debug("---> In delete blog method");
		log.warn("You are deleteing blog with id : " + blogId);
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		blog = blogDAO.getBlogById(blogId);
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		if (loggedInUserRole != null) {

			if (blog.getUserId() == loggedInUserId || loggedInUserRole.equals("ROLE_ADMIN")) {
				if (blogDAO.delete(blogId)) {
					log.debug("---> delete blog successfull");
					blog.setErrorCode("200");
					blog.setErrorMessage("Delete Blog successfull");
				} else {
					blog.setErrorCode("404");
					log.debug("---> delete blog failed");
					blog.setErrorMessage("Delete blog failed");
				}
			} else {
				blog.setErrorCode("404");
				blog.setErrorMessage("You are not authorised to delete this blog!");
			}
		} else {
			log.debug("user not logged in!");
			blog.setErrorCode("404");
			blog.setErrorMessage("Please login to perform this operation");

		}
		log.debug("---> Returning new blog with error codes and messages");
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}

	@PutMapping("/approveBlog/{blogId}")
	public ResponseEntity<Blog> approveBlog(@PathVariable("blogId") int blogId) {
		log.debug("---> in approveBlog");
		log.debug("---> Getting blog with id : " + blogId);
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		Blog blog = blogDAO.getBlogById(blogId);
		if (loggedInUserRole != null) {
			if (loggedInUserRole.equals("ROLE_ADMIN")) {
				blog.setRemarks("Approved by "+loggedInUserId);
				if (changeStatus(blog, "A")) {
					log.debug("---> Blog approved");
					blog.setErrorCode("200");
					blog.setErrorMessage("Blog Approved Successfully!");
				} else {
					log.debug("---> Failed to approve blog");
					blog.setErrorCode("404");
					blog.setErrorMessage("Blog Approve Failed!");
				}

			} else {
				log.debug("You are not authorised to approve this task since your role is : " + loggedInUserRole);
				blog.setErrorCode("404");
				blog.setErrorMessage(
						"You are not authorised to approve this task since your role is : " + loggedInUserRole);
			}
		} else {
			log.debug("user not logged in!");
			blog.setErrorCode("404");
			blog.setErrorMessage("Please login to perform this operation");

		}
		
		log.debug("---> Returning blog");
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);

	}

	@PutMapping("/rejectBlog/{blogId}")
	public ResponseEntity<Blog> rejectBlog(@PathVariable("blogId") int blogId) {
		log.debug("---> In reject Blog");
		log.debug("---> Gettng blog details with blog id : " + blogId);
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		Blog blog = blogDAO.getBlogById(blogId);
		if (loggedInUserRole != null) {
			if (loggedInUserRole.equals("ROLE_ADMIN")) {
				blog.setRemarks("Rejected by "+loggedInUserId+". For more details please contact admin!");
				if (changeStatus(blog, "R")) {
					log.debug("---> Blog rejected successfully");
					blog.setErrorCode("200");
					blog.setErrorMessage("Blog Rejected Successfully!");
				} else {
					log.debug("---> Failed to reject blog");
					blog.setErrorCode("404");
					blog.setErrorMessage("Blog Rejection Failed!");

				}
			} else {
				log.debug("You are not authorised to reject this task since your role is : " + loggedInUserRole);
				blog.setErrorCode("404");
				blog.setErrorMessage(
						"You are not authorised to reject this task since your role is : " + loggedInUserRole);
			}
		} else {
			log.debug("user not logged in!");
			blog.setErrorCode("404");
			blog.setErrorMessage("Please login to perform this operation");

		}
		log.debug("---> Returning updated blog");
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);

	}

	private boolean changeStatus(Blog blog, String status) {
		log.debug("---> In changeStatus private method");
		blog.setStatus(status);
		return blogDAO.update(blog);
	}

	private int getMaxId() {
		log.debug("---> In getMaxId private method");
		return blogDAO.getMaxBlogId();
	}
}
