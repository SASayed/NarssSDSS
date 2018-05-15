
<%@page import="org.narss.sdss.dto.GeoLayer"%>
<%@page import="org.narss.sdss.dto.Model"%>
<%@page import="org.narss.sdss.dto.Input"%>
<%@page import="java.util.List"%>
<%@page import="org.narss.sdss.dto.Tool"%>
<%@page import="java.util.ArrayList"%>
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
    
   
    </head>
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
                <!--Models table -->
                <%  // String empcode = (String)request.getParameter("recievedid"); 
//HttpSession sess = request.getSession(true);
                                       //set a string session attribute
            // sess.setAttribute("modelid", empcode);
                %>
  <!-- Model Editing -->
                <div id="EditModel" class="col-md-8" style="" >
                    <div>
                        <!-- For Model Editing-->
                        <div id="progressBar"></div>
                        <div>
                            <div class="pop-title"> <h1 id="title" style="color:#0a3e57;">Model Tools</h1></div>
                            <sidebar class="col-md-4">
                                <div class="sidebar">
                                    <ul>
                                        <li>
                                            <a class="side"><i class="fa fa-cog"></i>Extract Tools</a>
                                            <ul class="closed">
                                                <li style="display: none;"><i class="fa fa-mouse-pointer"></i>
                                                    <a  style="color: #292f36;" href="${contextPath}/">Select</a></li>
                                                <li style="display: none;"><i class="fa fa-map-marker"></i>
                                                    <a  style="color: #292f36;" href="${contextPath}/">By location</a></li>
                                                <li><i class="fa fa-adjust"></i>
                                                    <a  style="color: #292f36;" href="${contextPath}/clip" >Clip</a>
                                                </li>
                                                <li><i class="fa fa-expand"></i>
                                                    <a  style="color: #292f36;" href="${contextPath}/split">Split</a></li>
                                            </ul>
                                        </li>
                                        <li>
                                            <a class="side active"><i class="fa fa-object-ungroup"></i>Overlay Tools</a>
                                            <ul class="closed">
                                                <li><i class="fa fa-arrows-alt"></i>
                                                    <a  style="color: #292f36;"  href="${contextPath}/intersect" >Intersect</a></li>
                                                <li><i class="fa fa-object-group"></i>
                                                    <a  style="color: #292f36;" href="${contextPath}/union">Union</a></li>
                                                <li><i class="fa fa-mouse-pointer"></i>
                                                    <a  style="color: #292f36;" href="${contextPath}/identity">Identity</a></li>
                                                <li><i class="fa fa-stop-circle-o"></i>
                                                    <a  style="color: #292f36;" href="${contextPath}/spatialJoin">Spatial Joint</a></li>
                                            </ul>
                                        </li>
                                        <li>
                                            <a class="side" href="#"><i class="fa fa-transgender-alt"></i>Proximity Tools</a>
                                            <ul class="closed">
                                                <li><img src="../resources/assets/buffer.svg">
                                                    <a  style="color: #292f36;" href="${contextPath}/buffer" >Buffer</a></li>
                                                <li><img src="../resources/assets/near.svg">
                                                    <a  style="color: #292f36;" href="${contextPath}/near">Near</a></li>
                                            </ul>
                                        </li>
                                        <li style=" display: none;">
                                            <a class="side" href="#"><i class="fa fa-bar-chart-o"></i>Statistics</a>
                                            <ul class="closed">
                                                <li><i class="fa fa-line-chart"></i>
                                                    <a  style="color: #292f36;" href="">Simple regression</a></li>
                                                <li><i class="fa fa-area-chart"></i>
                                                    <a  style="color: #292f36;" href="">Multiple regression</a></li>
                                            </ul>
                                        </li>  
                                      
                                    </ul>
                                </div>
                            </sidebar>
                            <div class="container modelcreate">
                                <div class="col-md-7" id="editmodelformdiv">
                                    <%
                                       // HttpSession sess = request.getSession(true);
                                        ArrayList<Tool> toolsList = (ArrayList<Tool>) request.getAttribute("tools");
                                        List<String> Layernames = (ArrayList<String>) request.getAttribute("Layername");
                                        if (toolsList.isEmpty()) {
                                    %>
                                    <div style="padding-top: 40px;"><center><p>Start to create your model using the available tools</p></center></div>

                                    <%  } else {              %>

                                    <form action=''  id="editmodelform"method='POST'>
                                        <!-- <ul name="tools" id="tools">
                                         </ul>
                                          <select name="tools" id="tools">
                                          </select>-->

                                        <input type="hidden" value='' id='currentLayer' name='currentLayer'/>
                                        <input type="hidden" name="selectedValue" value="0"/>  
                                        <input type="hidden" value="<%=request.getAttribute("toollist")%>" id="toollist" name='toollist'/>
                                        
                                       <table  id="uTable" class="table table-hover">
                                            <thead>
                                                <tr>
                                                    <th>Model Name</th><td>
                                                    </td>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%for (int i = 0; i < toolsList.size(); i++) {
                                                %>
                                                <tr>
                                                    <td>
                                                        <%=i + 1%>
                                                    </td>
                                                    <td>
                                                        <%=toolsList.get(i).getName()%>
                                                       
                                                    </td>
                                                    <!--Get Tool Inputs-->
                                                    <td>
                                                        <%if (toolsList.get(i).getInputs().size() != 0) {
                                                                List<Input> inputs = toolsList.get(i).getInputs();
                                                                
                                                                for (int j = 0; j < inputs.size(); j++) {
                                                                    if (i == 0) {  
                                                        if ("number".equals(inputs.get(j).getType()) || ("splitCoor".equals(inputs.get(j).getType()))||("coor".equals(inputs.get(j).getType()))||("splitCoorline".equals(inputs.get(j).getType())) ) 
                                                 {%>
                                                 <div id="unabled">   
                                            <p><%=inputs.get(j).getName()%></p>
                                            <input type="text" id="<%=inputs.get(j).getType()%>" name="<%=toolsList.get(i).getId()%>,<%=inputs.get(j).getId()%>,<%=i%>" class="form-control" placeholder="<%=inputs.get(j).getPlaceholder()%>" required />
                                            
                                            <% }
                                                 else if ("vector".equals(inputs.get(j).getType())) {%>
                                            <p><%=inputs.get(j).getName()%></p> 
                                                        
                                                        <select  id="<%=inputs.get(j).getType()%>" name="<%=toolsList.get(i).getId()%>,<%=inputs.get(j).getId()%>,<%=i%>" class='form-control' required disabled >
                                                          <% if (!Layernames.isEmpty()){ %>
                                                            <option value='<%=Layernames.get(j)%>' selected><%=Layernames.get(j)%></option><%} else {%> 
                                                             <option value=''>Select....</option>
                                                             <%}%>
                                                            
                                                          <%
                                                                List<GeoLayer> geoLayers = (ArrayList<GeoLayer>) request.getAttribute("geoLayers");
                                                                for (int g = 0; g < geoLayers.size(); g++) {
                                                            %>
                                                            <option value="<%=geoLayers.get(g).getName()%>"><%=geoLayers.get(g).getName()%></option>
                                                            <%
                                                                
                                                               // session.setAttribute("layer", geoLayers.get(g));
                                                                }
                                                            %>

                                                        </select>
                                                             
                                                        <% }
                                                                    }
                                                        else if (i != 0 && j!=0){
                                                     if(!("vector".equals(inputs.get(j).getType()))) {
                                                        %>
                                                        
                                                        <p><%=inputs.get(j).getName()%></p>
                                                       <input type="text" id="<%=inputs.get(j).getType()%>" name="<%=toolsList.get(i).getId()%>,<%=inputs.get(j).getId()%>,<%=i%>" class="form-control" placeholder="<%=inputs.get(j).getPlaceholder()%>" required />
                                                        <%
                                                                    } 
                                                        else if ("vector".equals(inputs.get(j).getType()))
                                                              {%>
                                                        <p><%=inputs.get(j).getName()%></p> 
                                                        
                                                        <select  id="<%=inputs.get(j).getType()%>" name="<%=toolsList.get(i).getId()%>,<%=inputs.get(j).getId()%>,<%=i%>" class='form-control' required disabled>
                                                          <% if (!Layernames.isEmpty()){ %>
                                                              <option value='<%=Layernames.get(i+1)%>'selected><%=Layernames.get(i+1)%></option><%} else {%> 
                                                             <option value=''>Select....</option>
                                                             <%}%>
                                                          
                                                          <%
                                                                List<GeoLayer> geoLayers = (ArrayList<GeoLayer>) request.getAttribute("geoLayers");
                                                                for (int g = 0; g < geoLayers.size(); g++) {
                                                            %>
                                                            <option value="<%=geoLayers.get(g).getName()%>"><%=geoLayers.get(g).getName()%>
                                                            </option>
                                                            <%
                                                               //session.setAttribute("layer", geoLayers.get(g)); 
                                                             }%>

                                                        </select>
                                                          </div>   
                                                       <%}}
                                                        }

                                                        %>
                                                    </td>
                                                    <%   String modelid = (String) session.getAttribute("modelid");%>
                                                    <td><a href="${contextPath}/removeTool?tool=<%=i%>&toolid=<%=toolsList.get(i).getId()%>&modelid=<%=modelid%>">Delete</a></td>
                                                  </tr>
                                                <% }}
                                                %>
                                                
                                            </tbody>
                                        </table>

                                        <input type="hidden" id="Form_Action" name="Form_Action"/>
                                        <div class="form-group col-md-12" id="edit_btn">
                                            <button type="submit" id='' class="btn btn-primary" style="float:right;"  onclick="enabletestdrpdown(); return false;" >Enable layers</button>
                                            
                                        </div>
                                        <div class="form-group col-md-12" id="test_btn" >
                                            <button type="submit" id='testbtn'class="btn btn-primary" style="float:right;" onclick="return testModel()">Run Model</button>
                                        </div>
                                    </form>
                                    <% }%>
                                </div>

                            </div>
                               
                        </div>

                    </div>
                                
                </div><!-- end of the edit modeling slidebar and edit form-->
<div  id="container" style="overflow:scroll; width: 100% ; height: 50%; border-style:solid;"> </div>

            </div>
        </div>
    </body>

    <!--Scripts of dwr and tool managing -->
    <script type='text/javascript' src='/sdss/dwr/engine.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/ToolManager.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/ZPFS.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/LayerManager.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Properties.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Model.js'></script>
    <script type='text/javascript' src='/sdss/dwr/interface/Swap.js'></script>
    <script type='text/javascript' src='/sdss/dwr/util.js'></script>
    <script src="../resources/js/geoTools.js" type="text/javascript"></script>
      <script src='../resources/js/nprogress.js'></script>

 <!--Scripts of Graphic Library -->
    <script src="../resources/js/konva.js"></script>
    <script src="../resources/js/graphicLibrary.js"></script>
    <!--End of dwr scripts -->
    <script>
      //  function toggleStatus() {
  //  if ($('#toggleElement').is(':checked')) {
  //      $('#unabled :input').attr('disabled', true);
  //  } else {
   //     $('#unabled :input').removeAttr('disabled');
   // }   
//}
;
 
  function enabletestbtn() {
    $('#unabled :input').removeAttr('disabled');
        //$('#unabled :input').attr('disabled', true);
    return false ;}
    
         function enabletestdrpdown()
         {
             //document.getElementById("vector").disabled=false;
             $(' input[type="checkbox"], select').prop("disabled", false);
          
            
         }
        var jsArr = new Array();
            <% 
                for (int y=0; y < toolsList.size(); y++) {
            %> 
                jsArr[<%= y %>] =" <%=toolsList.get(y).getName()%>"; 
     <%}%>
     ToolObjects=document.getElementById('toollist').value;
     PrintToolObjects(ToolObjects,jsArr);
     /*var inputs = $(".vector");  
    window.onload = function()
    {
        for(var i = 0; i < sessionStorage.length; i++){ 
            if(a !== null)
            { 
           var a = sessionStorage.getItem('vector')[i];
           document.getElementsByClassName(vector).value = a;
        }
} 
    }
   
   
    window.onbeforeunload = function() {
        
    for(var i = 0; i < inputs.length; i++){
   sessionStorage.setItem("vector", $(inputs[i]).val());
   //alert($(inputs[i]).val());
} 
    }*/


 //SUBMIT FORM
/*$(document).ready(function() { 
    var options = { 
        target:        '',
        dataType:      'jsp',          
        beforeSubmit:  showRequest_reassign,        
        success:       showResponse_reassign
    }; 

    $("#editmodelformdiv").delegate("#editmodelformdiv","submit",function () {
        clearTimeout(interval); 
        $(this).ajaxSubmit(options); 
        return false;
    });

});
function showRequest_reassign(formData, jqForm, options){
    return true; 
}
function showResponse_reassign(responseText, statusText, xhr, $form){
   alert(responseText)
}
   */
/*window.onload = function() {
    
    var selItem = sessionStorage.getItem("SelItem");  
    $('#vector').val(selItem);
    
    }
    $('#vector').change(function() { 
      
        var selVal = $(this).val();
        
        
        var ddlArray= new Array();
        
        var ddl = document.getElementById('vector');
        for (i = 0; i < ddl.options.length; i++) 
        {
            ddlArray[i] =selVal;
        }
        sessionStorage.setItem("SelItem", selVal);
        
    });var selectedItem = sessionStorage.getItem("SelectedItem");  

$('#vector').val(selectedItem);

$('#vector').change(function() { 
    var dropVal = $(this).val();
    sessionStorage.setItem("SelectedItem", dropVal);
});*/
   // var selected=document.getElementById('#vector').selectedValue;
   // session.setAttribute("layer",selected);
    


  </script>

    <!--<script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>-->

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
    <script src="../resources/js/stickybar.js"></script>
    <script src="../resources/js/sidebar.js"></script>
    <script src="../resources/js/datatables/datatables.min.js"></script>
    <script src="../resources/js/select2.min.js"></script>
    <script src="../resources/js/datatables/datatables_basic.js"></script>

</html>