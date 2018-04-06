package com.niit.controller;

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

import com.niit.dao.FriendDAO;
import com.niit.dao.UserDAO;
import com.niit.model.User;

@RestController
public class UserController {
	
	/*
	 *  /getAllUsers				- Get
	 *  /getNewUsers				- Get
	 *  /getRejectedUsers			- Get
	 *  /validate					- Post
	 *  /register					- Post
	 *  /signOut					- Get
	 *  /approveUser/{userId}		- Put
	 *  /changeUserRole/{userId}	- Put
	 *  /rejectUser/{userId}		- Put
	 *  /myProfile					- Get
	 *  /getNotMyFriendsList		- Get
	 */
	
	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserDAO userDAO;

	@Autowired
	User user;
	
	@Autowired
	FriendDAO friendDAO;

	@Autowired
	HttpSession session;

	@GetMapping("/getAllUsers")
	public ResponseEntity<List<User>> getAllUsers() {
		log.debug("---> in method listAllUsers");
		return new ResponseEntity<List<User>>(userDAO.list(), HttpStatus.OK);
	}
	
	@GetMapping("/getNewUsers")
	public ResponseEntity<List<User>> getNewUsers() {
		log.debug("---> In method netNewUsers");
		return new ResponseEntity<List<User>>(userDAO.list("N"), HttpStatus.OK);
	}
	
	@GetMapping("/getRejectedUsers")
	public ResponseEntity<List<User>> getRejectedUsers() {
		log.debug("---> In method getRejectedUsers");
		return new ResponseEntity<List<User>>(userDAO.list("R"), HttpStatus.OK);
	}

	@PostMapping("/validate")
	public ResponseEntity<User> validateUser(@RequestBody User user) {
		log.info("---> Starting of validate method");
		user = userDAO.validate(user.getUserId(), user.getPassword());
		if (user == null) {
			log.debug("--->Invalid user credentials");
			user = new User();
			user.setErrorCode("404");
			user.setErrorMessage("Invalid login details!");
		} else {
			if(userDAO.getUserById(user.getUserId(), "A") != null){
				// String fullName = user.getFirstname() +" "+ user.getLastname();
				log.debug("---> Valid user credentials");
				user.setIsOnline("Y");
				friendDAO.setOnline(user.getUserId());
				userDAO.update(user);
				// userDAO.setOnline(user.getUserId());
				user.setErrorCode("200");
				user.setErrorMessage("Login Successfull!");
				log.debug("---> Saving user details in session");
				session.setAttribute("User", user);
				session.setAttribute("loggedInUserId", user.getUserId());
				session.setAttribute("loggedInUserRole", user.getRole());
			} else if(userDAO.getUserById(user.getUserId(), "R") != null){
				user = new User();
				user.setErrorCode("404");
				user.setErrorMessage("Your account has been rejected by the admin!");
			} else if(userDAO.getUserById(user.getUserId(), "N") != null){
				user = new User();
				user.setErrorCode("404");
				user.setErrorMessage("Please wait until admin approves your account!");
			}
			else {
				user = new User();
				user.setErrorCode("404");
				user.setErrorMessage("Unknown error!");
			}
		}
		log.info("---> Ending of validate method");
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		log.debug("---> Starting of register method");
		log.debug("---> setting degault online status to N");

		if (userDAO.getUserById(user.getUserId()) == null) {

			user.setIsOnline("N");

			String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");

			/*if (loggedInUserRole != null && loggedInUserRole.equals("ROLE_ADMIN")) {
				log.debug("---> Role is set by admin as" + user.getRole());
			} else {
				log.debug("---> Default role is set to user");
				user.setRole("ROLE_USER");
			}*/

			log.debug("---> If Logged in then user role : " + loggedInUserRole);
			if (loggedInUserRole != null && loggedInUserRole.equals("ROLE_ADMIN")) {
				log.debug("---> Admin created account and hence Accepted by default");
				user.setStatus("A");
			} else {
				log.debug("---> Default status New");
				user.setStatus("N");
			}

			if (userDAO.save(user)) {
				log.debug("---> Saving new user details");
				user.setErrorCode("200");
				user.setErrorMessage("Registration Successfull!");
			} else {
				log.debug("---> failed to save user");
				user.setErrorCode("404");
				user.setErrorMessage("Failed to Register! Please try again.");
			}
			log.debug("---> Ending of register method");
		}
		else{
			user.setErrorCode("404");
			user.setErrorMessage("User id already exists. Please choose an other userId");
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PutMapping("/updateUser")
	public ResponseEntity<User> updateUser(@RequestBody User user){
		log.debug("Starting of updateUser method");
		if(userDAO.getUserById(user.getUserId()) != null){
			if(userDAO.update(user)){
				user.setErrorCode("200");
				user.setErrorMessage("User update successfull");
			} else {
				user.setErrorCode("404");
				user.setErrorMessage("User update failed! Please try again!");
			}
		} else {
			user.setErrorCode("404");
			user.setErrorMessage("The user id :"+user.getUserId()+" is not present in database!");
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping("/signOut")
	public ResponseEntity<User> signOut() {

		log.debug("---> Started signing out");
		user = (User) session.getAttribute("User");

		if (user != null) {
			// userDAO.setOffline(user.getUserId());
			user.setIsOnline("N");
			friendDAO.setOffline(user.getUserId());
			userDAO.update(user);
			session.invalidate();
			user.setErrorCode("200");
			user.setErrorMessage("Sign Out successfull");
			log.debug("---> Signing out successfull!");
		} else {
			log.debug("Exception arised while signing out!");
			user = new User();
			session.invalidate();
			user.setErrorCode("404");
			user.setErrorMessage("You are not signed in!");
		}

		return new ResponseEntity<User>(user, HttpStatus.OK);

	}

	@PutMapping("/approveUser/{userId}")
	public ResponseEntity<User> approveUser(@PathVariable("userId") String userId) {
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		if (loggedInUserRole!=null && loggedInUserRole.equals("ROLE_ADMIN")) {
			user = userDAO.getUserById(userId);
			user.setRemarks("You are approved by : "+loggedInUserId);
			if (changeStatus(user, "A")) {
				user.setErrorCode("200");
				user.setErrorMessage("User approved successfull");
			} else {
				user.setErrorCode("404");
				user.setErrorMessage("Failed to approve user! Please try again.");
			}
		} else {
			user = new User();
			user.setErrorCode("404");
			user.setErrorMessage("You are not admin to approve this user!");
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PutMapping("/changeUserRole/{userId}")
	public ResponseEntity<User> changeUserRole(@RequestBody User user, @PathVariable("userId") String userId){
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		User actualUser;
		if(loggedInUserRole!=null && loggedInUserRole.equals("ROLE_ADMIN")){
			actualUser = userDAO.getUserById(userId);
			actualUser.setRole(user.getRole());
			if(actualUser != null){
				if(userDAO.update(actualUser)){
					actualUser.setErrorCode("200");
					actualUser.setErrorMessage("User Role updated successfully to : "+actualUser.getRole());
				} else{
					actualUser.setErrorCode("404");
					actualUser.setErrorMessage("User Role failed to update");
				}
			} else{
				actualUser.setErrorCode("404");
				actualUser.setErrorMessage("User with userId: "+userId+" not found!");
			}
		} else {
			actualUser = new User();
			actualUser.setErrorCode("404");
			actualUser.setErrorMessage("You are not logged in as Admin!");
		}
		return new ResponseEntity<User>(actualUser, HttpStatus.OK);
	}

	@PutMapping("/rejectUser/{userId}/{remarks}")
	public ResponseEntity<User> rejectUser(@PathVariable("userId") String userId, @PathVariable("remarks") String remarks) {
		String loggedInUserRole = (String) session.getAttribute("loggedInUserRole");
		if (loggedInUserRole!=null && loggedInUserRole.equals("ROLE_ADMIN")) {
			user = userDAO.getUserById(userId);
			user.setRemarks(remarks);
			if (changeStatus(user, "R")) {
				user.setErrorCode("200");
				user.setErrorMessage("User rejected successfully");
			} else {
				user.setErrorCode("404");
				user.setErrorMessage("Failed to reject user! Please try again.");
			}
		} else {
			user = new User();
			user.setErrorCode("404");
			user.setErrorMessage("You are not admin to reject this user!");
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@GetMapping("/myProfile")
	public ResponseEntity<User> myProfile() {
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		log.debug("retreving data of : " + loggedInUserId);
		user = userDAO.getUserById(loggedInUserId);
		if (user == null) {
			user = new User();
			user.setErrorCode("404");
			user.setErrorMessage("User not loggedin");
			log.debug("User didnot login or no data in loggedInUserId");
		} else {
			user.setErrorCode("200");
			user.setErrorMessage(
					"Profile retrived successfully Mr. " + user.getFirstname() + " " + user.getLastname() + "!");

		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@GetMapping("/getNotMyFriendsList")
	public ResponseEntity<List<User>> getNotMyFriendsList(){
		List<User> users = new LinkedList<User>();
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		users = userDAO.notMyFriendList1(loggedInUserId);
		users.remove(userDAO.getUserById(loggedInUserId));
		if(users.isEmpty()){
			user = new User();
			user.setErrorCode("404");
			user.setErrorMessage("All users are in your friends list");
			users.add(user);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	private boolean changeStatus(User user, String status) {
		user.setStatus(status);
		return userDAO.update(user);
	}
	/*private boolean changeStatus(User user, String status, String remarks) {
		user.setStatus(status);
		user.setRemarks(remarks);
		return userDAO.update(user);
	}*/
}
