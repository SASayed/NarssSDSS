
<%@page import="org.narss.sdss.dto.Style"%>
<%@page import="org.narss.sdss.daoImpl.StyleDaoImpl"%>
<%@page import="org.narss.sdss.dto.CaseStudy"%>
<%@page import="org.narss.sdss.dto.Model"%>
<%@page import="org.narss.sdss.daoImpl.CaseStudyDaoImpl"%>
<%@page import="org.narss.sdss.daoImpl.ModelDaoImpl"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.narss.sdss.dto.Question"%>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
    <head>
        <%@include file="jspf/includes.jspf"%>
        <title>Decision Maker Dashboard</title>
        <link rel="stylesheet" type="text/css" href="resources/css/icomoon/styles.css">
        <link href="resources/css/model.css" rel="stylesheet" type="text/css"/>
        <link href="resources/css/ol.css" rel="stylesheet" type="text/css"/>
        <link href="resources/css/model.css" rel="stylesheet" type="text/css"/>
        <!-- The line below is only needed for old environments like Internet Explorer and Android 4.x -->
        <script src="resources/js/polyfill.min.js" type="text/javascript"></script>
        <script src="resources/js/ol.js" type="text/javascript"></script>
    </head>
    <body class="bodybg">
        <%@include file="jspf/decision_makerheader.jspf" %>


        <div style="margin-top: 360px;" id="primary_nav_wrap">
        </div>
        <div style="background: #fff;">
            <div class="container" style="padding-top: 2rem;">

                <div class="col-md-4" style="width: 40%;">

                   
                    <%
                        ArrayList<CaseStudy> casesList = (ArrayList<CaseStudy>) request.getAttribute("casesList");
                        if (casesList.isEmpty()) {
                    %>
                    <legend>No Saved Case Studies to Display</legend>

                    <%} else {%>
                    <table class="table table-hover datatable-scroll-y">
                        <thead>
                            <tr>       
                                <th>Case Studies</th>
                                <th class="text-right">Actions</th>
                            </tr>
                        </thead>
                        <input type="hidden" value="" id="caseId" name="caseId"/>
                        <tbody>
                            <%for (int i = 0; i < casesList.size(); i++) {%>
                            <tr>
                                <td><%=i + 1%></td>
                                <td>
                                    <%=casesList.get(i).getName()%>
                                </td>
                               <% 
                                                    CaseStudyDaoImpl casestudydaoimpl=new CaseStudyDaoImpl();
                                                    CaseStudy casestudy;
                                                    ModelDaoImpl mdi= new ModelDaoImpl();
                                                    StyleDaoImpl stlimp= new StyleDaoImpl();
                                                int id= casesList.get(i).getId();
                                                String name=casesList.get(i).getName();
                                                casestudy =casestudydaoimpl.getCaseStudy(id);
                                                Model mdls=mdi.getModelById(casestudy.getModel());
                                                Style styl= stlimp.getStyleById(casestudy.getStyle());
                                                String layername=mdls.getResult_layer(); 
                                                String Stylename=styl.getName();
                                                
                                                %>
                                <td class="text-center">
                                    <ul class="icons-list">
                                        <li class="dropdown">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                                <i class="icon-menu9"></i>
                                            </a>

                                            <ul class="dropdown-menu dropdown-menu-right">
                                                <li><a href="${contextPath}/decision_maker/viewReport?caseid=<%=casesList.get(i).getId()%>"><i class="fa fa-file-pdf-o"></i> Report</a></li>
                                               <li><a id="viewmodel" onclick="return load('narss:<%=layername%>', '<%=Stylename%>');"><i class="fa fa-pencil-square-o"></i> Layer</a></li>
                                            </ul>
                                               <% if(name.startsWith("Water_Pollution"))
                                            {%>
                                            <ul class="dropdown-menu dropdown-menu-right">
                                                <li><a href="${contextPath}/decision_maker/ViewatReport"><i class="fa fa-file-pdf-o"></i> Report</a></li>
                                                <li><a id="viewmodel" onclick="loadpoint('Water_Validity', 'point');"><i class="fa fa-pencil-square-o"></i> Layer</a></li>
                                            </ul>
                                       <%}%>
                                        </li>
                                    </ul>
                                </td>
                               
                            </tr>
                            <% }
                                }%>
                        </tbody>
                    </table>
                         <button type="button" class="btn clear" id="AskQuestion" onclick="AskQuestion(this.id, 'close');"> Request New Case</button>

                </div>
                <div class="col-md-8"style="width: 60%;height: 400px;margin-top: -2rem;">

                    <div id="map" style="height: 100%;">

                        <div class="ui-dialog ui-widget ui-widget-content" id="popup">
                        </div>
                    </div>

                </div>


                <!-- New Question Part -->
                <div id="question" class="modal">
                    <div class="newModal-content">
                        <span id="close" class="close">&times;</span>
                        <form action="${contextPath}/sendQuestion" method="POST" class="popup_form">
                            <table class="table table-hover">
                                <thead></thead>
                                <tbody>
                                <th class="subtitle"> Request New Case</th>
                                <tr><td>
                                        <div class="form-group col-md-12">
                                            <label>Case Name</label>
                                            <input type="text" class="form-control" name="Question" placeholder="Your Question" required>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>   <div class="form-group col-md-12">
                                            <label>Case Category</label>
                                            <select id="categoriesName"  type="text" name="categoriesName" class="form-control" required>
                                                <option value="" disabled selected>Select Categories</option>
                                                <option value="Siteselection">Site selection</option>
                                                <option value="Locationallocation">Location allocation</option>
                                                <option value="Landuseselection">Land use selection</option>
                                                <option value="Landuseallocation">Land use allocation</option>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>  <div class="form-group col-md-12">
                                            <label>Case Scope</label>
                                            <select id="ScopeName"  type="text" name="ScopeName" class="form-control" required>
                                                <option value="" disabled selected>Select Scope</option>
                                                <option value="Water">Water</option>
                                                <option value="Soil">Soil</option>
                                                <option value="Agriculture">Agriculture</option>
                                                <option value="Space">Space</option>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <div class="form-group col-md-12">
                                            <label for="exampleTextarea">Case Description</label>
                                            <textarea class="form-control" name="desc" rows="3" placeholder="Question Description" required></textarea>
                                        </div>

                                    </td></tr><tr><td>
                                        <div class="form-group col-md-12">
                                            <button type="submit" class="btn btn-primary" style="float:right;">Send</button>
                                        </div>
                                    </td></tr>
                                </tbody>
                            </table>
                        </form>
                    </div>
                </div>

                <!-- End of New Model Part -->

            </div>
        </div>
 <script type='text/javascript' src='/sdss/dwr/engine.js'></script>
        <script src="resources/js/jquery-3.1.1.slim.min.js" type="text/javascript"></script>     
        <!--Scripts of dwr and tool managing -->
    <script type='text/javascript' src='/sdss/dwr/engine.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/ToolManager.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/ZPFS.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/LayerManager.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Properties.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Model.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Swap.js'></script>
    <script type='text/javascript' src='/sdss/dwr/util.js'></script>


    <script src='resources/js/geoTools.js'></script> 
        <script src="resources/js/desicionMaker.js"></script>

        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
        <script src="resources/js/bootstrap.js"></script>
        <script src="resources/js/stickybar.js"></script>
        <script src="resources/js/datatables/datatables.min.js"></script>
        <script src="resources/js/select2.min.js"></script>
        <script src="resources/js/datatables/datatables_basic.js"></script>
    </body>

</html>