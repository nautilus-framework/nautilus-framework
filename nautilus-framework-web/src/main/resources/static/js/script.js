function formatDate(date){
	return moment(date).format("YYYY-MM-DD HH:mm:ss")
}

function isSafari(){
	return /^((?!chrome|android).)*safari/i.test(navigator.userAgent);
}

function getCookie(cname) {
	
	var name = cname + "=";
	var decodedCookie = decodeURIComponent(document.cookie);
	var ca = decodedCookie.split(';');

	for (var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}
		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}
	return "";
}

function getLanguage(){
	return getCookie('localeInfo') || "en_US";
}

function getDatatableLanguage(){
	
	if(getLanguage() == 'pt_BR'){
		return "//cdn.datatables.net/plug-ins/1.10.19/i18n/Portuguese-Brasil.json"
	}
	return null;
}

function confirm(message, callback){
	
	bootbox.dialog({
		 message: message,
		 closeButton: false,
		 title: $("#confirm #title").text(),
		 onEscape: true,
		 buttons: {
			 confirm: {
				label: $("#confirm #btn-confirm").text(),
	            className: "btn-danger",
	            callback: function(result){
	            	callback();
				}
	        },
	        cancel: {
	            label: $("#confirm #btn-cancel").text(),
	            className: "btn-light pull-right"
	        },
		 },
	});
}

function getNoOrderableIndexes(){

	var indexes = [];
	
	$('.table-datatable-no-orderable').find("th.no-orderable").each(function(index, value){
		indexes.push($(this).index());
	})

	return indexes;
}

var executionQueue = $("#execution-queue ul");

function updateExecutionQueue(){
	
	if(!executionQueue) return;
	
	$.get({
        url: "/api/execution/queue",
        cache: false
    }).done(function (response) {
    	
    	executionQueue.html("")
    	
    	if(response.length == 0){
    		
    		var item = (
		    	'<li class="list-group-item">No execution in this queue</li>'
		    );
    		
    		executionQueue.html(item);
    		
    		return;
    	}
    	
    	var html = "";
    	
    	for(var i =0; i<response.length;i++){
	    	
    		var item = (
		    	'<li class="list-group-item">'+
			        '<span>@TITLE@</span>'+
			        '<div class="progress mt-2">'+
			              '<div class="progress-bar progress-bar-striped" role="progressbar" style="width: @PROGRESS@%" >@PROGRESS@%</div>'+
			        '</div>'+
			    '</li>'
		    );
	    	
    		item = item.replace(/@TITLE@/g, "teste")
	    	item = item.replace(/@PROGRESS@/g, response[i].progress.toFixed(1))
		    
	    	html = html + item;
    	}
    	
    	executionQueue.html(html);
    	
    	console.log(response);
    	
    });
}
 
$(function(){
    
    $('.nav-pills a:first-child').tab('show');
    $('.list-group[id=list-tab] a:first-child').tab('show');
    
	$("button[data-confirm], a[data-confirm]").click(function(e){
		e.preventDefault();
		
		var that = $(this);
		
		if(confirm(that.data("confirm"), function(){
            
            const target = that.data("target");
            
            if (target) {
                $(target).submit();
            } else {
                that.parent().submit();   
            }
		}));
		
		return false;
	});

	$("form[data-confirm]").submit(function(e){
		e.preventDefault();
		
		var that = $(this);
		
		if(confirm(that.data("confirm"), function(){
			// We have to unbind because it generates an infinity loop
			that.unbind('submit').submit()
		}));
		
		return false;
	});
	
	// Try to guess the user timezone
	
	var tz = moment.tz.guess();
	
	moment.tz.setDefault(tz);
	
	// Format all data with this class to a friendly date
	
//	$(".format-date").html(function(index, value) {
//		
//		var date = moment(value);
//		
//		if(date.isValid()){
//			return date.tz(tz).format("YYYY-MM-DD HH:mm:ss")
//		}
//		
//		return value;
//	});
	
	jQuery.validator.setDefaults({
	    highlight: function(element) {
	        jQuery(element).closest('.form-group').addClass('has-error');
	    },
	    errorElement: 'div',
	    errorClass: 'invalid-feedback',
	});
	
	bsCustomFileInput.init()
	
	
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
	
	$(".objectiveIds").click(function(e){
		
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
	
	$("div.datatable-toolbar").html('<a th:href="@{/role/add}" class="btn btn-outline-success">New</a>');
   
	$('.table-datatable').DataTable({
	    responsive: true,
		language: {
	        url: getDatatableLanguage()
	    }		
	});
	
	$('.table-datatable-no-paginaton').dataTable({
	    "bPaginate": false,
	    responsive: true,
	    language: {
	        url: getDatatableLanguage()
	    }
	});
	
	$('.table-datatable-no-orderable').dataTable({
	   responsive: true,
		"columnDefs": [ {
			"targets": getNoOrderableIndexes(),
			"orderable": false
		}],
		language: {
	        url: getDatatableLanguage()
	    }	
	});
	
	$('.table-datatable-with-checkbox').dataTable({
	   responsive: true,
		columnDefs: [ {
            orderable: false,
            className: 'select-checkbox',
            targets:   0
        } ],
        select: {
            style:    'os',
            selector: 'td:first-child'
        },
        order: [[ 1, 'asc' ]]	
	});
	
	$('.table-datatable-without-search').dataTable( {
	   responsive: true,
	  "searching": false,
	  language: {
	        url: getDatatableLanguage()
	    }
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
		$('.nav-tabs .nav-link:first').tab('show');
	}
	
	if(!isSafari()){
        
        $('.horizontal-scroll').on("mousewheel", function(e) {

            var delta = e.originalEvent.wheelDelta;

            $(this).scrollLeft(this.scrollLeft + (-delta));

            e.preventDefault();
        });
    }
	
	$(".form-get-without-parameters").submit(function(e){
		
		e.preventDefault();
		
		var action = $(this).attr("action");
		
		var value = $(this).find("select").val();
		
		window.location.href = action+value
	});
	
	$( ".checkbox-all-selected input[type='checkbox']" ).prop( "checked", true );
	
	$("form.needs-validation").submit(function(event){
		
		$(this).each(function(i, form){
			
			if (form.checkValidity() === false) {
	          event.preventDefault();
	          event.stopPropagation();
	        }
				
			form.classList.add('was-validated');
		});
	});
	
	$('.boostrap-slider').slider({
		formatter: function(value) {
			return 'Current value: ' + value;
		}
		
	});
	
	
//	$('.optimize-algorithm').change(function (e) {
//		e.preventDefault();
//		
//		var selected = $(this).find(":selected").val();
//		
//		if(selected == "r-nsga-ii"){
//			$(".nav-preferences").removeClass("disabled");
//		} else {
//			$(".nav-preferences").addClass("disabled");
//		}
//		
//		var def = 134400;
//		
//		if(selected == "cor-nsga-ii"){
//			$("#maxEvaluations").val(def/3);
//		}else{
//			$("#maxEvaluations").val(def);
//		}
//		
//		return false;
//	});
	
	$('.rp-objective').change(function (e) {
		e.preventDefault();
		
		var targetId = $(this).data("target-id")
		
		if($(this).is(':checked')){
			$("#"+targetId).val(0.0.toFixed(1));
		}else{
			$("#"+targetId).val(1.0.toFixed(1));
		}
		
		return false;
	});
})