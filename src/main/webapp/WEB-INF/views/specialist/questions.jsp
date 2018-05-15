


<%@page import="org.narss.sdss.dto.Question"%>
<%@page import="java.util.ArrayList"%>
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
    </head>
    <body class="bodybg">
        <%@include file="../jspf/specialistheader.jspf" %>


        <div id="primary_nav_wrap">
            <ul>
                <li><a href="${contextPath}/specialist/modelling">Modelling</a></li>
                <li><a href="${contextPath}/specialist/MCDA">MCDA</a></li>
                <li><a href="${contextPath}/specialist/report">Reports</a></li>
                <li><a href="${contextPath}/specialist/style">Styling</a></li>
                <li><a href="${contextPath}/specialist/caseStudy">Case Study</a></li>
                <li class="current-menu-item"><a href="${contextPath}/specialist/questions">Questions</a></li>
            </ul>
        </div>
        <div style="background: #fff;">
            <div class="container modelcreate" style="padding-top: 2rem;">
                <div class="col-md-10">
                    <div style="padding-top: 24px;">
                        <%
                            ArrayList<Question> questionsList = (ArrayList<Question>) request.getAttribute("questionsList");
                            if (questionsList.isEmpty()) {
                        %>
                        <legend>No Saved Requests to Display</legend>
                        <%} else {%>
                        <legend>Requested Cases</legend>
                        <table class="table table-hover datatable-scroll-y">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Scope</th>
                                    <th>Category</th>
                                    <th class="text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%for (int i = 0; i < questionsList.size(); i++) {%>
                                <tr>
                                    <td>
                                        <%=i + 1%>
                                    </td>
                                    <td>
                                        <%=questionsList.get(i).getQuestion()%>
                                    </td>
                                    <td>
                                        <%=questionsList.get(i).getQuestionDescription()%>
                                    </td>
                                    <td>
                                        <%=questionsList.get(i).getScopeName()%>
                                    </td>
                                    <td>
                                        <%=questionsList.get(i).getCategoryName()%>
                                    </td>
                                    <td class="text-center">
                                        <ul class="icons-list">
                                            <li class="dropdown">
                                                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                                    <i class="icon-menu9"></i>
                                                </a>

                                                <ul class="dropdown-menu dropdown-menu-right">
                                                    <li><a href="${contextPath}/specialist/modelling"><i class="fa fa-pencil-square-o"></i> Solve</a></li>

                                                    <li><a href="${contextPath}/specialist/questions/deleteQuestion?name=<%=questionsList.get(i).getQuestion()%>"><i class="fa fa-times"></i> Delete</a></li>

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
