package com.niit.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "CJobApplied")
@Component
public class JobApplied extends BaseDomain {

	@Id
	private int jobAppliedId;
	private String userId, jobId, remarks, status; //N - New, A - Accept, R - Rejected, I - call for Interview
	private Date dateApplied;

	public int getJobAppliedId() {
		return jobAppliedId;
	}

	public void setJobAppliedId(int jobAppliedId) {
		this.jobAppliedId = jobAppliedId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateApplied() {
		return dateApplied;
	}

	public void setDateApplied(Date dateApplied) {
		if (dateApplied == null) {
			this.dateApplied = new Date(System.currentTimeMillis());
		} else {
			this.dateApplied = dateApplied;
		}
	}

}
