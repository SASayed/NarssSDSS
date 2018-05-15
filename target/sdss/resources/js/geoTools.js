/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var UrlforWPS = "http://192.168.1.160:8080/geoserver/ows?service=wps";
//var WPS_Output = "C:\Users\Administrator\Desktop\\sdss-demo\\geoserverOutput\\";
//var modellingPath = "C:\Users\Administrator\Desktop\\sdss-demo\\src\\main\\webapp\\resources\\geoModelling\\";
//var QUERY_PATH = "C:\Users\Administrator\Desktop\\sdss-demo\\geoserverQueries\\";
var extension = ["shp", "cst", "dbf", "prj", "shx"];
var workspace = "narss:";
var model_Id;
var d = new Date();
var unique = d.getTime();
var ToolList = [];
var ToolObjects = [];
var toolFiles = [];
var testmap = new ol.Map({
    target: document.getElementById('testmap2'),
    controls: ol.control.defaults().extend([
        new ol.control.ScaleLine(),
        new ol.control.ZoomSlider()
    ]),
    view: new ol.View({
        center: ol.proj.transform([29.62794100171786, 27.120001767098363], 'EPSG:4326', 'EPSG:3857'),
        zoom: 6
    })
});

//add pop layer
var popup2 = new ol.Overlay({
    element: document.getElementById('popup2')
});
testmap.addOverlay(popup2);
var container2 = document.getElementById('popup2');


testmap.on('click', function (evt) {
    container2.innerHTML = '';
    var coordinate = evt.coordinate;
    displayFeatureInfo2(evt.pixel, coordinate);
});

function pickRandomProperty2() {
    var prefix = ['bottom', 'center', 'top'];
    var randPrefix = prefix[Math.floor(Math.random() * prefix.length)];
    var suffix = ['left', 'center', 'right'];
    var randSuffix = suffix[Math.floor(Math.random() * suffix.length)];
    return randPrefix + '-' + randSuffix;
}

var displayFeatureInfo2 = function (pixel, coordinate) {
    var features = [];
    testmap.forEachFeatureAtPixel(pixel, function (feature, layer) {
        features.push(feature);
    });
    if (features.length > 0) {
        var info = [];
        var featureInfo = document.createElement("div");
        featureInfo.className = "dialog ui-dialog-content ui-widget-content";
        featureInfo.id = "featureInfo";
        var span = document.createElement("span");
        span.className = "ui-dialog-title";
        span.id = "ui-id-63";
        span.innerHTML = "Feature Attributes";
        var div = document.createElement("div");
        div.className = "ui-widget-header";
        div.appendChild(span);
        container2.appendChild(div);
        container2.appendChild(featureInfo);
        for (var i = 0, ii = features.length; i < ii; ++i) {
            var table = document.createElement("table");

            //info[i] = features[i].get('SEC_NAME_A');
            info[i] = features[i].getProperties();
            var attributes = Object.keys(info[i]);
            // attributes.length;
            //var count = 0;
            var j;
            for (j in attributes) {
                var tr = document.createElement("tr");
                var td1 = document.createElement("td");
                //Attribute Name
                for (var a = 0; a < realAttributes.length; a++) {
                    if (attributes[j] == realAttributes[a]) {
                        td1.innerHTML = aliasAttributes[a];
                        break;
                    }
                }
                if (td1.innerHTML == "") {
                    td1.innerHTML = attributes[j];
                }
                var attr_value = info[i][attributes[j]];
                var td2 = document.createElement("td");
                td2.innerHTML = attr_value;
                tr.appendChild(td1);
                tr.appendChild(td2);
                table.appendChild(tr);
            }
            featureInfo.appendChild(table);
        }

        //container.innerHTML = info.join(', ') || '(unknown)';
        var randomPositioning = pickRandomProperty2();
        popup2.setPositioning(randomPositioning);
        popup2.setPosition(coordinate);
    } else {
        container2.innerHTML = '';
    }
}

function runTest() {
    viewTestMap('testmap', 'QueryBuilder', 2);
    testmap.updateSize();
    var osm = new ol.layer.Tile({
        source: new ol.source.OSM()
    });
    testmap.addLayer(osm);
    testmap.renderSync();
    loadTestMap(workspace + model_Id + unique);
}

function viewTestMap(divId, queryDivId, SpanId) {
    // Get map
    var mapDiv = document.getElementById(divId);
    // Get query
    var queryDiv = document.getElementById(queryDivId);

    mapDiv.style.display = "block";
    var span = document.getElementsByClassName('close')[SpanId];
    // When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        mapDiv.style.display = "none";
        queryDiv.style.display = "none";
    }
}

function loadTestMap(shp) {

    var myurl = 'http://192.168.1.160:8080/geoserver/narss/wfs?service=wfs&\n\
version=1.0.0&request=GetFeature&typeName=' + shp + '&outputformat=application/json';
    var vectorSource = new ol.source.Vector({
        format: new ol.format.GeoJSON(),
        url: myurl
    });
    var vectorjson = new ol.layer.Vector({
        source: vectorSource,

    });

    var Testextent = [28.75954438638267, 30.764111682170817, 28.9407379114019, 30.934289947367297];
    Testextent = ol.extent.applyTransform(Testextent, ol.proj.getTransform("EPSG:4326", "EPSG:3857"));
    testmap.getView().fit(Testextent, testmap.getSize());
    testmap.addLayer(vectorjson);
}

///End Of Test Map Handelingwindow.onbeforeunload =

 function saveChanges() {
     
     //document.getElementById("testbtn").disabled = false;
     //document.getElementById("savebtn").disabled = true;
   
    dwr.util.setValue('JtoolList', ToolList);
    dwr.util.setValue('Form_Action', 'editModel');
  
    //return true;
}


//Test Model
function testModel() {
    dwr.util.setValue('JtoolList', ToolList);
    dwr.util.setValue('Form_Action', 'testModel');
       return true;
   /* NProgress.start();
    var firstLayer;
    firstLayer = document.getElementsByName('vector_0')[0];
    for (var i = 0; i < ToolList.length; i++) {
        var Vectors = document.getElementsByName('vector_' + i);
        var Numbers = document.getElementsByName("number_" + i);
        var Coors = document.getElementsByName("coor_" + i);
        var inputsSplit = document.getElementsByName("splitCoor_" + i);

        var Vectors_Layers = [];
        var Numbers_input = 0;
        var Coors_input = 0;
        for (var x = 0; x < Vectors.length; x++) {
            Vectors_Layers[x] = Vectors[x].value;
        }
        if (Numbers.length > 0) {
            Numbers_input = Numbers[0].value;
        }
        if (Coors.length > 0) {
            Coors_input = Coors[0].value;
        }

        //set Unique number for each tool File
        var new_d = new Date();
        var uni = new_d.getTime();
        //Set Tool File
        var tool_Name = ToolList[i].replace(" ", "");
        var toolFile = modellingPath + tool_Name + ".xml";
        var newtoolFile = QUERY_PATH + tool_Name + uni + ".xml";
        toolFiles[i] = newtoolFile;
        if (i == 0) {
            if (Vectors_Layers.length != 0) {
                LayerManager.J_InsertLayers(workspace, Vectors_Layers, toolFile, newtoolFile, function (data) {});
            }
            if (Numbers_input != 0) {
                LayerManager.J_InsertDistance(newtoolFile, newtoolFile, Numbers_input, function (data) {});
            }
            if (Coors_input != 0) {
                LayerManager.J_InsertPoint(newtoolFile, newtoolFile, Coors_input, function (data) {});
            }
        } else if (i != 0) {
            if (Vectors_Layers.length != 0) {
                LayerManager.J_InsertSingelLayer(workspace, Vectors_Layers, toolFile, newtoolFile, function (data) {});
            }
            if (Numbers_input != 0) {
                LayerManager.J_InsertDistance(toolFile, newtoolFile, Numbers_input, function (data) {});
            }
            if (Coors_input != 0) {
                LayerManager.J_InsertPoint(toolFile, newtoolFile, Coors_input, function (data) {});
            }
        }
        if (inputsSplit.length > 1) {
            LayerManager.J_InsertSplitInputs(toolFile, newtoolFile, inputsSplit, function (data) {});
        }

    }
    setTimeout(function () {
        ToolManager.createModelRunFile(toolFiles, function (file) {
            if (file != "") {
                LayerManager.JpostRequest(UrlforWPS, file, WPS_Output + model_Id + unique + ".zip", function (data) {
                    ZPFS.renameFilesInZip(model_Id + unique + ".zip", extension, firstLayer.value, model_Id + unique, function (data2) {
                        if (data2) {
                            LayerManager.publishLayer(WPS_Output, model_Id + unique, function (data3) {
                                NProgress.done();
                                if (data3 == true) {
                                    viewDiv('QueryBuilder', 1);
                                } else {
                                    alert("Sorry Error occurred Please try again");
                                }
                            });
                        } else {
                            NProgress.done();
                            alert("Sorry There is an error in your model");
                        }
                    });
                });
            } else {
                NProgress.done();
                alert("Sorry Error occurred Please try again");
            }
        })
    }, 18000);*/
}

function createQuery() {
    var rules_basic = {
        condition: 'AND',
        rules: [{
                id: 'Records',
                operator: 'equal',
                value: 200
            }]
    };
    $('#builder').queryBuilder({
        filters: [
            {
                id: 'Publish Layer',
                label: 'Publish Layer',
                type: 'integer',
                input: 'radio',
                values: {
                    1: 'Yes',
                    0: 'No'
                },
                operators: ['equal']
            }, {
                id: 'Records',
                label: 'Records',
                type: 'double',
                validation: {
                    min: 50,
                    step: 50
                }
            }],

        rules: rules_basic
    });
}

//Print Inputs 
function intersect() {
    ToolManager.intersectTool(function (data) {
//alert(data.name);
        var i = ToolList.length;
        ToolList[i] = data.name;
        ToolObjects[i] = data;
        //data.id = 1;
        //alert(data.id);
        //dwr.util.addOptions("tools", [data], "name");
        add_Inputs(ToolObjects, data, "uTable", i);
    });
    /*
     ToolManager.addTool(ToolList[0],function (data) {      
     $.post("../../../sdss/dwr/jsonp/ToolManager/addTool/" + ToolList[0], { },
     function(data) {
     dwr.util.setValue("JtoolList", data.reply);
     }, "jsonp"); 
     });
     */
}

function buffer() {
    ToolManager.bufferTool(function (data) {
//alert(data.name);
        var i = ToolList.length;
        ToolList[i] = data.name;
        ToolObjects[i] = data;
        add_Inputs(ToolObjects, data, "uTable", i);
    });
}

function union() {
    ToolManager.unionTool(function (data) {
//alert(data.name);
        var i = ToolList.length;
        ToolList[i] = data.name;
        ToolObjects[i] = data;
        add_Inputs(ToolObjects, data, "uTable", i);
    });
}

function near() {
    ToolManager.nearTool(function (data) {
//alert(data.name);
        var i = ToolList.length;
        ToolList[i] = data.name;
        ToolObjects[i] = data;
        add_Inputs(ToolObjects, data, "uTable", i);
    });
}

function clip() {
    ToolManager.clipTool(function (data) {
//alert(data.name);
        var i = ToolList.length;
        ToolList[i] = data.name;
        ToolObjects[i] = data;
        add_Inputs(ToolObjects, data, "uTable", i);
    });
}

function split() {
    ToolManager.splitTool(function (data) {
// alert(data.name);
        var i = ToolList.length;
        ToolList[i] = data.name;
        ToolObjects[i] = data;
        add_Inputs(ToolObjects, data, "uTable", i);
    });
}

function identity() {
    ToolManager.identityTool(function (data) {
//alert(data.name);
        var i = ToolList.length;
        ToolList[i] = data.name;
        ToolObjects[i] = data;
        add_Inputs(ToolObjects, data, "uTable", i);
    });
}

function spatialJoin() {
    ToolManager.spatialJoinTool(function (data) {
//alert(data.name);
        var i = ToolList.length;
        ToolList[i] = data.name;
        ToolObjects[i] = data;
        add_Inputs(ToolObjects, data, "uTable", i);
    });
}

function add_Inputs(ToolObjects, data, tableId, j) {
    PrintToolObjects(ToolObjects);
    var table = document.getElementById(tableId);
    $("tr").remove();
    for (var j = 0; j < ToolObjects.length; j++) {
        var tr = document.createElement("tr");
        tr.className = "toolName tool" + j;
        /*var td_1 = document.createElement("td");
         td_1.innerHTML = j;*/
        var td_2 = document.createElement("td");
        td_2.innerHTML = ToolList[j];
        td_2.rowspan = 2;
        //tr.appendChild(td_1);
        tr.appendChild(td_2);
        var td_4 = document.createElement("td");
        td_4.innerHTML = "<span id='" + j + "'class='removeTool' onclick='removeTool(this.id)'>&times;</span>";
        tr.appendChild(td_4);
        table.appendChild(tr);
        var inputs = ToolObjects[j].inputs;
        if (j == 0) {
            for (var x = 0; x < inputs.length; x++)
            {
                var tr2 = document.createElement("tr");
                var td_extra = document.createElement("td");
                var td_3 = document.createElement("td");
                if (inputs[x].type == "vector") {
                    td_3.innerHTML = "<p>" + inputs[x].name + "</p><select id='vector_" + j + "' name='vector_" + j + "' class='form-control' required><option value='false'>Select...</option></select>";
                } else if (inputs[x].type == "number") {
                    td_3.innerHTML = "<p>" + inputs[x].name + "</p><input type='text' id='number' name='number_" + j + "' class='form-control' placeholder='2.2' required/>";
                } else if (inputs[x].type == "coor") {
                    td_3.innerHTML = "<p>" + inputs[x].name + "</p><input type='text' id='coor' name='coor_" + j + "' class='form-control' placeholder='2.3434 4.4343' required/>";
                } else if (inputs[x].type == "splitCoor") {
                    td_3.innerHTML = "<p>" + inputs[x].name + "</p><input type='text' id='splitCoor' name='splitCoor_" + j + "' class='form-control' placeholder='2.3434 4.4343' required/>";
                }
                tr2.appendChild(td_extra);
                tr2.appendChild(td_3);
                table.appendChild(tr2);
                //tr.appendChild(td_3);
            }

        } else {
            var tr2 = document.createElement("tr");
            var td_extra = document.createElement("td");
            var td_3 = document.createElement("td");
            if (inputs[0].type == "vector") {
                td_3.innerHTML = "<p>" + inputs[0].name + "</p><select id='vector1_" + j + "' name='vector_" + j + "' class='form-control' required><option value='false'>Select...</option></select>";
            } else if (inputs[0].type == "number") {
                td_3.innerHTML = "<p>" + inputs[0].name + "</p><input type='text' id='number' name='number_" + j + "' class='form-control' placeholder='2.2' required/>";
            } else if (inputs[0].type == "coor") {
                td_3.innerHTML = "<p>" + inputs[0].name + "</p><input type='text' id='coor' name='coor_" + j + "' class='form-control' placeholder='2.3434 4.4343' required/>";
            } else if (inputs[0].type == "splitCoor") {
                td_3.innerHTML = "<p>" + inputs[0].name + "</p><input type='text' id='splitCoor' name='splitCoor_" + j + "' class='form-control' placeholder='2.3434 4.4343' required/>";
            }
            tr2.appendChild(td_extra);
            tr2.appendChild(td_3);
            table.appendChild(tr2);
            //tr.appendChild(td_3);
        }
        ///fill <select>  with geoserver layers names
        var select_main = document.getElementById('vector_' + j);
        if (select_main != null) {
            dwr.util.addOptions("vector_" + j, geoLayers);
        }
        var select2 = document.getElementById('vector1_' + j);
        if (select2 != null) {
            dwr.util.addOptions("vector1_" + j, geoLayers);
        }
        var select_length = document.getElementsByName('vector_' + j).length;
        if (select_length > 1) {
            var select = document.getElementsByName('vector_' + j)[1];
            for (var i = 0; i < geoLayers.length; i++) {
                var opt = document.createElement('option');
                opt.value = geoLayers[i];
                opt.innerHTML = geoLayers[i];
                select.appendChild(opt);
            }
        }
    }

}
function reprintTools(ToolObjects, tableId) {
    //graphic library
    PrintToolObjects(ToolObjects);
    var table = document.getElementById(tableId);
    $("tr").remove();
    for (var j = 0; j < ToolObjects.length; j++) {
        var tr = document.createElement("tr");
        tr.className = "toolName tool" + j;
        var td_2 = document.createElement("td");
        td_2.innerHTML = ToolList[j];
        //tr.appendChild(td_1);
        tr.appendChild(td_2);
        var td_4 = document.createElement("td");
        td_4.innerHTML = "<span id='" + j + "'class='removeTool' onclick='removeTool(this.id)'>&times;</span>";
        tr.appendChild(td_4);
        table.appendChild(tr);
        var inputs = ToolObjects[j].inputs;

        if (j == 0) {
            for (var x = 0; x < inputs.length; x++)
            {
                var tr2 = document.createElement("tr");
                var td_extra = document.createElement("td");
                var td_3 = document.createElement("td");
                if (inputs[x].type == "vector") {
                    td_3.innerHTML = "<p>" + inputs[x].name + "</p><select id='vector_" + j + "' name='vector_" + j + "' class='form-control' required><option value='false'>Select...</option></select>";
                } else if (inputs[x].type == "number") {
                    td_3.innerHTML = "<p>" + inputs[x].name + "</p><input type='text' id='number' name='number_" + j + "' class='form-control' placeholder='2.2' required/>";
                } else if (inputs[x].type == "coor") {
                    td_3.innerHTML = "<p>" + inputs[x].name + "</p><input type='text' id='coor' name='coor_" + j + "' class='form-control' placeholder='2.3434 4.4343' required/>";
                } else if (inputs[x].type == "splitCoor") {
                    td_3.innerHTML = "<p>" + inputs[x].name + "</p><input type='text' id='splitCoor' name='splitCoor_" + j + "' class='form-control' placeholder='2.3434 4.4343' required/>";
                }
                tr2.appendChild(td_extra);
                tr2.appendChild(td_3);
                table.appendChild(tr2);
            }

        } else {
            var tr2 = document.createElement("tr");
            var td_extra = document.createElement("td");
            var td_3 = document.createElement("td");
            if (inputs[0].type == "vector") {
                td_3.innerHTML = "<p>" + inputs[0].name + "</p><select id='vector1_" + j + "' name='vector_" + j + "' class='form-control' required><option value='false'>Select...</option></select>";
            } else if (inputs[0].type == "number") {
                td_3.innerHTML = "<p>" + inputs[0].name + "</p><input type='text' id='number' name='number_" + j + "' class='form-control' placeholder='2.2' required/>";
            } else if (inputs[0].type == "coor") {
                td_3.innerHTML = "<p>" + inputs[0].name + "</p><input type='text' id='coor' name='coor_" + j + "' class='form-control' placeholder='2.3434 4.4343' required/>";
            } else if (inputs[0].type == "splitCoor") {
                td_3.innerHTML = "<p>" + inputs[0].name + "</p><input type='text' id='splitCoor' name='splitCoor_" + j + "' class='form-control' placeholder='2.3434 4.4343' required/>";
            }
            tr2.appendChild(td_extra);
            tr2.appendChild(td_3);
            table.appendChild(tr2);
        }

        ///fill <select>  with geoserver layers names
        dwr.util.addOptions("vector_" + j, geoLayers);
        var select2 = document.getElementById('vector1_' + j);
        if (select2 != null) {
            dwr.util.addOptions("vector1_" + j, geoLayers);
        }
        var select_length = document.getElementsByName('vector_' + j).length;
        if (select_length > 1) {
            var select = document.getElementsByName('vector_' + j)[1];
            for (var i = 0; i < geoLayers.length; i++) {
                var opt = document.createElement('option');
                opt.value = geoLayers[i];
                opt.innerHTML = geoLayers[i];
                select.appendChild(opt);
            }
        }
    }

}

function createModel(divId, spanId) {
// Get the modal
    var modal = document.getElementById('myModal');
    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName('close')[spanId];
    // Get the button that opens the modal
    var btn = document.getElementById(divId);
    // When the user clicks the button, open the modal 
    btn.onclick = function () {
        modal.style.display = "block";
    }

// When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
    }

}


function viewEditModel(divId, spanId) {
    // Get the modal
    var modal = document.getElementById(divId);
    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName('close')[spanId];
    // When the user clicks the button, open the modal 
    modal.style.display = "block";
    // When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        modal.style.display = "none";
        //location.reload();
    }

// When the user clicks anywhere outside of the modal, close it
    /* window.onclick = function (event) {
     if (event.target == modal) {
     modal.style.display = "none";
     }
     }*/

}

   function Edit_Model(modelId) {
    viewEditModel('EditModel', 0);
    model_Id = modelId;
    dwr.util.setValue('model_id', modelId);

}


/*Edit_Model : function(deferred) {
				var that = this;
				var url = this.model.get("authUrl");
				//var username = jQuery("#loginFormUsername").val();
				  modelid= $("#model_id").val();
                                dwr.util.setValue('model_id', modelId);
				var ajaxArgs = {
					type : "POST",
					context : that,
					crossDomain : true,
                                        url : url+"modelid="+modelid,
					xhrFields : {
						withCredentials : false
					},
					data : {
						"modelid" : modelid
						
					},
					dataType : "json",
					success : function(data) {
                                                if(data.Authentication === "Authenticated")
                                                {
                                                    that.model.set({
                                                            authenticated : true,
                                                            modelid: data.modelid
                                                           
                                                            
                                                    });
                                                }
						if (that.model.get("authenticated")) {
                                                        deferred.resolve();
						}
						else  {
							that.showLoginMessage("ERROR, User " + data.name+ " dose not exist");
						}
					},
					error : that.loginResponseError
				};
				return $.ajax(ajaxArgs);
			}
                        */
function viewDiv(divId, SpanId) {
    // Get map
    var mapDiv = document.getElementById(divId);
    mapDiv.style.display = "block";
    var span = document.getElementsByClassName('close')[SpanId];
    // When the user clicks on <span> (x), close the modal
    span.onclick = function () {
        mapDiv.style.display = "none";
    }
}
function testprint(){ 
      var i = ToolList.length;
        ToolList[i] = data.name;
        
        data=document.getElementById('toollist').value;
        ToolObjects[i] = data;
        PrintToolObjects(ToolObjects);
    }

