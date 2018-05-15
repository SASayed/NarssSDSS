/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.narss.sdss.controllers.OperatorController;
import org.narss.sdss.controllers.Properties;
import org.narss.sdss.dto.Tool;
import org.narss.sdss.dto.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
public class ToolManager {

    //@Autowired
    // private layerManger layerManger;
    LayerManager layerManger = new LayerManager();

    @Autowired
    private Tool tool;

    private List<Tool> tools = new ArrayList<Tool>();

   // String workingDirectory = OperatorController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    //String modellingPath = workingDirectory.substring(0, workingDirectory.lastIndexOf("WEB-INF/")) + "resources/geoModelling/";
  String modellingPath = Properties.GeoModelling;
    ///unique number 
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    LocalDateTime now = LocalDateTime.now();
    String unique = dtf.format(now);

    public List<Tool> addTool(Tool tool) {
        tools = new ArrayList<Tool>();
        tools.add(tool);
        return tools;
    }

    public Tool intersectTool() {

        tool = new Tool();
        tool.setName("intersect");

        //set tool inputs
        List<Input> inputs = new ArrayList<Input>();
        Input input1 = new Input();
        input1.setName("Layer name");
        input1.setType("vector");
        inputs.add(input1);

        Input input2 = new Input();
        input2.setName("Layer name");
        input2.setType("vector");
        inputs.add(input2);

        modellingPath = modellingPath.replaceFirst("^/(.:/)", "$1");
        //File resultFile = intersectFile(new File(modellingPath + "intersect.xml"), inputs);
        tool.setFile(new File(modellingPath + tool.getName() + ".xml"));

        tool.setInputs(inputs);
        tool.setCountOfInputLayers(2);
        tool.setCountOfoutputLayer(1);
        return tool;
    }

    public Tool bufferTool() {
        tool = new Tool();
        tool.setName("buffer");

        //set tool inputs
        List<Input> inputs = new ArrayList<Input>();
        Input input1 = new Input();
        input1.setName("Distance");
        input1.setType("number");
        inputs.add(input1);

        Input input2 = new Input();
        input2.setName("Layer name");
        input2.setType("vector");
        inputs.add(input2);

        modellingPath = modellingPath.replaceFirst("^/(.:/)", "$1");
        //File bufferResult = bufferFile(new File(modellingPath + "Buffer.xml"), inputs);
        //tool.setFile(bufferResult.toString());
        tool.setFile(new File(modellingPath + tool.getName() + ".xml"));
        tool.setInputs(inputs);
        tool.setCountOfInputLayers(2);
        tool.setCountOfoutputLayer(1);
        return tool;
    }

    public Tool unionTool() {

        tool = new Tool();
        tool.setName("union");
        //set tool inputs
        List<Input> inputs = new ArrayList<Input>();
        Input input1 = new Input();
        input1.setName("Layer name");
        input1.setType("vector");
        inputs.add(input1);

        Input input2 = new Input();
        input2.setName("Layer name");
        input2.setType("vector");
        inputs.add(input2);

        modellingPath = modellingPath.replaceFirst("^/(.:/)", "$1");
        /* File unionResult = unionFile(new File(modellingPath + "Union.xml"), inputs);
        tool.setFile(unionResult.toString());*/
        tool.setFile(new File(modellingPath + tool.getName() + ".xml"));

        tool.setInputs(inputs);
        tool.setCountOfInputLayers(2);
        tool.setCountOfoutputLayer(1);
        return tool;
    }

    public Tool nearTool() {
        tool = new Tool();
        tool.setName("nearest");

        //set tool inputs
        List<Input> inputs = new ArrayList<Input>();
        Input input1 = new Input();
        input1.setName("Point");
        input1.setType("coor");
        inputs.add(input1);

        Input input2 = new Input();
        input2.setName("Layer name");
        input2.setType("vector");
        inputs.add(input2);

        /*  modellingPath = modellingPath.replaceFirst("^/(.:/)", "$1");
        File nearResult = nearFile(new File(modellingPath + "Nearest.xml"), inputs);
        tool.setFile(nearResult.toString());*/
        tool.setFile(new File(modellingPath + tool.getName() + ".xml"));
        tool.setInputs(inputs);
        tool.setCountOfInputLayers(1);
        tool.setCountOfoutputLayer(1);
        return tool;

    }

    public Tool clipTool() {
        tool = new Tool();
        tool.setName("clip");

        //set tool inputs
        List<Input> inputs = new ArrayList<Input>();
        Input input1 = new Input();
        input1.setName("Layer name");
        input1.setType("vector");
        inputs.add(input1);

        Input input2 = new Input();
        input2.setName("Layer name");
        input2.setType("vector");
        inputs.add(input2);

        modellingPath = modellingPath.replaceFirst("^/(.:/)", "$1");
        /*File clipResult = clipFile(new File(modellingPath + "Clip.xml"), inputs);
        tool.setFile(clipResult.toString());*/
        tool.setFile(new File(modellingPath + tool.getName() + ".xml"));

        tool.setInputs(inputs);
        tool.setCountOfInputLayers(2);
        tool.setCountOfoutputLayer(1);
        return tool;
    }

    public Tool splitTool() {
        tool = new Tool();
        tool.setName("split");

        //set tool inputs
        List<Input> inputs = new ArrayList<Input>();
        Input input1 = new Input();
        input1.setName("Polygon");
        input1.setType("splitCoor");
        inputs.add(input1);

        Input input2 = new Input();
        input2.setName("Line");
        input2.setType("splitCoor");
        inputs.add(input2);

        modellingPath = modellingPath.replaceFirst("^/(.:/)", "$1");
        /*File splitResult = splitFile(new File(modellingPath + "Split.xml"), inputs);
        tool.setFile(splitResult.toString());*/
        tool.setFile(new File(modellingPath + tool.getName() + ".xml"));

        tool.setInputs(inputs);
        tool.setCountOfInputLayers(0);
        tool.setCountOfoutputLayer(0);
        return tool;
    }

    public Tool identityTool() {

        tool = new Tool();
        tool.setName("identity");

        //set tool inputs
        List<Input> inputs = new ArrayList<Input>();
        Input input1 = new Input();
        input1.setName("Layer name");
        input1.setType("vector");
        inputs.add(input1);

        Input input2 = new Input();
        input2.setName("Layer name");
        input2.setType("vector");
        inputs.add(input2);

        /*  modellingPath = modellingPath.replaceFirst("^/(.:/)", "$1");
        File resultFile = identityFile(new File(modellingPath + "Identity.xml"), inputs);
        tool.setFile(resultFile.toString());*/
        tool.setFile(new File(modellingPath + tool.getName() + ".xml"));
        tool.setInputs(inputs);
        tool.setCountOfInputLayers(2);
        tool.setCountOfoutputLayer(1);
        return tool;
    }

    public Tool spatialJoinTool() {

        tool = new Tool();
        tool.setName("spatial Join");

        //set tool inputs
        List<Input> inputs = new ArrayList<Input>();
        Input input1 = new Input();
        input1.setName("Layer name");
        input1.setType("vector");
        inputs.add(input1);

        Input input2 = new Input();
        input2.setName("Layer name");
        input2.setType("vector");
        inputs.add(input2);

        modellingPath = modellingPath.replaceFirst("^/(.:/)", "$1");
        /* File resultFile = SpatialJoinFile(new File(modellingPath + "SpatialJoin.xml"), inputs);
        tool.setFile(resultFile.toString());*/
        tool.setFile(new File(modellingPath + "SpatialJoin.xml"));

        tool.setInputs(inputs);
        tool.setCountOfInputLayers(2);
        tool.setCountOfoutputLayer(1);
        return tool;
    }

    public List<Tool> getModelTools(String modelId) {
        List<Tool> tools = new ArrayList<Tool>();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(Properties.MODEL);

            NodeList models_Ids = doc.getElementsByTagName("id");
            //CaseStudy caseStudy= null;

            for (int i = 0; i < models_Ids.getLength(); i++) {
                Node id = models_Ids.item(i);
                if (modelId.equals(id.getTextContent())) {
                    Node model = id.getParentNode();
                    NodeList modelItems = model.getChildNodes();
                    for (int j = 0; j < modelItems.getLength(); j++) {
                        Node toolNode = modelItems.item(j);
                        if ("tool".equals(toolNode.getNodeName())) {
                            ///list of tool inputs 
                            List<Input> toolInputs = new ArrayList<Input>();

                            NamedNodeMap attr = toolNode.getAttributes();
                            int toolId = Integer.parseInt(attr.getNamedItem("id").getTextContent());
                            String toolName = attr.getNamedItem("name").getTextContent();

                            Tool tool = new Tool();
                            tool.setId(toolId);
                            tool.setName(toolName);

                            ///get node inputs 
                            NodeList toolsinputs = toolNode.getChildNodes();
                            for (int x = 0; x < toolsinputs.getLength(); x++) {
                                if ("toolInput".equals(toolsinputs.item(x).getNodeName())) {
                                    NamedNodeMap attr2 = toolsinputs.item(x).getAttributes();
                                    String toolInputName = attr2.getNamedItem("name").getTextContent();
                                    String toolInputType = attr2.getNamedItem("type").getTextContent();
                                    String toolInputValue = attr2.getNamedItem("value").getTextContent();
                                    String submodel = attr2.getNamedItem("submodel").getTextContent();
                                    Input toolInput = new Input();
                                    toolInput.setName(toolInputName);
                                    toolInput.setType(toolInputType);
                                    toolInput.setValue(toolInputValue);
                                    toolInput.setSubmodel(submodel);
                                    toolInputs.add(toolInput);
                                }
                            }
                            tool.setInputs(toolInputs);
                            tools.add(tool);
                        }

                    }
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        } catch (ParserConfigurationException pc) {
            pc.printStackTrace();
        }

        return tools;

    }

    public String createModelRunFile(String[] toolFiles) {
        File finalFile = null;
        try {
            ////create model file
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = null;
            Document doc2 = null;

            if (toolFiles.length > 1) {

                for (int j = 0; j < toolFiles.length - 1; j++) {

                    File toolFile = new File(toolFiles[j]);
                    File nxttoolFile = new File(toolFiles[j + 1]);

                    doc = docBuilder.parse(toolFiles[j]);
                    Node main = doc.getElementsByTagName("wps:Execute").item(0);
                    removeAllAttributesExcept(doc, "wps:Execute", "service", "version");
                    changeResponseOutput(doc, "wps:RawDataOutput", j);

                    ///get next toolfile to insert the pervious tool in it
                    doc2 = docBuilder.parse(toolFiles[j + 1]);
                    Node input = doc2.getElementsByTagName("wfs:GetFeature").item(0);
                    input = input.getParentNode().getParentNode().getParentNode();
                    if ("wps:Input".equals(input.getNodeName())) {
                        //clear <wps:input> of the current tags
                        String featureType = removeChilds(input);
                        //import them into doc2
                        Node newmain = doc2.importNode(main, true);
                        //add new tags needed for chaining
                        createMissingNodes(doc2, input,featureType, newmain);
                    }

                    // write the content into xml file
                    TransformerFactory.newInstance().newTransformer().
                            transform(new DOMSource(doc2), new StreamResult(nxttoolFile));
                    finalFile = nxttoolFile;
                }

            } else {
                toolFiles[0] = toolFiles[0].replaceAll("\\s+", "");
                File modeFiles = new File(toolFiles[0]);
                finalFile = modeFiles;
            }

        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }
        return finalFile.getPath();
    }

    private void removeAllAttributesExcept(Document doc, String nodeName, String attName1, String attName2) {
        Node execute = doc.getElementsByTagName(nodeName).item(0);
        for (int i = 0; i < execute.getAttributes().getLength(); i++) {
            Node att = execute.getAttributes().item(i);
            if (att.getNodeName() != attName1 && att.getNodeName() != attName2) {
                execute.getAttributes().removeNamedItem(att.getNodeName());
            }
        }
    }

    private void createMissingNodes(Document doc, Node main,String featureType, Node newNode) {

        Element identifier = doc.createElement("ows:Identifier");
        identifier.appendChild(doc.createTextNode(featureType));
        main.appendChild(identifier);

        Element ref = doc.createElement("wps:Reference");
        ref.setAttribute("mimeType", "text/xml");
        ref.setAttribute("xlink:href", "http://geoserver/wps");
        ref.setAttribute("method", "POST");
        main.appendChild(ref);

        Element body = doc.createElement("wps:Body");
        ref.appendChild(body);

        body.appendChild(newNode);
    }

    private void changeResponseOutput(Document doc, String elementName, int i) {
        Node response = doc.getElementsByTagName(elementName).item(i);
        Node att = response.getAttributes().getNamedItem("mimeType");
        att.setTextContent("text/xml; subtype=wfs-collection/1.0");

    }

    private String removeChilds(Node node) {
        String result = "";
        while (node.hasChildNodes()) {
            if ("ows:Identifier".equals(node.getFirstChild().getNodeName())) {
                if (result == "") {
                    result = node.getFirstChild().getTextContent();
                }
            }
            node.removeChild(node.getFirstChild());
        }
        return result;
    }
    /* private File intersectFile(File toolfile, List<Input> inputs) {
        LocalDateTime now = LocalDateTime.now();
        String unique = dtf.format(now);
        File newIntersect = new File(Properties.QUERY_PATH + unique + "newIntersect.xml");
        newIntersect = layerManger.createIntersect(inputs, "SECOND", toolfile, newIntersect);
        return newIntersect;
    }

    private File bufferFile(File toolfile, List<Input> inputs) {
        LocalDateTime now = LocalDateTime.now();
        String unique = dtf.format(now);
        File newBuffer = new File(Properties.QUERY_PATH + unique + "newBuffer.xml");
        newBuffer = layerManger.createBuffer(toolfile, inputs, newBuffer);
        return newBuffer;
    }

    private File unionFile(File toolfile, List<Input> inputs) {
        LocalDateTime now = LocalDateTime.now();
        String unique = dtf.format(now);
        File newUnion = new File(Properties.QUERY_PATH + unique + "newUnion.xml");
        newUnion = layerManger.createUnion(toolfile, inputs, newUnion);
        return newUnion;
    }

    private File clipFile(File toolfile, List<Input> inputs) {
        LocalDateTime now = LocalDateTime.now();
        String unique = dtf.format(now);
        File newClip = new File(Properties.QUERY_PATH + unique + "newClip.xml");
        newClip = layerManger.createClip(toolfile, inputs, newClip);
        return newClip;
    }

    private File splitFile(File toolfile, List<Input> inputs) {
        LocalDateTime now = LocalDateTime.now();
        String unique = dtf.format(now);
        File newSplit = new File(Properties.QUERY_PATH + unique + "newSplit.xml");
        newSplit = layerManger.createSplit(toolfile, inputs, newSplit);
        return newSplit;
    }

    private File nearFile(File toolfile, List<Input> inputs) {
        LocalDateTime now = LocalDateTime.now();
        String unique = dtf.format(now);
        File newNearest = new File(Properties.QUERY_PATH + unique + "newNearest.xml");
        newNearest = layerManger.getNearest(toolfile, inputs, newNearest);
        return newNearest;
    }

    private File identityFile(File toolfile, List<Input> inputs) {
        LocalDateTime now = LocalDateTime.now();
        String unique = dtf.format(now);
        ////intersect between different type of festure (polygons and points)
        File newIdentity = new File(Properties.QUERY_PATH + unique + "newIdentity.xml");
        newIdentity = layerManger.createIntersect(inputs, "FIRST", toolfile, newIdentity);
        return newIdentity;
    }

    private File SpatialJoinFile(File toolfile, List<Input> inputs) {
        LocalDateTime now = LocalDateTime.now();
        String unique = dtf.format(now);
        ////intersect between different type of festure (polygons and points)
        File newJoin = new File(Properties.QUERY_PATH + unique + "newSpatialJoin.xml");
        newJoin = layerManger.createIntersect(inputs, "FIRST", toolfile, newJoin);
        return newJoin;
    }

    public String changeDistance(File toolfile, String distance) {

        layerManger.insertDistance(toolfile, distance);
        return toolfile.toString();
    }

    public String changePoint(File toolfile, String coor) {

        layerManger.insertPoint(toolfile, coor);
        return toolfile.toString();
    }

    public String changeSplitInput(File toolfile, List<Input> inputs, String[] splitInputs) {

        layerManger.insertSplitInputs(toolfile, inputs, splitInputs);
        return toolfile.toString();
    }*/
}
