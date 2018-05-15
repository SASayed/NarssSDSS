<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@include file="jspf/includes.jspf"%>
        <title>Specialist Dashboard</title>
        <style>
            header .logo {
                padding-left: 120px;
            }
        </style>
    </head>
    <body class="bodybg">

        <%@include file="jspf/specialistheader.jspf" %>

        
        <div class="container modelcreate" style="padding-top: 24rem;">
<div class="col-md-12 text-center">
                <a style="  color: #9a846f;  padding: 12px 23px; text-align: center; background: #fff; border-radius: .5rem;"
                   href="${contextPath}/specialist/exploreData">Explore Data</a>    
            </div>
            
            <div class="col-md-4 item-parent">
                <div class="item text-center">
                    <img class="item-icon" src="resources/media/destroyed-planet.svg">
                    <a href="${contextPath}/specialist/modelling">Modelling</a>
                    <p>Geoprocessing workflow</p>
                </div>
            </div>
            <div class="col-md-4 item-parent">
                <div class="item text-center">
                    <img class="item-icon" src="resources/media/liftoff-1.svg">
                    <a href="${contextPath}/specialist/MCDA">MCDA</a>
                    <p>Multiple Criteria Decision Analysis</p>
                </div>
            </div>
            <div class="col-md-4 item-parent">
                <div class="item text-center">
                    <img class="item-icon" src="resources/media/monitors.svg">
                    <a href="${contextPath}/specialist/report">Reports</a>
                    <p>Principal Investigator Report Management</p>
                </div>
            </div>
            <div class="col-md-4 item-parent">
                <div class="item text-center">
                    <img class="item-icon" src="resources/media/observatory.svg">
                    <a href="${contextPath}/specialist/questions">Questions</a>
                    <p>Requested Case Studies</p>
                </div>
            </div>
            <div class="col-md-4 item-parent">
                <div class="item text-center">
                    <img class="item-icon" src="resources/media/planet-earth.svg">
                    <a href="${contextPath}/specialist/style">Styling</a>
                    <p>Layer Style Management</p>
                </div>
            </div>
            <div class="col-md-4 item-parent">
                <div class="item text-center">
                    <img class="item-icon" src="resources/media/observatory.svg">
                    <a href="${contextPath}/specialist/caseStudy">Case Study</a>
                    <p>Case Study Management</p>
                </div>
            </div>
        </div>


        <footer>

            <div class="footer">
                All copyrights reserved © 2016
            </div>
        </footer>


    </body>

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
    <script src="resources/js/stickybar.js"></script>


</html>
