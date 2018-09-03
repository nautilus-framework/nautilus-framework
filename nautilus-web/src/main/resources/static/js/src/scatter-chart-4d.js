class ScatterChart4D {
  
	constructor() {
		this.yAxisName = "Objective Values";
		this.xAxisName = [];
		this.onClickListener = undefined
		this.series = []
	}
	
	setSerie(serie){
		this.series = serie;
	}
	
	setXAxisName(name){
		this.xAxisName = name;
	}
	
	setOnClickListener(callback){
		this.onClickListener = callback;
	}
	
	plot(el){
		
		var that = this;
		
		Highcharts.chart(el, {
			chart: {
		        type: 'line',
		        zoomType: 'y'
		    },
			title: {
				text: undefined
			},		
			subtitle: {
				text: undefined
			},
			scrollbar: {
//				enabled: true
			},		
			yAxis: {
				title: {
					text: that.yAxisName
				},
				min: 0, max: 1, tickInterval: 0.1,
			},
			tooltip: {
			    formatter: function() {
			    	return '<b>Solution</b> ' + (this.series.index + 1) + '<br/> <b>Objectives</b><br/>' + this.series.yData.join(",  ");
			    }
			},
			boost: {
		        useGPUTranslations: true,
		        usePreAllocated: true
		    },
			plotOptions: {
		        line: {
		            dataLabels: {
		                enabled: false
		            },
		        },
				series:{
					  allowPointSelect: true,
				    point:{
				      events:{
				        select: function(e){
				        	if(that.onClickListener){
				        		that.onClickListener(this.series.index)
				        	}
				        }
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

