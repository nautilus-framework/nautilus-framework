class WebSocket {
  
	constructor() {
		this.stompClient = undefined;
		this.connected = false;
		this.callbacks = {}
		this.callbacks['onDisconnected'] = []
		this.callbacks['onConnected'] = []
		this.callbacks['onProgress'] = []
		this.callbacks['onTitleChanged'] = []
		this.callbacks['onDone'] = []
		this.callbacks['onException'] = []
	}
	
	connect(callback){
		
		var that = this
		
		this.sessionId = uuidv1().split("-")[0]
		
		if(this.isConnected()){
			return;
		}
		
		var socket = new SockJS('/gs-guide-websocket', [], {
		    sessionId: () => {
		       return that.sessionId
		    }
		 });
		
		this.stompClient = Stomp.over(socket);
	    
		this.stompClient.connect({}, function (frame) {
	        
	    	that.connected = true;
	    	
	    	if(callback) {
	    		callback();
	    	}
	        
	    	that.stompClient.subscribe('/topic/optimize/progress.'+that.sessionId, function (response) {
	    		that.callbacks['onProgress'].forEach(function(callback) {
		    		callback(response.body);
		    	})
	        });
	    	that.stompClient.subscribe('/topic/optimize/title.'+that.sessionId, function (response) {
	    		that.callbacks['onTitleChanged'].forEach(function(callback) {
		    		callback(response.body);
		    	})
	        });
	    	that.stompClient.subscribe('/topic/optimize/done.'+that.sessionId, function (response) {
	    		that.callbacks['onDone'].forEach(function(callback) {
		    		callback(response.body);
		    	})      	
	        });
	    	that.stompClient.subscribe('/topic/optimize/exception.'+that.sessionId, function (response) {
	    		that.callbacks['onException'].forEach(function(callback) {
		    		callback(response.body);
		    	})
	        });
	    });
	    
	    var onCloseFromStomp = socket.onclose; 
	    
	    socket.onclose = function() {
	    	
	    	that.callbacks['onDisconnected'].forEach(function(callback) {
	    		callback();
	    	})
	    	
	    	that.connected = false;
	    	
	    	onCloseFromStomp();
		};
	}
	
	execute(obj){
		this.stompClient.send("/app/hello."+this.sessionId, {}, obj);
	}
	
	subscribe(url, callback){
		this.stompClient.subscribe(url, callback)
	}
	
	onDisconnectedListener(callback){
		this.callbacks['onDisconnected'].push(callback) 
	}
	
	onProgressListener(callback){
		this.callbacks['onProgress'].push(callback) 
	}
	
	onTitleChangedListener(callback){
		this.callbacks['onTitleChanged'].push(callback) 
	}
	
	onDoneListener(callback){
		this.callbacks['onDone'].push(callback) 
	}
	
	onExceptionListener(callback){
		this.callbacks['onException'].push(callback) 
	}
	
	disconnect() {
		
		if (this.stompClient !== null) {
	        this.stompClient.disconnect();
	    }
		
		this.connected = false;
	}
	
	isConnected(){
		return this.connected === true
	}
}

