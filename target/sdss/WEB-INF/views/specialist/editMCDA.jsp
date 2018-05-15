
<%@page import="org.narss.sdss.dto.Model"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.narss.sdss.dto.MCDA"%>
<%@page import="java.util.List"%>
<%@page import="org.narss.sdss.controllers.MCDAList"%>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@include file="../jspf/includes_inner2.jspf"%>
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
                <li><a href="${contextPath}/specialist/MCDA">MCDA</a></li>
                <li><a href="${contextPath}/specialist/report">Reports</a></li>
                <li><a href="${contextPath}/specialist/style">Styling</a></li>
                <li class="current-menu-item"><a href="${contextPath}/specialist/caseStudy">Case Study</a></li>
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
                                    <th>ID</th>
                                    <th class="text-right">Name</th>
                                </tr>
                            </thead>
                             <tbody>
                        <%
                            ArrayList<MCDA> mcdas = (ArrayList<MCDA>) request.getAttribute("mcdaList");
                            if (mcdas.isEmpty()) {
                        %>
                        <legend>No Saved MCDAs to Display</legend>
                        <%} else {%>
                                <%for (int i = 0; i < mcdas.size(); i++) {%>
                                <tr>
                                    <td>
                                        <%=i + 1%>
                                    </td>
                                    <td class="text-right">
                                        <%=mcdas.get(i).getName()%>
                                    </td>
                                    
                                </tr>
                                <% }
                                    }%>
                            </tbody>
                        </table>
                    </div>
                </div>
                 
                <div class="col-md-8" style="width: 60%;">

                    <div style="padding-top: 24px;">
                        <form action="" method="POST"  >
                            <%
                                MCDA mcda = (MCDA) request.getAttribute("mcda");
                            %>
                            <legend>Edit MCDA</legend>
                            <div class="form-group col-md-12">
                                <label>Name</label>
                                <input type="hidden"  value="<%=mcda.getName()%>" class="form-control" name="mcdaNameHidden">
                                <input type="text"  value="<%=mcda.getName()%>" class="form-control" name="mcdaName" placeholder="The MCDA Name" required disabled>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="The MCDA Name"></i>
                            </div>
                            <div class="form-group col-md-12">
                                <label for="exampleTextarea">Description</label>
                                <textarea class="form-control" name="mcdaDescription" rows="3"  placeholder="The MCDA Description" required><%=mcda.getDescription()%></textarea>
                           <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Give a brief description for what this MCDA will do"></i>
                            </div>
                            <div class="form-group col-md-12">
                                <label>Technique</label>
                                <select id="serviceName" type="text" name="serviceName" class="form-control"  required>
                                    <option value="<%=mcda.getAlgorithm()%>" selected><%=mcda.getAlgorithm()%></option>
                                    <option value="weightedSum-PyXMCDA.py">Weighted Sum</option>
                                    <option value="weightedSum-PyXMCDA.py">Ordered Weighted Averaging</option>
                                    <option value="weightedSum-PyXMCDA.py">Random Criteria Weights</option>
                                    <option value="weightedSum-PyXMCDA.py">Sort Alternatives Values</option>
                                    <option value="weightedSum-PyXMCDA.py">Thresholds Sensitivity Analysis</option>
                                    <option value="weightedSum-PyXMCDA.py">Regression</option>
                                </select>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the MCDA Technique"></i>
                            </div>
                            <div class="form-group col-md-12">
                                <label>Model</label>
                                <select id="modelName" type="text" name="modelName" class="form-control"  required>
                                    <%
                                       String modelName = (String) request.getAttribute("mdlName");
                                    %>
                                    <option value="<%=modelName%>" selected><%=modelName%></option>
                                    <%
                                        ArrayList<Model> models = (ArrayList<Model>) request.getAttribute("modelsList");
                                        for (int i = 0; i < models.size(); i++) {
                                    %>
                                    <option value="<%=models.get(i).getName()%>"><%=models.get(i).getName()%></option>  


                                    <%
                                        }
                                    %>
                                </select>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the model on which the MCDA will be applied"></i>
                            </div>
                            <!--<div class="form-group col-md-4">
                                <legend>Alternatives</legend>
                                <div class="form-group">
                                    <label>Name</label>
                                    <input type="text" class="form-control" id="mcdaname" placeholder="Alternativ name">
                                </div>
                                <button class="btn btn-secondary">Add</button>
                            </div>


                            <div class="form-group col-md-4">
                                <legend>Criteria</legend>
                                <div class="form-group">
                                    <input type="text" class="form-control" id="mcdaname" placeholder="Criteria Name">
                                </div>
                                <div class="form-group">
                                    <input type="text" class="form-control" id="mcdaname" placeholder="Criteria Prefrence">
                                </div>
                                <div class="form-group">
                                    <input type="text" class="form-control" id="mcdaname" placeholder="Criteria Threshold">
                                </div>
                                <div class="form-group">
                                    <input type="text" class="form-control" id="mcdaname" placeholder="Criteria Value">
                                </div>


                                <button class="btn btn-secondary">Add</button>
                            </div>

                            <div class="form-group col-md-4">
                                <legend>Weights</legend>
                                <div class="form-group">
                                    <label>Criterion ID</label>
                                    <input type="text" class="form-control" id="mcdaname" placeholder="Criterion ID">
                                </div>
                                <div class="form-group">
                                    <label>Value</label>
                                    <input type="text" class="form-control" id="mcdaname" placeholder="Value">
                                </div>
                                <button class="btn btn-secondary">Add</button>
                            </div>-->
                            <input type="hidden" id="mcdaIdHidden" name="mcdaIdHidden" value="<%=mcda.getId()%>" />
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

</html>