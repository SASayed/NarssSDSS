<%-- 
    Document   : caseStudy
    Created on : Jan 9, 2017, 2:17:16 AM
    Author     : Administrator
--%>

<%@page import="org.narss.sdss.dto.Style"%>
<%@page import="org.narss.sdss.dto.CaseStudy"%>
<%@page import="org.narss.sdss.dto.MCDA"%>
<%@page import="org.narss.sdss.dto.Model"%>
<%@page import="org.narss.sdss.dto.Report"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
                <li><a href="${contextPath}/specialist/MCDA">MCDA</a></li>
                <li><a href="${contextPath}/specialist/report">Reports</a></li>
                <li><a href="${contextPath}/specialist/style">Styling</a></li>
                <li class="current-menu-item"><a href="${contextPath}/specialist/caseStudy">Case Study</a></li>
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
                                    <th>CaseStudies</th>
                                    <th class="text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    ArrayList<CaseStudy> casesList = (ArrayList<CaseStudy>) request.getAttribute("casesList");
                                    if (casesList.isEmpty()) {
                                %>
                            <legend>No Saved Case Studies to Display</legend>

                            <%} else {%>

                            <%for (int i = 0; i < casesList.size(); i++) {%>
                            <tr>
                                <td>
                                    <%=i + 1%>
                                </td>
                                <td>
                                    <%=casesList.get(i).getName()%>
                                </td>
                                <td class="text-center">
                                    <ul class="icons-list">
                                        <li class="dropdown">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                                <i class="icon-menu9"></i>
                                            </a>

                                            <ul class="dropdown-menu dropdown-menu-right">
                                                <li><a href="${contextPath}/specialist/caseStudy/editCaseStudy?name=<%=casesList.get(i).getName()%>"><i class="fa fa-pencil-square-o"></i> Edit</a></li>
                                                <li><a href="${contextPath}/specialist/caseStudy/deleteCaseStudy?name=<%=casesList.get(i).getName()%>"><i class="fa fa-times"></i> Delete</a></li>
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
                <div class="col-md-8"style="width: 60%;">

                    <div style="padding-top: 24px;"></div>

                    <form action="" method="POST">
                        
                        <legend>New Case Study</legend>
                        <div class="form-group col-md-12">
                            <label>Name</label>
                            <input type="text" class="form-control" id="casename" name="casename"  placeholder="Case Study Name">
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="The name of this Case Study"></i>
                        </div>

                        <div class="form-group col-md-12">
                            <label for="exampleTextarea">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3"  placeholder="The CaseStudy Description"></textarea>
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Give a brief description for what this Case Study will do"></i>
                        </div>

                        <div class="form-group col-md-12">
                            <label>Model</label>
                            <select id="model" type="text" name="model"  class="form-control" required>
                                <option value="" disabled selected>Select Model</option>
                                <%
                                    ArrayList<Model> models = (ArrayList<Model>) request.getAttribute("modelsList");
                                    for (int i = 0; i < models.size(); i++) {
                                %>
                                <option value="<%=models.get(i).getName()%>"><%=models.get(i).getName()%></option>  


                                <%
                                    }
                                %>   
                            </select>
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the Model this Case Study will run on"></i>
                        </div>

                        <div class="form-group col-md-12">
                            <label>MCDA</label>
                            <select id="mcda" type="text" name="mcda"   class="form-control" required>
                                <option value="" disabled selected>Select MCDA</option>
                                <%
                                    ArrayList<MCDA> mcdas = (ArrayList<MCDA>) request.getAttribute("mcdasList2");
                                    for (int j = 0; j < mcdas.size(); j++) {
                                %>
                                <option value="<%=mcdas.get(j).getName()%>"><%=mcdas.get(j).getName()%></option>  

                                <%
                                    }
                                    //System.out.println(attributes[i]);
                                %>   
                            </select>
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the MCDA that will be used by this Case Study"></i>
                        </div>
                            <div class="form-group col-md-12">
                            <label>report</label>
                            <select id="report" type="text" name="report"   class="form-control" required>
                                <option value="" disabled selected>Select Report</option>
                                <%
                                    ArrayList<Report> report = (ArrayList<Report>) request.getAttribute("reportList");
                                    for (int k = 0; k < report.size(); k++) {
                                %>
                                <option value="<%=report.get(k).getId()%>"><%=report.get(k).getName()%></option>  

                                <%
                                    }
                                    //System.out.println(attributes[i]);
                                %>   
                            </select>
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the report that will be used by this Case Study"></i>
                        </div>
                                <div class="form-group col-md-12">
                            <label>Style</label>
                            <select id="style" type="text" name="style"   class="form-control" required>
                                <option value="" disabled selected>Select Style</option>
                                <%
                                    ArrayList<Style> style = (ArrayList<Style>) request.getAttribute("styleList");
                                    for (int t = 0; t <  style.size(); t++) {
                                %>
                                <option value="<%=style.get(t).getId()%>"><%=style.get(t).getName()%></option>  

                                <%
                                    }
                                    //System.out.println(attributes[i]);
                                %>   
                            </select>
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the style that will be used by this Case Study"></i>
                        </div>
                        <div class="form-group col-md-12">
                            <button type="submit" class="btn btn-primary" style="float:right;">Save</button>
                        </div>
                    </form> 

                </div>


            </div></div>

    </body>
    <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
    <script src="../resources/js/bootstrap.js"></script>
    <script src="../resources/js/stickybar.js"></script>
    <script src="../resources/js/datatables/datatables.min.js"></script>
    <script src="../resources/js/select2.min.js"></script>
    <script src="../resources/js/datatables/datatables_basic.js"></script>
</html>
