var red = 0;
var green = 100;

function isColorize(){
	return $("input[name='colorize']").is(":checked")
}

function showLines(){
	return $("input[name='showLines']").is(":checked")
}

function normalize(value, a, b, min, max) {
	return a + (((value - min) * (b - a)) / (max - min));
}

function getColor(percent, start, end) {
  
	var a = percent / 100,
     	b = (end - start) * a,
     	c = b + start;

	return 'hsl('+c+', 60%, 50%)';
}

function getColorForDistance(distance, distances){
	
	if(!isColorize()){
		return "#7cb5ec";
	}
	
	var maxDistance = distances.reduce(function(a, b) {
	    return Math.max(a.toFixed(5), b.toFixed(5));
	});
	var minDistance = distances.reduce(function(a, b) {
	    return Math.min(a.toFixed(5), b.toFixed(5));
	});
	
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

function getSeries(rows, lineWidthPlus){
	
	var data = getData(rows);
	var distances = getDistances(rows);
	
	var series = [];
	
	$.each(data, function(index, row){
		series.push({
			name: ' ',
	        lineWidth: 0,
	        showInLegend: false,
	        animation: {
                duration: 0
            },
			marker: {
				symbol: "circle",
				enabled: true,
				radius: 4,
				fillColor: getColorForDistance(distances[index],distances)
			},
			states: {
				hover: {
					lineWidthPlus: lineWidthPlus
				}
			},
			data: [row]
		})
	})
	
	return series
}

function plot2D(tableHeader, rows){

	var series = getSeries(rows, 0);
	
	var chart = new ScatterChart2D()

	chart.addSerie(series)
	chart.setXAxisName(tableHeader[0]);
	chart.setYAxisName(tableHeader[1]);
	chart.setOnClickListener(function(solutionIndex){
		openSolution(solutionIndex);
	})

	chart.plot("execution-chart");
}

function plot3D(tableHeader, rows){

	var series = getSeries(rows, 0);
	
	var chart = new ScatterChart3D()

	chart.addSerie(series)
	chart.setXAxisName(tableHeader[0]);
	chart.setYAxisName(tableHeader[1]);
	chart.setZAxisName(tableHeader[2]);
	chart.setOnClickListener(function(solutionIndex){
		openSolution(solutionIndex);
	})

	chart.plot("execution-chart");
}

function plot4D(tableHeader, rows){

	var chart = new ScatterChart4D()
	
	var data = getData(rows);
	var distances = getDistances(rows);
	
	var series = [];
	
	$.each(data, function(index, row){
		
		series.push({
			name: ' ',
			lineWidth: showLines()? 1 : 0,
	        showInLegend: false,
	        animation: {
                duration: 0
            },
            color: getColorForDistance(distances[index],distances),
			marker: {
				symbol: "circle",
				enabled: true,
				radius: 4,
				fillColor: getColorForDistance(distances[index],distances)
			},
			states: {
				hover: {
					lineWidthPlus: 3
				}
			},
			data: row
		})
	})
	
	chart.setSeries(series)
	chart.setXAxisName(tableHeader)
	chart.setOnClickListener(function(solutionIndex){
		openSolution(solutionIndex);
	})
	
	chart.plot("execution-chart");
}

function openSolution(solutionIndex){
	
	var url = window.location.href.split("/");
	
	var executionId = url[url.length-1]
	
	window.location.href = "/solution/" + executionId+ "/" + solutionIndex;
}

$(function(){

	var table = $('#execution-table').DataTable();
	
	var tableHeader = getObjectivesNames("#execution-table");
	
	var rows = table.rows().data();
	
	if(tableHeader.length == 1){
		
	} else if(tableHeader.length == 2){
		plot2D(tableHeader, rows)
	}else if(tableHeader.length == 3){
		plot3D(tableHeader, rows)
	}else{
		plot4D(tableHeader, rows)
	}
});