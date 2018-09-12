function isSafari(){
	return /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
}
 
$(function(){
   
	$('.table-datatable').DataTable();
	
	$('.table-datatable-no-paginaton').dataTable({
	    "bPaginate": false
	});
	
	$('.nav-tabs a').click(function(e) {
		e.preventDefault();
		e.stopImmediatePropagation();
		$(this).tab('show');
	});
	
	// on load of the page: switch to the currently selected tab
	var hash = window.location.hash;
	
	if(hash){
		$('.nav-tabs a[href="' + hash + '"]').tab('show');
	}else{
		$('.nav-link:first').tab('show');
	}
	
	// store the currently selected tab in the hash value
	$('a[data-toggle="tab"]').on("shown.bs.tab", function(e) {
		window.location.hash = $(e.target).attr("href").substr(1);
	});
	
	if(!isSafari()){
        
        $('.horizontal-scroll').on("mousewheel", function(e) {

            var delta = e.originalEvent.wheelDelta;

            $(this).scrollLeft(this.scrollLeft + (-delta));

            e.preventDefault();
        });
    }
})