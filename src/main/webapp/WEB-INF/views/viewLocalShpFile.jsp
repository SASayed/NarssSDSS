<%-- 
    Document   : viewLocalShpFile
    Created on : Mar 4, 2017, 12:45:14 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
            <!--DIV OF SHP_PREVIEW FOR MODEL TESt -->
        <!-- <div id="wrap" class="wrap">
 
             <footer id="footer"><div class="ui page grid">
 
                     <div class="sixteen wide column center aligned">
                        
                         <div id="addZipfile" class="tips large ui positive right labeled icon button">
                             Upload zip file
                         </div>
                         <div id="removeLayer" class="negative large ui button">Remove All Features</div>
                     </div>
 
                 </div></footer>
         </div>
         <div id="shp" class="shp-modal"><div class="ui page grid">
 
                 <div class="sixteen wide aligned column">
 
                     <div class="ui form segment">
                         <div class="field"><div class="ui teal fluid labeled icon button upload" id="zipfile" data-content="Mandatory files : SHP , DBF" data-variation="inverted large">
                                 Upload zip file
                                 <i class="file archive outline icon"></i>
                                 <input type="file" id="file" accept=".zip">
                             </div></div>
 
                         <div class="field" id="dataInfo"></div>
 
 
                         <div class="two fields">
                             <div class="field"><div class="ui teal fluid labeled icon disabled button" id="preview">Preview
                                     <i class="add icon"></i>
                                 </div></div>                    
                             <div id="cancel" class="negative right ui button">Cancel</div>
 
                         </div>
                     </div>
                     <div class="ui inverted dimmer">
                         <div class="ui large text loader">Loading</div>
                     </div>
 
                 </div>
 
                 <div class="ui small modal">
                     <i class="close icon"></i>
                     <div class="header">Error Messages</div>
                     <div class="content">
                         <div class="image"><i class="file archive outline icon"></i></div>
                         <div class="description">
                             <p>Sorry, this format are not supported.</p>
                         </div>
                     </div>
                     <div class="actions"><div class="ui red button">
                             <i class="remove icon"></i>Cancel
                         </div></div>
                 </div>
 
             </div></div> -->

        <!-- -->
    </body>
      <!--Scripts of shp-Preview of Model Test -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/proj4js/2.3.3/proj4.js"></script>
    <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
    <script src="http://cdn.leafletjs.com/leaflet-0.5/leaflet.js"></script>

    <script src="../resources/js/jszip.js"></script>
    <script src="../resources/js/jszip-utils.js"></script>
    <script src="../resources/js/semantic.min.js"></script>
    <script src="../resources/js/preprocess.js"></script>
    <script src="../resources/js/shp-preview.js"></script>
    <script type="text/javascript" charset="UTF-8">
        /*  $(document).ready(function () {
         
         var map = L.map('map').setView([0, 0], 5),
         file,
         vector;
         L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {maxZoom: 13}).addTo(map);
         
         function initVector() {
         vector = L.geoJson([], {
         style: function (feature) {
         return feature.properties.style;
         },
         onEachFeature: function (feature, layer) {
         
         layer.on({click: function (e) {
         vector.eachLayer(function (l) {
         vector.resetStyle(l);
         });
         
         $('.tbodyContent').remove();
         var tbody = '<tbody class="tbodyContent">';
         for (var key in e.target.feature.properties) {
         tbody +=
         ('<tr class="center aligned"><td>' + key + '</td><td>' + e.target.feature.properties[key] + '</td></tr>');
         }
         $('#attribute').append(tbody + '</tbody>');
         $('#attr').fadeIn(300);
         map.panTo(e.latlng);
         
         if ('setStyle' in e.target)
         e.target.setStyle({
         fillColor: '#FF0',
         fillOpacity: 0.6
         });
         }});
         }
         }).addTo(map);
         }
         
         function loadShpZip() {
         var epsg = 4326,
         encoding = 'UTF-8';
         if (file.name.split('.')[1] == 'zip') {
         if (file)
         $('.dimmer').addClass('active');
         loadshp({
         url: file,
         encoding: encoding,
         EPSG: epsg
         }, function (data) {
         var URL = window.URL || window.webkitURL || window.mozURL || window.msURL,
         url = URL.createObjectURL(new Blob([JSON.stringify(data)], {type: "application/json"}));
         
         $('#link').attr('href', url);
         $('#link').html(file.name + '.geojson' + '<i class="download icon"></i>').attr('download', file.name + '.geojson');
         
         $('.shp-modal').toggleClass('effect');
         
         $('#wrap').toggleClass('blur');
         
         vector.addData(data);
         map.fitBounds([
         [data.bbox[1], data.bbox[0]], [data.bbox[3], data.bbox[2]]
         ]);
         $('.dimmer').removeClass('active');
         $('#preview').addClass('disabled');
         
         
         });
         } else {
         $('.modal').modal('show');
         }
         }
         
         initVector();
         
         $("#file").change(function (evt) {
         file = evt.target.files[0];
         alert(file);
         if (file.size > 0) {
         $('#dataInfo').text(' ').append(file.name + ' , ' + file.size + ' kb');
         $('#preview').removeClass('disabled');
         alert(this.value);
         //alert($('#dataInfo'));
         }
         });
         
         $('#preview').click(function () {
         $('#file').trigger('change');
         loadShpZip();
         
         });
         
         $('.button').popup({
         inline: true,
         position: 'bottom left'
         });
         
         $('#entireLayer').click(function () {
         map.fitBounds(vector.getBounds());
         });
         
         $('#addZipfile').click(function () {
         $('.shp-modal').toggleClass('effect');                     
         $('#wrap').toggleClass('blur');
         });
         $('#cancel').click(function () {
         $('.shp-modal').toggleClass('effect');
         
         $('#wrap').toggleClass('blur');
         });
         $('#removeLayer').click(function () {
         $('#attr').fadeOut(300);
         window.location.reload();
         });
         
         
         $("#attr").draggable({containment: $(this).parent().parent(), scroll: false, cursor: "move"});
         $('#cancelAttr').click(function () {
         $('#attr').slideUp(300);
         });
         
         });*/
    </script>
</html>
