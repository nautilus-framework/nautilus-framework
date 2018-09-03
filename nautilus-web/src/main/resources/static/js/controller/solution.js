
$(function(){
   
	$("#btn-feedback").click(function(e){
		e.preventDefault();
		
		$("#modal-feedback").modal('show');
		$("#slider-feedback").slider("refresh");
		
		return false;
	});
	
	// With JQuery
	$("#slider-feedback").slider({
	    ticks: [-1, 0, 1],
	    ticks_labels: ['Bad', '|', 'Good'],
	    step: 0.01,
		min: -1,
		value: 0,
		max: 1,
		tooltip: 'always'
	});
})