<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@include file="../jspf/includes.jspf"%>
        <title>Specialist Dashboard</title>
        <link rel="stylesheet" type="text/css" href="../../resources/css/bootstrap-theme.css">
        <link rel="stylesheet" type="text/css" href="../../resources/css/bootstrap.css">
        <link rel="stylesheet" type="text/css" href="../../resources/css/theme.css">
        <!-- Progress bar -->
        <link rel='stylesheet' href='../../resources/css/nprogress.css'/>
        <!--  Progrss bar js-->
        <script src='../../resources/js/nprogress.js'></script>
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

                <div class="col-md-12">
                    <legend class="legend">Prepare MCDA</legend>
                    <div>
                        <form action="${contextPath}/specialist/MCDA/testMCDA" method="POST">
                            <%
                                String resultLayer = (String) request.getAttribute("resultLayer");
                            %>
                            <div class="form-group col-md-3">
                                <legend>Alternatives</legend>
                                <div class="form-group">
                                    <select id="alternative"  type="text" name="alternative" class="form-control" required>
                                        <option value="" disabled selected>Select</option>
                                        <%
                                            String[] attributes = (String[]) request.getAttribute("result");
                                            for (int i = 0; i < attributes.length; i++) {

                                        %>
                                        <option value="<%=attributes[i]%>"><%=attributes[i]%></option>
                                        <%
                                            }
                                            //System.out.println(attributes[i]);
                                        %>

                                    </select>
                                    <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the attribute that will represent Alternatives" ></i>

                                </div>
                            </div>


                            <div class="form-group col-md-3">
                                <legend>Criteria</legend>
                                <div class="form-group" id="criteriaDiv0">
                                    <select id="criteria"  type="text" name="criteria" class="form-control" required>
                                        <option value="" disabled selected>Select</option>
                                        <%
                                            for (int i = 0; i < attributes.length; i++) {

                                        %>
                                        <option value="<%=attributes[i]%>"><%=attributes[i]%></option>
                                        <%
                                            }
                                            //System.out.println(attributes[i]);
                                        %>

                                    </select>
                                    <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the attribute that will represent Criteria" ></i>
                                </div>
                            </div>
                            <div class="form-group col-md-3">
                                <legend>Preferences</legend>
                                <div id="prefDiv0" class="form-group">
                                    <select id="preference"  type="text" name="preference" class="form-control" required>
                                        <option value="" disabled selected>Select</option>
                                        <option value="max">Max</option>
                                        <option value="min">Min</option>                           
                                    </select>
                                    <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="whether to Maximize or Minimize a specific Criteria"></i>
                                </div>
                            </div>
                            <div class="form-group col-md-3">
                                <legend>Weights</legend>
                                <div id="weightDiv0" class="form-group">
                                    <input type="text" class="form-control" id="weight" name="weight" placeholder="Value">
                                    <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="This value represents the Priority of a specific Criteria"></i>
                                </div>
                            </div>

                            <div class="form-group col-md-12">
                                <button class="btn btn-primary" style="float:right;" onclick="duplicate()">Add</button>
                            </div>
                            <div class="form-group col-md-12">
                                <button type="submit" class="btn btn-primary" onclick="return progressBar();" style="float:right;">Submit</button>
                            </div>
                            <div id="progressBar" class="form-group col-md-12"></div>
                            <input type="hidden" id="resultLayer" name="resultLayer" value="<%=resultLayer%>">
                        </form>

                    </div>

                </div>

            </div>
        </div>

    </body>
    <script>
          $('#alternative option')[1].selected = true;
        function progressBar() {
            NProgress.start();
            return true;
        }
    </script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
    <script src="../../resources/js/stickybar.js"></script>
    <script src="../../resources/js/duplicate.js"></script>

</html>
