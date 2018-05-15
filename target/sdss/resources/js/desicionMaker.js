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
var headertitle = document.getElementById('title');
headertitle.innerHTML = "Decision Maker Dashboard";
var container = document.getElementById('popup');
var wmsLayer;
var raster = new ol.layer.Tile({
    source: new ol.source.OSM()
});
var map = new ol.Map({
    layers: [raster],
    target: document.getElementById('map'),
   controls: ol.control.defaults().extend([
        new ol.control.ScaleLine(),
        new ol.control.ZoomSlider()
    ]),
    view: new ol.View({
        center: ol.proj.transform([29.62794100171786, 27.120001767098363], 'EPSG:4326', 'EPSG:3857'),
        zoom: 5
    })
});

function load(layerName,style){
   loadShp(layerName);
   loadMapstyle(layerName, style);
    
}

function loadMapstyle(layername, stylename)
{
    var url = "http://192.168.1.160:8080/geoserver/narss/wms?service=WMS&version=1.1.0&request=GetMap&layers=" + layername + "&styles=" + stylename + "&bbox=&width=512&height=511&srs=EPSG:900913&format=image%2Fpng";

    var wmsSource = new ol.source.TileWMS({
        url: url,
        params: {'LAYERS': layername, TILED: false},
        serverType: 'geoserver',
        crossOrigin: null,
        extractStyles: false,
        Zoomify:5
      
    });
  
    wmsLayer = new ol.layer.Tile({
       
        source: wmsSource
        //extent:[2762794.100171786,2512102.3127712854,3964804.3678022977,3712000.1767098363],


    });
    
    
   
       // map.addLayer(wmsLayer);
    //var extent = [28.75954438638267, 30.764111682170817, 28.9407379114019, 30.934289947367297];
  //  extent = ol.extent.applyTransform(extent, ol.proj.getTransform("EPSG:4326", "EPSG:3857"));
    //map.getView().fit(extent, map.getSize());
            /* LayerManager.getLayerExtent(layername, function (data) {
            // Trasnform extent to EPSG:3857
            var extent = [parseFloat(data[0]), parseFloat(data[1]), parseFloat(data[2]), parseFloat(data[3])];
            extent = ol.extent.applyTransform(extent, ol.proj.getTransform("EPSG:4326", "EPSG:3857"));
            map.getView().fit(extent, map.getSize());
            map.addLayer(wmsLayer);
        });*/
      map.addLayer(wmsLayer);
   var extent = [28.75954438638267, 30.764111682170817, 28.9407379114019, 30.934289947367297];
    extent = ol.extent.applyTransform(extent, ol.proj.getTransform("EPSG:4326", "EPSG:3857"));
    map.getView().fit(extent, map.getSize());
    

}

function loadShp(layername)
{
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
       map.addLayer(vectorjson);
   var extent = [28.75954438638267, 30.764111682170817, 28.9407379114019, 30.934289947367297];
    extent = ol.extent.applyTransform(extent, ol.proj.getTransform("EPSG:4326", "EPSG:3857"));
    map.getView().fit(extent, map.getSize());

}
var popup = new ol.Overlay({
    element: document.getElementById('popup')
});
function pickRandomProperty() {
    var prefix = ['bottom', 'center', 'top'];
    var randPrefix = prefix[Math.floor(Math.random() * prefix.length)];
    var suffix = ['left', 'center', 'right'];
    var randSuffix = suffix[Math.floor(Math.random() * suffix.length)];
    return randPrefix + '-' + randSuffix;
}
map.addOverlay(popup);
map.on('click', function (evt) {
    container.innerHTML = '';
    var coordinate = evt.coordinate;
    displayFeatureInfo(evt.pixel, coordinate);
});
var displayFeatureInfo = function (pixel, coordinate) {
    var features = [];
    map.forEachFeatureAtPixel(pixel, function (feature, layer) { 
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

function AskQuestion(divId, spanId) {
// Get the modal
    var modal = document.getElementById('question');
    // Get the <span> element that closes the modal
    var span = document.getElementById(spanId);
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
function loadpoint(layerName,style){

   loadPointShp(layerName); loadPointMapstyle(layerName, style);
}
//water layer

function loadPointShp(layername)
{
    var myurl = 'http://'+"192.168.1.160"+':8080/geoserver/narss/wfs?service=wfs&\n\
version=1.0.0&request=GetFeature&typeName=' + layername + '&outputformat=application/json';
        var vectorSource = new ol.source.Vector({
            id: 'id',
            format: new ol.format.GeoJSON(),
            url: myurl

        });
        var vectorjson = new ol.layer.Vector({
            source: vectorSource,
         

        });
        map.addLayer(vectorjson);
}

function loadPointMapstyle(layername, stylename)
{
    var url = "http://"+"192.168.1.160"+":8080/geoserver/narss/wms?service=WMS&version=1.1.0&request=GetMap&layers=" + layername + "&styles=" + stylename + "&bbox=&width=512&height=511&srs=EPSG:900913&format=image%2Fpng";

    var wmsSource = new ol.source.TileWMS({
        url: url,
        params: {'LAYERS': layername, TILED: false},
        serverType: 'geoserver',
        crossOrigin: null,
        extractStyles: false,
        Zoomify:5
      
    });
  
    wmsLayer = new ol.layer.Tile({
       
        source: wmsSource
    });
    
    
   
        map.addLayer(wmsLayer);
    
    var extent = [31.0261,30.1644, 31.1461, 30.1902];
    extent = ol.extent.applyTransform(extent, ol.proj.getTransform("EPSG:4326", "EPSG:3857"));
    map.getView().fit(extent, map.getSize());

}