
class ProgressBar {
	
	constructor(){
		
		var that = this
		
		this.el= "#modal-progressbar";
		
		this.progressBar = $(this.el+" .progress-bar")
		
		this.lastProgress = -1;
		
		$(this.el + " #btn-cancel").click(function(){
			if(that.onCloseListener){
				that.onCloseListener()
			}
		})
		
		this.title = document.title;
	}
	
	show(){
		$(this.el).modal('show');
	}
	
	hide(){
		$(this.el).modal('hide');
		this.setProgress(0);
	}
	
	setTitle(text){
		$(this.el+" .modal-title").text(text);
	}
	
	setProgress(progress) {
		
		progress = parseFloat(progress)
		
		progress = progress.toFixed(1)
		
		if(progress > this.lastProgress){
			this.progressBar.text(progress+"%");
			this.progressBar.css("width", progress+"%");
			this.progressBar.attr("aria-valuenow", progress);
			this.updateTitle(progress+"%")
		}
		
		this.lastProgress = progress;		
	}
	
	onCancelListener(callback){
		this.onCloseListener = callback
	}
	
	updateTitle(title){
		document.title = title;
	}
}

