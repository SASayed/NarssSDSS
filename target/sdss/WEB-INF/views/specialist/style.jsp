
<%@page import="org.narss.sdss.dto.color"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.http.client.fluent.Response"%>
<%@page import="org.apache.http.client.fluent.Request"%>
<%@page import="org.narss.sdss.controllers.Properties"%>
<%@page import="org.narss.sdss.dto.GeoLayer"%>
<%@page import="org.narss.sdss.dto.Style"%>
<%@page import="Manager.LayerManager"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@include file="../jspf/includes_inner.jspf"%>
        <title>Specialist Dashboard</title>
        <style>
            header .logo {
                padding-left: 120px;
            } .error-MCDA{
                background-color: rgb(232, 232, 232);
                font-size: 20px;
                border: 1px solid;
                height: 25px;
                padding: 10px 20px 50px 20px;
                text-align: center;
            } .error{

                color: red;
                font-size: 30px;

            }
        </style>
       
    </head>
    <body class="bodybg">
        <%@include file="../jspf/specialistheader.jspf" %>


        <div id="primary_nav_wrap">
            <ul>
                <li><a href="${contextPath}/specialist/modelling">Modelling</a></li>
                <li class="current-menu-item"><a href="${contextPath}/specialist/MCDA">MCDA</a></li>
                <li><a href="${contextPath}/specialist/report">Reports</a></li>
                <li><a href="${contextPath}/specialist/style">Styling</a></li>
                <li><a href="${contextPath}/specialist/caseStudy">Case Study</a></li>
                <li><a href="${contextPath}/specialist/questions">Questions</a></li>
            </ul>
        </div>
        <div style="background: #fff;">
            <div class="container modelcreate" style="padding-top: 2rem;">
                <%
                                    String error = (String) session.getAttribute("error");
                                    if(error != null) {
                            %>
                    <div class="col-md-12" style="  padding-bottom: 50px;">
                        <legend>Message</legend>
                        <div class="error-MCDA"><p class="error">${error}</p></div>
                    </div>
                    <%
                        session.removeAttribute("error");
                    }%>
                <div class="col-md-4" style="width: 40%;">
                    <div style="padding-top: 24px;">
                        <table class="table table-hover datatable-scroll-y">
                            <thead>
                                <tr>
                                    <th>Style</th>
                                    <th class="text-right">Actions</th>
                                </tr>
                            </thead>
                             <tbody>
                        <%
                            ArrayList<Style> styles = (ArrayList<Style>) request.getAttribute("styleList");
                            if (styles.isEmpty()) {
                        %>
                        <legend>No Saved Style to Display</legend>
                        <%} else {%>
                                <%for (int i = 0; i < styles.size(); i++) {%>
                                <tr>
                                    <td>
                                        <%=i + 1%>
                                    </td>
                                    <td>
                                        <%=styles.get(i).getName()%>
                                    </td>
                                    <td class="text-center">
                                        <ul class="icons-list">
                                            <li class="dropdown">
                                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                                    <i class="icon-menu9"></i>
                                                </a>

                                                <ul class="dropdown-menu dropdown-menu-right">
                                                    <li><a href="${contextPath}/specialist/editStyle?style=<%=styles.get(i).getId()%>" onclick=""><i class="fa fa-pencil-square-o"></i> Edit</a></li>
                                                    
                                                    <li><a href="${contextPath}/deleteStyle?style=<%=styles.get(i).getId()%>"><i class="fa fa-times"></i> Delete</a></li>

                                                </ul>
                                            </li>
                                        </ul>
                                    </td>
                                </tr>
                                <% }
                                    }%>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div id="savereport" class="col-md-8" style="width: 60%; " >

                    <div style="padding-top: 24px;">
                         <form action='${contextPath}/specialist/saveStyle' method="POST">
                            
                             <legend>New Style</legend>
                            <div class="form-group col-md-12">
                                <label>Name:</label>
                                <input type="text" class="form-control"   name="styleName" placeholder="The Style Name"  required>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="The Style Name"></i>
                            </div>
                                   <div class="form-group col-md-12">
                                <label>Description: </label>
                                
                                <textarea class="form-control" name="styleDescription" rows="3"  placeholder="Write introduction for your style" required></textarea>
                           <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Write the description of a style"></i>
                                   
                       
                    </div> 
                          
                            <div class="form-group col-md-12">
                                <label>Data Type:</label>
                                <select id="dataType" type="text" name="dataType" class="form-control"  required>
                                    <option value="" disabled selected>Select Data Type</option>
                                    <option value="Point" >Point</option>
                                    <option value="Line" >Line</option>
                                    <option value="Polygon" >Polygon</option>
                                    <option value="Raster" >Raster</option>
        
                                </select>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the datatype for your style"></i>
                            </div>
                             
                           <div class="form-group col-md-12">
                                <label>Color of high values :</label>
                               
                                    <select id="highColor" type="text" name="highcolor" class="form-control"  required>
                                    <option value="" disabled selected>Select color</option>
                                    <%
                                        ArrayList<color> col = (ArrayList<color>) request.getAttribute("colorList");
                                        for (int i = 0; i < col.size(); i++) {
                                    %>
                                    <option value="<%=col.get(i).getCode()%>"><%=col.get(i).getName()%></option>  


                                    <%
                                        }
                                    %>
                                </select>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the color of the layer"></i>
                            </div>
                                   <div class="form-group col-md-12">
                                <label>Color of intermediate values :</label>
                                <select id="medColor" type="text" name="medcolor" class="form-control"  required>
                                     <option value="" disabled selected>Select color</option>
                                    <%
                                        ArrayList<color> col1 = (ArrayList<color>) request.getAttribute("colorList");
                                        for (int i = 0; i < col1.size(); i++) {
                                    %>
                                    <option value="<%=col1.get(i).getCode()%>"><%=col1.get(i).getName()%></option>  


                                    <%
                                        }
                                    %>
        
                                </select>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the color of the layer"></i>
                            </div>
                                   <div class="form-group col-md-12">
                                <label>Color of low values :</label>
                                <select id="lowColor" type="text" name="lowcolor" class="form-control"  required>
                                   <option value="" disabled selected>Select color</option>
                                    <%
                                        ArrayList<color> col2 = (ArrayList<color>) request.getAttribute("colorList");
                                        for (int i = 0; i < col2.size(); i++) {
                                    %>
                                    <option value="<%=col2.get(i).getCode()%>"><%=col2.get(i).getName()%></option>  


                                    <%
                                        }
                                    %>
        
                                </select>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the color of the layer"></i>
                            </div>
                                     
                          
                       
                          
                                <div class="form-group col-md-12">
                                <button type="submit" class="btn btn-primary" style="float:right;">Save</button>
                            </div>
                         </form>
                </div>
          
            </div>

        </div>

    </body>

    
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>   
    <script src="../resources/js/bootstrap.js"></script>
    <script src="../resources/js/stickybar.js"></script>
    <script src="../resources/js/datatables/datatables.min.js"></script>
    <script src="../resources/js/select2.min.js"></script>
    <script src="../resources/js/datatables/datatables_basic.js"></script>
    <script src="../resources/js/geoTools.js" type="text/javascript"></script>

 
</html>

