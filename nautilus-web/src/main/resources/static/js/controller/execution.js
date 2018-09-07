var red = 0;
var green = 120;

function normalize(value, a, b, min, max) {
	return a + (((value - min) * (b - a)) / (max - min));
}

function getColor(percent, start, end) {
  
	var a = percent / 100,
     	b = (end - start) * a,
     	c = b + start;

	return 'hsl('+c+', 60%, 50%)';
}

function getColorForDistance(distance, distances, objectives){
	
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

function getEuclideanDistance(s1, s2){
	
	var sum = 0.0;

	for (var i = 0; i < s1.length; i++) {
		sum += Math.pow(s1[i] - s2[i], 2);
	}

	return Math.sqrt(sum);
}


function getObjectivesNames(table){
	
	var header = [];
	
	$(table + " thead .objectives-names").each(function (key, val) {
		header.push($(val).text());
	});
	
	return header;
}

function getObjectivesValues(table){
	
	var data = [];
	
	$(table + " tbody tr").each(function (key, row) {
		
		var point = [];
		
		$(row).find(".objectives-values").each(function (key, val) {
			point.push(parseFloat($(val).text()));
		});
		
		data.push(point);
	});
	
	console.log(data)
	
	return data;
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
			if(index === (row.length - 4)){
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
			
			if(index === 5){
			
				distances.push(parseFloat(col));
			}
		});
	});
	
	return distances;
}

function plot2D(tableHeader, rows){

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
				fillColor: getColorForDistance(distances[index],distances, row)
			},
			tooltip: {
				valueDecimals: 2
			},
			states: {
				hover: {
					lineWidthPlus: 0
				}
			},
			data: [row]
		})
	})
	
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

	var data = getData(rows);
	
	var serie = {
		name: ' ',
        lineWidth: 0,
        showInLegend: false,  
		marker: {
			enabled: true,
			radius: 4
		},
		tooltip: {
			valueDecimals: 2
		},
		states: {
			hover: {
				lineWidthPlus: 0
			}
		},
		data: data
	};
	
	var chart = new ScatterChart3D()

	chart.addSerie(serie)
	chart.setXAxisName(tableHeader[0]);
	chart.setYAxisName(tableHeader[1]);
	chart.setZAxisName(tableHeader[2]);
	chart.setOnClickListener(function(solutionIndex){
		openSolution(solutionIndex);
	})

	chart.plot("execution-chart");
}

function plot4D(tableHeader, rows){

	var data = getData(rows);
	
	var series = [];
	
	$.each(data, function(index, row){
		series.push({
			name: ' ',
	        showInLegend: false,  
			marker: {
				enabled: true,
				radius: 4
			},
			states: {
				hover: {
					lineWidthPlus: 3
				}
			},
			data: row
		})
	})
	
	var chart = new ScatterChart4D()
	
	chart.setSerie(series)
	chart.setXAxisName(tableHeader);
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