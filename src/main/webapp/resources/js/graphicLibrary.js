/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//var width = window.innerWidth;
var width = 2900;
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
//var ToolObjects = document.getElementById('tools');

function PrintToolObjects(ToolObjects,toolname) {
 
   // var = document.getElementById('toollist').value;
 
 
    layer.removeChildren();
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


    for (var t = 0; t < ToolObjects; t++) {
        //first Inputs
        FirstInputsDouble();
        if (t > 0 && right) {
            if (checkPageWidthForLine(rectX, newRectWidth, HLineLength, newRectWidth, width)) {
                addHConnector(50, rectX + rectWidth, rectY / 2, HLineLength);
                rectX = rectX + rectWidth + HLineLength;
            } else {
                addVConnector(0, rectX, rectY, rectWidth, rectHeight, VLineLength);
                rectY = rectY + 200;
                left = true;
                right = false;
                firstVertical = true;
            }
        } else if (t > 0 && left) {
            addHConnector(50, rectX + HLineLength, (rectY + 200) / 2, -(HLineLength + 15));
            rectX = rectX - rectWidth - HLineLength;
        }
        if (right) {
            if (checkPageWidthForProcess(rectX, newRectWidth, width)) {
                if (t === 0) {
                    addProcess(rectX, rectY, newRectWidth,toolname[t],0,0);
                } else {
                    addProcessWithInput(rectX, rectY, newRectWidth, rectHeight, toolname[t], radiusX, radiusY, '#09c', HLineLength);
                }
                if (checkPageWidthForLine(rectX, newRectWidth, HLineLength, radiusX * 2, width)) {
                    addHConnector(50, rectX + newRectWidth, rectY / 2, HLineLength);
                    rectX = rectX + newRectWidth + HLineLength;
                    addOutputFeature(rectX, rectY, radiusX, radiusY, '#6c6', 'Output Feature');
                } else {
                    addVConnector(0, rectX+50, rectY, rectWidth, rectHeight, VLineLength);
                    rectY = rectY + 200;
                    firstVertical = true;
                    left = true;
                    right = false;
                    addOutputFeature(rectX+radiusX, rectY, radiusX, radiusY, '#6c6', 'Output Feature');
                }

            } else {
                left = true;
                right = false;
            }
        }
        else if (!firstVertical) 
        {
            addVConnector(50, rectX, rectY, rectWidth, rectHeight, VLineLength);
            rectY = rectY + 200;
            firstVertical = true;
        }
        else if (left) 
        { 
            rectX = rectX - 15;
           if (t == 0) {
               
                addProcess(rectX, rectY, newRectWidth,toolname[t],0,0);
                addVConnector(50, rectX, rectY + rectHeight, newRectWidth, -rectHeight, HLineLength);
            }
            else {
         addProcessWithInput(rectX, rectY, newRectWidth, rectHeight, toolname[t], radiusX, radiusY, '#09c', HLineLength);
         addHConnector(50, rectX, (rectY + 200) / 2, -HLineLength);
         rectX = rectX - HLineLength;
         addLeftOutputFeature(rectX, rectY + 55, radiusX, radiusY, '#6c6', 'Output Feature');
         rectX = rectX - newRectWidth;
    
            }
        }
        
        if(
                rectX===startX)
        {
            right=true;
        
        left=false;
    }
        
    }
    stage.add(layer);
}



function FirstInputsDouble() {

    ////START INPUTS
    var Finput = new Konva.Ellipse({
        x: startX,
        y: rectY - startY,
        radius: {
            x: radiusX,
            y: radiusY
        },
        fill: '#0cf',
        stroke: 'black',
        strokeWidth: 2
    });



    var Finput2 = new Konva.Ellipse({
        x: startX,
        y: rectY * 2 + startY,
        radius: {
            x: radiusX,
            y: radiusY
        },
        fill: '#0cf',
        stroke: 'black',
        strokeWidth: 2
    });

    ////START LINES
    var FlineX1 = startX + radiusX;
    var FlineX2 = FlineX1 + HLineLength;

    var FLine = new Konva.Line({
        y: 50,
        points: [FlineX1, rectY - radiusX - 15, FlineX2, rectY / 3],
        stroke: 'black',
        strokeWidth: 2,
        lineCap: 'round',
        lineJoin: 'round',
        draggable: false
    });

    var FLine2 = new Konva.Line({
        y: 50,
        points: [FlineX1, startY2, FlineX2, rectY / 1.5],
        stroke: 'black',
        strokeWidth: 2,
        lineCap: 'round',
        lineJoin: 'round',
        draggable: false
    });

    layer.add(Finput);
    layer.add(Finput2);
    layer.add(FLine);
    layer.add(FLine2);

    addTextToInput(startX - 40, rectY / 2, 'Input Feature', 15);
    addTextToInput(startX - 40, rectY * 2.1, 'Input Feature', 15);
}

function addProcess(rectX, rectY, rectWidth, text,text_x,text_y) {
    addRectangular(rectX, rectY, rectWidth);
    addTextToProcess(text, rectX, rectY,text_x,text_y);
}

function addRectangular(rectX, rectY, rectWidth) {
    var box = new Konva.Rect({
        x: rectX,
        y: rectY,
        width: rectWidth,
        height: rectHeight,
        fill: '#fc3',
        stroke: 'black',
        strokeWidth: 2,
        draggable: false,
        cornerRadius: 6
    });
    layer.add(box);
}

function addProcessWithInput(rectX, rectY, rectWidth, rectHeight, text, radiusX, radiusY, fill, lineLength) {
    addProcess(rectX, rectY, rectWidth, text,0,0);
    VConnector(50, rectX, rectY, rectWidth, lineLength);
    addInput(rectX + 23, rectY + rectHeight + lineLength, radiusX, radiusY, fill, 'Input Feature',10,20);

}

function addTextToProcess(text, rectX, rectY,text_x,text_y) {
    var simpleText = new Konva.Text({
        x: (rectX + rectWidth / 3 )+ text_x,
        y: (rectY + rectHeight / 4) + text_y,
        text: text,
        fontSize: 22,
        fontFamily: 'Calibri',
        fill: 'black'
    });
    layer.add(simpleText);
}

function addEllipse(rectX, rectY, radiusX, radiusY, fill) {
    var oval = new Konva.Ellipse({
        x: rectX + radiusX,
        y: rectY + radiusY,
        radius: {
            x: radiusX,
            y: radiusY
        },
        fill: fill,
        stroke: 'black',
        strokeWidth: 2
    });
    layer.add(oval);
}

function addExtraInput(rectX, rectY, radiusX, radiusY, fill) {
    var oval = new Konva.Ellipse({
        x: rectX + radiusX,
        y: rectY * 3 + radiusY,
        radius: {
            x: radiusX,
            y: radiusY
        },
        fill: fill,
        stroke: 'black',
        strokeWidth: 2,
        draggable: true
    });
    layer.add(oval);
    addTextToInput(rectX + 10, rectY * 3 + 20, 'Input Feature', 15);
}

function addInput(rectX, rectY, radiusX, radiusY, fill, text,Text_x,Text_y) {
    var oval = new Konva.Ellipse({
        x: rectX + radiusX,
        y: rectY + radiusY,
        radius: {
            x: radiusX,
            y: radiusY
        },
        fill: fill,
        stroke: 'black',
        strokeWidth: 2,
        draggable: true
    });
    layer.add(oval);
    addTextToInput(rectX + Text_x, rectY + Text_y, text, 15);
}

function addTextToBuffer(rectX, rectY) {
    var simpleText = new Konva.Text({
        x: rectX + rectWidth / 3.6,
        y: rectY + rectHeight / 3.9,
        text: 'Buffer',
        fontSize: 20,
        fontFamily: 'Calibri',
        fill: 'black'
    });
    layer.add(simpleText);
}

function addTextToInput(rectX, rectY, text, size) {
    var simpleText = new Konva.Text({
        x: rectX,
        y: rectY,
        text: text,
        fontSize: size,
        fontFamily: 'Calibri',
        fill: 'black'
    });
    layer.add(simpleText);
}

function checkPageWidthForProcess(rectX, rectWidth, pageWidth) {
    var lineX = rectX + rectWidth;
    if (lineX > pageWidth) {
        return false;
    } else {
        return true;
    }
}

function checkPageWidthForLine(rectX, rectWidth, lineLength, shapeWidth, pageWidth) {

    var lineX = rectX + rectWidth + lineLength + shapeWidth;
    if (lineX > pageWidth) {
        return false;
    } else {
        return true;
    }
}

function addHConnector(y, lineX1, middleY, linelength) {
    var lineX2 = linelength + lineX1;
    var line = new Konva.Line({
        y: y,
        points: [lineX1, middleY, lineX2, middleY],
        stroke: 'black',
        strokeWidth: 2,
        lineCap: 'round',
        lineJoin: 'round',
        draggable: true
    });
    layer.add(line);

}

function addVConnector(y, rectX, rectY, rectWidth, rectHeight, lineLength) {
    var lineX = rectX + rectWidth / 2;
    var lineY1 = rectY + rectHeight;
    var lineY2 = lineY1 + lineLength;
    var line = new Konva.Line({
        y: y,
        points: [lineX, lineY1, lineX, lineY2],
        stroke: 'black',
        strokeWidth: 2,
        lineCap: 'round',
        lineJoin: 'round',
        draggable: true
    });
    layer.add(line);
}

function VConnector(y, rectX, rectY, rectWidth, lineLength) {
    var lineX = rectX + rectWidth / 2;
    var lineY1 = rectY;
    var lineY2 = lineY1 + lineLength;
    var line = new Konva.Line({
        y: y,
        points: [lineX, lineY1, lineX, lineY2],
        stroke: 'black',
        strokeWidth: 2,
        lineCap: 'round',
        lineJoin: 'round',
        draggable: true
    });
    layer.add(line);
}

function addOutputFeature(rectX, rectY, radiusX, radiusY, fill, text) {
    var oval = new Konva.Ellipse({
        x: rectX + radiusX,
        y: rectY + radiusY,
        radius: {
            x: radiusX,
            y: radiusY
        },
        fill: fill,
        stroke: 'black',
        strokeWidth: 2
    });
    layer.add(oval);
    addTextToInput(rectX + 3, rectY + 20, text, 15);
}

function addLeftOutputFeature(rectX, rectY, radiusX, radiusY, fill, text) {
    var oval = new Konva.Ellipse({
        x: rectX - radiusX,
        y: rectY - radiusY,
        radius: {
            x: radiusX,
            y: radiusY
        },
        fill: fill,
        stroke: 'black',
        strokeWidth: 2
    });
    layer.add(oval);
    addTextToInput(rectX - 95, rectY - 40, text, 15);
}

function addExtraLine(rectX, startY2, radiusX, lineLength, rectY) {
    ////START LINES

    var FlineX2 = rectX + lineLength;
    var FLine = new Konva.Line({
        y: 50 + rectY / 2,
        points: [rectX + radiusX, startY2, FlineX2, rectY / 1.5],
        stroke: 'black',
        strokeWidth: 5,
        lineCap: 'round',
        lineJoin: 'round',
        draggable: true
    });
    layer.add(FLine);
}

function addCursorStyle() {
    // add cursor styling
    /* box.on('mouseover', function () {
     document.body.style.cursor = 'pointer';
     });
     box.on('mouseout', function () {
     document.body.style.cursor = 'default';
     });
     */
}