package com.niit.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name="CFriend")
@Component
public class Friend extends BaseDomain{
	
	@Id
	private int tableId;
	private String userId, friendId, status, isOnline;
	//Status : N-New, A-Approved, R-Rejected, U-Unfriend
	//isOnline : Y-Yes, N-No
	
	public int getTableId() {
		return tableId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}
	public void setTableId(int tableId) {
		this.tableId = tableId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	
	
	
	

}
