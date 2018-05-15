
<%@page import="org.narss.sdss.dto.Model"%>
<%@page import="org.narss.sdss.dto.Input"%>
<%@page import="java.util.List"%>
<%@page import="org.narss.sdss.dto.Tool"%>
<%@page import="java.util.ArrayList"%>
<%@page import ="javax.servlet.http.HttpSession"%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <%@include file="../jspf/includes_inner.jspf"%>
        <meta charset="utf-8">
        <title>Specialist Dashboard</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <style>
            header .logo {
                padding-left: 120px;
            } .error-MCDA{
                background-color: rgb(232, 232, 232);
                font-size: 20px;
                border: 1px solid;
                height: 25px;
                padding: 10px 20px 50px 20px;
                text-align: center;
            } .error{

                color: red;
                font-size: 30px;

            }
        </style>

        <link rel="stylesheet" type="text/css" href="../resources/css/editor.css">
        <!--  model css-->
        <link rel="stylesheet" type="text/css" href="../resources/css/model.css">
        <!-- Progress bar -->
        <link rel='stylesheet' href='../resources/css/nprogress.css'/>
        <!--Open Layer Libraries-->
        <link rel="stylesheet" href="../resources/css/ol.css" type="text/css">
        <!-- The line below is only needed for old environments like Internet Explorer and Android 4.x -->
        <script src="../resources/js/ol.js"></script>
        <!-- Query Builder -->
        <link href="../resources/css/query-builder.default.min.css" rel="stylesheet" type="text/css"/>
        <!--Query Builder Scripts -->
        <script src="../resources/js/jquery-3.1.1.slim.min.js" type="text/javascript"></script>
        <script src="../resources/js/bootstrap.js"></script>
        <script src="../resources/js/doT.js" type="text/javascript"></script>
        <script src="../resources/js/jQuery.extendext.js" type="text/javascript"></script>
        <script src="../resources/js/query-builder.min.js" type="text/javascript"></script>
        <!--  Progrss bar js-->
        <script src='../resources/js/nprogress.js'></script>
        

    <body class="bodybg">
        <%@include file="../jspf/specialistheader.jspf" %>
        <div id="primary_nav_wrap">
            <ul>
                <li class="current-menu-item"><a href="${contextPath}/specialist/modelling">Modelling</a></li>
                <li><a href="${contextPath}/specialist/MCDA">MCDA</a></li>
                <li><a href="${contextPath}/specialist/report">Reports</a></li>
                <li><a href="${contextPath}/specialist/style">Styling</a></li>
                <li><a href="${contextPath}/specialist/caseStudy">Case Study</a></li>
                <li><a href="${contextPath}/specialist/questions">Questions</a></li>
            </ul>
        </div>
        <div style="background: #fff;">
            <div class="container modelcreate" style="padding-top: 2.3rem;">
                <%
                                    String error = (String) session.getAttribute("error");
                                    if(error != null) {
                            %>
                    <div class="col-md-12" style="  padding-bottom: 50px;">
                        <legend>Message</legend>
                        <div class="error-MCDA"><p class="error">${error}</p></div>
                    </div>
                    <%
                        session.removeAttribute("error");
                    }%>
                <!--Models table -->
                <div class="col-md-4" style="width: 40%;">
                    <div style="padding-top: 24px;">
                        <table class="table table-hover datatable-scroll-y">
                            <thead>
                                <tr>
                                    <th>Models</th>
                                   <th class="text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    ArrayList<Model> models = (ArrayList<Model>) request.getAttribute("modelList");

                                    if (models.isEmpty()) {
                                %>

                                <legend>No Saved Models to Display</legend> 
                                <% } else {%>

                            <%for (int i = 0; i < models.size(); i++) {%>
                            <tr>
                                <td>
                                    <%=i + 1%>
                                </td>
                                <td>
                                    <%=models.get(i).getName()%>
                                </td>
                                <td class="text-center">
                                    <ul class="icons-list">
                                        <li class="dropdown">
                                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                                <i class="icon-menu9"></i>
                                            </a>
                       
                                            <ul class="dropdown-menu dropdown-menu-right">
                                                <% //request.setAttribute("model_id",models.get(i).getId()); %>
                                                <li><a id="<%=models.get(i).getId()+ ""%>"  href="${contextPath}/specialist/editmodel?modname=<%=models.get(i).getName()%>&modid=<%=models.get(i).getId()%>"onclick="Edit_Model(this.id)"><i class="fa fa-pencil-square-o"></i>
                                              Build Model</a></li>
                                      
                                        <li><a  href="${contextPath}/deleteModel?modid=<%=models.get(i).getId()%>"><i class="fa fa-times"></i> Delete</a></li>
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

               

                <!-- New Model Part -->
                <div class="col-md-8" style="width: 60%;" >
                                          
                        <form action="${contextPath}/saveModel" method="POST">
                            <legend>New Model</legend>
                            <div class="form-group col-md-12">
                                <label>Name</label>
                                <input type="text" class="form-control" name="modelName" placeholder="Model Name" required>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="The name of this Model"></i>
                            </div>
                            <div class="form-group col-md-12">
                                <label for="exampleTextarea">Description</label>
                                <textarea class="form-control" name="desc" rows="3" placeholder="Model Description" required></textarea>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Give a brief description for what this Model will do"></i>
                            </div>
                            <div class="form-group col-md-12">
                                <label>Run MCDA</label>
                                <select id="hasMCDA" type="text" name="hasMCDA" class="form-control"  required>
                                    <option value="" disabled selected>Select Option</option>
                                    <option value="true">Yes</option>
                                    <option value="false">No</option>
                                </select>
                                <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Will the Model Run MCDA?"></i>
                            </div>
                            <div class="form-group col-md-12">
                                <button type="submit" class="btn btn-primary" style="float:right;">Save</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>


    <!--Scripts of Graphic Library -->
    <script src="../resources/js/konva.js"></script>
    <script src="../resources/js/graphicLibrary.js"></script>

    <!--Scripts of dwr and tool managing -->
    <script type='text/javascript' src='/sdss/dwr/engine.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/ToolManager.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/ZPFS.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/LayerManager.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Properties.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Model.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Swap.js'></script>
    <script type='text/javascript' src='/sdss/dwr/util.js'></script>

     <script src='../resources/js/geoTools.js'></script> 
    <script src='../resources/js/map.js'></script>   


    <!--End of dwr scripts -->
    <script>
                                createQuery();
    </script>


    <!--<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>-->

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="../resources/js/stickybar.js"></script>
    <script src="../resources/js/sidebar.js"></script>
    <script src="../resources/js/datatables/datatables.min.js"></script>
    <script src="../resources/js/select2.min.js"></script>
    <script src="../resources/js/datatables/datatables_basic.js"></script>

</html>