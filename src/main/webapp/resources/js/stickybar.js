	$(window).scroll(function(){
		console.log('Event Fired');
    if ($(window).scrollTop() >= 240) {
       $('nav').addClass('fixed-header');
    }
    else {
       $('nav').removeClass('fixed-header');
    }
});