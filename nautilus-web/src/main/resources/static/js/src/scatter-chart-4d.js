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
	
	appendSeries(serie){
		this.highcharts.addSeries(serie, false);
	}
	
	redraw(){
		this.highcharts.redraw()
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
			subtitle: {
				text: undefined
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
//			boost: {
//		        useGPUTranslations: true,
//		        usePreAllocated: true
//		    },
			plotOptions: {
//		        line: {
//		            dataLabels: {
//		                enabled: false
//		            },
////		            states: {
////			            hover: {
////			            	animation: {
////			            		duration: 0
////		            		},
////			                color: 'red'                                                           
////			            }
////			        },
//		        },
		         
				series:{
//					color: '#7cb5ec',
//					animation: {
//		                duration: 0
//		            },
					allowPointSelect: true,
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

