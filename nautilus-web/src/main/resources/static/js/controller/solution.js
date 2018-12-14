
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
	
	$(".variable-feedback").each(function(index, element){
		sum += parseFloat($(element).val())
	});
	
	var total = $(".variable-feedback").length;
	
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
   
	$(".slider-slave").on("change", function(event) {
		$(".slider-master").slider('setValue', getAverageFeedback())
		changeColor();
	});
	
	$(".slider-master").on("change", function(event) {
		$(".slider-slave").each(function(index, element){
			$(this).slider('setValue', event.value.newValue)
		})
		changeColor();
	});
	
	$(".slider-master").slider('setValue', getAverageFeedback());
	changeColor();
	
	
	$("#btn-clear-user-feedback").click(function(e){
		
		e.preventDefault();
		
		bootbox.dialog({
			 message: "Are you sure to clear the user feedback for this solution?",
			 closeButton: false,
			 title: "Confirm",
			 onEscape: true,
			 buttons: {
		        confirm: {
		            label: "Confirm",
		            className: "btn-outline-danger",
		            callback: function(result){
		            	$("#form-clear-user-feedback").submit();
					}
		        },
		        cancel: {
		            label: "Cancel",
		            className: "btn-outline-secondary pull-right"
		        },
			 },
		});
		
		return false;
	})
})
