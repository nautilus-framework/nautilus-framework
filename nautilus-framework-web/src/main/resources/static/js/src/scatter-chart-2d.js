class ScatterChart2D {
  
	constructor() {
		this.yAxisName = "Objective 1";
		this.xAxisName = "Objective 2";
		this.onClickListener = undefined
		this.series = [];
		this.solutionIndexes = [];
		this.min = 0;
		this.max = 1;
	}
	
	addSerie(serie){
		this.series = serie;
	}
	
	setXAxisName(name){
		this.xAxisName = name;
	}
	
	setSolutionIndexes(solutionIndexes){
		this.solutionIndexes = solutionIndexes;
	}
	
	setYAxisName(name){
		this.yAxisName = name;
	}
	
	setOnClickListener(callback){
		this.onClickListener = callback;
	}
	
	plot(el){
		
		var that = this;
		
		Highcharts.chart(el, {
			chart: {
		        zoomType: 'xy',
		    },
			title: {
				text: undefined
			},		
			legend: {
		        enabled: false
		    },
			tooltip: {
			    formatter: function() {
			    	
			    	var solutionIndex = that.solutionIndexes[this.series.index];
			    	
			    	return '<b>Solution</b> ' + (solutionIndex) + '<br/> <b>Objectives</b><br/>' + this.point.options.x +", "+ this.point.options.y;
			    }
			},
			plotOptions:{
			  series:{
			  	marker: {
					symbol: "circle",
					enabled: true,
					radius: 4,
				},
				allowPointSelect: true,
			    point:{
			      events:{
			    	  click: function(e){
				        	if(that.onClickListener){
				        		
				        		var solutionIndex = that.solutionIndexes[this.series.index];
				        		
				        		that.onClickListener(solutionIndex)
				        	}
				        }
			      }
			    }
			  }
			},
			yAxis: {
				title: {
					text: that.yAxisName
				},
				min: that.min, 
				max: that.max, 
				tickInterval: 0.1,
			},	
			xAxis: {
				title: {
					text: that.xAxisName
				},
				min: that.min, 
				max: that.max,
				tickInterval: 0.1,
			},	
			series: that.series,
		});
	}
}

