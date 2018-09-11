function isSafari(){
	return /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
}

function getDatatableButtons(el){
	
	var buttons = [];
	
	$(el).each(function(index, el){
		
		var that = $(this);
		
		buttons.push({
            text: that.text(),
            className: that.attr("class"),
            action: function ( e, dt, node, config ) {
            	location.href = that.attr("href")
            }
        })
	});
	
	return buttons;
}
 
$(function(){
   
	$('.table-datatable').DataTable();
	
	var array = 
	
	console.log()
	
	$('#instances-table').DataTable({
		dom: "Bfrtp",
		buttons: getDatatableButtons("#datatable-buttons")
	});
	
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