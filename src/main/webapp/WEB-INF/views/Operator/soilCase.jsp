<%-- 
    Document   : newjsp
    Created on : Feb 12, 2017, 1:46:39 AM
    Author     : Administrator
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>

        <h1>Soil Suitability</h1>
        <div id="container"></div>

        <script>
            var width = window.innerWidth;
            var height = window.innerHeight;
            var stage = new Konva.Stage({
                container: 'container',
                width: width,
                height: height
            });
            var layer = new Konva.Layer();
            //Oval dimension
            var radiusX = 50;
            var radiusY = (radiusX / 2) * 1.2;
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
            var VLineLength = 150;
            var HLineLength = 50;
            var newRectWidth = rectWidth * 1.5;

            ///set direction
            var left = false;
            var right = true;
            var firstVertical = false;
            //first Inputs
            FirstInputsDouble();
            var loopsaiz = 4;
            for (var i = 0; i < loopsaiz; i++) {


                if (i > 0 && right) {
                    if (checkPageWidthForLine(rectX, newRectWidth, HLineLength, newRectWidth, width)) {
                        addHConnector(50, rectX + rectWidth, rectY / 2, HLineLength);
                        rectX = rectX + rectWidth + HLineLength;
                    } else {
                        addVConnector(0, rectX, rectY, rectWidth, rectHeight+10, VLineLength);
                        rectY = rectY + 200;
                        left = true;
                        right = false;
                        firstVertical = true;
                    }
                } else if (i > 0 && left) {

                    addHConnector(50, rectX + HLineLength, (rectY + 200) / 2, -(HLineLength + 15));
                    rectX = rectX - rectWidth - HLineLength;
                }


                if (right) {
                    if (checkPageWidthForProcess(rectX, newRectWidth, width)) {
                        if (i == 0 || i == loopsaiz - 1) {
                            addProcess(rectX, rectY, newRectWidth, 'Spatial Join',0,0);
                        } else {
                            addProcessWithInput(rectX, rectY, newRectWidth, rectHeight, 'Intersect', radiusX, radiusY, '#09c', HLineLength);
                        }
                        if (checkPageWidthForLine(rectX, newRectWidth, HLineLength, radiusX * 2, width)) {
                            addHConnector(50, rectX + newRectWidth, rectY / 2, HLineLength);
                            rectX = rectX + newRectWidth + HLineLength;
                            addOutputFeature(rectX, rectY, radiusX, radiusY, '#6c6', 'Output Feature');
                        } else {
                            left = true;
                            right = false;
                        }


                    } else {
                        left = true;
                        right = false;
                    }
                } else if (!firstVertical) {
                    addVConnector(50, rectX, rectY, rectWidth, rectHeight, VLineLength);
                    rectY = rectY + 200;
                    firstVertical = true;
                } else if (left) {
                    rectX = rectX - 15;
                    if (i == 0 || i == loopsaiz - 1) {
                        addProcess(rectX, rectY, newRectWidth, 'Intersect',0,0);
                        addVConnector(50, rectX, rectY + rectHeight, newRectWidth, -rectHeight, HLineLength);
                    } else {
                        addProcessWithInput(rectX, rectY, newRectWidth, rectHeight, 'Intersect', radiusX, radiusY, '#09c', HLineLength);
                    }

                    addHConnector(50, rectX, (rectY + 200) / 2, -HLineLength);
                    rectX = rectX - HLineLength;
                    addLeftOutputFeature(rectX, rectY + 55, radiusX, radiusY, '#6c6', 'Output Feature');
                    rectX = rectX - newRectWidth;
                }
                //rectX = rectX + newRectWidth + HLineLength;
                // addOutputFeature(rectX, rectY, radiusX, radiusY, '#6c6', 'Output Feature');
            }


            var buffer_Y = height - rectHeight * 1.3;
            var buffer_X1 = 150;
            var buffer_X2 = 600;
            var unionY = 450;
            var unionX = width / 2.8;
            var VConnectorLength = buffer_Y - unionY - rectHeight;
            //First Buffer
            addProcess(buffer_X1, buffer_Y, rectWidth, 'Buffer',-10,0);
            addHConnector(50, buffer_X1, buffer_Y - (rectHeight / 2), -HLineLength);
            addInput(buffer_X1 - 150, buffer_Y, radiusX, radiusY, '#09c', 'Input Feature',10,20);
            addHConnector(50, buffer_X1 + rectWidth, buffer_Y - (rectHeight / 2), HLineLength);
            addInput(buffer_X1 + 150, buffer_Y, radiusX, radiusY, '#09c', 'Buffer Distance',10,20);
            VConnector(50, buffer_X1, buffer_Y - rectHeight, rectWidth, -VConnectorLength);
            //Second Buffer
            addProcess(buffer_X2, buffer_Y, rectWidth, 'Buffer',-10,0);
            addHConnector(50, buffer_X2 + rectWidth, buffer_Y - (rectHeight / 2), HLineLength);
            addInput(buffer_X2 + 150, buffer_Y, radiusX, radiusY, '#09c', 'Input Feature',10,20);
            addHConnector(50, buffer_X2, buffer_Y - (rectHeight / 2), -HLineLength);
            addInput(buffer_X2 - 150, buffer_Y, radiusX, radiusY, '#09c', 'Buffer Distance',10,20);
            VConnector(50, buffer_X2, buffer_Y - rectHeight, rectWidth, -VConnectorLength);

            // Union Of Two Layers

            addProcess(unionX, unionY, rectWidth, 'Union',-10,0);
            addHConnector(50, unionX, unionY - (rectHeight / 2), -(HLineLength + 80));
            addInput(unionX - 215, unionY - 5, radiusX, radiusY, '#6c6', 'Buffers',28,20);
            addHConnector(50, unionX + rectWidth, unionY - (rectHeight / 2), HLineLength + 80);
            addInput(unionX + 230, unionY - 5, radiusX, radiusY, '#6c6', 'Buffers',28,20);
            //output of union    
            VConnector(50, unionX, unionY - rectHeight, rectWidth, -HLineLength);
            addInput(unionX, unionY - 100, radiusX, radiusY, '#6c6', 'Union Feature',10,20);

            stage.add(layer);
        </script>