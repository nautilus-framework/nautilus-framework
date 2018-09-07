function contains(data, point){
	
	for(var i = 0; i < data.length; i++){
		
		var contains = true;
		
		for(var j = 0; j < data[i].length; j++){
			
			if(data[i][j] != point[j]){
				contains = false
			}
		}

		if(contains){
			return true;
		}
	}
	
	return false;
}

function getTableHeader(table){
	
	var header = [];
	
	var table = $(table + " thead");

	table.find('tr').each(function (key, val) {
		
		var size = $(this).find('th').length;
		
		$(this).find('th').each(function (key, val) {
			if(key === 0){
				return;
			}
			
			if(key === (size - 1)){
				return;
			}
			if(key === (size - 2)){
				return;
			}
			if(key === (size - 3)){
				return;
			}

			header.push($(val).text());
		});
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
		
		//if(!contains(data, point)){
			data.push(point);
		//}
	});
	
	return data;
}

function plot2D(tableHeader, rows){

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
	
	var chart = new ScatterChart2D()

	chart.addSerie(serie)
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
	var tableHeader = getTableHeader("#execution-table");
	
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