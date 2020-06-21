
function plot(objectiveIds, series){
	
	Highcharts.chart('chart', {
		
		chart: {
	        type: 'line',
	        zoomType: 'xy',
	    },
	    title: {
			text: undefined
		},	
	    yAxis: {
	        title: {
	            text: 'Objective Values'
	        },
		    min: 0, 
			max: 1, 
			tickInterval: 0.1,
	    },
	    xAxis: {
			categories: objectiveIds
		},
	    legend: {
	        layout: 'horizontal',
	        align: 'center',
	        verticalAlign: 'bottom'
	    },

	    series: series,

	    responsive: {
	        rules: [{
	            condition: {
	                maxWidth: 500
	            },
	            chartOptions: {
	                legend: {
	                    layout: 'horizontal',
	                    align: 'center',
	                    verticalAlign: 'bottom'
	                }
	            }
	        }]
	    }

	});
}

$(function(){
   
	var executions = $("#data").text();
	
	var objectiveIds = $("#objectiveIds").text();
	
	objectiveIds = JSON.parse(objectiveIds)
	executions = JSON.parse(executions);
	
	var series = [];
	//alert("oi")
	for (var i = 0; i < executions.length; i++){
		
		var execution = executions[i];
		
		var solutions = execution.solutions;
		
		var name = execution.algorithmId;
		
		var serie = {
			name: name,
			data: []
		};
		for (var j = 0; j < solutions.length; j++){
			
			var id = solutions[j].attributes.id;
			
			serie.data.push(solutions[j].objectives);
		    
		}
		console.log(serie)
		
		series.push(serie);
	}
	
	plot(objectiveIds, series);	
})
