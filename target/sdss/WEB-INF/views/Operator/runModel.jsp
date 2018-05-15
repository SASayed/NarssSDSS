<%-- 
    Document   : runModel
    Created on : Jan 12, 2017, 1:23:52 AM
    Author     : Administrator
--%>

<%@page import="java.io.File"%>
<%@page import="org.narss.sdss.dto.Tool"%>
<%@page import="org.narss.sdss.dto.Input"%>
<%@page import="java.util.List"%>
<%@page import="org.narss.sdss.dto.Model"%>
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
     <!--  Progrss bar js-->
        <script src='../resources/js/nprogress.js'></script>
        
        <style>
            header .logo {
                padding-left: 120px;
            }/*.toolname{
                    color: #ffffff;
     border: 1px solid black; 
    padding: 20px 20px 5px;
    border-radius: 15px;
    background-color: #063f5b;
            }*/
            
        </style>
    </head>
    <body class="bodybg">
        <%@include file="../jspf/operatorheader.jspf" %>
        <div id="primary_nav_wrap">
        </div>


        <div style="background: #fff;">

            <div class="container modelcreate" style="padding-top: 2rem;">



                <div class="col-md-12">
                    <div>
                        <form action="${contextPath}/Operator/runModel" method="POST">
                            <legend class="legend" style="margin-bottom: 0px !important; padding: 24px 0 24px 0;">Geoprocessing Model</legend>
                            <%
                                ////  Tools of Model
                                List<String> tools = (ArrayList<String>) request.getAttribute("toolNames");
                                List<String> inputNames = (ArrayList<String>) request.getAttribute("inputNames");
                                if (!tools.isEmpty()) {

                            %>

                            <table class="table table-hover">
                                <thead>
                                    <tr>
                                        <th style="font-size: 20px;text-align: center; color: #adadad "colspan="2">Model Tools</th>
                                    </tr>
                                </thead>
                                <tbody class="col-md-12" style="text-align: -webkit-center;">
                                    <% 
                                        for (int j = 0; j < tools.size(); j++) {
                                    %>
                                    <tr class="toolName">
                                        <td colspan=1>
                                            <%=tools.get(j)%>
                                        </td>
                                    </tr>
                                    
                                    <tr>
                                        <td><label><%="Layer "+ (j+1)%></label></td>
                                        <td>
                                            <input id="layer" type="text" placeholder="<%=inputNames.get(j)%>" class="form-control" readonly/>
                                        </td>
                                    </tr>

                                                <%

                                                        }
                                                    }%>
                                     
                                    
                                </tbody>
                            </table>
                            <div class="form-group col-md-8">
                                <input type="hidden" id="output_name" type="text" name="output_name" class="form-control" placeholder="test" required/>
                            </div>
                            <div class="form-group col-md-12">

                                <%
                                    String modelId = (String) request.getAttribute("modelId");
                                %>
                                <input type="hidden" value="<%=modelId%>" name="model_id" id="model_id"/>
                                <button type="submit" class="btn btn-primary" style="float:right;" onclick="return progressBar()" value="<%=modelId%>">Run Model</button>
                            </div>   
                            <div id="progressBar" class="form-group col-md-12"></div>
                        </form>


                    </div>
                </div>

                <!---Graphic Library Script view ---->
                <legend class="legend">Model View</legend>
                <input type="hidden" value="<%=request.getAttribute("counttool")%>" id="counttool" name='counttool'/>
                <div  id="container" style="overflow:scroll; width: 100% ; height: 50%; border-style:solid;"> </div>
            </div>
        </div>
                            <!--Scripts of Graphic Library -->
    <script src="../resources/js/konva.js"></script>
    <script src="../resources/js/graphicLibrary.js"></script>
        <script>
            function progressBar() {
                NProgress.start();
                return true;
            }
        </script>
         <script>
       
        
        var jsArr = new Array();
            <% 
                tools.remove(1);
                for (int y=0; y < tools.size(); y++) {
            %> 
                jsArr[<%= y %>] ="<%=tools.get(y)%>"; 
              
               
     <%}
     
 
     %>
     ToolObjects=document.getElementById('counttool').value;
     PrintToolObjects(ToolObjects,jsArr);
     </script>
        <script type='text/javascript' src='/sdss/dwr/engine.js'></script>
        <script type='text/javascript' src='/sdss/dwr/interface/ToolManager.js'></script>
        <script type='text/javascript' src='/sdss/dwr/util.js'></script>

        

    </body>
</html>
