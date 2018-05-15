/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dto;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.narss.sdss.controllers.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
public class CaseStudy {
    
    private int id;
    private String name;
    private String description;
    private String status;
    private String expert;
    private String owner;
    private int model;
    private int mcda;
    private int style ;
    private int report ;

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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpert() {
        return expert;
    }

    public void setExpert(String expert) {
        this.expert = expert;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public int getMcda() {
        return mcda;
    }

    public void setMcda(int mcda) {
        this.mcda = mcda;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getReport() {
        return report;
    }

    public void setReport(int report) {
        this.report = report;
    }
    

    public CaseStudy(){
        id = 0;
        name = "";
        description = "";
        status = "Solved";
        expert = "";
        owner = "";
        model = 0;
        mcda = 0;
        style = 0;
        report = 0;
    }
    
    
    /*public String getModelNamebyId(String modelId) {
        String name = "";
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
                        if ("name".equals(toolNode.getNodeName())) {
                            ///list of tool inputs 
                            name = toolNode.getTextContent();
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

        return name;
    }
    
    public String getMCDANamebyId(String mcdaId) {
        String name = "";
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(Properties.MCDA);

            NodeList models_Ids = doc.getElementsByTagName("ID");
            //CaseStudy caseStudy= null;

            for (int i = 0; i < models_Ids.getLength(); i++) {
                Node id = models_Ids.item(i);
                if (mcdaId.equals(id.getTextContent())) {
                    Node model = id.getParentNode();
                    NodeList modelItems = model.getChildNodes();
                    for (int j = 0; j < modelItems.getLength(); j++) {
                        Node toolNode = modelItems.item(j);
                        if ("Name".equals(toolNode.getNodeName())) {
                            ///list of tool inputs 
                            name = toolNode.getTextContent();
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

        return name;
    }*/

}
