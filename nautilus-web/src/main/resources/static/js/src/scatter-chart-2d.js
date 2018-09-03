class ScatterChart2D {
  
	constructor() {
		this.yAxisName = "Objective 1";
		this.xAxisName = "Objective 2";
		this.onClickListener = undefined
		this.series = []
	}
	
	addSerie(serie){
		this.series.push(serie);
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

//		// we have to sort the data before plot it. As we
//		// have a two-dimensional array, we sort it by first position
//		$.each(that.series, function(i, serie){
//			serie.data.sort(function(a, b){
//				return a[0] - b[0]
//			});
//		});
		
		
		Highcharts.chart(el, {
			title: {
				text: undefined
			},		
			subtitle: {
				text: undefined
			},
			scrollbar: {
//				enabled: true
			},	
			tooltip: {
			    formatter: function() {
			    	return '<b>Solution</b> ' + (this.point.index + 1) + '<br/> <b>Objectives</b><br/>' + this.point.options.x +", "+ this.point.options.y;
			    }
			},
			plotOptions:{
			  series:{
				  allowPointSelect: true,
			    point:{
			      events:{
			    	  click: function(e){
				        	if(that.onClickListener){
				        		that.onClickListener(e.point.index)
				        	}
				        }
			      }
			    }
			  }
			},
			boost: {
		        useGPUTranslations: true,
		        usePreAllocated: true
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

