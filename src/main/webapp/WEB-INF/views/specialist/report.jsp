
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
                                                    <li><a href="${contextPath}/testReport?report=<%=reports.get(i).getId()%>"><i class="fa fa-play-circle"></i> Test</a></li>
                                                    <li><a href="${contextPath}/DeleteReport?report=<%=reports.get(i).getId()%>"><i class="fa fa-times"></i> Delete</a></li>

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
                         <form action='${contextPath}/specialist/saveReport' method="POST">
                            
                             <legend>New Report</legend>
                            <div class="form-group col-md-12">
                                <label>Name</label>
                                <input type="text" class="form-control"   name="reportName" placeholder="The Report Name"  required>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="The Report Name"></i>
                            </div>
                                   <div class="form-group col-md-12">
                                <label>What is your report about ?</label>
                                
                                <textarea class="form-control" name="reportDescription" rows="3"  placeholder="write introduction for your report" required></textarea>
                           <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Write a brief introduction of you case-study"></i>
                                   
                       
                    </div> 
                          
                            <div class="form-group col-md-12">
                                <label>Department</label>
                                <select id="DepartmentName" type="text" name="DepartmentName" class="form-control"  required>
                                    <option value="" disabled selected>Select Department</option>
                                    <%
                                        ArrayList<Department> departments = (ArrayList<Department>) request.getAttribute("deptList");
                                        for (int i = 0; i < departments.size(); i++) {
                                    %>
                                    <option value="<%=departments.get(i).getId()%>"><%=departments.get(i).getName()%></option>  


                                    <%
                                        }
                                    %>
                                </select>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the Department on which the Report will be applied"></i>
                            </div>
                               
                           
                                
                          
                                       <div class="form-group col-md-12">
                                <label for="exampleTextarea">Type of chart </label>
                                
                                <select id="chart" type="text" name="chart" class="form-control"  required>
                                    <option value="" disabled selected>Select Chart Type</option>
                                    
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
           <div  id="editreport" class="col-md-8" style="width: 60%; display: none;">

                    <div style="padding-top: 24px;">
                         <form action='${contextPath}/EditReport' method="POST">
                            <legend>New Report</legend>
                            <div class="form-group col-md-12">
                                <label>Name</label>
                                <input type="text" class="form-control" value='<%=request.getAttribute("Name")%>'  name="Name" placeholder="The Report Name"  required>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="The Report Name"></i>
                            </div>
                            <div class="form-group col-md-12">
                                <label for="exampleTextarea">Description</label>
                                <textarea class="form-control" value='<%=request.getAttribute("Description")%>'name="Description" rows="3"  placeholder="The Report Description" required></textarea>
                           <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Give a brief description for what this Report will do"></i>
                            </div>
                          
                            <div class="form-group col-md-12">
                                <label>Department</label>
                                <select id="DepartmentName" type="text" name="Name" class="form-control"  required>
                                    <option value='' disabled selected>Select Departments</option>
                                    <%
                                        ArrayList<Department> department = (ArrayList<Department>) request.getAttribute("deptList");
                                        for (int i = 0; i < department.size(); i++) {
                                    %>
                                    <option value="<%=request.getAttribute("DepName")%>"><%=department.get(i).getName()%></option>  


                                    <%
                                        }
                                    %>
                                </select>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the Department on which the Report will be applied"></i>
                            </div>
                               
                            <div class="form-group col-md-12">
                                <label for="exampleTextarea">Report XML</label>
                                <textarea class="form-control" value='<%=request.getAttribute("xml")%>'name="xml" rows="3"  placeholder="The Report XML " required></textarea>
                           <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Give a xml for  Report "></i>
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
