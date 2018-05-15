<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                    <legend>Messages</legend>
                        ${outputStringMessages}
                    <legend class="legend">Alternatives Values Plot</legend>
                        ${outputStringPlot}
                    <a href="${contextPath}/Operator/viewReport" class="btn btn-primary" onclick="return progressBar();" style="float:right;color: white;">Generate Reports</a>
                </div>

            </div>

        </div>
    </div>

</body>
<script>
        function progressBar() {
            NProgress.start();
            return true;
        }
    </script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
<script src="resources/js/stickybar.js"></script>


</html>
