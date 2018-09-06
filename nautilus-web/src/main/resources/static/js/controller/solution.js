
var red = 0;
var  green = 120;

function normalize(value, a, b, min, max) {
	return a + (((value - min) * (b - a)) / (max - min));
}

function getColor(percent, start, end) {
  var a = percent / 100,
      b = (end - start) * a,
      c = b + start;

  return 'hsl('+c+', 60%, 50%)';
}

function getAverageFeedback(){
	
	var sum = 0.0;
	
	$(".feedback-in-variable").each(function(index, element){
		sum += parseFloat($(element).val())
	});
	
	var total = $(".feedback-in-variable").length;
	
	return sum / total;
}

function changeColor(){
	
	$(".slider-colorful").each(function(index, el) {
		var percentage = normalize($(this).val(), 0, 100, -1, 1);
		
		var color = getColor(percentage,red, green);
		
		$(this).parent().find(".slider-selection").css('background', color)
		$(this).parent().find(".slider-track-high").css('background', color)
	});
}

$(function(){
   
//	$("#btn-feedback").click(function(e){
//		e.preventDefault();
//		
//		$("#modal-feedback").modal('show');
//		$("#slider-feedback").slider("refresh");
//		
//		return false;
//	});
	
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
	
	$(".feedback-for-variable").on("change", function(event) {
		$("#general-feedback").slider('setValue', getAverageFeedback())
		changeColor();
	});
	
	$("#general-feedback").on("change", function(event) {
		$(".feedback-for-variable").each(function(index, element){
			$(this).slider('setValue', event.value.newValue)
		})
		changeColor();
	});
	
	changeColor();
})
