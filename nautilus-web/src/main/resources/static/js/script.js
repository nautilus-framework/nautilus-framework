function isSafari(){
	return /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
}
 
$(function(){
	
	$(".boostrap-dual-listbox").bootstrapDualListbox({
		
	});
	
	$("#form-delete-plugin").submit(function(){
		
		if(confirm("Are you sure to delete this item?")){
			return true;
		}
		
		return false;
	});
	
	$("#form-delete-plugin a").click(function(){
		$(this).parent().submit();
	});
	
	// Hide the alert when it is available
//	window.setTimeout(function() {
//		$(".alert").fadeTo(500, 0).slideUp(500, function(){
//	        $(this).remove(); 
//	    });
//	}, 4000);
	
	var tz = moment.tz.guess();
	
	moment.tz.setDefault(tz);
	
	$(".dates").html(function(index, value) {
		return moment(value).tz(tz).format("ddd, DD MMM YYYY HH:mm:ss");
	});
	
	jQuery.validator.setDefaults({
	    highlight: function(element) {
	        jQuery(element).closest('.form-group').addClass('has-error');
	    },
	    errorElement: 'div',
	    errorClass: 'invalid-feedback',
	});
	
	
	$(".checkbox-select-all").each(function(index, value){
		
		var targets = $(this).parent().parent().find(".card-body input[type='checkbox']");
		
		var counter = 0;
		
		targets.each(function(index, target){
			if($(target).is(':checked')){
				counter++;
			}
		});
		
		if(counter == targets.length){
			$(this).prop("checked", true);
		}else{
			$(this).prop("checked", false);
		}
	});
	
	$(".checkbox-select-all").click(function(e){
		
		var that = this;
		
		var targets = $(this).parent().parent().find(".card-body input[type='checkbox']");
		
		targets.each(function(index, value){
			$(value).prop("checked", $(that).is(':checked'));
		});
	});
	
	$(".objectiveKeys").click(function(e){
		
		var parent = $(this).parent().parent().parent().parent().parent();
		
		var targets = parent.find("input[type='checkbox']");
		
		var counter = 0;
		
		targets.each(function(index, value){
			if($(value).is(':checked')){
				counter++;
			}
		});
		
		if(counter == targets.length){
			parent.parent().find(".checkbox-select-all").prop("checked", true);
		}else{
			parent.parent().find(".checkbox-select-all").prop("checked", false);
		}
	})
	
	
   
	$('.table-datatable').DataTable();
	
	$('.table-datatable-no-paginaton').dataTable({
	    "bPaginate": false
	});
	
	$('.table-datatable-with-option').dataTable({
		"columnDefs": [ {
			"targets": $('.table-datatable-with-option tr:first').find("th").length-1,
			"orderable": false
		}]
	});
	
	$('a[data-toggle="tab"]').click(function(e) {
		e.preventDefault();
		
		var tab_name = $(this).attr("href")
		
		if (history.pushState) {
			history.pushState(null, null, tab_name)
		}else {
    		location.hash = tab_name
  		}
  		
  		$(this).tab('show');
		
		return false;
	});
	
	// on load of the page: switch to the currently selected tab
	var hash = window.location.hash;
	
	if(hash){
		$('a[href="' + hash + '"]').tab('show');
	}else{
		$('.nav-link:first').tab('show');
	}
	
	if(!isSafari()){
        
        $('.horizontal-scroll').on("mousewheel", function(e) {

            var delta = e.originalEvent.wheelDelta;

            $(this).scrollLeft(this.scrollLeft + (-delta));

            e.preventDefault();
        });
    }
})