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
                <div class="col-md-12">
                    <form action="" method="POST">
                        
                        <div class="form-group col-md-12">
                            <legend class="legend">Water Samples Analysis Results</legend>
                            <table class="table table-hover datatable-scroll-y">
                                <thead style="background: wheat">
                                    <tr>
                                        <th>ID</th>
                                        <th>Sample Site</th>
                                        <th>Chlorophyll</th>
                                        <th>SSC</th>
                                        <th>TDS</th>
                                        <th>TSS</th>
                                        <th>Turbidity</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        ArrayList<SamplePoint> points = (ArrayList<SamplePoint>) request.getAttribute("points");
                                        double[][] results = (double[][]) request.getAttribute("results");
                                        Chlorophyll chl = (Chlorophyll) request.getAttribute("chl");
                                        SSC ssc = (SSC) request.getAttribute("ssc");
                                        TDS tds = (TDS) request.getAttribute("tds");
                                        TSS tss = (TSS) request.getAttribute("tss");
                                        Turbidity tur = (Turbidity) request.getAttribute("tur");
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
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td><%=results[i][0]%></td>
                                        <td><%=results[i][1]%></td>
                                        <td><%=results[i][2]%></td>
                                        <td><%=results[i][3]%></td>
                                        <td><%=results[i][4]%></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td></td>
                                        <td style="background: yellowgreen">Valid</td>
                                        <%if(results[i][1] <= ssc.getReferenceValue()) {%>
                                        <td style="background: yellowgreen">Valid</td>
                                        <%}else {%>
                                        <td style="background: orangered">Invalid</td>
                                        <%} if(results[i][2] <= tds.getReferenceValue()){%>
                                        <td style="background: yellowgreen">Valid</td>
                                        <%}else{%>
                                        <td style="background: orangered">Invalid</td>
                                        <%} if(results[i][3] <= tss.getReferenceValue()) {%>
                                        <td style="background: yellowgreen">Valid</td>
                                        <%} else {%>
                                        <td style="background: orangered">Invalid</td>
                                        <%} %>
                                        <td style="background: yellowgreen">Valid</td>
                                    </tr>
                                    <%}%>
                                </tbody>
                            </table>             
                        </div>
                         <div class="form-group col-md-8">
                             <a href="${contextPath}/Operator/viewWaterReport" class="btn btn-primary" onclick="return progressBar();" style="float:right;color: white;">Generate Reports</a>
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
