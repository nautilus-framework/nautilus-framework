var executionQueueItem;
var stompClient;

// This method was extracted from:
// https://stackoverflow.com/questions/105034/create-guid-uuid-in-javascript/2117523#2117523
function uuidv4() {
	
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
		return v.toString(16);
	});
}

function loadItems(){
	
	executionQueueItem = {}
	
	$("#execution-queue .list-group-item").each(function(index, item){
		
		if(!$(this).hasClass( "list-group-item-empty" )){
			
			var id = $(this).attr('id');
			
			executionQueueItem[id] = {
				el : $(this),
				progress: $(this).find('.progress-bar'),
				status: $(this).find('.execution-queue-status'),
				btnCancel: $(this).find('.execution-queue-btn-cancel')
			}
			
			$($(this).find('.execution-queue-btn-cancel')).click(function(){
				
				$.get( "/optimize/execution/cancel/"+id,  function( data ) {
					  console.log(data)
				});
				
				return false;
			})
		}
	});	
	
	if(!$.isEmptyObject(executionQueueItem)){
		$("#execution-queue .list-group-item-empty").hide();
	}
	
	
}

function appendToQueue(template, item){
	
	var html = template.clone().attr('id',item.id);
	
	html.find('.execution-queue-title').text(item.title);
	html.find('.execution-queue-title').attr("href", "/execution/"+item.id);
	html.find('.execution-queue-status').text(item.status);
	
	$("#execution-queue ul").append(html)
	
	loadItems();
}

$(function(){
	
	loadItems();
	
	var template = $(".execution-queue-item").clone().show();
	
	var sessionId = uuidv4();
	
	var socket = new SockJS('/ws', [], {
	    sessionId: () => {
	       return sessionId
	    }
	 });
	
	stompClient = Stomp.over(socket);
	
	// Remove the log
	stompClient.debug = null
	
	stompClient.connect({}, function (frame) {
		
		console.log("Connected to Websocket")
		
		stompClient.subscribe('/user/'+sessionId+'/execution/queue/progress', function (response) {
    		
    		var item = JSON.parse(response.body)
    		
    		if(executionQueueItem[item.id]){
    			executionQueueItem[item.id].progress.text(item.progress.toFixed(1)+"%");
    			executionQueueItem[item.id].progress.css("width", item.progress.toFixed(0)+"%");
    			executionQueueItem[item.id].status.text(item.status);
    			
    			if(item.progress == 100.0){
    				executionQueueItem[item.id].btnCancel.remove();
    			}
    		}else{
    			appendToQueue(template, item);
    		}	
        });
		
		stompClient.subscribe('/user/'+sessionId+'/execution/queue/append', function (response) {
			
			var item = JSON.parse(response.body)
			
			appendToQueue(template, item);
		});
		
		stompClient.subscribe('/user/'+sessionId+'/execution/queue/error', function (response) {
			
			var item = JSON.parse(response.body)
			
			if(executionQueueItem[item.id]){
				executionQueueItem[item.id].status.text(item.status);
				executionQueueItem[item.id].btnCancel.remove();
			}
		});
    });
    
    var onCloseFromStomp = socket.onclose; 
    
    socket.onclose = function() {
    	
    	console.log("Closed")
	};
})

