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
		        zoomType: 'y',
		        animation: false
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
			    	return '<b>Solution</b> ' + (this.series.index + 1) + '<br/> <b>Objectives</b><br/>' + this.series.yData.join(",  ");
			    }
			},
			plotOptions: {
		        line: {
		            dataLabels: {
		                enabled: false
		            },
		            states: {
			            hover: {
			            	animation: {
			            		duration: 0
		            		},
			                color: 'red'                                                           
			            }
			        },
		        },
		         
				series:{
					//stickyTracking: false,
					color: '#7cb5ec',
					animation: {
		                duration: 0
		            },
					allowPointSelect: true,
				    point:{
				      events:{
				        select: function(e){
				        	if(that.onClickListener){
				        		that.onClickListener(this.series.index)
				        	}
				        },
				        mouseOver: function () {
				        	this.series.update({
					            color: 'black'
					        });
					    },
					    mouseOut: function () {
					    	var self = this;
                        setTimeout(function() {
							self.series.update({
					            color: '#7cb5ec'
					        });	
					        }, 12);				                              
					     }
				      }
				    }
				  }
		    },
		    xAxis: {
				categories: that.xAxisName
			},	
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
		    },
			series: that.series,
		});
	}
}

