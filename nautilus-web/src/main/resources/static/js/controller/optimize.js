var referencePoints = [];

function getReferencePointAsHTML(index, rp){
	return '<div class="input-group mb-2">'+
    '<input type="text" class="form-control" id="referencePoints" readonly name="referencePoints['+index+']" value="'+rp+'">'+
	'  <div class="input-group-append">'+
	'    <button type="button" class="btn btn-secondary btn-remove-reference-point" ><i class="fa fa-trash"></i></button>'+
	'  </div>'+
	'</div>'
}

function rearrangeIndex(){
	
	$("#reference-points input[type='text']").each(function(i, obj){
		$(obj).attr("name", "referencePoints["+i+"]");
	});
}

$(function(){
	
	var modal = $('#modal-upload-reference-point');
	
	
	$(document).on('click', ".btn-remove-reference-point",function(event){
		event.preventDefault();
		
		$(this).parent().parent().remove();
		
		rearrangeIndex();
		
		return false;
	});
	
	$("#btn-add-reference-point").click(function(event){
		event.preventDefault();
	
		modal.modal('show');
		
		return false;
	});
	
	modal.on('shown.bs.modal', function (e) {
		$(this).find("input[type='number']:visible").first().focus();
	});
	
	modal.on('show.bs.modal', function (e) {
		
		$("input[name='objectiveIds']").each(function(i, obj){
			
			var objectiveId = $(obj).attr("value");
		
			if($(obj).is(':checked')){
				$("#rp-"+objectiveId).parent().show();
			}else{
				$("#rp-"+objectiveId).parent().hide();
			}	
		});
	})
	
	modal.find("form").submit(function(event){
		
		event.preventDefault();
		
		modal.modal('hide');
		
		var values = [];
		
		$(this).find("input[type='number']:visible").each(function(i, value){
			values.push(parseFloat($(value).val()));
		});
		
		$("#reference-points").append(getReferencePointAsHTML(1, values));
		
		rearrangeIndex();
		
		return false;
	})
});