myApp.controller("JobController", function($scope, $http, JobService, $rootScope, $cookieStore, $location, $route) {

	console.log("Starting of JobController");
	
	this.job = {
			errorMessage: "",
	        errorCode: "",
	        jobId: "",
	        jobTitle: "",
	        jobQualification: "",
	        jobStatus: "",
	        jobDescription: "",
	        createDate: ""
	}
	
	this.jobApplication = {
			errorMessage: '',
	        errorCode: '',
	        jobAppliedId: '',
	        userId: '',
	        jobId: '',
	        remarks: '',
	        status: '',
	        dateApplied: ''
	}
	
	this.jobApplications = [];
	
	this.jobs = [];
	this.MyAppliedJobs = [];
	
	
	this.jobStatus = "";
	
	$scope.selectedSort = "jobId";
	$scope.selectedJobApplicationsSort = "jobId";
	
	//getAllJobs
	this.getAllJobs = function(){
		console.log("Starting of method getAllJobs");
		
		JobService.getAllJobs()
		.then(
				function(dataFromService) {
					this.jobs = dataFromService;
					$rootScope.jobs = dataFromService;
					localStorage.setItem('jobs', JSON.stringify(this.jobs));
					//$cookieStore.put("jobs", this.jobs);
					//$http.defaults.headers.common['Authorization'] = 'Basic ' +$rootScope.blogs;
				}, 
				function(errResponse) {
					console.error("Error wile fetching jobs");
				}
		);
	};
	
	this.getAllJobs();
	//getMyAppliedJobs
	this.getMyAppliedJobs = function(){
		console.log("Starting of method getMyAppliedJobs");
		
		JobService.getMyAppliedJobs()
		.then(
				function(dataFromService) {
					console.log(dataFromService);
					this.MyAppliedJobs = dataFromService;
					$rootScope.MyAppliedJobs = MyAppliedJobs;
					localStorage.setItem('MyAppliedJobs', JSON.stringify(this.MyAppliedJobs));
					//$cookieStore.put("jobs", this.jobs);
					//$http.defaults.headers.common['Authorization'] = 'Basic ' +$rootScope.blogs;
				}, 
				function(errResponse) {
					console.error("Error wile fetching jobs");
				}
		);
	};
	
	this.getMyAppliedJobs();
	
	this.getAllJobApplications = function(){
		console.log("Starting of getAllJobApplications in JobController");
		
		JobService.getAllJobApplications()
		.then(
				function(dataFromService) {
					this.jobApplications = dataFromService;
					$rootScope.jobApplications = dataFromService;
					localStorage.setItem('jobApplications', JSON.stringify(this.jobApplications));
					console.log(this.jobApplications);
				}
		);
	};
	this.getAllJobApplications();
	
	this.postNewJob = function(job, insertjob){
		console.log("Starting of postNewJob() in JobController");
		
		JobService.postNewJob(job)
		.then(
				function(response){
					if(response.errorCode=="404"){
						$rootScope.errorMessage = response.errorMessage;
						console.error(response.errorMessage)
					} else {
						this.getAllJobs;
						console.log(response.errorMessage);
						$rootScope.successMessage = response.errorMessage;
						$scope.resetForm(insertjob);
						$route.reload();
						
					}
				}
		)
		
	}
	

	
	this.closeJob = function(jobId){
		console.log("Starting of method closeJob() in JobController");
		
		JobService.closeJob(jobId)
		.then(
				function(dataFromService){
					this.job = dataFromService;
					this.getAllJobs;
					console.log(this.job.errorMessage);
					if(this.job.errorCode == "404"){
						$rootScope.errorMessage = this.job.errorMessage;
					} else {
						$rootScope.successMessage = this.job.errorMessage;
						$route.reload();
					}
				},
				null
		);
	};
	
	this.openJob = function(jobId){
		console.log("Starting of method openJob() in JobController");
		
		JobService.openJob(jobId)
		.then(
				function(dataFromService){
					this.job = dataFromService;
					this.getAllJobs;
					console.log(this.job.errorMessage);
					if(this.job.errorCode == "404"){
						$rootScope.errorMessage = this.job.errorMessage;
					} else {
						$rootScope.successMessage = this.job.errorMessage;
						$route.reload();
					}
				},
				null
		);
	};
	
	this.getManageAdminJobStatus = function(){
		console.log("Starting of getManageAdminJobStatus method");
		if($location.path() == "/ManageOpenJobs"){
			this.jobStatus = 'O';
			return 'O';
		}
		else if($location.path() == "/ManageClosedJobs"){
			this.jobStatus = 'C';
			return 'C';
		}
		else {
			this.jobStatus = "";
			//console.log("Invalid usage of method getManageAdminJobStatus");
		}
	};
	
	this.alertwin = function(msg){
		alert(msg);
	}
	
	this.editJob = function(jobId,jobTitle,jobQualification,jobDescription){
		console.log("Starting of editJob method");
		$rootScope.job={};
		$rootScope.job.jobId = jobId;
		$rootScope.job.jobDescription = jobDescription;
		$rootScope.job.jobQualification = jobQualification;
		$rootScope.job.jobTitle = jobTitle;
		console.log(this.job);
		console.log("Ending of editJob method");
	}
	
	this.approveJobApplication = function(userId, jobId){
		console.log("Starting of approveJobApplication in JobController")
		var remarks = prompt("Enter remarks","Approved by "+$rootScope.loggedInUser.userId+".");
		
		JobService.approveJobApplication(userId, jobId, remarks)
		.then(
				function(response){
					this.jobApplication = response;
					if(response.errorCode == "404"){
						$rootScope.errorMessage = response.errorMessage;
					} else {
						$rootScope.successMessage = response.errorMessage;
						$route.reload();
					}
				}
		)		
	}
	this.rejectJobApplication = function(userId, jobId){
		console.log("Starting of rejectJobApplication in JobController")
		var remarks = prompt("Enter remarks","Rejected by "+$rootScope.loggedInUser.userId+".");
		
		JobService.rejectJobApplication(userId, jobId, remarks)
		.then(
				function(response){
					this.jobApplication = response;
					if(response.errorCode == "404"){
						$rootScope.errorMessage = response.errorMessage;
					} else {
						$rootScope.successMessage = response.errorMessage;
						$route.reload();
					}
				}
		)		
	}
	this.callForInterviewJobApplication = function(userId, jobId){
		console.log("Starting of callForInterviewJobApplication in JobController")
		var remarks = prompt("Enter remarks","Accepted for interviewe by "+$rootScope.loggedInUser.userId+".");
		
		JobService.callForInterviewJobApplication(userId, jobId, remarks)
		.then(
				function(response){
					this.jobApplication = response;
					if(response.errorCode == "404"){
						$rootScope.errorMessage = response.errorMessage;
					} else {
						$rootScope.successMessage = response.errorMessage;
						$route.reload();
					}
				}
		)		
	}
	
	this.applyJob = function(jobId){
		console.log("Starting of applyJob() in JobController");
		
		JobService.applyJob(jobId)
		.then(
				function(response){
					this.jobApplication = response;
					$rootScope.jobApplications = response;
					if(this.jobApplication.errorCode == '404'){
						$rootScope.errorMessage = this.jobApplication.errorMessage;
						console.log(this.jobApplication.errorMessage);
					} else {
						$rootScope.successMessage = this.jobApplication.errorMessage;
						console.log(this.jobApplication.errorMessage);
						$route.reload();
					}
				}
		)
	}
	
	
	
	$rootScope.updateJob = function(job, insertjob){
		console.log("Starting of postNewJob() in JobController");
		
		JobService.updateJob(job)
		.then(
				function(response){
					if(response.errorCode=="404"){
						$rootScope.errorMessage = response.errorMessage;
						console.error(response.errorMessage)
					} else {
						$rootScope.job = {};
						this.getAllJobs;
						console.log(response.errorMessage);
						$rootScope.successMessage = response.errorMessage;
						$scope.resetForm(insertjob);
						$route.reload();
						
					}
				}
		)
		
	}
	
	
	$scope.resetForm = function(insertjob){
		console.log("Starting reserForm method");
		this.job = {
				errorMessage: '',
		        errorCode: '',
		        jobId: '',
		        jobTitle: '',
		        jobQualification: '',
		        jobStatus: '',
		        jobDescription: '',
		        createDate: ''
		};
		insertjob.$setPristine();
		insertjob.$setUntouched();
	}
	
	console.log("Ending of JobController");

});