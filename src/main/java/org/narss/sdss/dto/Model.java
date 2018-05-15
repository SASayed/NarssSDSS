/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dto;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.narss.sdss.controllers.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Model {

    private int id;
    private String name;
    private String description;
    //private List<Tool> tools;
    //private List<Input> inputs;
    private File file;
    private boolean hasMcda;
    private String result_layer;

    public Model() {
        id = 0;
        name = "";
        description = "";
        //tools = new ArrayList<Tool>();
        //inputs = new ArrayList<Input>();
        file = new File("");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    /*public List<Tool> getTools() {
        return tools;
    }

    public List<Tool> getTools(String modelId) {
        this.tools = getModelTools(modelId);
        return tools;
    }

    public void setTools(List<Tool> tools) {
        this.tools = tools;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> inputs) {
        this.inputs = inputs;
    }*/

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Boolean getHasMcda() {
        return hasMcda;
    }

    public void setHasMcda(Boolean has) {
        this.hasMcda = has;
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

    public File createModelRunFile(String[] toolFiles) {
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

                    doc = docBuilder.parse(toolFile);
                    Node main = doc.getElementsByTagName("wps:Execute").item(0);
                    /////remove all attributes of <wps:Execute> except 'service' and 'version' attributes
                    removeAllAttributesExcept(doc, "wps:Execute", "service", "version");
                    /// change output response from 'application/zip' to 'application/xml'
                    changeResponseOutput(doc, "wps:RawDataOutput", j);

                    ///get next toolfile to insert the pervious tool in it
                    doc2 = docBuilder.parse(nxttoolFile);
                    Node input = doc2.getElementsByTagName("wfs:GetFeature").item(0);
                    input = input.getParentNode().getParentNode().getParentNode();
                    if ("wps:Input".equals(input.getNodeName())) {
                        //clear <wps:input> of the current tags
                        String featureType = removeChilds(input);
                        //import the previous tool <wps:Execute>  into doc2
                        Node newmain = doc2.importNode(main, true);
                        //add new tags needed for chaining
                        createMissingNodes(doc2, input, featureType, newmain);
                    }

                    // write the content into xml file
                    TransformerFactory.newInstance().newTransformer().
                            transform(new DOMSource(doc2), new StreamResult(nxttoolFile));
                    this.setFile(nxttoolFile);
                }

            } else {
                File modeFiles = new File(toolFiles[0]);
                this.setFile(modeFiles);
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
        return this.file;
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

    private void createMissingNodes(Document doc, Node main, String featureType, Node newNode) {

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
    
    public String getResult_layer() {
        return result_layer;
    }
    
    public void setResult_layer(String result_layer) {
        this.result_layer = result_layer;
    }
}
