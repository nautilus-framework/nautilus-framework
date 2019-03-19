
var summary = null;
var paretoFronts = null;
var correlation = null;

var algorithms = ["nsga-ii", "r-nsga-ii", "nsga-iii", "nautilus-1", "nautilus-2", "nautilus-3"]

function filter(array, index, value){

	var found = new Array();
	
	for(var i in array){
	
		if(array[i][index] == value){
			found.push(array[i]);
		}
	}
	
	return found;
}

function plotObjectives(xLabel, yLabel, series){
	
	Highcharts.chart('canvas-objectives', {
	    chart: {
	        type: 'scatter',
	        zoomType: 'xy'
	    },
	    title: {
	        text: null
	    },
	    xAxis: {
	    	min: 0,
	    	max: 1,
	    	tickInterval: 0.1,
	        title: {
	            text: xLabel
	        }, 
	    },
	    yAxis: {
	    	min: 0,
	    	max: 1,
	    	tickInterval: 0.1,
	        title: {
	            text: yLabel
	        }
	    },
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle',
	        floating: true,
	    },
	    series: series
	});
}

function plotQualityAttributes(found){

	var series = [];
	
	$.each(algorithms, function(i, algorithm){
		
		var entries = filter(found, 0, algorithm);
		
		var values = [];
		
		for(var i in entries){
			values.push(parseFloat(entries[i][3]));
		}
		
		series.push({name:algorithm, data: values});
	});
	
	Highcharts.chart('canvas-qa', {
	    title: {
	        text: null
	    },
	
		xAxis: {
			categories: ['0.1', '0.2', '0.3', '0.4', '0.5', '0.6', '0.7', '0.8', '0.9', '1.0']
		},
		
		yAxis: {
			min: isValueBetweenZeroAndOne()? 0 : null,
			max: isValueBetweenZeroAndOne()? 1 : null
		},
	   
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },
	
	    "series": series,
	});

}

function loadSummary(instance, dimension, callback){
	
	var url = "/spartakus/output-"+dimension+"/"+instance+"/SUMMARY.txt";
	
	$.get(url, function(result){
		
		summary = result.split("\n");
		
		for(var i in summary){
			summary[i] = summary[i].trim().split(" ")
		}
		
		callback();
	})
}

function getResponse(url){
	return $.ajax({type: "GET", url: url, async: false}).responseText;
}

function loadPFKnown(instance, dimension, algorithm, callback){
	
	paretoFronts = [];
	
	for(var i in algorithms){
		
		var url = "/spartakus/output-"+dimension+"/"+instance+"/"+algorithms[i]+"/PFKNOWN.txt";
		
		var response = getResponse(url);
		
		paretoFronts.push({algorithm:algorithms[i], paretoFront: JSON.parse(response)});
	}
}

function loadCorrelation(instance, dimension, callback){
	
	var url = "/spartakus/output-"+dimension+"/"+instance+"/CORRELATION_PFAPPROX_DOT.txt";
	
	correlation = getResponse(url);
}

function getInstance(){
	return $("#instances").val();
}

function getDimension(){
	return $("#dimension").val();
}

function getQualityAttribute(){
	return $("#qa").val();
}

function getXAxisObjective(){
	return {
		value: $("#x-axis-objective").val(),
		text: $("#x-axis-objective option:selected").text()
	};
}

function getYAxisObjective(){
	return {
		value: $("#y-axis-objective").val(),
		text: $("#y-axis-objective option:selected").text()
	};
}

function isValueBetweenZeroAndOne(){
	
	var value = $("#y-range-0-1").val()
	
	if(value == "yes"){
		return true;
	}
	
	return false;
}

function loadQualityAttributeTab(){

	var metric = getQualityAttribute();
	
	var found = filter(summary, 2, metric);
		
	plotQualityAttributes(found)
}

function loadObjectivesTab(){
	
	var x = getXAxisObjective();
	var y = getYAxisObjective();
	
	var series = [];
	
	var index4X = x.value;
	var index4Y = y.value;
	
	for(var i in paretoFronts){
		
		var pf = paretoFronts[i];
		
		var data = [];
		
		$.each(pf.paretoFront.solutions, function(index, solution){
			
			var entry = [
				solution.objectives[index4X],
				solution.objectives[index4Y],
			];
			
			data.push(entry)
		});
		
		series.push({
	        name: pf.algorithm,
	        data: data  
		});
	}
	
	plotObjectives(x.text, y.text, series);
}

function loadCorrelationTab(){
	plotCorrelation(correlation);
}

function reloadAll(){
	
	loadPFKnown(getInstance(), getDimension())
	
	loadCorrelation(getInstance(), getDimension())
	
	loadSummary(getInstance(), getDimension(), function(){
		
		loadQualityAttributeTab();
		loadObjectivesTab();
		loadCorrelationTab();
	});
}

function plotCorrelation(dotText){
	
	 var beforeUnloadMessage = null;
		
	 var resizeEvent = new Event("paneresize");
	  
	 var parser = new DOMParser();
	 var worker;
	 var result;

	 function updateGraph() {
		 if (worker) {
			 worker.terminate();
		 }

	   document.querySelector("#output").classList.add("working");
	   document.querySelector("#output").classList.remove("error");

   worker = new Worker("./spartakus/worker.js");

   worker.onmessage = function(e) {
     document.querySelector("#output").classList.remove("working");
     document.querySelector("#output").classList.remove("error");
     
     result = e.data;
     
     updateOutput();
   }

   worker.onerror = function(e) {
     document.querySelector("#output").classList.remove("working");
     document.querySelector("#output").classList.add("error");
     
     var message = e.message === undefined ? "An error occurred while processing the graph input." : e.message;
     
     var error = document.querySelector("#error");
     while (error.firstChild) {
       error.removeChild(error.firstChild);
     }
     
     document.querySelector("#error").appendChild(document.createTextNode(message));
     
     console.error(e);
     e.preventDefault();
   }
   
   
   var params = {
     src: dotText,
     options: {
       engine: document.querySelector("#engine select").value,
       format: document.querySelector("#format select").value
     }
   };
   
   // Instead of asking for png-image-element directly, which we can't do in a worker,
   // ask for SVG and convert when updating the output.
   
   if (params.options.format == "png-image-element") {
     params.options.format = "svg";
   }
   
   worker.postMessage(params);
 }
 
 function updateOutput() {
   var graph = document.querySelector("#output");

   var svg = graph.querySelector("svg");
   if (svg) {
     graph.removeChild(svg);
   }

   var text = graph.querySelector("#text");
   if (text) {
     graph.removeChild(text);
   }

   var img = graph.querySelector("img");
   if (img) {
     graph.removeChild(img);
   }
   
   if (!result) {
     return;
   }
   
   if (document.querySelector("#format select").value == "svg" && !document.querySelector("#raw input").checked) {
     var svg = parser.parseFromString(result, "image/svg+xml").documentElement;
     svg.id = "svg_output";
     graph.appendChild(svg);

     panZoom = svgPanZoom(svg, {
       zoomEnabled: true,
       controlIconsEnabled: true,
       fit: true,
       center: true,
       minZoom: 0.1
     });

     svg.addEventListener('paneresize', function(e) {
       panZoom.resize();
     }, false);
     window.addEventListener('resize', function(e) {
       panZoom.resize();
     });
   } else if (document.querySelector("#format select").value == "png-image-element") {
     var image = Viz.svgXmlToPngImageElement(result);
     graph.appendChild(image);
   } else {
     var text = document.createElement("div");
     text.id = "text";
     text.appendChild(document.createTextNode(result));
     graph.appendChild(text);
   }
 }
 
 window.addEventListener("beforeunload", function(e) {
   return beforeUnloadMessage;
 });
 
 document.querySelector("#engine select").addEventListener("change", function() {
   updateGraph();
 });

 document.querySelector("#format select").addEventListener("change", function() {
   if (document.querySelector("#format select").value === "svg") {
     document.querySelector("#raw").classList.remove("disabled");
     document.querySelector("#raw input").disabled = false;
   } else {
     document.querySelector("#raw").classList.add("disabled");
     document.querySelector("#raw input").disabled = true;
   }
   
   updateGraph();
 });

 document.querySelector("#raw input").addEventListener("change", function() {
   updateOutput();
 });
 
 updateGraph();
	
	
}


$(function(){
	
	reloadAll();
	
	$('#btn-show').click(function() {
		loadSummary(getInstance(), getDimension(), function(){
			loadQualityAttributeTab();
		});
	});
	
	$("#qa, #y-range-0-1").on('change', function() {
	 	loadQualityAttributeTab();
	});
	
	$('#instances, #dimension').on('change', function() {
		reloadAll();
	});
	
	$('.reload-objective-tab').on('change', function() {
		loadObjectivesTab();
	});
})