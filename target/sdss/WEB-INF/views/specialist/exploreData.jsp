
<%@page import="org.narss.sdss.dto.Model"%>
<%@page import="org.narss.sdss.dto.Input"%>
<%@page import="java.util.List"%>
<%@page import="org.narss.sdss.dto.Tool"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <%@include file="../jspf/includes_inner.jspf"%>
        <meta charset="utf-8">
        <title>Specialist Dashboard</title>

        <meta name="viewport" content="width=device-width, initial-scale=1">

        <style>
            header .logo {
                padding-left: 120px;
            }
        </style>

        <link rel="stylesheet" type="text/css" href="../resources/css/editor.css">     
        <!--  model css-->
        <link rel="stylesheet" type="text/css" href="../resources/css/model.css">

        <!--Open Layer Libraries-->
        <link rel="stylesheet" href="../resources/css/ol.css" type="text/css">
        <!-- The line below is only needed for old environments like Internet Explorer and Android 4.x -->

        <script src="../resources/js/ol.js"></script>
    </head>


    <body class="bodybg">
        <%@include file="../jspf/specialistheader.jspf" %>

        <!--<div class="header-banner" id="header">
            <h1>Water Department</h1>
        </div>-->

        <div id="primary_nav_wrap">
            <ul>
                <li class="current-menu-item"><a href="${contextPath}/specialist/modelling">Modelling</a></li>
                <li><a href="${contextPath}/specialist/MCDA">MCDA</a></li>
                <li><a href="${contextPath}/specialist/report">Reports</a></li>
                <li><a href="${contextPath}/specialist/style">Styling</a></li>
                <li><a href="${contextPath}/specialist/caseStudy">Case Study</a></li>
                <li><a href="${contextPath}/specialist/questions">Questions</a></li>
            </ul>
        </div>
        <div style="background: #fff;">
            <div class="container modelcreate" style="padding-top: 2.3rem;">

                <div class="col-md-4" style="width: 40%;">
                    <div>
                        <div class="dataTables_scrollHead"> <div class="arrow-margin tableCell colExpand" style="width: 10px;">

                            </div>
                            <span class="subtitle">Layers</span>
                            <span><button type="button" class="btn clear" onclick="clearMap()">Clear</button></span>
                        </div>
                        <ul class="geoLayers" id="geolayers">

                        </ul>
                    </div>      
                </div>
                <div class="col-md-8" style="width: 60%;" >
                    <div>
                        <div id="map" >

                            <div class="ui-dialog ui-widget ui-widget-content" id="popup">
                                <!--<div class="ui-widget-header">
                                    <span id="ui-id-63" class="ui-dialog-title">Feature Attributes</span>
                                </div>-->
                                <div id="featureInfo" class="dialog ui-dialog-content ui-widget-content">   
                                </div>
                            </div>
                        </div> 
                    </div>
                </div>

            </div>
        </div>
    </body>

    <!--Scripts of dwr and tool managing -->
    <script type='text/javascript' src='/sdss/dwr/engine.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/ToolManager.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/ZPFS.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/LayerManager.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Properties.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Model.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Swap.js'></script>
    <script type='text/javascript' src='/sdss/dwr/util.js'></script>


    <script src='../resources/js/geoTools.js'></script> 
    <script src='../resources/js/map.js'></script>   


    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="../resources/js/stickybar.js"></script>
    <script src="../resources/js/editor.js"></script>
    <script src="../resources/js/sidebar.js"></script>

</html>