function isSafari(){
	return /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
}
 
$(function(){
   
	$('.table-datatable').DataTable();
	
	$('.table-datatable-no-paginaton').dataTable({
	    "bPaginate": false
	});
	
	if(!isSafari()){
        
        $('.horizontal-scroll').on("mousewheel", function(e) {

            var delta = e.originalEvent.wheelDelta;

            $(this).scrollLeft(this.scrollLeft + (-delta));

            e.preventDefault();
        });
    }
})