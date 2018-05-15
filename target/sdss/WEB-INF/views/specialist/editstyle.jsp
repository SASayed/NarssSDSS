
<%@page import="org.narss.sdss.dto.color"%>
<%@page import="org.narss.sdss.dto.Style"%>
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
                            ArrayList<Style> styles = (ArrayList<Style>) request.getAttribute("StyleList");
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
 
           <div  id="editreport" class="col-md-8" style="width: 60%;">

                    <div style="padding-top: 24px;">
                        <form action='${contextPath}/EditStyle' method="POST">
                            <legend>Edit Style</legend>
                            <div class="form-group col-md-12">
                                <label>Name</label>
                                <input type="text" class="form-control" value='<%=request.getAttribute("styleName")%>' name="styleName" placeholder="The style Name"  readonly="readonly">
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="The style Name"></i>
                            </div>
                            <div class="form-group col-md-12">
                                <label for="exampleTextarea">Description</label>
                                <textarea class="form-control" value='<%=request.getAttribute("styleDescription")%>'name="styleDescription" rows="3"  placeholder="The style Description" required><%=request.getAttribute("styleDescription")%></textarea>
                           <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Give a brief description for what this style will do"></i>
                            </div>
                                 
                            <div class="form-group col-md-12">
                                <label>Data Type</label>
                                <select id="dataType" type="text" name="dataType" class="form-control"  required>
                                     <option value="<%=request.getAttribute("dataType")%>" selected ><%=request.getAttribute("dataType")%></option>
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
                                     <option value="" disabled selected ><%=request.getAttribute("highColor")%></option>
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
                                     <<option value="" disabled selected ><%=request.getAttribute("medcolor")%></option>
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
                                   <option value="" disabled selected ><%=request.getAttribute("lowcolor")%></option>
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

