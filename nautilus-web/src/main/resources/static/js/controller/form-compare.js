function fetchInstancesFromServer(problemId){
	
	$.get("/api/problem/" + problemId + "/instances", function(instances){
		
		$("#instance").empty();
		
		$.each(instances, function(i, instance){
			$("#instance").append($('<option>', {value:instance, text:instance}));
		});
		
		fetchExecutionsFromServer(getProblemId(), getInstance());
	});
}

function fetchExecutionsFromServer(problemId, instance){
	
	$.get(`/api/execution/${problemId}/${instance}?selectedSolutions=1`, function(executions){
		
		$("#executionIds").empty();
		
		$.each(executions, function(i, execution){
			
			var date = moment(execution.creationDate).format("YYYY-MM-DD HH:mm:ss")
			
			$("#executionIds").append($('<option>', {
				value: execution.id, 
				text: date+", "+execution.instance+", "+execution.algorithmId
			}));
		});
	});
}

function getProblemId(){ 
	return $("#problemId option:selected").val();
}

function getInstance(){
	return $("#instance option:selected").val();
}

$(function(){
	
	var selectedProblemId = $("#problemId option:selected").val()
	
	fetchInstancesFromServer(selectedProblemId);
	
	var $dualList = $('.dualx-list').bootstrapDualListbox({
		moveOnSelect: false,
		preserveSelectionOnMove: 'moved',
	});
	
	$('#problemId').change(function (e) {
		e.preventDefault();
		
		var problemId = $(this).find(':selected').val()
		
		fetchInstancesFromServer(problemId);
		
		return false;
	});
	
	$('.filter-executions').change(function (e) {
		e.preventDefault();
		
		fetchExecutionsFromServer(getProblemId(), getInstance());
	
		return false;
	});
	
	console.log($dualList)
	
})
