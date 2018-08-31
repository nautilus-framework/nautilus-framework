var stompClient = null;

function AlertBox(){
	
	this.el= "#alertbox";
	
	this.show = function(){
		$(this.el).show();
	}
	
	this.hide = function(){
		$(this.el).hide();
	}
	
	this.setTitle = function(text){
		$(this.el+" .modal-title").text(text);
	}
	
	this.appendToConsole = function(text){
		$(this.el+" .console").append(text).append("\n");
	}
}

