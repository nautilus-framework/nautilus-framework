function isSafari(){
	return /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
}
 
$(function(){
	
	jQuery.validator.setDefaults({
	    highlight: function(element) {
	        jQuery(element).closest('.form-group').addClass('has-error');
	    },
	    errorElement: 'div',
	    errorClass: 'invalid-feedback',
	});
   
	$('.table-datatable').DataTable();
	
	$('.table-datatable-no-paginaton').dataTable({
	    "bPaginate": false
	});
	
	$('a[data-toggle="tab"]').click(function(e) {
		e.preventDefault();
		
		var tab_name = $(this).attr("href")
		
		if (history.pushState) {
			history.pushState(null, null, tab_name)
		}else {
    		location.hash = tab_name
  		}
  		
  		$(this).tab('show');
		
		return false;
	});
	
	// on load of the page: switch to the currently selected tab
	var hash = window.location.hash;
	
	if(hash){
		$('a[href="' + hash + '"]').tab('show');
	}else{
		$('.nav-link:first').tab('show');
	}
	
	if(!isSafari()){
        
        $('.horizontal-scroll').on("mousewheel", function(e) {

            var delta = e.originalEvent.wheelDelta;

            $(this).scrollLeft(this.scrollLeft + (-delta));

            e.preventDefault();
        });
    }
})