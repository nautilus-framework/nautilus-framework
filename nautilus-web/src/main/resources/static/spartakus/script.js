
var summary = null;

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

function getByQualityAttribute(key){
	return filter(summary, 2, key);
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
	   
	    legend: {
	        layout: 'vertical',
	        align: 'right',
	        verticalAlign: 'middle'
	    },
	
	    "series": series,
	});

}

function loadInstance(instance, dimension, callback){
	
	var url = "/spartakus/output-"+dimension+"/"+instance+"/SUMMARY.txt";
	
	$.get(url, function(result){
		
		summary = result.split("\n");
		
		for(var i in summary){
			summary[i] = summary[i].trim().split(" ")
		}
		
		callback();
	})
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

function loadQualityAttributeTab(){

	var metric = getQualityAttribute();
	
	var found = getByQualityAttribute(metric)
		
	plotQualityAttributes(found)
}

function reloadAll(){
	loadInstance(getInstance(), getDimension(), function(){
		
		loadQualityAttributeTab();
	});
}

$(function(){
	
	reloadAll();
	
	$('#btn-show').click(function() {
		loadInstance(getInstance(), getDimension(), function(){
			loadQualityAttributeTab();
		});
	});
	
	$('#qa').on('change', function() {
	 	loadQualityAttributeTab();
	});
	
	$('#instances').on('change', function() {
		reloadAll();
	});
})