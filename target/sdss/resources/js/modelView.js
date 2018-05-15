/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/*

     var width = window.innerWidth / 1.39;
        var height = window.innerHeight / 2;

        var stage = new Konva.Stage({
            container: 'container',
            width: width,
            height: height
        });
        var layer = new Konva.Layer();

        //Oval dimension
        var radiusX = 50;
        var radiusY = (radiusX / 2)*1.2;

        ////start inputs
        var startX = 70;
        var startY = 15;
        var startY2 = startY + radiusY * 2;

        //Rectangular dimension
        var rectX = startX + radiusX + 50;
        var rectY = 50;
        var rectWidth = 100;
        var rectHeight = 50;

        //line dimension
        var lineLength = 50;


        <% if (!toolsList.isEmpty()) {

                for (int i = 0; i < toolsList.size(); i++) {
                    if (i == 0) {
        %>

        FirstInputsDouble();

        <%
            }

            if (i > 0) {
        %>
        if (checkPageWidth(rectX, rectWidth, lineLength, width)) {
            addHConnector(50, rectX, rectY, rectWidth, lineLength);
        } else {
            addVConnector(50, rectX, rectY, rectHeight, lineLength);
        }

        ///set new 'x' for the next object
        rectX = rectX + rectWidth + lineLength;

        <%
            }
            String toolName = toolsList.get(i).getName();
            if ("clip".equals(toolName)) {
        %>

        addRectangular(rectX, rectY);
        addTextToProcess('Clip', rectX, rectY);
        if (checkPageWidth(rectX, rectWidth, lineLength, width)) {
            addHConnector(50, rectX, rectY, rectWidth, lineLength);
        } else {
            addVConnector(50, rectX, rectY, rectHeight, lineLength);
        }
        rectX = rectX + rectWidth + lineLength;
        addOutputFeature(rectX, rectY, radiusX, radiusY, '#6c6');
        //rectX = rectX + rectWidth;

        <% } else if ("buffer".equals(toolName)) {%>

        //addEllipse(rectX, rectY, radiusX, radiusY, '#3ea07f');
        //addTextToBuffer(rectX, rectY);
        addRectangular(rectX, rectY);
        addTextToProcess('Buffer', rectX, rectY);
        if (checkPageWidth(rectX, rectWidth, lineLength, width)) {
            addHConnector(50, rectX, rectY, rectWidth, lineLength);
        } else {
            addVConnector(50, rectX, rectY, rectHeight, lineLength);
        }
        rectX = rectX + rectWidth + lineLength;
        
        <%       } else if ("nearest".equals(toolName)) {
        %>
        addRectangular(rectX, rectY);
        addTextToProcess('Near', rectX, rectY);
        if (checkPageWidth(rectX, rectWidth, lineLength, width)) {
            addHConnector(50, rectX, rectY, rectWidth, lineLength);
        } else {
            addVConnector(50, rectX, rectY, rectHeight, lineLength);
        }
        rectX = rectX + rectWidth + lineLength;
        addOutputFeature(rectX, rectY, radiusX, radiusY, '#6c6');
        <%
                    }
                }
            }%>

        stage.add(layer);
  */