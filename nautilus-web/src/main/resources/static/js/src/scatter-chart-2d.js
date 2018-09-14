class ScatterChart2D {
  
	constructor() {
		this.yAxisName = "Objective 1";
		this.xAxisName = "Objective 2";
		this.onClickListener = undefined
		this.series = []
	}
	
	addSerie(serie){
		this.series = serie;
	}
	
	setXAxisName(name){
		this.xAxisName = name;
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
			    	return '<b>Solution</b> ' + (this.series.index) + '<br/> <b>Objectives</b><br/>' + this.point.options.x +", "+ this.point.options.y;
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
				        		that.onClickListener(this.series.index)
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
				min: 0, max: 1,tickInterval: 0.1,
			},	
			xAxis: {
				title: {
					text: that.xAxisName
				},
				min: 0, max: 1,tickInterval: 0.1,
			},	
			series: that.series,
		});
	}
}

