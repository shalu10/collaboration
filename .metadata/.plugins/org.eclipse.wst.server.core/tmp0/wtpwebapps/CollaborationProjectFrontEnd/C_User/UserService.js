myApp.service('UserService', function($http, $q) {
	
	console.log('Starting of UserService');
	var BackendUrl = 'http://localhost:8080/CollaborationProjectBackEnd';
	
	return {
		
		// Get All Users
		getAllUsers : function(){
			console.log("Starting of getAllUsers method in UserService");
			
			return $http.get(BackendUrl+"/getAllUsers")
			.then(
					function(response) {
						return response.data;
					}, function(errResponse) {
						console.log("Error while fetching all users");
					}
			);
		},
		
		//Validate 
		validate : function(user){
			console.log("Starting of validate method in UserService");
			
			return $http.post(BackendUrl+'/validate', user)
			.then(
					function(response) {
						return response.data; //returning user json object
					},
					null
			);
		},
	
		//SignOut
		signOut : function(){
			console.log("Starting signout method in UserService");
			
			return $http.get(BackendUrl+"/signOut")
			.then(
					function(response) {
						return response.data;
					},
					null
			);
		},
		
		//Register
		register : function(user){
			console.log("Starting of register method in UserService");
			
			return $http.post(BackendUrl+"/register", user)
			.then(
					function(response) {
						return response.data;
					}, 
					function(errResponse) {
						console.log("Error while registering");
						return $q.reject(errResponse);
					});
		},
		
		//Update user
		updateUser : function(user){
			console.log("Starting of updateUser() in UserService");
			
			return $http.put(BackendUrl+"/updateUser", user)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse) {
						console.log("Failed to update user!")
					}
			)
		},
		
		//Approve user
		approveUser : function(userId) {
			console.log("Starting of approveUser() in UserService");
			
			return $http.put(BackendUrl+"/approveUser/"+userId)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse) {
						console.log("Error occured while accepting user!")
					}
			);
		},
		//Approve user
		rejectUser : function(userId, remarks) {
			console.log("Starting of rejectUser() in UserService");
			
			return $http.put(BackendUrl+"/rejectUser/"+userId+"/"+remarks)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse) {
						console.log("Error occured while accepting user!")
					}
			);
		}		
		
		
		
		
	};
	
});