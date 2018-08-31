var stompClient = null;

function ProgressBar(el){
	
	this.el= el;
	
	this.show = function(){
		$(this.el).show();
	}
	
	this.hide = function(){
		$(this.el).hide();
	}
	
	this.setTitle = function(text){
		$(this.el+" .title").text(text);
	}
	
	this.setProgress = function(progress){
		$(this.el+" .progress-bar").text(progress+"%");
		$(this.el+" .progress-bar").css("width", progress+"%");
		$(this.el+" .progress-bar").attr("aria-valuenow", progress);
	}
}

