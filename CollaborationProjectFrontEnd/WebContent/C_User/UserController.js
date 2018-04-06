myApp.controller("UserController", function($scope,$http, UserService, $rootScope, $cookieStore, $route, $location) {
	
	console.log('Starting of UserController');
	
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
	
	this.loggedInUser = {	
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
	
	this.successMessage = "";
	this.errorMessage = "";
	
	this.isUserLoggedIn = false;
	
	this.users = [];
	this.userStatus = "";
	
	$scope.selectedSort = 'userId';
		
	//Get All Users
	this.getAllUsers = function(){
		console.log("Starting of getAllUsers() in UserController");
		
		UserService.getAllUsers()
		.then(
				function(response){
					this.users = response;
					$rootScope.users = response;
					localStorage.setItem('users', JSON.stringify(this.users));
				}
		);
	};
	this.getAllUsers();
	
	//validate
	this.validate = function (user){
		console.log("Starting of validate method");
		
		UserService.validate(user)
		.then(
				function(dataFromService){
					this.user = dataFromService;
					console.log("Validate error code : "+this.user.errorCode);
					
					if(this.user.errorCode == "404"){
						//
						$rootScope.errorMessage = this.user.errorMessage;
						this.user.userId = "";
						this.user.password = "";
					} else {
						//valid credentials
						console.log("Valid credentials! Returning to home page");
						$rootScope.successMessage = this.user.errorMessage+" Welcome "+this.user.firstname+" "+this.user.lastname;
						$rootScope.isUserLoggedIn = true;
						console.log("Logged is as : "+this.user);
						$rootScope.loggedInUser = this.user;
						$cookieStore.put('loggedInUser', this.user);
						$http.defaults.headers.common['Authorization'] = 'Basic ' +$rootScope.loggedInUser;
						if(this.user.role == "ROLE_ADMIN"){
							console.log("Redirecting to admin page");
							$location.path('/AdminHome');
						} else {
							console.log("Redirecting to home page");
							$location.path('/');
						}
						
					}
				}, function(errResponse){
					console.error('Error while authentication user');
				}
				
				
		);
	};
	
	$rootScope.updateUser = function(user, formName){
		console.log("Starting of updateUser method");
		
		UserService.updateUser(user)
		.then(
				function(response) {
					if(response.errorCode == "404"){
						$rootScope.errorMessage = response.errorMessage;
						console.log(response.errorMessage);
					} else {
						$rootScope.successMessage = response.errorMessage;
						console.log(response.errorMessage);
						$rootScope.user = {};
						$scope.resetForm(formName);
						$route.reload();
						this.getAllUsers;
					}
				}
		)
		
	}
	
	//signout
	this.signOut = function(){
		console.log("Starting of method signOut in UserController");
		$rootScope.loggedInUser = {};
		$rootScope.isUserLoggedIn = false;
		$cookieStore.remove("loggedInUser");
		UserService.signOut();
		$rootScope.successMessage = "Successfully Signed Out! Welcome Back!!";
		$location.path("/");
	}
	
	//register user
	this.register = function(user){
		console.log("Starting of registration method");
		UserService.register(user)
		.then(function(dataFromService) {
			this.user = dataFromService;
			if(this.user.errorCode == "404"){
				console.log(this.user.errorMessage);
				$rootScope.errorMessage = this.user.errorMessage;
			} else {
				console.log(this.user.errorMessage);
				$rootScope.successMessage = this.user.errorMessage+" Please wait for admin to approve!";
				
				$location.path('/');
				
			}
		}, function(reason) {
			console.log("Error occured during registration");
			
			
		});
		
	} 
	
	this.approveUser = function(userId){
		console.log("Starting of approveUser() in UserController");
		
		UserService.approveUser(userId)
		.then(
				function(response) {
					this.user = response;
					if(this.user.errorCode == "404"){
						$rootScope.errorMessage = response.errorMessage;
						console.log(response.errorMessage);
					} else {
						$rootScope.successMessage = response.errorMessage;
						this.getAllUsers;
						$route.reload();
					}
				}
		)
	}
	this.rejectUser = function(userId){
		console.log("Starting of rejectUser() in UserController");
		var remarks = prompt("Enter remarks for rejection.","Rejected by "+$rootScope.loggedInUser.userId);
		UserService.rejectUser(userId, remarks)
		.then(
				function(response) {
					this.user = response;
					if(this.user.errorCode == "404"){
						$rootScope.errorMessage = response.errorMessage;
						console.log(response.errorMessage);
					} else {
						$rootScope.successMessage = response.errorMessage;
						this.user = response;
						this.getAllUsers;
						$route.reload();
					}
				}
		)
	}
	
	this.editUser = function(user){
		console.log("Starting of editUser method");
		//console.log(user);
		$rootScope.user = user;
		console.log($rootScope.user);
		//$route.reload();
	}
	
	
	
	this.getManageUserStatus = function(){
		if($location.path()=="/ManageNewUsers"){
			this.userStatus = 'N';
			return 'N';
		} else if($location.path() == "/ManageExistingUsers"){
			this.userStatus = 'A';
			return 'A';
		} else if($location.path() == "/ManageRejectedUsers"){
			this.userStatus = 'R';
			return 'R';
		} else {
			this.userStatus = '';
		}
	}
	
	this.whatIsUrl = function(){
		return $location.path();
	}
	
	$scope.resetForm = function(insertjob){
		console.log("Starting reserForm method");
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
		insertjob.$setPristine();
		insertjob.$setUntouched();
	}
	
	
	
	
});
