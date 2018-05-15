<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@include file="../jspf/includes.jspf"%>
        <title>Specialist Dashboard</title>
        <link rel="stylesheet" type="text/css" href="../../resources/css/bootstrap-theme.css">
        <link rel="stylesheet" type="text/css" href="../../resources/css/bootstrap.css">
        <link rel="stylesheet" type="text/css" href="../../resources/css/theme.css">
        <style>
            header .logo {
                padding-left: 120px;
            }
        </style>
    </head>
    <body class="bodybg">
        <%@include file="../jspf/specialistheader.jspf" %>
        <div id="primary_nav_wrap">
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
        </div>
        <div style="background: #fff;">
            <div class="container modelcreate" style="padding-top: 2rem;">

                <form action='${contextPath}/GenerateReport' method="POST">
                    <legend>Messages</legend>
                    ${outputStringMessages}
                    <legend  class="legend">Alternatives Values Plot</legend>
                    ${outputStringPlot}
                    <div class="form-group col-md-12">
                            <button type="submit" class="btn btn-primary" onclick="return progressBar();" style="float:right;">Generate report</button>
                        </div>
                    </form>

            </div>

        </div>
    </div>

</body>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<script src="resources/js/stickybar.js"></script>


</html>
