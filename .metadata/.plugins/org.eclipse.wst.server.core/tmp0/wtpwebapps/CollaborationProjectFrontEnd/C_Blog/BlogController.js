myApp.controller("BlogController", function($scope, $http, BlogService, $rootScope, $cookieStore, $location, $route) {
	
	console.log("Starting of BlogController");
	
	this.blogStatus = "";
	
	this.blog = {
			errorMessage: '',
	        errorCode: '',
	        blogId: '',
	        likes: '',
	        userId: '',
	        blogName: '',
	        blogContent: '',
	        remarks: '',
	        status: '',
	        createDate: ''
	}
	
	$scope.selectedSort = "blogId";
	
	this.blogs=[];
	
	this.blogComment = {
			errorMessage: '',
	        errorCode: '',
	        blogCommentId: '',
	        blogId: '',
	        userId: '',
	        username: '',
	        blogComment: '',
	        commentDate: ''
		    
	};
	
	this.blogComments = [];
	
	$scope.blogId = "";
	$scope.blogId1 = "";
	
	$scope.buttonMessage = "Show Comments";
	$scope.addBlogComment = false;
	$scope.showBlogComments = false;
   
	
	//getAllBlogs
	this.getAllBlogs = function(){
		console.log("Starting of method getAllBlogs");
		
		BlogService.getAllBlogs()
		.then(
				function(dataFromService){
					this.blogs = dataFromService;
					$rootScope.blogs = dataFromService;					
					localStorage.setItem('blogs', JSON.stringify(this.blogs));
					
				},
				function(errResponse){
					console.error("Error while fetching blogs!");
				}
		);
	};
	console.log($rootScope.blogs);

	
	this.getAllBlogs();
	//getAllBlogComments
	this.getAllBlogComments = function(){
		console.log("Starting of method getAllBlogComments");
		
		BlogService.getAllBlogComments()
		.then(
				function(dataFromService){
					this.blogComments = dataFromService;
					$rootScope.blogComments = dataFromService;
					localStorage.setItem('blogComments', JSON.stringify(this.blogComments));
					
				},
				function(errResponse){
					console.error("Error while fetching blogs!");
				}
		);
	};
	
	this.getAllBlogComments();
	
	//approveBlog
	this.approveBlog = function(blogId){
		console.log("Starting of method approveBlog in BlogController");
		BlogService.approveBlog(blogId)
		.then(
				function(dataFromService){
					this.blog = dataFromService;
					this.getAllBlogs;
					console.log(this.blog.errorMessage);
					if(this.blog.errorCode == "404"){
						
						$rootScope.errorMessage = this.blog.errorMessage;
					} else {
						$rootScope.successMessage = this.blog.errorMessage;
						$route.reload();
						//$location.path("/ManageNewBlogs");
					}
				},
				null
		);
	};
	
	//rejectBlog
	this.rejectBlog = function(blogId){
		console.log("Starting of method rejectBlog in BlogController");
		BlogService.rejectBlog(blogId)
		.then(
				function(dataFromService){
					this.blog = dataFromService;
					this.getAllBlogs;
					console.log(this.blog.errorMessage);
					if(this.blog.errorCode == "404"){
						
						$rootScope.errorMessage = this.blog.errorMessage;
					} else {
						$rootScope.successMessage = this.blog.errorMessage;
						$route.reload();
						//$location.path("/ManageNewBlogs");
					}
				},
				null
		);
	};
	
	//deleteBlog
	this.deleteBlog = function(blogId){
		console.log("Starting of method deleteBlog in BlogController");
		BlogService.deleteBlog(blogId)
		.then(
				function(dataFromService){
					this.blog = dataFromService;
					this.getAllBlogs;
					console.log(this.blog.errorMessage);
					if(this.blog.errorCode == "404"){
						
						$rootScope.errorMessage = this.blog.errorMessage;
					} else {
						$rootScope.successMessage = this.blog.errorMessage;
						$route.reload();
						//$location.path("/ManageNewBlogs");
					}
				},
				null
		);
	};
	
	this.getManageAdminBlogStatus = function(){
		console.log("Starting of manageAdminPage method");
		if($location.path() == "/ManageNewBlogs"){
			this.blogStatus = 'N';
			return 'N';
		}
		else if($location.path() == "/ManageUpdatedBlogs"){
			this.blogStatus = 'U';
			return 'U';
		}
		else if($location.path() == "/ManageRejectedBlogs"){
			this.blogStatus = 'R';
			return 'R';
		} else {
			this.blogStatus = "";
			//console.log("Invalid usage of method getManageAdminBlogStatus");
		}
	};
	
	
	this.saveBlogComment = function(blogComment,blogId){
		console.log("Starting of saveBlogComment() in BlogController");
		blogComment.blogId = blogId;
		BlogService.saveBlogComment(blogComment)
		.then(
				function(response){
					this.blogComment = response;
					if(blogComment.errorCode == "404"){
						console.error(this.blogComment.errorMessage);
						$rootScope.errorMessage = this.blogComment.errorMessage;
					} else {
						console.log(this.blogComment.errorMessage);
						$rootScope.successMessage = this.blogComment.errorMessage;
						this.blogComment = {};
						$route.reload();
					}
				}
		)
		
	};
	
	this.addBlog = function(blog, myForm){
		console.log("Starting of addBlog() in BlogController");
		
		BlogService.addBlog(blog)
		.then(
				function(response) {
					this.blog = response;
					//$rootScope.blog = response;
					if(this.blog.errorCode == "404"){
						$rootScope.errorMessage = this.blog.errorMessage;
						console.error(this.blog.errorMessage);
					} else {
						$rootScope.successMessage = this.blog.errorMessage;
						console.log(this.blog.errorMessage);
						$scope.resetForm(myForm);
						$route.reload();
						
					}
				}
		)
	};
	
	this.editBlog = function(blog){
		if($rootScope.blog == blog){
			$rootScope.blog={};
		} else{
			$rootScope.blog = blog;
		}
		//$location.path("/EditBlog");
	};
	
	$rootScope.updateBlog = function(blog, myForm){
		console.log("Starting of updateBlog() in BlogController");
		
		BlogService.updateBlog(blog)
		.then(
				function(response) {
					//console.log(response);
					this.blog = response;
					$rootScope.blog = response;
					if(this.blog.errorCode == "404"){
						$rootScope.errorMessage = this.blog.errorMessage;
						console.error(this.blog.errorMessage);
					} else {
						$rootScope.successMessage = this.blog.errorMessage;
						console.log(this.blog.errorMessage);
						$scope.resetForm(myForm);
						$location.path("/Blog");
						$rootScope.blog={};
						$route.reload();
						
						
					}
				}
		)
	};
	
	

	this.userClickedAddBlogComment = function(blogId){
		if($scope.blogId1 == blogId){
			$scope.blogId1 = "";
			
		} else {
			$scope.blogId1 = blogId;
		}
		$scope.addBlogComment = !$scope.addBlogComment;
		
		
	}

	$scope.toggleFilter = function(blogId) {
		if($scope.blogId == blogId){
			$scope.blogId = "";
			$scope.buttonMessage = "Show Comments";
			
		} else {
			$scope.blogId = blogId;
			$scope.buttonMessage = "Hide Comments";
		}
		$scope.showBlogComments = !$scope.showBlogComments;
	}
	
	
	$scope.resetForm = function(myForm){
		console.log("Starting reserForm method");
		this.blog = {
				errorMessage: '',
		        errorCode: '',
		        blogId: '',
		        likes: '',
		        userId: '',
		        blogName: '',
		        blogContent: '',
		        remarks: '',
		        status: '',
		        createDate: ''
		}
		myForm.$setPristine();
		myForm.$setUntouched();
	}

});