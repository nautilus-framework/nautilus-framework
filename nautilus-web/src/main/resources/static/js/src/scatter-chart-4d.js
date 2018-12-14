class ScatterChart4D {
  
	constructor() {
		this.yAxisName = "Objective Values";
		this.xAxisName = [];
		this.onClickListener = undefined
		this.series = []
		this.solutionIndexes = [];
		this.min = 0;
		this.max = 1;
	}
	
	setSeries(series){
		this.series = series;
	}
	
	setSolutionIndexes(solutionIndexes){
		this.solutionIndexes = solutionIndexes;
	}
	
	setXAxisName(name){
		this.xAxisName = name;
	}
	
	setOnClickListener(callback){
		this.onClickListener = callback;
	}
	
	plot(el){
		
		var that = this;
		
		this.highcharts = Highcharts.chart(el, {
			chart: {
		        type: 'line',
		        zoomType: 'xy',
		    },
			title: {
				text: undefined
			},		
			legend: {
		        enabled: false
		    },
			yAxis: {
				title: {
					text: that.yAxisName
				},
				min: that.min, 
				max: that.max, 
				tickInterval: 0.1,
			},
			tooltip: {
			    formatter: function() {
			    	
			    	var solutionIndex = that.solutionIndexes[this.series.index];
			    	
			    	return '<b>Solution</b> ' + (solutionIndex) + '<br/> <b>Objectives</b><br/>' + this.series.yData.join(",  ");
			    }
			},
			plotOptions: {
				series:{
					marker: {
						symbol: "circle",
						enabled: true,
						radius: 4,
					},
					allowPointSelect: true,
					states: {
						hover: {
							lineWidthPlus: 3
						}
					},
				    point:{
				      events:{
				        select: function(e){
				        	if(that.onClickListener){
				        		
				        		var solutionIndex = that.solutionIndexes[this.series.index];
				        		
				        		that.onClickListener(solutionIndex)
				        	}
				        },
				      }
				    }
				  }
		    },
		    xAxis: {
				categories: that.xAxisName
			},	
			 
			series: that.series,
		});
	}
}

