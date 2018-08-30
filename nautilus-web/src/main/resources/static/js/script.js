var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
    	$("#result").append("Connected");
        $("#conversation").show();
    }
    else {
    $("#result").append("Disconnected");
        $("#conversation").hide();
    }
    $("#greetings").html("");
    
    
}

function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    //var socket = new SockJS('/gs-guide-websocket', [], {
	//	sessionId: () => {
	//		return 12345
	//	}
	//});
	
	socket.onclose = function() {
    	console.log("disconnected");
	};	
	
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function (frame) {
        setConnected(true);
        
        stompClient.subscribe('/topic/greetings', function (greeting) {
            $("#result").append(greeting);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {

    var obj = JSON.stringify({
        'populationSize': $("#populationSize").val(),
        'maxEvaluations': $("#maxEvaluations").val()
    });

    console.log(obj)

    stompClient.send("/app/hello", {}, obj);
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function(){
   
   $("form").on('submit', function (e) {
        e.preventDefault();
    });
    
    $( "#btn-connect" ).click(function(e) { 
    	e.preventDefault();
    	
    	connect(); 
    	
    	return false;
	});
    $( "#btn-disconnect" ).click(function() { disconnect(); });
    $( "#btn-send" ).click(function() { sendName(); });
    
    
    window.onbeforeunload = function (e) {
	    var e = e || window.event;
	
	    // For IE and Firefox
	    if (e) {
	        e.returnValue = 'Leaving the page';
	    }
	
	    // For Safari
	    return 'Leaving the page';
	};
})