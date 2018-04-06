myApp.controller("FriendController", function($scope, $http, FriendService, $rootScope, $cookieStore, $location, $route){
	console.log("Starting of Friend Controller");
	
	this.user = {	
			userId 		: '',
			firstname 	: '',
			lastname 	: '',
			password 	: '',
			email 		: '',
			role 		: '',
			status 		: '',
			
			isOnline 	: '',
			remarks 	: '',
			errorMessage: '',
			errorCode	: ''		
		};
	
	this.friend = {
	        errorMessage: '',
	        errorCode: '',
	        tableId: '',
	        userId: '',
	        friendId: '',
	        status: '',
	        isOnline: ''
	}
	
	this.usersNotMyFriendsList=[];
	this.getMyFriendRequests=[];
	this.getMyFriends=[];
	this.friends = [];
	
	//Get All Users
	this.getNotMyFriendsList = function(){
		console.log("Starting of getNotMyFriendsList() in FriendController");
		
		FriendService.getNotMyFriendsList()
		.then(
				function(response){
					this.usersNotMyFriendsList = response;
					$rootScope.usersNotMyFriendsList = response;
					localStorage.setItem('usersNotMyFriendsList', JSON.stringify(this.usersNotMyFriendsList));
				}
		);
	};
	
	this.getNotMyFriendsList();
	
	this.getMyFriendRequests = function(){
		console.log("Starting of getMyFriendRequests() in FriendController");
		
		FriendService.getMyFriendRequests()
		.then(
				function(response){
					if(response.errorCode!='404'){
						this.getMyFriendRequests = response;
						$rootScope.getMyFriendRequests = response;
						localStorage.setItem('getMyFriendRequests', JSON.stringify(this.getMyFriendRequests));
					} else {
						$rootScope.errorMessage = response.errorMessage;
						console.error(response.errorMessage);
						localStorage.removeItem('getMyFriendRequests');
					}
					console.log(response);
				}
		);
	};
	
	this.getMyFriendRequests();
	
	this.getFriendRequestsSentByMe = function(){
		console.log("Starting of getFriendRequestsSentByMe() in FriendController");
		
		FriendService.getFriendRequestsSentByMe()
		.then(
				function(response){
					if(response.errorCode!='404'){
						this.getFriendRequestsSentByMe = response;
						$rootScope.getFriendRequestsSentByMe = response;
						localStorage.setItem('getFriendRequestsSentByMe', JSON.stringify(this.getFriendRequestsSentByMe));
					} else {
						$rootScope.errorMessage = response.errorMessage;
						console.error(response.errorMessage);
						localStorage.removeItem('getFriendRequestsSentByMe');
					}
					console.log(response);
				}
		);
	};
	
	this.getFriendRequestsSentByMe();
	
	this.getMyFriends = function(){
		console.log("Starting of getMyFriends() in FriendController");
		
		FriendService.getMyFriends()
		.then(
				function(response){
					console.log(response);
					if(response.errorCode!='404'){
						this.getMyFriends = response;
						$rootScope.getMyFriends = response;
						localStorage.setItem('getMyFriends', JSON.stringify(this.getMyFriends));
					} else {
						$rootScope.errorMessage = response.errorMessage;
						console.error(response.errorMessage);
						localStorage.removeItem('getMyFriends');
					}
					console.log(response);
				}
		);
	};
	
	this.getMyFriends();
	
	
	
	this.viewProfile = function(user){
		console.log('Starting of viewProfile');
		$rootScope.user = user;
		$location.path("/ViewProfile")
	};
	
	this.addFriend = function(friendId){
		console.log("Starting of addFriend() in FriendController");
		
		FriendService.addFriend(friendId)
		.then(
				function(response){
					this.friend = response;
					$rootScope.friend = response;
					if(this.friend.errorCode == '404'){
						console.error(this.friend.errorMessage);
						$rootScope.errorMessage = this.friend.errorMessage;
					} else {
						console.log(this.friend.errorMessage);
						$rootScope.successMessage = this.friend.errorMessage;
						
					}
				}
		)
	};
	
	this.acceptFriend = function(friendId){
		console.log("Starting of acceptFriend() in FriendController");
		
		FriendService.acceptFriend(friendId)
		.then(
				function(response){
					this.friend = response;
					$rootScope.friend = response;
					if(this.friend.errorCode == '404'){
						console.error(this.friend.errorMessage);
						$rootScope.errorMessage = this.friend.errorMessage;
					} else {
						console.log(this.friend.errorMessage);
						$rootScope.successMessage = this.friend.errorMessage;
						$route.reload();
					}
				}
		)
	};
	this.rejectFriend = function(friendId){
		console.log("Starting of rejectFriend() in FriendController");
		
		FriendService.rejectFriend(friendId)
		.then(
				function(response){
					this.friend = response;
					$rootScope.friend = response;
					if(this.friend.errorCode == '404'){
						console.error(this.friend.errorMessage);
						$rootScope.errorMessage = this.friend.errorMessage;
					} else {
						console.log(this.friend.errorMessage);
						$rootScope.successMessage = this.friend.errorMessage;
						$route.reload();
					}
				}
		)
	};
	this.unfriend = function(friendId){
		console.log("Starting of unfriend() in FriendController");
		
		FriendService.unfriend(friendId)
		.then(
				function(response){
					this.friend = response;
					$rootScope.friend = response;
					console.log(this.friend.errorMessage);
					$rootScope.successMessage = this.friend.errorMessage;
					if($route.reload())
						console.log("page refresh");
				}
		)
	};
	
	this.deleteFriendRequest = function(friendId){
		console.log("starting of deleteFriendRequest in FriendController");
		
		FriendService.deleteFriendRequest(friendId)
		.then(
				function(response){
					this.friend = response;
					$rootScope.friend = response;
					console.log(this.friend.errorMessage);
					$rootScope.successMessage = this.friend.errorMessage;
					if($route.reload())
						console.log("page refresh");
				}
		)
	}
	
	console.log("Ending of Friend Controller");
});