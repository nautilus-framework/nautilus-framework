var stompClient = null;
var progressBar = new ProgressBar();
var sessionId = uuidv1().split("-")[0]
var alertBox = new AlertBox();

function append(text){
	progressBar.appendToConsole(text);
}

function setConnected(connected) {
	if (connected) {
    	append("Connected");
    }else {
    	append("Disconnected");
    }
}

function showModal(){
	$('#modal-progressbar').modal('show')

}

function connect() {
    
	var socket = new SockJS('/gs-guide-websocket', [], {
	    sessionId: () => {
	       return sessionId
	    }
	 });
	
	append(sessionId)
	
	stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function (frame) {
        
    	setConnected(true);
        
//        stompClient.subscribe('/topic/greetings.'+sessionId, function (greeting) {
//        	
//        	$("#progress").html(greeting.body);
//        	
//        	progressBar.setProgress(greeting.body);
//        	//append(greeting.body);
//        });
        
        stompClient.subscribe('/topic/optimize/progress.'+sessionId, function (response) {
        	progressBar.setProgress(response.body);
        });
        stompClient.subscribe('/topic/optimize/title.'+sessionId, function (response) {
        	progressBar.setTitle(response.body);
        });
        stompClient.subscribe('/topic/optimize/done.'+sessionId, function (response) {
			window.location.href = "/view/execution/" + response.body;        	
        });
        stompClient.subscribe('/topic/optimize/exception.'+sessionId, function (response) {
        	progressBar.setTitle("Ooopsss...");
        	progressBar.appendToConsole(response.body);
        });
    });
    
    var onCloseFromStomp = socket.onclose; 
    
    socket.onclose = function() {
    	
    	append("Whoops! Lost connection to server");
    	
    	onCloseFromStomp();
	};
}

function disconnect() {
    
	if (stompClient !== null) {
        stompClient.disconnect();
    }
	
	setConnected(false);
}

function sendName() {
	
	progressBar.setTitle("Waiting the executor...");
	progressBar.setProgress(0);

    var obj = JSON.stringify({
        'populationSize': $("#populationSize").val(),
        'maxEvaluations': $("#maxEvaluations").val()
    });

 showModal()
    console.log(obj)

    stompClient.send("/app/hello."+sessionId, {}, obj);
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}


$(function(){
   
	$("form").on('submit', function (e) {
		e.preventDefault();
	});
	
	$( "#btn-execute" ).click(function(e) { 
		e.preventDefault();
    	
    	connect(); 
    	
    	return false;
	});
    
    $( "#btn-disconnect" ).click(function() { disconnect(); });
    $( "#btn-send" ).click(function() { sendName(); });
	
	alertBox.hide();

    //progressBar.hide();
    
    //progressBar.setTitle("ola")
    progressBar.setProgress(0);
    //setTitle("este")
//    window.onbeforeunload = function (e) {
//	    var e = e || window.event;
//	
//	    // For IE and Firefox
//	    if (e) {
//	        e.returnValue = 'Leaving the page';
//	    }
//	
//	    // For Safari
//	    return 'Leaving the page';
//	};


})