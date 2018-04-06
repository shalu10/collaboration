myApp.service("ForumService", function($http, $q) {
	console.log("Starting forum service");
	
	var BackendUrl = 'http://localhost:8080/CollaborationProjectBackEnd';

	return {
		//getAllForums
		getAllForums : function(){
			console.log("Starting of getAllForums method in ForumService");
			
			return $http.get(BackendUrl+"/getAllForums")
			.then(
					function(response) {
						return response.data;
					}, 
					null
			);
		},
		
		getAllForumComments : function(){
			console.log("Starting of getAllForumComments in FOrumService");
			
			return $http.get(BackendUrl+"/getAllForumComments")
			.then(
					function(response) {
						return response.data;
					}, function(errResponse) {
						console.log("Error while getting forum comments");
					}
			)
		},
		
		//insertForum
		insertForum : function(forum){
			console.log("Starting of insertForum() in ForumService");
			
			return $http.post(BackendUrl+"/insertForum", forum)
			.then(
					function(response) {
						return response.data;
					},
					function(errResponse){
						console.error("Error while adding new forum!");
					}
			);
		},
		
		//updateForum
		updateForum : function(forum){
			console.log("Starting of updateForum() in ForumController");
			
			return $http.put(BackendUrl+"/updateForum/"+forum.forumId, forum)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse) {
						console.error("Error while updating forum");
					}
			)
		},
		
		//approveForum
		approveForum : function(forumId){
			console.log("Starting of method approveForum in ForumService");
			
			return $http.put(BackendUrl+"/approveForum/"+forumId)
			.then(function(response) {
				return response.data;
			}, function(errResponse) {
				console.error("Error while approving forum");
			}
			);
		},
		
		//rejectForum
		rejectForum : function(forumId){
			console.log("Starting of method rejectForum in ForumService");
			
			return $http.put(BackendUrl+"/rejectForum/"+forumId)
			.then(function(response) {
				return response.data;
			}, function(errResponse) {
				console.error("Error while rejecting forum");
			}
			);
		},
		
		//deleteForum
		deleteForum : function(forumId){
			console.log("Starting of method deleteForum in ForumService");
			
			return $http.delete(BackendUrl+"/deleteForum/"+forumId)
			.then(function(response) {
				return response.data;
			}, function(errResponse) {
				console.error("Error while deleting forum");
			}
			);
		},
		
		//saveForumComment
		saveForumComment : function(forumComment){
			console.log("Starting of method saveForumComment in ForumService");
			
			return $http.post(BackendUrl+"/saveForumComment",forumComment)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse) {
						console.log("Error while saving forum comment");
					}
			);
		}
	}
	
	
	console.log("Ending forum service");
	

});