class Console {
	
	static log(msg){
		$("#modal-console").modal('show');
		$("#modal-console textarea").append(msg).append("\n")
	}
}

