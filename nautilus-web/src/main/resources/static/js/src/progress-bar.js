
class ProgressBar {
	
	constructor(){
		
		var that = this
		
		this.el= "#modal-progressbar";
		
		$(this.el + " #btn-cancel").click(function(){
			if(that.onCloseListener){
				that.onCloseListener()
			}
		})
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
	
		$(this.el+" .progress-bar").text(progress+"%");
		$(this.el+" .progress-bar").css("width", progress+"%");
		$(this.el+" .progress-bar").attr("aria-valuenow", progress);
	}
	
	onCancelListener(callback){
		this.onCloseListener = callback
	}
}

