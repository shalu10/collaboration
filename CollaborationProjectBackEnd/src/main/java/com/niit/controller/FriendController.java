package com.niit.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.FriendDAO;
import com.niit.dao.UserDAO;
import com.niit.model.Friend;
import com.niit.model.User;

@RestController
public class FriendController {
	
	/*
	 * /getMyFriends				- Get
	 * /addFriend/{friendId}		- Post
	 * /unFriend/{friendId}			- Put
	 * /acceptFriend/{friendId}		- Put
	 * /rejectFriend/{friendId}		- Put
	 * /getMyFriendRequests			- Get
	 * /getFriendRequestsSentByMe	- Get
	 */
	
	private static Logger log = LoggerFactory.getLogger(FriendController.class);
	
	@Autowired Friend friend;
	@Autowired FriendDAO friendDAO;
	@Autowired UserDAO userDAO;
	@Autowired User user;
	@Autowired HttpSession session;
	
	@GetMapping("/getMyFriends")
	public ResponseEntity<List<Friend>> getMyFriends(){
		log.debug("---> ");
		log.debug("---> Starting of method getMyFriends");
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		List<Friend> friends = new LinkedList<Friend>();
		if(loggedInUserId == null){
			log.debug("---> User not loggedIn");
			friend.setErrorCode("404");
			friend.setErrorMessage("Please login to continue");
			friends.add(friend);
		} else {
			log.debug("---> User logged in, Retriving friends list of "+loggedInUserId);
			friends = friendDAO.getMyFriends(loggedInUserId);
			if(friends.isEmpty()){
				log.debug("---> No friends exist.");
				friend.setErrorCode("404");
				friend.setErrorMessage("No friends exist!");
				friends.add(friend);
			}
		}
		log.debug("---> Ending of method getMyFriends");
		return new ResponseEntity<List<Friend>>(friends, HttpStatus.OK);
	}
	
	@PostMapping("/addFriend/{friendId}")
	public ResponseEntity<Friend> addFriend(@PathVariable("friendId") String friendId){
		log.debug("---> ");
		log.debug("---> Starting of method addFriend");
		
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		friend.setFriendId(friendId);
		friend.setIsOnline("N");
		friend.setStatus("N");
		friend.setUserId(loggedInUserId);
		friend.setTableId(friendDAO.getMaxFriendId()+1);
		if(loggedInUserId != null){
			if(isUserExist(friendId)){
				if(!isAlreadyFriend(friendId, loggedInUserId) && !isAlreadyFriend(loggedInUserId, friendId)){
					if(!isFriendRequestAlreadySent(loggedInUserId, friendId)) {
						if(friendDAO.save(friend)){
							log.debug("---> Friend request sent successfully");
							friend.setErrorCode("200");
							friend.setErrorMessage("Friend request sent successfully");
						} else {
							log.debug("---> Friend request failed! Please try again!");
							friend.setErrorCode("404");
							friend.setErrorMessage("Friend request failed! Please try again!");
						}
					} else {
						log.debug("---> You already sent friend request to "+friendId);
						friend.setErrorCode("404");
						friend.setErrorMessage(friendId + " is already in friends list!");
					}
				} else {
					log.debug("---> "+friendId + " is already in friends list!");
					friend.setErrorCode("404");
					friend.setErrorMessage(" You already sent friend request to "+friendId);
				}
			} else {
				log.debug("---> "+friendId+" does not exist");
				friend.setErrorCode("404");
				friend.setErrorMessage(friendId+" does not exist");
			}
		} else {
			log.debug("---> Please login to send this friend request!");
			friend.setErrorCode("404");
			friend.setErrorMessage("Please login to send this friend request!");
		}
		log.debug("---> Ending of method addFriend");
		return new ResponseEntity<Friend>(friend, HttpStatus.OK);
	}
	
	@PutMapping("/unFriend/{friendId}")
	public ResponseEntity<Friend> unFriend(@PathVariable("friendId") String friendId){
		log.debug("---> ");
		log.debug("---> Starting of method unFriend");
		
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		
		if (loggedInUserId != null) {
			if (isAlreadyFriend(friendId, loggedInUserId) || isAlreadyFriend(loggedInUserId, friendId)) {
				friend = friendDAO.get(friendId, loggedInUserId);
				if (friend == null) {
					friend = friendDAO.get(loggedInUserId, friendId);
				}
				friend.setStatus("U");
				log.debug("----------------------> Friend id : " + friendId);
				log.debug("----------------------> User id : " + loggedInUserId);
				if (friendDAO.delete(friendId, loggedInUserId)) {
					log.debug("---> Unfriend successfull");
					friend.setErrorCode("200");
					friend.setErrorMessage("Unfriend successfull");
				} else if (friendDAO.delete(loggedInUserId, friendId)) {
					log.debug("---> Unfriend successfull");
					friend.setErrorCode("200");
					friend.setErrorMessage("Unfriend successfull");
				} else {
					log.debug("---> Unfriend Failed!");
					friend.setErrorCode("404");
					friend.setErrorMessage("Unfriend Failed!");
				}
			} else {
				log.debug("---> You are not friend with " + friendId);
				friend.setErrorCode("404");
				friend.setErrorMessage("You are not friend with " + friendId);
			}
		} else {
			log.debug("---> Please login to perform this operation!");
			friend.setErrorCode("404");
			friend.setErrorMessage("Please login to perform this operation!");
		}
		log.debug("---> Ending of method unFriend");
		return new ResponseEntity<Friend>(friend, HttpStatus.OK);
	}
	
	@PutMapping("/acceptFriend/{friendId}")
	public ResponseEntity<Friend> acceptFriend(@PathVariable("friendId") String friendId){
		log.debug("---> ");
		log.debug("---> Starting of method acceptFriend");
		
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		
		if(loggedInUserId != null){
			if(!isAlreadyFriend(loggedInUserId, friendId) && !isAlreadyFriend(friendId, loggedInUserId)){
				
				friend = friendDAO.get(friendId, loggedInUserId);
				if(friend != null){
					friend.setStatus("A");
					if(friendDAO.update(friend)){
						log.debug("---> Accept friend successfull");
						friend.setErrorCode("200");
						friend.setErrorMessage("Accept friend successfull");
					} else {
						log.debug("---> Accept friend Failed!");
						friend.setErrorCode("404");
						friend.setErrorMessage("Accept friend Failed!");
					}
				} else {
					friend = new Friend();
					friend.setErrorCode("404");
					friend.setErrorMessage(friendId+" did not sent friend request to "+loggedInUserId);
				}
			} else {
				log.debug("---> You are already friend with "+friendId);
				friend.setErrorCode("404");
				friend.setErrorMessage("You are already friend with "+friendId);
			}
		} else {
			log.debug("---> Please login to perform this operation!");
			friend.setErrorCode("404");
			friend.setErrorMessage("Please login to perform this operation!");
		}
		log.debug("---> Ending of method acceptFriend");
		return new ResponseEntity<Friend>(friend, HttpStatus.OK);
	}
	@PutMapping("/rejectFriend/{friendId}")
	public ResponseEntity<Friend> rejectFriend(@PathVariable("friendId") String friendId){
		log.debug("---> ");
		log.debug("---> Starting of method rejectFriend");
		
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		
		if(loggedInUserId != null){
			if(!isAlreadyFriend(friendId, loggedInUserId) && !isAlreadyFriend(loggedInUserId, friendId)){
				friend = friendDAO.get(friendId, loggedInUserId);
				friend.setStatus("R");
				if(friendDAO.update(friend)){
					log.debug("---> Reject friend successfull");
					friend.setErrorCode("200");
					friend.setErrorMessage("Reject friend successfull");
				} else {
					log.debug("---> Reject friend Failed!");
					friend.setErrorCode("404");
					friend.setErrorMessage("Reject friend Failed!");
				}
			} else {
				log.debug("---> You are already friend with "+friendId);
				friend.setErrorCode("404");
				friend.setErrorMessage("You are already friend with "+friendId);
			}
		} else {
			log.debug("---> Please login to perform this operation!");
			friend.setErrorCode("404");
			friend.setErrorMessage("Please login to perform this operation!");
		}
		log.debug("---> Ending of method rejectFriend");
		return new ResponseEntity<Friend>(friend, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteFriendRequest/{friendId}")
	public ResponseEntity<Friend> deleteFriendRequest(@PathVariable("friendId") String friendId){
		log.debug("---> ");
		log.debug("---> Starting of method deleteFriendRequest");
		
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		friend = new Friend();
		if(loggedInUserId != null){
			if(friendDAO.delete(loggedInUserId, friendId)){
				friend.setErrorCode("200");
				friend.setErrorMessage("Delete friend request successfull");
			} else {
				friend.setErrorCode("404");
				friend.setErrorMessage("Delete friend request failed");
			}
		} else {
			log.debug("---> Please login to perform this operation!");
			friend.setErrorCode("404");
			friend.setErrorMessage("Please login to perform this operation!");
		}
		log.debug("---> Ending of method deleteFriendRequest");
		return new ResponseEntity<Friend>(friend, HttpStatus.OK);
	}
	
	@GetMapping("/getMyFriendRequests")
	public ResponseEntity<List<Friend>> getMyFriendRequests(){
		
		log.debug("---> ");
		log.debug("---> Starting of method getMyFriendRequests");
		
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		List<Friend> friends = new LinkedList<Friend>();
		if(loggedInUserId != null){
			log.debug("---> getting friend requests");
			friends = friendDAO.getNewFriendRequests(loggedInUserId);
			if(friends.isEmpty()){
				friend =new Friend();
				log.debug("---> No friend requests");
				friend.setErrorCode("404");
				friend.setErrorMessage("You have no friend requests");
				friends.add(friend);
			}
		} else {
			log.debug("---> Please login to perform this operation!");
			friend.setErrorCode("404");
			friend.setErrorMessage("Please login to perform this operation!");
			friends.add(friend);
		}
		
		log.debug("---> Ending of method getMyFriendRequests");
		return new ResponseEntity<List<Friend>>(friends, HttpStatus.OK);
	}
	
	@GetMapping("/getFriendRequestsSentByMe")
	public ResponseEntity<List<Friend>> getFriendRequestsSentByMe(){
		log.debug("--->");
		log.debug("--->Starting of method getFriendRequestsSentByMe");
		
		String loggedInUserId = (String) session.getAttribute("loggedInUserId");
		List<Friend> friends = new LinkedList<Friend>();
		if(loggedInUserId != null){
			log.debug("---> Getting sent friend requests");
			friends = friendDAO.getFriendRequestsSentByMe(loggedInUserId);
			if(friends.isEmpty()){
				friend = new Friend();
				log.debug("---> You sent no friend requests");
				friend.setErrorCode("404");
				friend.setErrorMessage("You sent no friend requests");
				friends.add(friend);
			}
		} else{
			log.debug("---> Please login to perform this operation!");
			friend.setErrorCode("404");
			friend.setErrorMessage("Please login to perform this operation!");
			friends.add(friend);
		}
		
		log.debug("--->Ending of method getFriendRequestsSentByMe");
		return new ResponseEntity<List<Friend>>(friends,HttpStatus.OK);
	}
	
	
		
	private boolean isUserExist(String userId){
		if(userDAO.getUserById(userId) == null){
			return false;
		}
		return true;
	}
	
	private boolean isFriendRequestAlreadySent(String userId, String friendId){
		if(friendDAO.get(userId, friendId) == null){
			return false;
		}
		return true;
	}
	
	private boolean isAlreadyFriend(String userId, String friendId){
		if(friendDAO.get(userId, friendId, "A") == null){
			return false;
		}
		return true;
	}

}
