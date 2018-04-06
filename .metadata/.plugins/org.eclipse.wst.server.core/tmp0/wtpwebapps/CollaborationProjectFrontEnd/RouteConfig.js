myApp.config(function($routeProvider) {

	$routeProvider
	.when("/", {
		templateUrl : "Template/Home.html"
	})
	.when("/Login", {
		templateUrl : "C_User/Login.html",
		controller : "UserController"
	})
	.when("/Register", {
		templateUrl : "C_User/Register.html",
		controller : "UserController"
	})
	.when("/Blog", {
		templateUrl : "C_Blog/Blog.html",
		controller : "BlogController"
	})
	.when("/AddBlog", {
		templateUrl : "C_Blog/AddBlog.html",
		controller : "BlogController"
	})
	.when("/EditBlog", {
		templateUrl : "C_Blog/EditBlog.html",
		controller : "BlogController"
	})
	.when("/Forum", {
		templateUrl : "C_Forum/Forum.html",
		controller : "ForumController"
	})
	.when("/viewForum", {
		templateUrl : "C_Forum/ViewForum.html",
		controller : "ForumController"
	})
	.when("/Jobs", {
		templateUrl : "C_Jobs/Jobs.html",
		controller : "JobController"
	})
	.when("/SearchUsers", {
		templateUrl : "C_Friend/AllFriends.html",
		controller : "FriendController"
	})
	.when("/MyProfile", {
		templateUrl : "C_User/MyProfile.html",
		controller : "UserController"
	})
	.when("/ViewProfile", {
		templateUrl : "C_Friend/ViewProfile.html",
		controller : "UserController"
	})
	.when("/InFriendRequests", {
		templateUrl : "C_Friend/InFriendRequests.html",
		controller : "FriendController"
	})
	.when("/getMyFriends", {
		templateUrl : "C_Friend/ShowMyFriends.html",
		controller : "FriendController"
	})
	.when("/SentRequests", {
		templateUrl : "C_Friend/SentRequests.html",
		controller : "FriendController"
	})
	.when("/Chat", {
		templateUrl : "C_Chat/Chat.html",
		controller : "ChatController"
	})
	.when("/MyJobApplications", {
		templateUrl : "C_Jobs/MyJobApplications.html",
		controller : "JobController"
	})
	
	//Admin
	.when("/ManageNewBlogs", {
		templateUrl : "C_Blog/Admin_ManageBlogs.html",
		controller : "BlogController"
	})
	.when("/ManageUpdatedBlogs", {
		templateUrl : "C_Blog/Admin_ManageBlogs.html",
		controller : "BlogController"
	})
	.when("/ManageRejectedBlogs", {
		templateUrl : "C_Blog/Admin_ManageBlogs.html",
		controller : "BlogController"
	})
	.when("/ManageForums", {
		templateUrl : "C_Forum/Admin_ManageForums.html",
		controller : "ForumController"
	})
	.when("/CreateNewForum", {
		templateUrl : "C_Forum/Admin_CreateForum.html",
		controller : "ForumController"
	})
	.when("/CreateNewJob", {
		templateUrl : "C_Jobs/Admin_CreateJob.html",
		controller : "JobController"
	})
	.when("/ManageOpenJobs", {
		templateUrl : "C_Jobs/Admin_ManageJobs.html",
		controller : "JobController"
	})
	.when("/ManageClosedJobs", {
		templateUrl : "C_Jobs/Admin_ManageJobs.html",
		controller : "JobController"
	})
	.when("/ManageJobApplications", {
		templateUrl : "C_Jobs/Admin_ManageJobApplications.html",
		controller : "JobController"
	})
	.when("/ManageNewUsers", {
		templateUrl : "C_User/Admin_ManageUsers.html",
		controller : "UserController"
	})
	.when("/ManageExistingUsers", {
		templateUrl : "C_User/Admin_ManageUsers.html",
		controller : "UserController"
	})
	.when("/ManageRejectedUsers", {
		templateUrl : "C_User/Admin_ManageUsers.html",
		controller : "UserController"
	})
	.when("/CreateNewUsers", {
		templateUrl : "C_User/Register.html",
		controller : "UserController"
	})
	.otherwise({redirectTo:'/'});
});