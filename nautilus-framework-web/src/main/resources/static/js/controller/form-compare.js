function fetchInstancesFromServer(problemId){
	
	$.get("/api/problem/" + problemId + "/instances", function(instances){
		
		$("#instance").empty();
		
		$.each(instances, function(i, instance){
			$("#instance").append($('<option>', {value:instance, text:instance}));
		});
		
		fetchExecutionsFromServer(getProblemId(), getInstance());
	});
}

function fetchExecutionsFromServer(problemId, instance, userId){
	
	console.log(problemId, instance, userId);
	
	$.get(`/api/execution/${problemId}/${instance}/${userId}?selectedSolutions=1`, function(executions){
		
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

function getUserId(){ 
	return $("#userId option:selected").val();
}

function getProblemId(){ 
	return $("#problemId option:selected").val();
}

function getInstance(){
	return $("#instance option:selected").val();
}

$(function(){
	
//	fetchInstancesFromServer(getProblemId());
//	
//	$('#userId').change(function (e) {
//		e.preventDefault();
//		
//		fetchExecutionsFromServer(getProblemId(), getInstance(), getUserId());
//		
//		return false;
//	});
//	
//	$('#problemId').change(function (e) {
//		e.preventDefault();
//		
//		fetchExecutionsFromServer(getProblemId(), getInstance(), getUserId());
//		
//		return false;
//	});
//	
//	$('.filter-executions').change(function (e) {
//		e.preventDefault();
//		
//		fetchExecutionsFromServer(getProblemId(), getInstance(), getUserId());
//	
//		return false;
//	});
	
	
//	$("#form-compare").submit(function(e){
//		
//		e.preventDefault();
//		
//		var data = $('#execution-table').find('input, select').serializeArray();
//		
//		console.log(data)
//		console.log($(this).serializeArray())
//		
//	
//		return false;
//	});
})
