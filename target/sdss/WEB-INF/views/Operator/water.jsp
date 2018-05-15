<%-- 
    Document   : runModel
    Created on : Jan 12, 2017, 1:23:52 AM
    Author     : Administrator
--%>

<%@page import="java.io.File"%>
<%@page import="org.narss.sdss.water.pollution.Chlorophyll"%>
<%@page import="org.narss.sdss.water.pollution.SSC"%>
<%@page import="org.narss.sdss.water.pollution.TDS"%>
<%@page import="org.narss.sdss.water.pollution.TSS"%>
<%@page import="org.narss.sdss.water.pollution.Turbidity"%>
<%@page import="org.narss.sdss.water.pollution.SamplePoint"%>

<%@page import="java.util.List"%>

<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
    <head>
        <%@include file="../jspf/includes_inner.jspf"%>
        <title>Operator Dashboard</title>
        <!-- Progress bar -->
        <link rel='stylesheet' href='../resources/css/nprogress.css'/>
        <script src="../resources/js/konva.js"></script>
        <script src="../resources/js/graphicLibrary.js"></script>
        <!--  Progrss bar js-->
        <script src='../resources/js/nprogress.js'></script>
        <style>
            header .logo {
                padding-left: 120px;
            }.equation-div{
                background-color: rgb(232, 232, 232);
                font-size: 20px;
                border: 1px solid;
                height: 25px;
                padding: 10px 20px 50px 20px;
                text-align: center;
            }.equation{

                color: #0a3e57;
                font-size: 30px;

            }

        </style>
    </head>
    <body class="bodybg">
        <%@include file="../jspf/operatorheader.jspf" %>
        <div id="primary_nav_wrap">
        </div>


        <div style="background: #fff;">

            <div class="container modelcreate" style="padding-top: 2rem;">
                <div id="progressBar" class="form-group col-md-12"></div>
                <div class="col-md-12" style="  padding-bottom: 50px;">
                    <legend class="legend">Regression Model Equation</legend>
                    <div class="equation-div"><p class="equation">Y = AX + A0</p></div>
                </div>
                <div class="col-md-12">
                    <form action="" method="POST">
                        <div class="form-group col-md-4">
                            <legend class="legend">Co-Efficients</legend>
                            <table class="table table-hover datatable-scroll-y">
                                <thead style="background: wheat">
                                    <tr>
                                        <th>Pollutant Name</th>
                                        <th>A0</th>
                                        <th>A</th>
                                        <th>Reference Value</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        Chlorophyll chl = (Chlorophyll) request.getAttribute("chl");
                                        SSC ssc = (SSC) request.getAttribute("ssc");
                                        TDS tds = (TDS) request.getAttribute("tds");
                                        TSS tss = (TSS) request.getAttribute("tss");
                                        Turbidity tur = (Turbidity) request.getAttribute("tur");
                                    %>
                                    <tr>
                                        <td>
                                            Chlorophyll
                                        </td>
                                        <td>
                                            <%=chl.getA0()%>
                                        </td>
                                        <td>
                                            <%=chl.getA()%>
                                        </td>
                                        <td style="background: wheat">
                                            Not Available
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            SSC
                                        </td>
                                        <td>
                                            <%=ssc.getA0()%>
                                        </td>
                                        <td>
                                            <%=ssc.getA()%>
                                        </td>
                                        <td style="background: wheat">
                                            <%=ssc.getReferenceValue()%> mg/L
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            TDS
                                        </td>
                                        <td>
                                            <%=tds.getA0()%>
                                        </td>
                                        <td>
                                            <%=tds.getA()%>
                                        </td>
                                        <td style="background: wheat">
                                            <%=tds.getReferenceValue()%> mg/L
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            TSS
                                        </td>
                                        <td>
                                            <%=tss.getA0()%>
                                        </td>
                                        <td>
                                            <%=tss.getA()%>
                                        </td>
                                        <td style="background: wheat">
                                            <%=tss.getReferenceValue()%> mg/L
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            Turbidity
                                        </td>
                                        <td>
                                            <%=tur.getA0()%>
                                        </td>
                                        <td>
                                            <%=tur.getA()%>
                                        </td>
                                        <td style="background: wheat">
                                            Not Available
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                        <div class="form-group col-md-8">
                            <legend class="legend">Pollutants Reflectance Values (X) For Water Samples</legend>
                            <table class="table table-hover datatable-scroll-y">
                                <thead style="background: wheat">
                                    <tr>
                                        <th>ID</th>
                                        <th>Sample Site</th>
                                        <th>X_Chlorophyll</th>
                                        <th>X_SSC</th>
                                        <th>X_TDS</th>
                                        <th>X_TSS</th>
                                        <th>X_Turbidity</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        ArrayList<SamplePoint> points = (ArrayList<SamplePoint>) request.getAttribute("points");
                                        for(int i = 0; i < points.size(); i++) {
                                    %>
                                    <tr>
                                        <td>
                                            <%=points.get(i).getID()%>
                                        </td>
                                        <td>
                                            <%=points.get(i).getSampleSite()%>
                                        </td>
                                        <td>
                                            <%=points.get(i).getxOfChlorophyll()%>
                                        </td>
                                        <td>
                                            <%=points.get(i).getxOfSSC()%>
                                        </td>
                                        <td>
                                            <%=points.get(i).getxOfTDS()%>
                                        </td>
                                        <td>
                                            <%=points.get(i).getxOfTSS()%>
                                        </td>
                                        <td>
                                            <%=points.get(i).getxOfTurbidity()%>
                                        </td>
                                    </tr>
                                    <%}%>
                                </tbody>
                            </table>             
                        </div>
                        <div class="form-group col-md-8">
                            <button type="submit" class="btn btn-primary" onclick="return progressBar();" style="float:right;">Solve</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script>
            function progressBar() {
                NProgress.start();
                return true;
            }
        </script>
        <script type='text/javascript' src='/sdss/dwr/engine.js'></script>
        <script type='text/javascript' src='/sdss/dwr/interface/ToolManager.js'></script>
        <script type='text/javascript' src='/sdss/dwr/util.js'></script>
    </body>
</html>
