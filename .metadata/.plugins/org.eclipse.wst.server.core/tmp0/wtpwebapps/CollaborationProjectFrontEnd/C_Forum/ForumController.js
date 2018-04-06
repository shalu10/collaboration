myApp.controller("ForumController", function($scope, $http, ForumService, $rootScope, $cookieStore, $location, $route){
console.log("Starting of ForumController");
	
	this.forum = {
			errorMessage: '',
	        errorCode: '',
	        forumId: '',
	        likes: '',
	        userId: '',
	        forumName: '',
	        forumContent: '',
	        remarks: '',
	        status: '',
	        createDate: ''
	}
	
	this.forumComment = {
			errorMessage: '',
	        errorCode: '',
	        forumCommentId: '',
	        forumId: '',
	        userId: '',
	        username: '',
	        forumComment: '',
	        commentDate: ''
	}
	
	this.forumComments = [];
	
	this.forumStatus = "";
	
	$scope.selectedSort = "forumId";
	
	this.forums=[];
	
	//getAllForums
	this.getAllForums = function(){
		console.log("Starting of method getAllForums");
		
		ForumService.getAllForums()
		.then(
				function(dataFromService){
					this.forums = dataFromService;
					$rootScope.forums = dataFromService;
					localStorage.setItem('forums', JSON.stringify(this.forums));
					
					
				},
				function(errResponse){
					console.error("Error while fetching forums!");
				}
		);
	};
	
	this.getAllForums();
	
	this.getAllForumComments = function(){
		console.log("Starting of getAllForumComments() in ForumController");
		
		ForumService.getAllForumComments()
		.then(
				function(response){
					this.forumComments = response;
					$rootScope.forumComments = response;
					localStorage.setItem('forumComments', JSON.stringify(this.forumComments));
					console.log(this.forumComments);
				}
		);
	};
	
	this.getAllForumComments();
	
	
	//insertForum
	this.insertForum = function(forum, myForm){
		console.log("Starting of insertForum() in ForumController");
		ForumService.insertForum(forum)
		.then(
				function(response) {
					this.forum = response;
					$rootScope.forums = response;
					if(this.forum.errorCode == "404"){
						console.log(this.forum.errorMessage);
						$rootScope.errorMessage = this.forum.errorMessage;
					} else {
						$rootScope.successMessage = this.forum.errorMessage;
						console.log(this.forum.errorMessage);
						$scope.resetForm(myForm);
						$route.reload();
					}
				}, 
				null
		);
	};
	
	this.editForum = function(forum){
		if($rootScope.forum == forum){
			$rootScope.forum = {};
		}
		else {
			$rootScope.forum = forum;
			console.log($rootScope.forum);
		}
	}
	
	//updateForum
	$rootScope.updateForum = function(forum){
		console.log("Starting of updateForum() in ForumController");
		ForumService.updateForum(forum)
		.then(
				function(response) {
					this.forum = response;
					$rootScope.forum = response;
					if(this.forum.errorCode == "404"){
						console.log(this.forum.errorMessage);
						$rootScope.errorMessage = this.forum.errorMessage;
					} else {
						$rootScope.successMessage = this.forum.errorMessage;
						console.log(this.forum.errorMessage);
						$rootScope.forum = {};
						$route.reload();
					}
				}, 
				null
		);
	};
	
	//approveForum
	this.approveForum = function(forumId){
		console.log("Starting of method approveForum in ForumController");
		ForumService.approveForum(forumId)
		.then(
				function(dataFromService){
					this.forum = dataFromService;
					this.getAllForums;
					console.log(this.forum.errorMessage);
					if(this.forum.errorCode == "404"){
						
						$rootScope.errorMessage = this.forum.errorMessage;
					} else {
						$rootScope.successMessage = this.forum.errorMessage;
						$route.reload();
						//$location.path("/ManageNewForums");
					}
				},
				null
		);
	};
	
	//rejectForum
	this.rejectForum = function(forumId){
		console.log("Starting of method rejectForum in ForumController");
		ForumService.rejectForum(forumId)
		.then(
				function(dataFromService){
					this.forum = dataFromService;
					this.getAllForums;
					console.log(this.forum.errorMessage);
					if(this.forum.errorCode == "404"){
						
						$rootScope.errorMessage = this.forum.errorMessage;
					} else {
						$rootScope.successMessage = this.forum.errorMessage;
						$route.reload();
						//$location.path("/ManageNewForums");
					}
				},
				null
		);
	};
	
	//deleteForum
	this.deleteForum = function(forumId){
		console.log("Starting of method deleteForum in ForumController");
		ForumService.deleteForum(forumId)
		.then(
				function(dataFromService){
					this.forum = dataFromService;
					this.getAllForums;
					console.log(this.forum.errorMessage);
					if(this.forum.errorCode == "404"){
						
						$rootScope.errorMessage = this.forum.errorMessage;
					} else {
						$rootScope.successMessage = this.forum.errorMessage;
						$route.reload();
						//$location.path("/ManageNewForums");
					}
				},
				null
		);
	};
	
	
	this.getManageAdminForumStatus = function(){
		console.log("Starting of manageAdminPage method");
		if($location.path() == "/ManageNewForums"){
			this.forumStatus = 'N';
			return 'N';
		}
		else if($location.path() == "/ManageUpdatedForums"){
			this.forumStatus = 'U';
			return 'U';
		}
		else if($location.path() == "/ManageRejectedForums"){
			this.forumStatus = 'R';
			return 'R';
		} else {
			this.forumStatus = "";
			//console.log("Invalid usage of method getManageAdminForumStatus");
		}
	};
	
	this.viewForum = function(forum){
		$rootScope.forum1 = forum;
		localStorage.setItem('forum1', JSON.stringify(forum));
		$location.path("/viewForum");
	}
	
	this.saveForumComment = function(forumComment, forumId){
		console.log("Starting of method saveForumComment in ForumController");
		
		forumComment.forumId = forumId;
		
		ForumService.saveForumComment(forumComment)
		.then(
				function(response) {
					this.forumComment = response;
					$rootScope.forumComment = response;
					if(this.forumComment.errorCode=="404"){
						$rootScope.errorMessage = this.forumComment.errorMessage;
						console.error(this.forumComment.errorMessage);
					} else {
						$rootScope.successMessage = this.forumComment.errorMessage;
						console.log(this.forumComment.errorMessage);
						$rootScope.forumComment = {};
						$route.reload();
					}
				}
		)
	}
	
	
	$scope.resetForm = function(myForm){
		console.log("Starting reserForm method");
		this.forum = {
				errorMessage: '',
		        errorCode: '',
		        forumId: '',
		        likes: '',
		        userId: '',
		        forumName: '',
		        forumContent: '',
		        remarks: '',
		        status: '',
		        createDate: ''
		};
		myForm.$setPristine();
		myForm.$setUntouched();
	}

});