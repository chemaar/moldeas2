//Pre-load of code list: FIXME: optimize maps!
var codeArray= [];
var codeLabelArray= [];
var codeLabelMap= {};
var labelCodeMap= {};
var nutsLabelArray= [];
var nutsLabelMap= {};
var labelNutsMap= {};

$(document).ready(function () {  
	codeList();
	nutsCodeList();
	$("#loading").css("display", "inline");
	$("#loadingNuts").css("display", "inline");
	$("#searching").css("display", "none");
	$("#mainContent").css("display", "none");
/**
	$("#slider-range").slider({
			range: true,
			min: 0,
			max: 500,
			values: [75, 300],
			slide: function(event, ui) {
				$("#amount").val(ui.values[0] + ' € - ' + ui.values[1]+' €');
				search();
			}
		});
		
	$("#amount").val($("#slider-range").slider("values", 0) + ' € - ' + $("#slider-range").slider("values", 1)+' €');
*/
/**
 	$("#slider-range-duration").slider({
			range: true,
			min: 1,
			max: 50,
			values: [5, 15],
			slide: function(event, ui) {
				$("#duration").val(ui.values[0] + ' - ' + ui.values[1]+' ');
				search();
			}
		});
		
	$("#duration").val($("#slider-range-duration").slider("values", 0) + ' - ' + $("#slider-range-duration").slider("values", 1)+'');
 */
  	$("#slider-range-years").slider({
			range: true,
			min: 2008,
			max: 2011,
			values: [2009, 2010],
			slide: function(event, ui) {
				$("#years").val(ui.values[0] + ' - ' + ui.values[1]+' ');
				search();
			}
		});
		
	$("#years").val($("#slider-range-years").slider("values", 0) + ' - ' + $("#slider-range-years").slider("values", 1)+'');


	$("#bnt_lookup_codes").click(function(){
		$('#cpvCodes').append(new Option( $('#txt_buscador').get(0).value,  $('#txt_buscador').get(0).value, true, true));
		$("#cpvCodes").css("display", "inline");
			search();

	 });

	$("#bnt_remove_codes").click(function(){
		$('#cpvCodes :selected').remove();
		if ($('#cpvCodes option').size() == 0 ){
			$("#cpvCodes").css("display", "none");
		}else{
			search();
		}
	 });


	$("#bnt_lookup_nuts_codes").click(function(){
		$('#nutsCodes').append(new Option( $('#txt_buscador_nuts').get(0).value,  $('#txt_buscador_nuts').get(0).value, true, true));
		$("#nutsCodes").css("display", "inline");
		search();

	 });

	$("#bnt_remove_nuts_codes").click(function(){
		$('#nutsCodes :selected').remove();
		if ($('#nutsCodes option').size() == 0 ){
			$("#nutsCodes").css("display", "none");
		}
		search();
		
	 });



	$("input[name='company']").change(function(){
	  	search();
	});



 	/*$('#suggetionsList').hide();
 	providers();
 	$('#txt_buscador').click(function () {  this.value = "";});*/

}); 


function codeList(){
	$.ajax( {
	type:'GET',
	url:'http://localhost:9000/moldeas-web/services/moldeas/psc/listCodes',
	dataType: "xml",
	contentType: "application/xml",
	success: GetCodeOk
	});
}

function nutsCodeList(){
	$.ajax( {
	type:'GET',
	url:'http://localhost:9000/moldeas-web/services/moldeas/psc/listNuts',
	dataType: "xml",
	contentType: "application/xml",
	success: GetNutsCodeOk
	});
}

function GetNutsCodeOk(data){
	$("#loadingNuts").css("display", "none");
	$("#mainContent").css("display", "inline");
	var codeNumber = data.getElementsByTagName("code").length;
	for(i=0; i<codeNumber; i++){
		//Code pre-load
		var id = data.getElementsByTagName("code")[i].firstChild.nodeValue;
		var label = data.getElementsByTagName("label")[i].firstChild.nodeValue;
		nutsLabelArray[i]=label;
		nutsLabelMap[label] = id;
		labelNutsMap [id] = label;
	}
}

function GetCodeOk(data){
	$("#loading").css("display", "none");
	$("#mainContent").css("display", "inline");
	var codeNumber = data.getElementsByTagName("id").length;
	for(i=0; i<codeNumber; i++){
		//Code pre-load
		var id = data.getElementsByTagName("id")[i].firstChild.nodeValue;
		var label = data.getElementsByTagName("prefLabel")[i].firstChild.nodeValue;
		codeArray[i]=id;
		codeLabelArray[i]=label;
		codeLabelMap[label] = id;
		labelCodeMap[id] = label;
	}
}


function suggestion (){
	var str= $('#txt_buscador').get(0).value;
	var typeInputCPV = $("input[@name='cpvTextCode']:checked").val();
	var filtered = [];
	if (typeInputCPV == "text"){
		filtered  = codeLabelArray.filter(function (n){ return (n.match("^"+str)==str);});		
	}else{
		filtered  = codeArray.filter(function (n){ return (n.match("^"+str)==str);});
	}
	$('#suggestionsList').html();
	
	var suggestNumber = filtered.length;
	 
	 if(suggestNumber>0 && $('#txt_buscador').get(0).value!=""){
		//delete contents of the tag
	 	$("#suggestionsList").html("");
		
	 	//enter dynamic content
		var texto ="<ul>";
		for(i=0; i<suggestNumber; i++){
			texto +="<li class='tag'>"+filtered[i]+"</li>"
		}
		texto +="</ul>";
		
		//visual effect to show Suggestions
		$("#suggestionsList").html(texto);
		if(!$('#suggestionsList').is(":visible")){
			$('#suggestionsList').hide().delay(50).slideDown();	
		}
		
		//actions taken when you click on a suggestion
		$("li.tag").click(function () {  
			  $('#txt_buscador').get(0).value = this.innerHTML;
			  $('#suggestionsList').delay(50).slideUp(); 
			  $("#bnt_lookup_codes").css("display", "inline");
			  $("#bnt_remove_codes").css("display", "inline");

		});	
	 }
}

function suggestionNutsCodes (){
	var str= $('#txt_buscador_nuts').get(0).value;
	var typeInputNUTS = $("input[@name=nutsTextCode]:checked").val();
	var filtered = nutsLabelArray.filter(function (n){ return (n.match("^"+str)==str);});		
	$('#nutsCodesList').html();
	
	var suggestNumber = filtered.length;
	 
	 if(suggestNumber>0 && $('#txt_buscador_nuts').get(0).value!=""){
		//delete contents of the tag
	 	$("#nutsCodesList").html("");
		
	 	//enter dynamic content
		var texto ="<ul>";
		for(i=0; i<suggestNumber; i++){
			texto +="<li class='tag'>"+filtered[i]+"</li>"
		}
		texto +="</ul>";
		
		//visual effect to show Suggestions
		$("#nutsCodesList").html(texto);
		if(!$('#nutsCodesList').is(":visible")){
			$('#nutsCodesList').hide().delay(50).slideDown();	
		}
		
		//actions taken when you click on a suggestion
		$("li.tag").click(function () {  
			  $('#txt_buscador_nuts').get(0).value = this.innerHTML;
			  $('#nutsCodesList').delay(50).slideUp(); 
			  $("#bnt_lookup_nuts_codes").css("display", "inline");
			  $("#bnt_remove_nuts_codes").css("display", "inline");
		});	
	 }
}




function GetQueryOK(data){
	var query = data.getElementsByTagName("query")[0].firstChild.nodeValue;
	$('.button').CreateBubblePopup({
		selectable: false,
		align	 : 'center',
		innerHtml: '',
		innerHtmlStyle: {color:'#FFFFFF', 'text-align':'center'},
		themeName: 	'all-grey', 
		themePath: 	'images/jquerybubblepopup-theme'
		});
}

function search(){

	if ($('#cpvCodes option').size() != 0 ){			
		$("#searching").css("display", "inline");
		var idCpv = $('#txt_buscador').get(0).value;
		var cpvCodes = "";
		$("#cpvCodes :selected").each(function(i, selected){ 
		  var value =  $(selected).text();
		  if(value.match("[^0-9]") != null){
			//Description
		 	value = codeLabelMap[$(selected).text()];
		  }
		  cpvCodes = cpvCodes + value +","; 
		});	
		var nutsCodes = "";
		$("#nutsCodes :selected").each(function(i, selected){ 	
		  //nutsLabelMap[name] = code ; nutsLabelMap["FRANCE"] = FR
		  nutsCodes = nutsCodes + nutsLabelMap[$(selected).text()] +","; 
		});
		var minYear = $("#slider-range-years").slider("values", 0) ;
		var maxYear = $("#slider-range-years").slider("values", 1) ;
		var minCost = "0" ;//$("#slider-range").slider("values", 0);
		var maxCost = "0" ;//$("#slider-range").slider("values", 1);
		var minDuration = "0";// $("#slider-range-duration").slider("values", 0);
		var maxDuration = "0";// $("#slider-range-duration").slider("values", 1);
		var typeEnterprise = "SME";// $('input[@name="company"]:checked').val(); //FIXME: get the value of first radio group
		$.ajax( {
		type:'GET',
		url:'http://localhost:9000/moldeas-web/services/moldeas/search?cpvCodes='+cpvCodes+'&nutsCodes='+nutsCodes+'&minYear='+minYear+'&maxYear='+maxYear+'&minCost='+minCost+'&maxCost='+maxCost+'&minDuration='+minDuration+'&maxDuration='+maxDuration+'&typeEnterprise='+typeEnterprise,
		dataType: "xml",
		contentType: "application/xml",
		success: GetSearchOk
		});
	}
}


function GetSearchOk(data){
	var resultNumber = data.getElementsByTagName("scoredPnnsTO").length;
	var time = data.getElementsByTagName("time")[0].firstChild.nodeValue;
	 $("#results").html("<p>Results: "+resultNumber+", Time: "+(time/1000)+" sec.</p>");	 
	 if(resultNumber>0 && $('#txt_buscador').get(0).value!=""){
		var regionNumberOfPPNs= new Array();
		var dataResults=[];
		var jsonData = "\"items\" : [";
		for(i=0; i<resultNumber; i++){		
			//Generate row: ID, YEAR, URI, CPV Codes, NUTS Codes, Score
			//   {   id :                  "2222",
           		//       year :          "2009",
	   		//      cpvCode :                  ["1000000"],
			//    label :                 ["Services", "More services"],
			//    description :                 ["Services", "More services"],
			//    location :              ["Spain"],
			//    score :          "2",
			//    latLng:  ['37.585241,-77.497343']		
       			// },
			
			var scoredPPNTO=data.getElementsByTagName("scoredPnnsTO")[i];
			var ppnTO=scoredPPNTO.getElementsByTagName("ppnTO")[0];
			var cpvCodes=ppnTO.getElementsByTagName("pscCodes"); 
			var nutsCodes=ppnTO.getElementsByTagName("nutsCodes"); 
			var listCPVCodes = "<ul class=\"results\">";
			
			var cpvCodeIds = "";
			var cpvCodeLabels = "";
			for(j=0; j<cpvCodes.length;j++){
				var url = cpvCodes[j].getElementsByTagName("uri")[0].firstChild.nodeValue;
				var code = url.substring(url.lastIndexOf("/")+1,url.length);	
				var description = labelCodeMap[code];
				listCPVCodes += "<li><a href='"+url+"'>"+description+"</a></li>";
				cpvCodeIds += "\""+code+"\"";
				cpvCodeLabels += "\""+description+"\"";			
				if(j+1<cpvCodes.length){
					cpvCodeIds += ",";
					cpvCodeLabels += ",";
				}
			}
			listCPVCodes +="</ul>"

			var listNutsCodes = "<ul class=\"results\">";
			var nutsCodeIds = "";
			var nutsCodeLabels = "";
			for(j=0; j<nutsCodes.length;j++){
				var region = nutsCodes[j].getElementsByTagName("uri")[0].firstChild.nodeValue;
				var code = region.substring(region.lastIndexOf("/")+1,region.length);	
				var label = labelNutsMap[code];		
				if (label == undefined) label = code;	
	listNutsCodes += 
"<li><a href='http://maps.google.com/maps?q=http://geoservice.psi.enakting.org/nuts/polygon%3Fnuts_id%3D"+code+"%26format%3Dkml'>"+label+"</a></li>";
				if( regionNumberOfPPNs[region] === undefined ) {
					regionNumberOfPPNs [region] = 1;
				} else{
					regionNumberOfPPNs [region]= regionNumberOfPPNs [region] + 1;
				}
				nutsCodeIds += "\""+code+"\"";
				nutsCodeLabels += "\""+label+"\"";			
				if(j+1<nutsCodes.length){
					nutsCodeIds += ",";
					nutsCodeLabels += ",";
				}
			
			}
			listNutsCodes +="</ul>"
			//Complete json data
			jsonData += "{";
			jsonData += "\"id\": \""+ppnTO.getElementsByTagName("id")[0].firstChild.nodeValue+"\" ,";
			jsonData += "\"code\": \""+ppnTO.getElementsByTagName("id")[0].firstChild.nodeValue+"\" ,";
			jsonData += "\"year\": \""+ppnTO.getElementsByTagName("year")[0].firstChild.nodeValue+"\" ,";
			jsonData += "\"label\": \""+ppnTO.getElementsByTagName("id")[0].firstChild.nodeValue+"\" ,";
			jsonData += "\"cpvCode\": ["+cpvCodeIds+"],";
			jsonData += "\"description\": ["+cpvCodeLabels+"],";
			jsonData += "\"nutsCodes\": ["+nutsCodeIds+"],";
			jsonData += "\"location\": ["+nutsCodeLabels+"],";	
			jsonData += "\"latLong\": [],"; //FIXME	
			jsonData += "\"score\": \""+parseFloat(scoredPPNTO.getElementsByTagName("score")[0].firstChild.nodeValue).toFixed(2)+"\"";
			jsonData += "}";
			if(i+1<resultNumber){
				jsonData += ",";
			}
			//End json data
			dataResults[i]=[
			"<strong><a href='"+ppnTO.getElementsByTagName("uri")[0].firstChild.nodeValue+"'>"+ppnTO.getElementsByTagName("id")[0].firstChild.nodeValue+"</a></strong>", 
			ppnTO.getElementsByTagName("year")[0].firstChild.nodeValue,			
			listCPVCodes, 
			listNutsCodes,
			parseFloat(scoredPPNTO.getElementsByTagName("score")[0].firstChild.nodeValue).toFixed(2)];
			
		}//end for each ppn
		jsonData += "]";
	 }//end if


//Results Table
	 $("#searching").css("display", "none");
 	 $('#example').dataTable( {
		"aaData": dataResults,
		"aoColumns": [
			{ "sTitle": "ID" },
			{ "sTitle": "Year" },
		//	{ "sTitle": "URI" },
			{ "sTitle": "CPV Description" },
			{ "sTitle": "NUTS Names" },
			{ "sTitle": "Score" }
		],
		"bDestroy":true
	} );	
	$("#exhibit-content").css("display", "inline");
       onEverythingLoaded(jQuery.parseJSON("{"+jsonData+"}")); //FIXME: need to be improved
}


//For exhibit

var onEverythingLoaded = function(jsonData){
//    poll(window.Exhibit, [ "Timeline", "Exhibit","TimelineView", "MapView" ], onEverythingLoaded);
    window.database = Exhibit.Database.create();
    window.database.loadData(jsonData);
    window.exhibit = Exhibit.create();
    window.exhibit.configureFromDOM();
};

var poll = function(outer, variables, callback) {
    window.setTimeout(function() {
    for (var i = 0; i < variables.length; i++) {
        if (!(variables[i] in outer)) {
    window.setTimeout(arguments.callee, 500);
return;
    }
}
callback();
    }, 500);
};




