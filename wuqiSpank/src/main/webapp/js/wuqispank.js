function getContextPath()  {
    var base = document.getElementsByTagName('base')[0];
    if (base && base.href && (base.href.length > 0)) {
        base = base.href;
    } else {
        base = document.URL;
    }
    return base.substr(0,
        base.indexOf("/", base.indexOf("/", base.indexOf("//") + 2) + 1));
};



			function fnMessage ( sMesg )
			{
				var n = document.getElementById('info');
				n.innerHTML = sMesg+"<br>";
				n.scrollTop = n.scrollHeight;
			}
			function getText(el) {
			
				if (el.textContent) return el.textContent;
				if (el.innerText) return el.innerText;
				return el.innerHTML.replace(/<[^>]*>/g,'');
			}			
   	function selectText(containerid) {
        if (document.selection) {
            var range = document.body.createTextRange();
            range.moveToElementText(document.getElementById(containerid));
            range.select();
        } else if (window.getSelection) {
            var range = document.createRange();
            range.selectNode(document.getElementById(containerid));
            window.getSelection().addRange(range);
        }
    }			

/**
 * every X seconds, update wuqiSpank server status.
 * Implemented with plain old javascript.
 * url: document.URL + "status",
 */
   	
   	(function doStuff() {
   		myPoll();
   	   setTimeout(doStuff, 2000);
   	}());
   	
   	function myPoll() {
   		$.ajax({
   		    type: "GET",
   		    url: getContextPath() + "/status",
   		    data: { },
   		    success: function(xml) {
   		    
   	     	  var serverDateTime = $(xml).find('WuqiSpankServerDate').first().text();
   	     	  var n = document.getElementById('dateTimeLabel');
   	     	  n.innerHTML = serverDateTime;
   	     	  
   	     	  var inTraceStatus = $(xml).find('InTraceStatus').first()
   	     	  updateInTraceStatus(inTraceStatus);
   	     	  
   	     	  var influxdbStatus = $(xml).find('InfluxdbStatus').first()
   	     	  updateInfluxdbStatus(influxdbStatus);
   	     	  
   	     	  var grafanaStatus = $(xml).find('GrafanaStatus').first()
   	     	  updateGrafanaStatus(grafanaStatus);
   	     	  
   		    }
   		});   		
   	}
   	/**
   	 * <InTraceStatus>
   	 * 	<Healthy/> OR <Sick/>
   	 * 	<Host>localhost</Host>
   	 * 	<Port>9123</Port>
   	 * 	<ServletRequestCount>0</ServletRequestCount>
   	 * 	<InFlightRequestCount>0</InFlightRequestCount>
   	 * 	<EventCount>0</EventCount>
   	 * 	<SqlCount>0</SqlCount>
   	 * </InTraceStatus>
   	 * 
   	 * @param inTraceStatusMsg
   	 */
   	function updateInTraceStatus(inTraceStatusXml) {
   		var sqlCount = $(inTraceStatusXml).find('SqlCount').first().text();
   		var repoSize = $(inTraceStatusXml).find('InMemoryRepositorySize').first().text();//
   		var percentFull = $(inTraceStatusXml).find('PercentFull').first().text();
   		var freeMemory = $(inTraceStatusXml).find('FreeMemoryInMb').first().text();
   		var host = $(inTraceStatusXml).find('Host').first().text();
   		var port = $(inTraceStatusXml).find('Port').first().text();
   		var healthy = $(inTraceStatusXml).find('Healthy')[0];
   		console.log("Found intrace status [" + healthy + "] ["  + typeof healthy + "]");
   		
   		if (healthy) {
   			document.getElementById('intrace_Status').innerHTML = '<img class="health" src="' + getContextPath() + '/images/icon_green_check.png" >';
   		} else {
   			var sick = $(inTraceStatusXml).find('Sick').first();
   			if (sick) {
   				document.getElementById('intrace_Status').innerHTML = '<img class="health" src="' + getContextPath() + '/images/icon_red_x.png" >';
   			} else {
   				document.getElementById('intrace_Status').innerHTML = '??';
   			}
   		}

   		var tooltip = 'ServletRq / Sql / InMem Repo % Full / JVM free mem(mb)'
   		var dispText = '<p title="' + tooltip + '">' + repoSize + ' / ' + sqlCount  + ' / ' + percentFull + '% / ' + freeMemory + 'mb </p>';
	    var n = document.getElementById('intrace_Stats');
   	    n.innerHTML = dispText;

	    document.getElementById('intrace_Host').innerHTML = host;
	    document.getElementById('intrace_Port').innerHTML = port;

   		
   	}
   	function updateInfluxdbStatus(influxdbStatusXml) {
   		var successfulSqlWrites = $(influxdbStatusXml).find('SuccessfulSqlWrites').first().text();
   		var failedSqlWrites = $(influxdbStatusXml).find('FailedSqlWrites').first().text();
   		var tooltip = 'Successful SQL Writes / Failed SQL Writes';
   		var dispText = '<p title="' + tooltip + '">' + successfulSqlWrites + ' / ' + failedSqlWrites + '</p>';
	    var n = document.getElementById('influxdb_Stats');
   	    n.innerHTML = dispText;
   		
   		var host = $(influxdbStatusXml).find('Host').first().text();
   		var port = $(influxdbStatusXml).find('Port').first().text();
	    document.getElementById('influxdb_Host').innerHTML = host;
	    document.getElementById('influxdb_Port').innerHTML = port;

   		var healthy = $(influxdbStatusXml).find('Healthy')[0];
   		if (healthy) {
   			document.getElementById('influxdb_Status').innerHTML = '<img class="health" src="' + getContextPath() + '/images/icon_green_check.png" >';
   		} else {
   			var sick = $(influxdbStatusXml).find('Sick')[0];
   			if (sick) {
   				document.getElementById('influxdb_Status').innerHTML = '<img class="health" src="' + getContextPath() + '/images/icon_red_x.png" >';
   			} else {
   				document.getElementById('influxdb_Status').innerHTML = '??';
   			}
   		}
	    
   	}
   	function updateGrafanaStatus(grafanaStatusXml) {
   		var host = $(grafanaStatusXml).find('Host').first().text();
   		var port = $(grafanaStatusXml).find('Port').first().text();
	    document.getElementById('grafana_Host').innerHTML = host;
	    document.getElementById('grafana_Port').innerHTML = port;
   		
   		var healthy = $(grafanaStatusXml).find('Healthy')[0];
   		console.log("Found grafana status [" + healthy + "]");
   		if (healthy) {
   			document.getElementById('grafana_Status').innerHTML = '<img class="health" src="' + getContextPath() + '/images/icon_green_check.png" >';
   		} else {
   			var sick = $(grafanaStatusXml).find('Sick')[0];
   			if (sick) {
   				document.getElementById('grafana_Status').innerHTML = '<img class="health" src="' + getContextPath() + '/images/icon_red_x.png" >';
   			} else {
   				document.getElementById('grafana_Status').innerHTML = '??';
   			}
   		}
   		
   	}
   	
   	function createStatusTable(){
   	    var tbl  = document.createElement('table');
   	    tbl.style.width  = '100px';
   	    tbl.style.border = '1px solid black';
   	    tbl.style.borderCollapse = 'collapse';

   	    for(var i = 0; i < 3; i++){
   	        var tr = tbl.insertRow();
   	        for(var j = 0; j < 2; j++){
   	            if(i == 2 && j == 1){
   	                break;
   	            } else {
   	                var td = tr.insertCell();
   	                td.appendChild(document.createTextNode('foobar'));
   	                td.style.border = '1px solid black';
   	            }
   	        }
   	    }
   	 var div = document.getElementById('statusParent');
     div.appendChild(tbl);   	    
   	}
