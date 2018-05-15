<%-- 
    Document   : caseStudy
    Created on : Jan 9, 2017, 2:17:16 AM
    Author     : Administrator
--%>

<%@page import="org.narss.sdss.dto.Style"%>
<%@page import="org.narss.sdss.dto.Report"%>
<%@page import="org.narss.sdss.dto.CaseStudy"%>
<%@page import="org.narss.sdss.dto.MCDA"%>
<%@page import="org.narss.sdss.dto.Model"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
                                    ArrayList<CaseStudy> casesList = (ArrayList<CaseStudy>) request.getAttribute("caseList");
                                    if (casesList.isEmpty()) {
                                %>
                            <legend>No Saved Case Studies to Display</legend>

                            <%} else {%>

                            <%for (int i = 0; i < casesList.size(); i++) {%>
                            <tr>
                                <td>
                                    <%=i + 1%>
                                </td>
                                <td class="text-right">
                                    <%=casesList.get(i).getName()%>
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
                        <%
                             CaseStudy caseStudy = (CaseStudy)request.getAttribute("caseStudy");
                        %>
                        <legend>Edit Case Study</legend>
                        <div class="form-group col-md-12">
                            <label>Name</label>
                            <input type="hidden" value="<%=caseStudy.getName()%>" class="form-control" id="casenameHidden" name="casenameHidden">
                            <input type="text" value="<%=caseStudy.getName()%>" class="form-control" id="casename" name="casename"  placeholder="Case Study Name" disabled>
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="The name of this Case Study"></i>
                        </div>

                        <div class="form-group col-md-12">
                            <label for="exampleTextarea">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3"  placeholder="The CaseStudy Description"><%=caseStudy.getDescription()%></textarea>
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
                                <option value="<%=models.get(i).getName()%>" selected><%=models.get(i).getName()%></option>  


                                <%
                                    }
                                %>   
                            </select>
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the Model this Case Study will run on"></i>
                        </div>

                        <div class="form-group col-md-12">
                             
                            <label>MCDA</label>
                            <select id="mcda" type="text" name="mcda"   class="form-control" required>
                                <%
                                    String mcdaName =  (String) request.getAttribute("mcdaName");
                                    int mcdaId =  (int) request.getAttribute("mcdaId");
                                    
                             %>
                                <option value="<%=mcdaId%>" disabled selected><%=mcdaName%></option>
                                <%
                                    ArrayList<MCDA> mcdas = (ArrayList<MCDA>) request.getAttribute("mcdaList");
                                    for (int i = 0; i < mcdas.size(); i++) 
                                    {
                                %>
                                <option value="<%=mcdas.get(i).getId()%>"><%=mcdas.get(i).getName()%></option>  

                                <%
                                    }
                                %>   
                            </select>
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the MCDA that will be used by this Case Study"></i>
                        </div>
                            <div class="form-group col-md-12">
                              <%
                                    String reportName =  (String) request.getAttribute("reportName");
                                    int reportId =  (int) request.getAttribute("reportId");
                                    
                             %>
                            <label>report</label>
                            <select id="report" type="text" name="report"   class="form-control" required>
                               <option value="<%=reportId%>" selected><%=reportName%></option>
                                <%
                                    ArrayList<Report> report = (ArrayList<Report>) request.getAttribute("reportList");
                                    for (int f = 0; f < report.size(); f++) {
                                %>
                                <option value="<%=report.get(f).getId()%>"><%=report.get(f).getName()%></option>  

                                <%
                                    }
                                    //System.out.println(attributes[i]);
                                %>   
                            </select>
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the report that will be used by this Case Study"></i>
                        </div>
                            <div class="form-group col-md-12">
                              <%
                                    String StyleName =  (String) request.getAttribute("styleName");
                                    int styleId =  (int) request.getAttribute("styleId");
                                    
                             %>
                            <label>style</label>
                            <select id="style" type="text" name="style"   class="form-control" required>
                               <option value="<%=styleId%>" selected><%=StyleName%></option>
                                <%
                                    ArrayList<Style> style = (ArrayList<Style>) request.getAttribute("styleList");
                                    for (int f = 0; f < style.size(); f++) {
                                %>
                                <option value="<%=style.get(f).getId()%>"><%=style.get(f).getName()%></option>  

                                <%
                                    }
                                    //System.out.println(attributes[i]);
                                %>   
                            </select>
                            <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the report that will be used by this Case Study"></i>
                        </div>
                        <input type="hidden" id="caseIdHidden" name="caseIdHidden" value="<%=caseStudy.getId()%>" />
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
