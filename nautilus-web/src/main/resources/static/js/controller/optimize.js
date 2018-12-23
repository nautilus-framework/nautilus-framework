var progressBar = new ProgressBar();
var webSocket = new WebSocket();

webSocket.onDisconnectedListener(function(){
	progressBar.hide();
	Console.log("Whoops! Lost connection to server");
})

webSocket.onProgressListener(function(progress){
	progressBar.setProgress(progress);
	updateTitle(progress);
})

webSocket.onTitleChangedListener(function(title){
	progressBar.setTitle(title);
})

webSocket.onDoneListener(function(executionId){
	webSocket.disconnect();
	window.location.href = "/execution/" + executionId;
})

webSocket.onExceptionListener(function(msg){
	webSocket.disconnect();
	progressBar.hide();
	Console.log(msg);
})

progressBar.onCancelListener(function(){
	webSocket.disconnect();
})

function execute(array) {

	progressBar.show();
	
	progressBar.setTitle("Waiting the executor...");
	progressBar.setProgress(0);

	var obj = {};
	
	$.each(array, function(index, el){
		
		if(el.name in obj){
			if(!Array.isArray(obj[el.name])){
				obj[el.name] = [obj[el.name]]
			}
			obj[el.name].push(el.value)
		}else{
			obj[el.name] = el.value; 
		}
	})

	if(!Array.isArray(obj["objectiveIds"])){
		obj["objectiveIds"] = [obj["objectiveIds"]];
	}
	
	if(obj["referencePoints"]){
		
		var referencePoints = obj["referencePoints"].trim().split("\n");
		
		obj["referencePoints"] = []; 
		
		for(var i in referencePoints){
			
			var rp = referencePoints[i].trim();
			
			if(rp){
				var points = rp.split(";").map(e => parseFloat(e)).filter(e => !isNaN(e));
				
				obj["referencePoints"].push(points);
			}
		}
	}else{
		obj["referencePoints"] = []; 
	}
	
	obj = JSON.stringify(obj);
	
	webSocket.execute(obj);
}

function executeAgain(nextParameters ) {

	progressBar.show();
	
	progressBar.setTitle("Waiting the executor...");
	progressBar.setProgress(0);

	webSocket.execute(nextParameters);
}

$(function(){
	
	$("form").validate();
   
	$("#form-execute").on('submit', function (e) {
		e.preventDefault();
		
		var data = $(this).serializeArray();
		
		var form = $(this)
		
		if (form[0].checkValidity() === false) {
			event.preventDefault()
			event.stopPropagation()
	    }else{
	    	webSocket.connect(function(){
				execute(data);
			});
	    }
		
		form.addClass('was-validated');
		
		return false;
	});

	$("#btn-continue").click(function(e){
		e.preventDefault();
		
		var nextParameters = $("#next-parameters").text();
		
		webSocket.connect(function(){
			executeAgain(nextParameters);
		});
	
		return false;
	})
})