function ScatterChart2D(el){
	
	this.el = el;
	this.yAxisName = "Objective 1";
	this.XAxisName = "Objective 2";
	this.series = []

	this.setYAxisName = function(name){
		this.yAxisName = name
	}

	this.setXAxisName = function(name){
		this.XAxisName = name
	}

	this.addSerie = function(serie){
		this.series.push(serie);
	}
	
	this.plot = function(){
		
		var that = this;

		// we have to sort the data before plot it. As we
		// have a two-dimensional array, we sort it by first position
		$.each(that.series, function(i, serie){
			serie.data.sort(function(a, b){
				return a[0] - b[0]
			});
		});
		
		Highcharts.chart(that.el, {
			title: {
				text: undefined
			},		
			subtitle: {
				text: undefined
			},
			scrollbar: {
				enabled: true
			},		
			yAxis: {
				title: {
					text: that.yAxisName
				},
				min: 0, max: 1,
			},	
			xAxis: {
				title: {
					text: that.XAxisName
				},
				min: 0, max: 1,
			},	
			series: that.series,
		});
	}
}

