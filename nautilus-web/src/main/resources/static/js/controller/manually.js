
function getSelectec(){
	
	var array = [];
	
	$.each($(".checkbox-variable:checked"), function(){            
		array.push($(this).val());
    });
	
	return array;
}

$(function(){
   
	
	$('.checkbox-variable').change(function (e) {
		e.preventDefault();
	   
		console.log(getSelectec())
		
	    return false;
	 });
	
})
