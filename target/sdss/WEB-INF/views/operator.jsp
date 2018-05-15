
<%@page import="java.util.ArrayList"%>
<%@page import="org.narss.sdss.dto.CaseStudy"%>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
    <head>
        <%@include file="jspf/includes.jspf"%>
        <title>Operator Dashboard</title>
        <link rel="stylesheet" type="text/css" href="resources/css/icomoon/styles.css">
        <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,700" rel="stylesheet">
        <style>
            header .logo {
                padding-left: 120px;
            }
        </style>
    </head>
    <body class="bodybg">
        <%@include file="jspf/operatorheader.jspf" %>


        <div id="primary_nav_wrap">

        </div>
        <div style="background: #fff;">
            <div class="container modelcreate" style="padding-top: 2rem;">

                <div class="col-md-12">

                    <div style="padding-top: 24px;">
                        <table class="table table-hover datatable-scroll-y">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Case Studies</th>
                                    <th>Model</th>
                                    <th>MCDA</th>
                                    <th class="text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    ArrayList<CaseStudy> casesList = (ArrayList<CaseStudy>) request.getAttribute("caseList");
                                    if (casesList.isEmpty()) {
                                %>
                            <legend>No CaseStudy to display</legend>

                            <%  } else {              %>
                            <!-- <form action="" method="POST">-->

                            <%for (int i = 0; i < casesList.size(); i++) {%>

                            <tr>
                                <td>
                                    <%=i + 1%>

                                </td>
                                <td>
                                    <%=casesList.get(i).getName()%>
                                </td>
                                <td>
                                    <%=casesList.get(i).getModel()%>
                                </td>
                                <td>
                                    <%=casesList.get(i).getMcda()%>
                                </td>
                                <td class="text-center">
                                    <ul class="icons-list">
                                        <li class="dropdown">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                                <i class="icon-menu9"></i>
                                            </a>

                                            <ul class="dropdown-menu dropdown-menu-right" >
                                                <li><a href=""><i class="fa fa-pencil-square-o"></i> Edit</a></li>
                                                <li><a href="${contextPath}/run?case=<%=casesList.get(i).getId()%>"><i class="fa fa-play-circle"></i> Run</a></li>
                                                <li><a href=""><i class="fa fa-times"></i> Delete</a></li>

                                            </ul>
                                        </li>
                                    </ul>
                                </td>

                            </tr>
                            <input id="modelId" name="modelId" value="<%=casesList.get(i).getModel()%>" type="hidden"  class="form__input"/>
                            <input id="mcdaId" name="mcdaId" value="<%=casesList.get(i).getMcda()%>" type="hidden"  class="form__input"/>
                            <% }
                                }%>
                            </tbody>
                        </table>
                        <!--</form>-->
                    </div></div></div></div>

    </body>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="resources/js/bootstrap.js"></script>
    <script src="resources/js/stickybar.js"></script>
    <script src="resources/js/datatables/datatables.min.js"></script>
    <script src="resources/js/select2.min.js"></script>
    <script src="resources/js/datatables/datatables_basic.js"></script>
</html>