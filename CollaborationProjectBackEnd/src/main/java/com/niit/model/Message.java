package com.niit.model;


public class Message {



	private String message;

	private long id;
	
	public String userID;
	
	
	public String getUserID() {
		return userID;
	}

	public Message(String message, long id, String userID) {
		this.message = message;
		this.id = id;
		this.userID = userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	

	

	public Message()
	{
		
	}
	
	
	

}
