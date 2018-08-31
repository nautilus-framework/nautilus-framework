var stompClient = null;

function ProgressBar(){
	
	this.el= "#modal-progressbar";
	
	this.show = function(){
		$(this.el).modal('show');
	}
	
	this.hide = function(){
		$(this.el).modal('hide');
	}
	
	this.setTitle = function(text){
		$(this.el+" .modal-title").text(text);
	}
	
	this.appendToConsole = function(text){
		$(this.el+" .console").append(text).append("\n");
	}
	
	this.setProgress = function(progress){
	
		progress = parseFloat(progress)
		
		progress = progress.toFixed(1)
	
		$(this.el+" .progress-bar").text(progress+"%");
		$(this.el+" .progress-bar").css("width", progress+"%");
		$(this.el+" .progress-bar").attr("aria-valuenow", progress);
	}
}

