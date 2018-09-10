var progressBar = new ProgressBar();
var webSocket = new WebSocket();

webSocket.onDisconnectedListener(function(){
	progressBar.hide();
	Console.log("Whoops! Lost connection to server");
})

webSocket.onProgressListener(function(progress){
	progressBar.setProgress(progress);
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

	if(!Array.isArray(obj["objectiveKeys"])){
		obj["objectiveKeys"] = [obj["objectiveKeys"]];
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
   
	$("form").on('submit', function (e) {
		e.preventDefault();
		
		var form = $(this).serializeArray();
		
		webSocket.connect(function(){
			execute(form);
		});
	
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