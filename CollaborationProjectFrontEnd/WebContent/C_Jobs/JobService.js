myApp.service("JobService", function($http, $q) {
	console.log("Starting job service");
	
	var BackendUrl = 'http://localhost:8080/CollaborationProjectBackEnd';
	
	return{
		//getAllJobs
		getAllJobs : function(){
			console.log("Starting of getAllJobs method in JobService");
			
			return $http.get(BackendUrl+"/getAllJobs")
			.then(
					function(response){
						return response.data;
					},
					null
			);
		},
		
		closeJob : function(jobId){
			console.log("Starting of closeJob method in JobService");
			
			return $http.put(BackendUrl+"/closeJob/"+jobId)
			.then(
					function(response){
						return response.data;
					},
					function(errResponse){
						console.error("Error while closing job");
					}
			);
		},
		openJob : function(jobId){
			console.log("Starting of openJob method in JobService");
			
			return $http.put(BackendUrl+"/openJob/"+jobId)
			.then(
					function(response){
						return response.data;
					},
					function(errResponse){
						console.error("Error while closing job");
					}
			);
		},
		postNewJob : function(job) {
			console.log("Starting of postNewJob() in JobService");
			
			return $http.post(BackendUrl+"/postNewJob", job)
			.then(
					function(response){
						return response.data;
					},
					function(errResponse){
						console.error("Error while posting new job");
					}
			);
		},
		updateJob : function(job) {
			console.log("Starting of postNewJob() in JobService");
			
			return $http.put(BackendUrl+"/updateJob/"+job.jobId, job)
			.then(
					function(response){
						return response.data;
					},
					function(errResponse){
						console.error("Error while posting new job");
					}
			);
		},
		getAllJobApplications : function(){
			console.log("Starting of getAllJobApplications in JobService");
			
			return $http.get(BackendUrl+"/getAllJobApplications")
			.then(
					function(response) {
						return response.data;
					},
					function(errResponse){
						console.log("Error while getting all job applications");
					}
				);
		},
		approveJobApplication : function(userId,jobId,remarks){
			console.log("Starting of approveJobApplication() in JobService");
			
			return $http.put(BackendUrl+"/approveJobApplication/"+userId+"/"+jobId+"/"+remarks)
			.then(
					function(response) {
						return response.data;
					},
					function(errResponse) {
						console.log("Error while approving job application");
					}
			);
		},
		rejectJobApplication : function(userId,jobId,remarks){
			console.log("Starting of rejectJobApplication() in JobService");
			
			return $http.put(BackendUrl+"/rejectJobApplication/"+userId+"/"+jobId+"/"+remarks)
			.then(
					function(response) {
						return response.data;
					},
					function(errResponse) {
						console.log("Error while rejection job application");
					}
			);
		},
		callForInterviewJobApplication : function(userId,jobId,remarks){
			console.log("Starting of callForInterviewJobApplication() in JobService");
			
			return $http.put(BackendUrl+"/callForInterviewJobApplication/"+userId+"/"+jobId+"/"+remarks)
			.then(
					function(response) {
						return response.data;
					},
					function(errResponse) {
						console.log("Error while callForInterview job application");
					}
			);
		},
		
		applyJob : function(jobId){
			console.log("Starting of applyJob() in JobService");
			
			return $http.post(BackendUrl+"/applyJob/"+jobId)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse) {
						console.log("Error while applying for job");
					}
			)
		},
		
		getMyAppliedJobs : function(){
			console.log("Starting of getMyAppliedJobs() in JobService");
			
			return $http.get(BackendUrl+"/getMyAppliedJobs")
			.then(
					function(response) {
						return response.data;
					}, function(errResponse) {
						console.log("Error while getMyAppliedJobs");
					}
			)
		}
	
		
	}
	
	console.log("Ending job service");

});