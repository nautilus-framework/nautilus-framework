class ScatterChart4D {
  
	constructor() {
		this.yAxisName = "Objective Values";
		this.xAxisName = [];
		this.onClickListener = undefined
		this.series = []
	}
	
	setSeries(series){
		this.series = series;
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
				min: 0, max: 1, tickInterval: 0.1,
			},
			tooltip: {
			    formatter: function() {
			    	return '<b>Solution</b> ' + (this.series.index) + '<br/> <b>Objectives</b><br/>' + this.series.yData.join(",  ");
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
				        		that.onClickListener(this.series.index)
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

