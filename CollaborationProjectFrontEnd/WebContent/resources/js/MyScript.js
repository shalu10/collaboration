$(document).ready(function(e) {
	//Changing padding dynamically to match the navbar height
	var h = $('nav').height() + 20;
	
	
	$('body').animate({
		paddingTop : h
	});
	
    $('.nav a').on('click', function(){ 
        if($('.navbar-toggle').css('display') !='none'){
            $(".navbar-toggle").trigger( "click" );
        }
    });
	

	
});

