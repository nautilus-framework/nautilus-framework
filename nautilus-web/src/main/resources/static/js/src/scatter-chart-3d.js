class ScatterChart3D {
  
	constructor() {
		this.yAxisName = "Objective 1";
		this.xAxisName = "Objective 2";
		this.zAxisName = "Objective 3";
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
	
	setZAxisName(name){
		this.zAxisName = name;
	}
	
	setOnClickListener(callback){
		this.onClickListener = callback;
	}
	
	plot(el){
		
		var that = this;
		
		var chart = new Highcharts.Chart({
			title: {
				text: undefined
			},	
			chart: {
				renderTo: el,
	            type: 'scatter',
				options3d: {
					enabled: true,
					alpha: 10,
		            beta: 30,
		            depth: 250,
		            viewDistance: 5,
		            fitToPlot: false,
		            frame: {
		                bottom: { size: 1, color: 'rgba(0,0,0,0.02)' },
		                back: { size: 1, color: 'rgba(0,0,0,0.04)' },
		                side: { size: 1, color: 'rgba(0,0,0,0.06)' }
		            }
	            }
	        },
	        legend: {
		        enabled: false
		    },
			tooltip: {
			    formatter: function() {
			    	return '<b>Solution</b> ' + (this.series.index) + '<br/> <b>Objectives</b><br/>' + this.point.options.x +", "+ this.point.options.y +", "+ this.point.options.z;
			    }
			},
			plotOptions: {
		        scatter: {
		            width: 10,
		            height: 10,
		            depth: 10
		        },
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
			zAxis: {
				title: {
					text: that.zAxisName
				},
				min: 0, max: 1,tickInterval: 0.1,
			},
			series: that.series,
		});
		
		// Add mouse and touch events for rotation
		(function (H) {
		    function dragStart(eStart) {
		        eStart = chart.pointer.normalize(eStart);

		        var posX = eStart.chartX,
		            posY = eStart.chartY,
		            alpha = chart.options.chart.options3d.alpha,
		            beta = chart.options.chart.options3d.beta,
		            sensitivity = 5; // lower is more sensitive

		        function drag(e) {
		            // Get e.chartX and e.chartY
		            e = chart.pointer.normalize(e);

		            chart.update({
		                chart: {
		                    options3d: {
		                        alpha: alpha + (e.chartY - posY) / sensitivity,
		                        beta: beta + (posX - e.chartX) / sensitivity
		                    }
		                }
		            }, undefined, undefined, false);
		        }

		        chart.unbindDragMouse = H.addEvent(document, 'mousemove', drag);
		        chart.unbindDragTouch = H.addEvent(document, 'touchmove', drag);

		        H.addEvent(document, 'mouseup', chart.unbindDragMouse);
		        H.addEvent(document, 'touchend', chart.unbindDragTouch);
		    }
		    H.addEvent(chart.container, 'mousedown', dragStart);
		    H.addEvent(chart.container, 'touchstart', dragStart);
		}(Highcharts));
	}
}

