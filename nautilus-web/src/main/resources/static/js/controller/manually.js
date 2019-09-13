
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
		
		var obj = {
			instance: $("#instance").text(),
			problemId: $("#problem-id").text(),
			variables: getSelectec(),
			objectiveIds: [],
			normalizedObjectiveValues: [],
			objectiveValues: []
		}
		
		console.log(obj)
			
		$.ajax({
			type: "GET",
			dataType: 'json',
			url: "/api/calculate/objective/values",
			data: {obj : JSON.stringify(obj)},
			success: function(result){
				
				console.log(result);
				
				var $tbody = $("#solution-objective-table > tbody");
				
				$tbody.empty();
				
				for(var i=0;i<result.objectiveIds.length;i++){
					
					var str = "<tr>";
					
					str += ("<td>"+i+"</td>")
					str += ("<td>"+result.objectiveIds[i]+"</td>")
					str += ("<td>"+result.objectiveValues[i].toFixed(5)+"</td>")
					str += ("<td>"+result.normalizedObjectiveValues[i].toFixed(5)+"</td>")
					str += "</str>";
					
					$tbody.append(str)
				}
			},
			error: function(a, b, c){
				console.log(a, b, c)
			}
		});
			
	    return false;
	 });
	
})
