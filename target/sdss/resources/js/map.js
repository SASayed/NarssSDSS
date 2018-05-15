/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var realAttributes = ["Slope_Slop","Slope_Slo0","Slope_Slo1","Slope_Slo2",
    "Slope_Slo3","Slope_Slo4","Slope_Slo5","Slope_Slo6","Slope_Slo7",
    "Slope_Soil","Slope_Soi0","Slope_Soi1","Slope_Soi2","Slope_Soi3","Slope_Soi4","Slope_Soi5",
    "Slope_Soi6","Slope_Soi7","Slope_Soi8","Slope_Soi9","Slope_INTE",
    "Roads_Name","Roads_Shap","Roads_Id"];
var aliasAttributes = ["ID", "Slope", "Shape_Leng", "Shape_Area",
    "Land_ID", "MU", "SU", "GU", "Intersection",
    "Soil_ID", "CaCO3", "pH", "EC", "Num", "Depth", "Capa_Index",
    "Salinity", "Temprature", "Rainfull", "LimeStone", "Intersection",
    "Roads_Name", "Roads_Length", "Road_Id"];

//alert("REAL : "+realAttributes.length);
//alert("Alias : "+aliasAttributes.length);
var map = new ol.Map({
    target: document.getElementById('map'),
    controls: ol.control.defaults().extend([
        new ol.control.ScaleLine(),
        new ol.control.ZoomSlider()
    ]),
    layers: [
        // Add a new Tile layer getting tiles from OpenStreetMap source
        new ol.layer.Tile({
            source: new ol.source.OSM()
        })
    ],
    // Create a view centered on the specified location and zoom level
    view: new ol.View({
        center: ol.proj.transform([29.62794100171786, 27.120001767098363], 'EPSG:4326', 'EPSG:3857'),
        zoom: 6
    })

});

var popup = new ol.Overlay({
    element: document.getElementById('popup')
});
map.addOverlay(popup);
// Compute the current extent of the view given the map size
var extent = map.getView().calculateExtent(map.getSize());
// Transform the extent from EPSG:3857 to EPSG:4326
extent = ol.extent.applyTransform(extent, ol.proj.getTransform("EPSG:3857", "EPSG:4326"));
var container = document.getElementById('popup');
//groserver layers
var geoLayers = null;
LayerManager.getGeoLayers("http://192.168.1.160:8080/geoserver/rest/", "narss", "json", function (data) {
    geoLayers = data;
    //add layers to the view list of page
    var items = [];
    $.each(data, function (i, item) {
        //loadShp( map , 'narss:'+item);
        items.push('<div class="tableRow">' +
                '<div class="tableCell colExpand" style="width: 10px;"><div class="button expandControl notExpanded" title="Show preview controls"></div></div>' +
                '<div class="tableCell colType" style="width: 30px;"><div class="typeIcon polygonIcon" title="Polygon"></div></div>' +
                '<div class="tableCell colTitle" title="' + item + '" style="width: 204px;"><div class="cellWrapper"><a class="pointer" id="' + item + '" onclick="loadLayer(' + item + ');">' + item + '</a></div></div>' +
                '<div class="tableCell colIdentify"><div class="button infoControl viewIdentifyControl" title="Identify" id="' + item + '" ><a onclick="identify();"></a></div></div>' +
                '<div class="tableCell colIdentify"><div class="button pointer zoomToLayerControl" title="Zoom to geographic extent of layer"></div></div>' +
                '</div>');
    });
    $('#geolayers').append(items.join(''));
});


//SLIDER OF GEOSERVER LAYERS
/*$("#slider").click(function () {
    if(this.className == "button expandControl notExpanded"){
        this.className = "button expandControl expanded";
    }else{
        this.className = "button expandControl notExpanded";
    }
    $("#geolayers").animate({
        opacity: 0.7,
        left: "+=50",
        height: "toggle"
    }, 1000, function () {
        // Animation complete.
    });
});*/

map.on('click', function (evt) {
    container.innerHTML = '';
    var coordinate = evt.coordinate;
    displayFeatureInfo(evt.pixel, coordinate);
});

function pickRandomProperty() {
    var prefix = ['bottom', 'center', 'top'];
    var randPrefix = prefix[Math.floor(Math.random() * prefix.length)];
    var suffix = ['left', 'center', 'right'];
    var randSuffix = suffix[Math.floor(Math.random() * suffix.length)];
    return randPrefix + '-' + randSuffix;
}

var displayFeatureInfo = function (pixel, coordinate) {
    var features = [];
    map.forEachFeatureAtPixel(pixel, function (feature, layer) {
        /* var layerName = feature.getId().substr(0, feature.getId().indexOf('.'));
         $('#' + layerName).onclick = function () {
         var currentLyr = this.id;
         };*/
        //if (layerName == currentLyr) {
        features.push(feature);
        // }

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
        container.appendChild(div);
        container.appendChild(featureInfo);
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
        var randomPositioning = pickRandomProperty();
        popup.setPositioning(randomPositioning);
        popup.setPosition(coordinate);
    } else {
        container.innerHTML = '';
    }
}
function loadLayer(layername) {
    //alert(layername);
    layername = layername[0].id;
    loadShp(map, 'narss:' + layername);
}

function identify() {
    $('#currentLayer').val = "layername";

}
function loadShp(map, layername) {
    /*  var url = "http://localhost:8080/geoserver/narss/wms?service=WMS&version=1.1.0&request=GetMap";
     var wmsSource = new ol.source.TileWMS({
     url: url,
     params: {'LAYERS': layername},
     serverType: 'geoserver',
     crossOrigin: null
     });
     var wmsLayer = new ol.layer.Tile({
     source: wmsSource
     
     });*/
    if (layername != null) {
        var myurl = 'http://192.168.1.160:8080/geoserver/narss/wfs?service=wfs&\n\
version=1.0.0&request=GetFeature&typeName=' + layername + '&outputformat=application/json';
        var vectorSource = new ol.source.Vector({
            id: 'id',
            format: new ol.format.GeoJSON(),
            url: myurl

        });
        var vectorjson = new ol.layer.Vector({
            source: vectorSource,
             

        });
        LayerManager.getLayerExtent(layername, function (data) {
            // Trasnform extent to EPSG:3857
            var extent = [parseFloat(data[0]), parseFloat(data[1]), parseFloat(data[2]), parseFloat(data[3])];
            extent = ol.extent.applyTransform(extent, ol.proj.getTransform("EPSG:4326", "EPSG:3857"));
            map.getView().fit(extent, map.getSize());
            map.addLayer(vectorjson);
        });
    }
    //wmsLayer.on('click', alert ("hi"));
}

function clearMap() {

    for (var i = map.getLayers().getLength() - 1; i >= 0; i--) {
        if (i !== 0) {
            map.removeLayer(map.getLayers().a[i]);
        }
    }
   /* var extent = [24.288353222821893, 21.805543050445188, 37.2479355506401, 31.91501479443457];
    extent = ol.extent.applyTransform(extent, ol.proj.getTransform("EPSG:4326", "EPSG:3857"));
    map.getView().fit(extent, map.getSize());*/
    container.innerHTML = "";
}
