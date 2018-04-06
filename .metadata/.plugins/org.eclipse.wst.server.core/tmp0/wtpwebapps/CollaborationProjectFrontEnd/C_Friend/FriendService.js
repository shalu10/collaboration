myApp.service("FriendService", function($http, $q) {
	console.log("Starting friend service");
	var BackendUrl = 'http://localhost:8080/CollaborationProjectBackEnd';
	
	return {
		getNotMyFriendsList : function(){
			console.log("Starting of getNotMyFriendsList() in FriendService");
			
			return $http.get(BackendUrl+"/getNotMyFriendsList")
			.then(
					function(response){
						return response.data;
					}, function(errResponse){
						console.log("Error while retreving NotMyFriendList");
					}
			);
		},
		
		addFriend : function(friendId){
			console.log("Starting of add friend method in FriendService")
			
			return $http.post(BackendUrl+"/addFriend/"+friendId)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse){
						console.log("Error while sending friend request");
					}
			);
		},
		getMyFriendRequests : function(){
			console.log("Starting of getMyFriendRequests() in FriendService");
			
			return $http.get(BackendUrl+"/getMyFriendRequests")
			.then(
					function(response){
						return response.data;
					}, function(errResponse){
						console.log("Error while retreving getMyFriendRequests");
					}
			);
		},
		getFriendRequestsSentByMe : function(){
			console.log("Starting of getFriendRequestsSentByMe() in FriendService");
			
			return $http.get(BackendUrl+"/getFriendRequestsSentByMe")
			.then(
					function(response){
						return response.data;
					}, function(errResponse){
						console.log("Error while retreving getFriendRequestsSentByMe");
					}
			);
		},
		getMyFriends : function(){
			console.log("Starting of getMyFriends() in FriendService");
			
			return $http.get(BackendUrl+"/getMyFriends")
			.then(
					function(response){
						return response.data;
					}, function(errResponse){
						console.log("Error while retreving myFriends");
					}
			);
		},
		acceptFriend : function(friendId){
			console.log("Starting of accept friend method in FriendService")
			
			return $http.put(BackendUrl+"/acceptFriend/"+friendId)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse){
						console.log("Error while accepting friend request");
					}
			);
		},
		rejectFriend : function(friendId){
			console.log("Starting of reject friend method in FriendService")
			
			return $http.put(BackendUrl+"/rejectFriend/"+friendId)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse){
						console.log("Error while rejecting friend request");
					}
			);
		},
		unfriend : function(friendId){
			console.log("Starting of unfriend method in FriendService")
			
			return $http.put(BackendUrl+"/unFriend/"+friendId)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse){
						console.log("Error while unfriending request");
					}
			);
		},
		deleteFriendRequest : function(friendId){
			console.log("Starting of delete friend request in FriendService");
			
			return $http.delete(BackendUrl+"/deleteFriendRequest/"+friendId)
			.then(
					function(response) {
						return response.data;
					}, function(errResponse){
						console.log("Error while deleting friend request");
					}
			);
		}
	}
	
	console.log("Ending friend service");
});