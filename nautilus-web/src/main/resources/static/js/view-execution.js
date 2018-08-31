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

function plot(){

	var data = [];

	var table = $("#execution-table tbody");

	table.find('tr').each(function (key, val) {
		
		var point = [];

		$(this).find('td').each(function (key, val) {
			if(key === 0){
				return;
			}

			point.push(parseFloat($(val).text()));
		});

		if(!contains(data, point)){
			data.push(point);
		}
	});

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

	var chart = new ScatterChart2D("execution-chart")

	chart.addSerie(serie)

	chart.plot();
}

$(function(){

    plot();

    $('#execution-table').DataTable();
});