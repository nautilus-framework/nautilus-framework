var red = 0;
var green = 100;

function getColorize(){
	return $("#colorize").val();
}

function showLines(){
	return $("input[name='showLines']").is(":checked")
}

function normalize(value, a, b, min, max) {
	return a + (((value - min) * (b - a)) / (max - min));
}

function getColor(percent, start, end) {
  
  	console.log()
  
	var a = percent / 100,
     	b = (end - start) * a,
     	c = b + start;
	
	return tinycolor('hsl('+c+', 60, 50)').toHexString() ;
}

function getColorForDistance(distance, minDistance, maxDistance){
	
	distance = isNaN(distance)? 0.0 : distance; 
	minDistance = isNaN(minDistance)? 0.0 : minDistance; 
	maxDistance = isNaN(maxDistance)? 0.0 : maxDistance; 
	
	if(getColorize() == 0){
		return "#7cb5ec";
	}
	
	if(minDistance === maxDistance){
		return "#7cb5ec";
	}
	
	var normal = normalize(distance.toFixed(2), 0,100, minDistance, maxDistance) ;
	
	return getColor(normal, green, red);
}

function getObjectivesNames(table){
	
	var header = [];
	
	$(table + " thead .objectives-names").each(function (key, val) {
		header.push($(val).text());
	});
	
	return header;
}

function getData(rows){
	
	var data = [];

	$.each(rows, function(index, row){
		
		var point = [];
		
		$.each(row, function(index, col){
			if(index === 0){
				return;
			}
			if(index === (row.length - 1)){
				return;
			}
			if(index === (row.length - 2)){
				return;
			}
			if(index === (row.length - 3)){
				return;
			}
			point.push(parseFloat(col));
		});
		
		data.push(point);
	});
	
	return data;
}

function getDistances(rows){
	
	var distances = [];
	
	$.each(rows, function(index, row){
		
		$.each(row, function(index, col){
			
			if(index === row.length - 1){
			
				distances.push(parseFloat(col));
			}
		});
	});
	
	return distances;
}

function getSolutionIndexes(rows){
	
	var indexes = [];
	
	$.each(rows, function(i, row){
		
		$.each(row, function(j, col){
			
			if(j == 0){
				indexes.push(parseFloat($(col).text()));
			}
		});
	});
	
	return indexes;
}

function getSeries(rows, lineWidthPlus){
	
	var data = getData(rows);
	var distances = getDistances(rows);
	
	var maxDistance = distances.reduce(function(a, b) {
	    return Math.max(a.toFixed(5), b.toFixed(5));
	});
	
	var minDistance = distances.reduce(function(a, b) {
	    return Math.min(a.toFixed(5), b.toFixed(5));
	});
	
	var series = [];
	
	$.each(data, function(index, row){
		series.push({
			lineWidth: 0,
	        marker: {
				fillColor: getColorForDistance(distances[index], minDistance, maxDistance)
			},
			data: [row]
		})
	})
	
	return series
}

function plot2D(tableHeader, rows, normalize){

	var series = getSeries(rows, 0);
	
	var chart = new ScatterChart2D()

	chart.setSolutionIndexes(getSolutionIndexes(rows));
	chart.addSerie(series)
	chart.setXAxisName(tableHeader[0]);
	chart.setYAxisName(tableHeader[1]);
	chart.setOnClickListener(function(solutionIndex){
		openSolution(solutionIndex);
	})
	
	if(!normalize){
		chart.min = chart.max = null;
	}

	chart.plot("execution-chart");
}

function plot3D(tableHeader, rows, normalize){

	var series = getSeries(rows, 0);
	
	var chart = new ScatterChart3D()

	chart.setSolutionIndexes(getSolutionIndexes(rows));
	chart.addSerie(series)
	chart.setXAxisName(tableHeader[0]);
	chart.setYAxisName(tableHeader[1]);
	chart.setZAxisName(tableHeader[2]);
	chart.setOnClickListener(function(solutionIndex){
		openSolution(solutionIndex);
	})
	
	if(!normalize){
		chart.min = chart.max = null;
	}

	chart.plot("execution-chart");
}

function plot4D(tableHeader, rows, normalize){

	var data = getData(rows);
	
	var distances = getDistances(rows);
	
	var maxDistance = distances.reduce(function(a, b) {
	    return Math.max(a.toFixed(5), b.toFixed(5));
	});
	var minDistance = distances.reduce(function(a, b) {
	    return Math.min(a.toFixed(5), b.toFixed(5));
	});
	
	var series = [];
	
	$.each(data, function(index, row){
		
		var color = getColorForDistance(distances[index], minDistance, maxDistance);
		
		var serie = {
			lineWidth: showLines()? 1 : 0,
	        color: color,
			marker: {
				fillColor: color
			},
			data: row
		}
		
		series.push(serie);
	})
	
	var chart = new ScatterChart4D()
	
	chart.setSolutionIndexes(getSolutionIndexes(rows));
	chart.setXAxisName(tableHeader)
	chart.setSeries(series)
	chart.setOnClickListener(function(solutionIndex){
		openSolution(solutionIndex);
	})
	
	if(!normalize){
		chart.min = chart.max = null;
	}
	
	chart.plot("execution-chart");
}

function openSolution(solutionIndex){

	var hash = window.location.hash;
	
	var url = window.location.href.split("/");
	
	var executionId = url[url.length-1]
	
	executionId = executionId.replace(hash,'');
	
	window.location.href = "/solution/" + executionId+ "/" + solutionIndex;
}

$(function(){
	
	var table = $('#execution-table').DataTable();
	
	var tableHeader = getObjectivesNames("#execution-table");
	
	var rows = table.rows().data();
	var normalize = $("#normalize").val() == "don-t-normalize" ? false : true;
	
	if(tableHeader.length == 1){
		
	} else if(tableHeader.length == 2){
		plot2D(tableHeader, rows, normalize)
	}else if(tableHeader.length == 3){
		plot3D(tableHeader, rows, normalize)
	}else{
		plot4D(tableHeader, rows, normalize)
	}
});