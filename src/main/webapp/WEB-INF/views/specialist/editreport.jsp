
<%@page import="org.narss.sdss.dto.Report"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.narss.sdss.dto.Department"%>
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
        <script>
            function showDiv() {
       document.getElementById("savereport").style.display="none"; 
        document.getElementById("editreport").style.display = "block";
        return false;
    
}</script>
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
                                    <th>Reports</th>
                                    <th class="text-right">Actions</th>
                                </tr>
                            </thead>
                             <tbody>
                        <%
                            ArrayList<Report> reports = (ArrayList<Report>) request.getAttribute("reportList");
                            if (reports.isEmpty()) {
                        %>
                        <legend>No Saved Report to Display</legend>
                        <%} else {%>
                                <%for (int i = 0; i < reports.size(); i++) {%>
                                <tr>
                                    <td>
                                        <%=i + 1%>
                                    </td>
                                    <td>
                                        <%=reports.get(i).getName()%>
                                    </td>
                                    <td class="text-center">
                                        <ul class="icons-list">
                                            <li class="dropdown">
                                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                                    <i class="icon-menu9"></i>
                                                </a>

                                                <ul class="dropdown-menu dropdown-menu-right">
                                                    <li><a href="${contextPath}/specialist/editreport?report=<%=reports.get(i).getId()%>" onclick=""><i class="fa fa-pencil-square-o"></i> Edit</a></li>
                             
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
 
           <div  id="editreport" class="col-md-8" style="width: 60%;">

                    <div style="padding-top: 24px;">
                         <form action='${contextPath}/EditReport' method="POST">
                            <legend>Edit Report</legend>
                            <div class="form-group col-md-12">
                                <label>Name</label>
                                <input type="text" class="form-control" value='<%=request.getAttribute("Name")%>'  name="Name" placeholder="The Report Name"  readonly="readonly">
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="The Report Name"></i>
                            </div>
                            <div class="form-group col-md-12">
                                <label for="exampleTextarea">Description</label>
                                <textarea class="form-control" value='<%=request.getAttribute("Description")%>'name="Description" rows="3"  placeholder="The Report Description" required><%=request.getAttribute("Description")%></textarea>
                           <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Give a brief description for what this Report will do"></i>
                            </div>
                          
                            <div class="form-group col-md-12">
                                <label>Department</label>
                                <select id="DepartmentName" type="text" name="DepartmentName" class="form-control"  required>
                                    <option value="<%=request.getAttribute("deptid")%>"  selected><%=request.getAttribute("DepName")%></option>
                                    <%
                                        ArrayList<Department> department = (ArrayList<Department>) request.getAttribute("deptList");
                                        for (int i = 0; i < department.size(); i++) {
                                    %>
                                    
                                    <option value="<%=department.get(i).getId()%>"><%=department.get(i).getName()%></option>  


                                    <%
                                        }
                                    %>
                                </select>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the Department on which the Report will be applied"></i>
                            </div>
                               
                                     <div class="form-group col-md-12">
                                <label for="exampleTextarea">Type of chart </label>
                                
                                <select id="Chart" type="text" name="Chart" class="form-control"  required>
                                    <option value="<%=request.getAttribute("Chart")%>" selected > <%=request.getAttribute("Chart")%></option>
                                    
                                  <option value="Pie_Chart">Pie Chart</option>  
                                  <option value="Bar_Chart">Bar Chart</option>
                                 
                                  

                                    
                                </select>
                           <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Give a XML for  Report "></i>
                                   
                       
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
