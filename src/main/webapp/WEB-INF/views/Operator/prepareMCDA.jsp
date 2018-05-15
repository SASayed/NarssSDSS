<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
    <head>
        <%@include file="../jspf/includes_inner.jspf"%>
        <title>Operator Dashboard</title>
        <link href="../resources/css/query-builder.default.min.css" rel="stylesheet" type="text/css"/>
        <!-- Progress bar -->
        <link rel='stylesheet' href='../resources/css/nprogress.css'/>
        <!--Query Builder Scripts -->
        <script src="../resources/js/jquery-3.1.1.slim.min.js" type="text/javascript"></script>
        <script src="../resources/js/bootstrap.js"></script>
        <script src="../resources/js/doT.js" type="text/javascript"></script>
        <script src="../resources/js/jQuery.extendext.js" type="text/javascript"></script>

        <script src="../resources/js/query-builder.min.js" type="text/javascript"></script>
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

                <div class="col-md-12">
                    <legend class="legend">Prepare MCDA</legend>
                    <div>
                        <!--Model Builder -->
                        <div id="builder" class="form-group col-md-12">

                        </div>
                        <form action="${contextPath}/Operator/prepareMCDA" method="POST">
                            <div id="progressBar" class="form-group col-md-12"></div>
                            <div class="form-group col-md-3">
                                <legend>Alternatives</legend>
                                <div class="form-group">
                                    <select id="alternative" type="text" name="alternative" class="form-control" required>
                                        <option value="" disabled selected>Select</option>
                                        <%
                                            String[] attributes = (String[]) request.getAttribute("result");
                                            String[] swap_alias = (String[]) request.getAttribute("Swap_alias");
                                            String[] swap_real = (String[]) request.getAttribute("Swap_Real");
                                        %>
                                           <!-- <input type="hidden" value="<%=attributes%>" name="attributes" id="attributes"/>-->
                                        <%
                                            for (int i = 0; i < attributes.length; i++) {
                                                boolean Swap = false;
                                                String alias = "";
                                                for (int a = 0; a < swap_real.length; a++) {
                                                    if (attributes[i].equals(swap_real[a])) {
                                                        Swap = true;
                                                        alias = swap_alias[a];
                                                        break;
                                                    }
                                                }
                                                if (Swap) {
                                        %>
                                        <option value="<%=attributes[i]%>"><%=attributes[i]%></option>
                                        <%
                                        } else {%>
                                        <option value="<%=attributes[i]%>"><%=attributes[i]%></option>
                                        <%
                                                }
                                            }
                                            //System.out.println(attributes[i]);
                                        %>
                                    </select>
                                    <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="Select the attribute that will represent Alternatives"></i>

                                </div>
                            </div>


                            <div class="form-group col-md-3">
                                <legend>Criteria</legend>
                                <div class="form-group" id="criteriaDiv0">
                                    <select id="criteria" type="text" name="criteria"  class="form-control" required>
                                        <option value="" disabled selected>Select</option>
                                        <%
                                            for (int i = 0; i < attributes.length; i++) {
                                                boolean Swap = false;
                                                String alias = "";
                                                for (int a = 0; a < swap_real.length; a++) {
                                                    if (attributes[i].equals(swap_real[a])) {
                                                        Swap = true;
                                                        alias = swap_alias[a];
                                                        break;
                                                    }
                                                }
                                                if (Swap) {

                                        %>
                                        <option value="<%=attributes[i]%>"><%=attributes[i]%></option>
                                        <%
                                        } else {%>
                                        <option value="<%=attributes[i]%>"><%=attributes[i]%></option>
                                        <%

                                                }
                                            }
                                            //System.out.println(attributes[i]);
                                        %>

                                    </select>
                                          <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top"  title="Select the attribute that will represent Criteria"></i>
                                </div>
                            </div>
                            <div class="form-group col-md-3">
                                <legend>Preferences</legend>
                                <div class="form-group" id="prefDiv0">
                                    <select id="preference" type="text" name="preference" class="form-control" required>
                                        <option value="" disabled selected>Select</option>
                                        <option value="max">Max</option>
                                        <option value="min">Min</option>                           
                                    </select>
                                    <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top"  title="whether to Maximize or Minimize a specific Criteria" ></i>
                                </div>
                            </div>
                            <div class="form-group col-md-3">
                                <legend>Weights</legend>
                                <div class="form-group" id="weightDiv0">
                                    <input type="text" class="form-control" id="weight" name="weight"   placeholder="Value">
                                 <i class="fa fa-info-circle" aria-hidden="true" data-toggle="tooltip" data-placement="top" title="This value represents the Priority of a specific Criteria"></i>
                                </div>
                               
                            </div>

                            <div class="form-group col-md-12">
                                <button class="btn btn-primary" style="float:right;" onclick="duplicate()">Add</button>
                            </div>
                            <div class="form-group col-md-12">
                                <button type="submit" class="btn btn-primary" style="float:right;" onclick="return progressBar();">Submit</button>
                            </div>

                            <input type="hidden" value="<%=(String) request.getAttribute("lyrname")%>" name="lyrname"/>

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
    <script>
        $('#alternative option')[1].selected = true;
        // var attributes =  $("#attributes").val();
        var rules_basic = {
            condition: 'AND',
            rules: [{
                    id: 'District',
                    operator: 'equal',
                    value: 'Alalmin'
                }]
        };
        $('#builder').queryBuilder({
            filters: [{
                    id: 'District',
                    label: 'District',
                    type: 'string',
                    input:'select',
                    values: ['Cairo', 'Alexandria','Aswan','Asyut','Sohag', 'Alalmin']
                },
                {
                    id: 'SoilDepth',
                    label: 'SoilDepth',
                    type: 'double'
                },
                {
                    id: 'Slope',
                    label: 'Slope',
                    type: 'double'
                },
                {
                    id: 'Salinity',
                    label: 'Salinity',
                    type: 'double'
                },
                {
                    id: 'Temprature',
                    label: 'Temprature',
                    type: 'double'
                },
                {
                    id: 'Rainfull',
                    label: 'Rainfull',
                    type: 'double'
                },
                {
                    id: 'LimeStone',
                    label: 'LimeStone',
                    type: 'double'
                }, {
                    id: 'CaCO3',
                    label: 'CaCO3',
                    type: 'double'
                }, {
                    id: 'pH',
                    label: 'pH',
                    type: 'double'
                }, {
                    id: 'EC',
                    label: 'EC',
                    type: 'double'
                }],

            rules: rules_basic
        });
    </script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
    <script src="../resources/js/stickybar.js"></script>
    <script src="../resources/js/duplicate.js"></script>

</html>
