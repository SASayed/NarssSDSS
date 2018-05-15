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
            }.error-MCDA{
                background-color: rgb(232, 232, 232);
                font-size: 20px;
                border: 1px solid;
                height: 25px;
                padding: 10px 20px 50px 20px;
                text-align: center;
            }.error{

                color: red;
                font-size: 30px;

            }
        </style>
    </head>
    <body class="bodybg">
        <%@include file="../jspf/operatorheader.jspf" %>
        <div id="primary_nav_wrap">

        </div>
        <div style="background: #fff;">
            <div class="container modelcreate" style="padding-top: 2rem;">
                <%
                    boolean noTicketValue = (boolean) request.getAttribute("noTicketValue");
                    boolean noTicketValue1 = (boolean) request.getAttribute("noTicketValue1");
                    if (noTicketValue || noTicketValue1) {
                %>
                <div class="col-md-12" style="  padding-bottom: 50px;">
                    <legend>Message</legend>
                    <div class="error-MCDA"><p class="error">Sorry Server is Unreachable, Please Try Again Later</p></div>
                </div>
                <% }%>
                <div class="col-md-12">
                    <form action="" method="POST">

                        <div class="form-group col-md-12">
                            <legend class="legend">Performance Table</legend>
                            ${outputString4}
                        </div>
                        <div class="form-group col-md-4">
                            <legend class="legend">Alternatives</legend>
                            ${outputString1}
                        </div>


                        <div class="form-group col-md-4">
                            <legend class="legend">Criteria</legend>
                            ${outputString2}
                        </div>

                        <div class="form-group col-md-4">
                            <legend class="legend">Weights</legend>
                            ${outputString3}
                        </div>
                        <div id="progressBar" class="form-group col-md-12"></div>
                        <div class="form-group col-md-12">
                            <button type="submit" class="btn btn-primary" onclick="return progressBar();" style="float:right;">Run MCDA</button>
                        </div>
                    </form>
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
