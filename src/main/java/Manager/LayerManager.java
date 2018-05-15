/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Manager;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.narss.sdss.controllers.ClientAuthentication;
import org.narss.sdss.controllers.Properties;
import org.narss.sdss.dto.Input;
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
public class LayerManager {

    String RESTURL = "http://localhost:8080/geoserver";
    String RESTUSER = Properties.GEOSERVER_ADMIN;
    String RESTPW = Properties.GEOSERVER_PASS;

    GeoServerRESTReader reader;
    GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);

    public  boolean publishLayer(String fileLoc, String fileToUpload) {
        File zipFile = new File(fileLoc + fileToUpload.substring(fileToUpload.lastIndexOf('/')+1, fileToUpload.length()));
        boolean published = false;
        try {
            published = publisher.publishShp("narss", "publicStore", fileToUpload, zipFile);
        } catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        } catch (IllegalArgumentException il) {
            il.printStackTrace();
        }
        return published;
    }

    public RESTLayer readLayer() {
        RESTLayer lyr = null;
        try {
            reader = new GeoServerRESTReader(RESTURL, RESTUSER, RESTPW);
            reader.getLayer(RESTURL, RESTPW);
        } catch (MalformedURLException mf) {
            mf.printStackTrace();
        }
        return lyr;
    }

    //get Lat-Lon of layer
    public List<String> getLayerExtent(String layername) {

        List<String> result = new ArrayList<String>();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(Properties.Capabilites);

            NodeList layers = doc.getElementsByTagName("Layer");
            //CaseStudy caseStudy= null;

            for (int i = 0; i < layers.getLength(); i++) {
                NodeList layer = layers.item(i).getChildNodes();
                for (int j = 0; j < layer.getLength(); j++) {
                    Node layerItem = layer.item(j);
                    if ("Name".equals(layerItem.getNodeName()) && layername.equals(layerItem.getTextContent())) {
                        while (layerItem.getNextSibling() != null) {
                            layerItem = layerItem.getNextSibling();
                            if ("BoundingBox".equals(layerItem.getNodeName())) {
                                NamedNodeMap attr = layerItem.getAttributes();
                                String minx = attr.getNamedItem("minx").getNodeValue();
                                String miny = attr.getNamedItem("miny").getNodeValue();
                                String maxx = attr.getNamedItem("maxx").getNodeValue();
                                String maxy = attr.getNamedItem("maxy").getNodeValue();
                                result.add(minx);
                                result.add(miny);
                                result.add(maxx);
                                result.add(maxy);
                                j = layer.getLength();
                                i = layers.getLength();
                                break;
                            }
                        }

                    }
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    //String URL;
    public String uploadLayer(CloseableHttpClient httpclient, String url, File fileToUpload, String content_Type) {
        //get file to upload
        MultipartEntity mpEntity = new MultipartEntity();
        mpEntity.addPart("userFile", new FileBody(fileToUpload, "zip"));
        String result = "";
        try {
            //initiate the request
            HttpPut httpPut = new HttpPut(url);
            httpPut.setHeader("Content-Type", content_Type);
            httpPut.setEntity(mpEntity);
            CloseableHttpResponse response = httpclient.execute(httpPut);
            result = response.toString();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return result;
    }

    public List<String> getAllLayers(CloseableHttpClient httpclient, String url, List<String> geoLayers) {

        try {
            //initiate the request
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            String responseString = new BasicResponseHandler().handleResponse(response);
            //System.out.println(responseString);

            JSONObject jsonObj = new JSONObject(responseString);

            JSONArray layersArr = jsonObj.getJSONObject("featureTypes").getJSONArray("featureType");
            for (int i = 0; i < layersArr.length(); i++) {
                String name = layersArr.getJSONObject(i).getString("name");
                geoLayers.add(name);
                //System.out.println(name);
            }

        } catch (IOException io) {
            io.printStackTrace();
        } catch (JSONException jex) {
            jex.printStackTrace();
        }

        return geoLayers;
    }

    public List<String> getGeoLayers(String geoRest, String workspace, String outputType) {

        //set Authentication
        ClientAuthentication clientAuthentication = new ClientAuthentication();
        CloseableHttpClient httpclient = clientAuthentication.authenticate(Properties.GEOSERVER_ADMIN, Properties.GEOSERVER_PASS);

        // List<Layer> geoLayers = new ArrayList<Layer>();
        List<String> geoLayers = new ArrayList<String>();
        geoLayers = getAllLayers(httpclient, geoRest + "workspaces/" + workspace + "/featuretypes." + outputType, geoLayers);
        System.out.print("-----Layer Count------" + geoLayers.size() + "------------------");
        return geoLayers;
    }

    public String descripeLayer(String URL, String Request, String WorkSpace, String Layername) {

        String post = URL + "/wfs?request=" + Request + "&typename=" + WorkSpace + Layername;
        return post;
    }

    public int countAttributes(String file) {

        int result = 0;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            NodeList elements = doc.getElementsByTagName("xsd:element");
            result = elements.getLength() - 3;

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }

        return result;
    }

    public String[] GetLayerAttributes(String file, int Count) {

        String[] result = new String[Count];
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(file);

            // Get the staff element , it may not working if tag has spaces, or
            // whatever weird characters in front...it's better to use
            // getElementsByTagName() to get it directly.
            // Node staff = company.getFirstChild();
            // Get the staff element by tag name directly
            NodeList elements = doc.getElementsByTagName("xsd:element");

            if (elements.getLength() != 0) {
                for (int i = 0; i < elements.getLength() - 3; i++) {

                    Node element = elements.item(i + 1);
                    NamedNodeMap attr = element.getAttributes();
                    Node elementName = attr.getNamedItem("name");
                    //elementName.getNodeValue();
                    System.out.println(elementName.getNodeValue());
                    result[i] = elementName.getNodeValue().toString();

                }
            }

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }

        return result;
        //String post = URL+"/wfs?request="+Request+"&typename="+WorkSpace+":"+Layername;
        // return post;
    }

    public File newQuery(String mainfile, String layerName, String attr, String newfile) {

        File finalresult = new File(newfile);
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(mainfile);

            // Get the query element by tag name directly
            Node Qurey = doc.getElementsByTagName("wfs:Query").item(0);
            Node attribute = doc.getElementsByTagName("wps:LiteralData").item(0);

            /// set layer name
            NamedNodeMap queryAttr = Qurey.getAttributes();
            Node lyrName = queryAttr.getNamedItem("typeName");
            lyrName.setNodeValue(layerName);

            //set attribue;
            attribute.setTextContent(attr);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(finalresult);
            transformer.transform(source, result);
            //finalresult = newfile;

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }

        return finalresult;

    }

    public File newFilter(File mainfile, String layerName, String attr, String aternatives, String criteria, File newfile) {

        File finalresult = newfile;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(mainfile);

            // Get the staff element by tag name directly
            Node Qurey = doc.getElementsByTagName("wfs:Query").item(0);
            Node attribute = doc.getElementsByTagName("wps:LiteralData").item(0);
            Node complexData = doc.getElementsByTagName("wps:ComplexData").item(0);

            /// set layer name
            NamedNodeMap queryAttr = Qurey.getAttributes();
            Node lyrName = queryAttr.getNamedItem("typeName");
            lyrName.setNodeValue(layerName);

            //set attribue;
            attribute.setTextContent(criteria);

            //set condition;
            Node cdata = doc.createCDATASection(attr + "='" + aternatives + "'");
            complexData.appendChild(cdata);
            //finalresult = doc.toString();

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(finalresult);
            transformer.transform(source, result);

        } catch (TransformerException te) {
            te.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }

        return finalresult;

    }

    public File postQuery(String url, File postedFile, File responseFile) {

        try {
            //Post Query to geoserver
            Response response = Request.Post(url).bodyFile(postedFile, ContentType.TEXT_XML).execute();
            response.saveContent(responseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseFile;
    }

    public Response postQueryString(String url, String postedFile) {
        Response response = null;
        try {
            //Post Query to geoserver
            response = Request.Post(url).bodyString(postedFile, ContentType.TEXT_XML).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String getRealWeightOfAlternative(String url, File mainfile, String layerName, String attr, String aternatives, String criteria, File newfile) {
        String result = "";
        try {
            // reader.getf
            File newQueryFilter = newFilter(mainfile, layerName, attr, aternatives, criteria, newfile);
            File response = postQuery(url, newQueryFilter, newfile);

            //Get value of the returned file 
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(response);

            Node mainNode = doc.getElementsByTagName("feature:" + criteria).item(0);
            result = mainNode.getTextContent();

            System.out.print(result);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }

        return result;
    }

    public int getValuesLength(File inputfile, String attr) {

        int result = 0;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputfile);

            //NodeList list =  doc.getElementsByTagName("feature:"+attr);
            NodeList list = doc.getElementsByTagName("feature:" + attr);
            result = list.getLength();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
        return result;
    }

    public String[] getAttributeValues(File inputfile, String attr, int valuesNo) {

        String[] result = new String[valuesNo];
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputfile);

            //NodeList list =  doc.getElementsByTagName("feature:"+attr);
            NodeList list = doc.getElementsByTagName("feature:" + attr);
            if (list.getLength() != 0) {
                //list.item(0).getNodeName();
                for (int i = 0; i < result.length; i++) {
                    result[i] = list.item(i).getTextContent();
                    //System.out.println(result[i]);
                }
            }

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
        return result;
    }

    public String[] createAlternativesFile(String emptyAlternativesFile, String[] alternatives, String newAlterantivesFile, int valuesNo) {

        String[] alternativeIds = new String[valuesNo];
        try {
            if (alternatives.length != 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(emptyAlternativesFile);

                for (int i = 0; i < alternatives.length; i++) {
                    if (alternatives[i] != null) {
                        Node mainNode = doc.getElementsByTagName("alternatives").item(0);
                        Element newNode = doc.createElement("alternative");
                        newNode.setAttribute("id", "A" + i);
                        newNode.setAttribute("name", alternatives[i]);
                        mainNode.appendChild(newNode);

                        alternativeIds[i] = "A" + i;
                    }
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(newAlterantivesFile));
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
        return alternativeIds;
    }

    public String[] createCriteriaFile(String emptyCriteriaFile, String[] criterias, String[] prefrences, String newCriteriaFile, int valuesNo) {

        String[] criteriaIds = new String[valuesNo];
        try {
            if (criterias.length != 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(emptyCriteriaFile);
                Node mainNode = doc.getElementsByTagName("criteria").item(0);
                String[] swap_alias = Properties.SWAPS_ALIAS;
                String[] swap_real = Properties.SWAPS_REAL;
                for (int i = 0; i < criterias.length; i++) {
                    if (criterias[i] != null) {
                        boolean Swap = false;
                        String alias = "";
                        //create main criteria node
                        Element newNode = doc.createElement("criterion");
                        newNode.setAttribute("id", "C" + i);
                        for (int a = 0; a < swap_real.length; a++) {
                            if (criterias[i].equals(swap_real[a])) {
                                Swap = true;
                                alias = swap_alias[a];
                                break;
                            }
                        }
                        if (Swap) {
                            newNode.setAttribute("name", alias);
                        } else {
                            newNode.setAttribute("name", criterias[i]);
                        }
                        mainNode.appendChild(newNode);

                        //create Subnodes
                        Element subNode_1 = doc.createElement("scale");
                        Element subNode_11 = doc.createElement("quantitative");
                        Element subNode_111 = doc.createElement("preferenceDirection");
                        subNode_111.appendChild(doc.createTextNode(prefrences[i]));

                        //Add Subnodes to Its main nodes
                        subNode_11.appendChild(subNode_111);
                        subNode_1.appendChild(subNode_11);
                        newNode.appendChild(subNode_1);

                        //create Subnodes
                        Element subNode_2 = doc.createElement("thresholds");
                        Element subNode_22 = doc.createElement("threshold");
                        subNode_22.setAttribute("mcdaConcept", "ind");
                        Element subNode_222 = doc.createElement("constant");
                        Element subNode_2222 = doc.createElement("real");
                        subNode_2222.appendChild(doc.createTextNode("0.0"));

                        Element subNode_23 = doc.createElement("threshold");
                        subNode_23.setAttribute("mcdaConcept", "pref");
                        Element subNode_233 = doc.createElement("constant");
                        Element subNode_2333 = doc.createElement("real");
                        subNode_2333.appendChild(doc.createTextNode("100.0"));

                        //Add Subnodes to Its main nodes
                        subNode_222.appendChild(subNode_2222);
                        subNode_22.appendChild(subNode_222);
                        subNode_2.appendChild(subNode_22);

                        subNode_233.appendChild(subNode_2333);
                        subNode_23.appendChild(subNode_233);
                        subNode_2.appendChild(subNode_23);

                        newNode.appendChild(subNode_2);

                        criteriaIds[i] = "C" + i;

                        System.out.println(criteriaIds[i]);

                    }
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(newCriteriaFile));
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
        return criteriaIds;
    }

    public void createWeightsFile(String emptyWeightFile, String[] criteriaIds, String[] weights, String newWeightFile) {

        try {
            if (criteriaIds.length != 0 && weights.length != 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(emptyWeightFile);

                Node mainNode = doc.getElementsByTagName("criteriaValues").item(0);
                //System.out.print("Done");
                for (int i = 0; i < criteriaIds.length; i++) {
                    if (criteriaIds[i] != null) {

                        //create main criteria node
                        Element newNode = doc.createElement("criterionValue");
                        mainNode.appendChild(newNode);

                        //create Subnodes
                        Element subNode_1 = doc.createElement("criterionID");
                        subNode_1.appendChild(doc.createTextNode(criteriaIds[i]));

                        Element subNode_2 = doc.createElement("value");
                        Element subNode_22 = doc.createElement("real");
                        subNode_22.appendChild(doc.createTextNode(weights[i]));

                        //Add Subnodes to Its main nodes
                        subNode_2.appendChild(subNode_22);

                        newNode.appendChild(subNode_1);
                        newNode.appendChild(subNode_2);

                    }
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(newWeightFile));
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
    }

    public void createPerformanceFile(String emptyPerformanceFile, String emptyAttributeFile, String newAttributeFile, String attribute, String[] alternatives, String[] alternativesIds, String[] criterias, String[] criteriaIds, String newPerformanceFile,
            String wpsUrl, File filterFile, String lyrName, File newFilterFile) {

        try {
            if (criteriaIds.length != 0 && alternativesIds.length != 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(emptyPerformanceFile);
                Document doc2 = docBuilder.parse(emptyAttributeFile);
                //System.out.print("Done");

                Node mainNode = doc.getElementsByTagName("performanceTable").item(0);
                Node mainNode_att = doc2.getElementsByTagName("performanceTable").item(0);

                for (int i = 0; i < alternativesIds.length; i++) {
                    if (alternativesIds[i] != null) {

                        //create main alternative Performance node
                        Element newNode = doc.createElement("alternativePerformances");
                        mainNode.appendChild(newNode);

                        Element alter_node = doc.createElement("alternativeID");
                        alter_node.appendChild(doc.createTextNode(alternativesIds[i]));
                        newNode.appendChild(alter_node);

                        //For Attribute File
                        Element newNode_Att = doc2.createElement("alternativePerformances");
                        mainNode_att.appendChild(newNode_Att);
                        Element alter_node_att = doc2.createElement("alternativeID");
                        alter_node_att.appendChild(doc2.createTextNode(alternativesIds[i]));
                        newNode_Att.appendChild(alter_node_att);
                        String[] swap_alias = Properties.SWAPS_ALIAS;
                        String[] swap_real = Properties.SWAPS_REAL;
                        //create main Performance nodes that equal the criteria no.
                        for (int j = 0; j < criteriaIds.length; j++) {
                            if (criteriaIds[j] != null) {

                                ///Nodes For Performance File
                                Element perf_1 = doc.createElement("performance");

                                Element criteria_1 = doc.createElement("criterionID");
                                criteria_1.appendChild(doc.createTextNode(criteriaIds[j]));
                                perf_1.appendChild(criteria_1);

                                Element val_1 = doc.createElement("value");
                                Element realVal_1 = doc.createElement("real");

                                //Get Real Value of Criteria for that Alternative
                                String realValue = getRealWeightOfAlternative(wpsUrl, filterFile, lyrName, attribute, alternatives[i], criterias[j], newFilterFile);

                                realVal_1.appendChild(doc.createTextNode(realValue));
                                val_1.appendChild(realVal_1);
                                perf_1.appendChild(val_1);

                                newNode.appendChild(perf_1);

                                ///Nodes For Attribute File For Report
                                Element perf_2 = doc2.createElement("performance");
                                Element criteria_2 = doc2.createElement("criterionID");
                                boolean Swap = false;
                                String alias = "";
                                for (int a = 0; a < swap_real.length; a++) {
                                    if (criterias[j].equals(swap_real[a])) {
                                        Swap = true;
                                        alias = swap_alias[a];
                                        break;
                                    }
                                }
                                Element realVal_2;
                                if (Swap) {
                                    criteria_2.appendChild(doc2.createTextNode(alias));
                                    realVal_2 = doc2.createElement("real_" + alias);

                                } else {
                                    criteria_2.appendChild(doc2.createTextNode(criterias[j]));
                                    realVal_2 = doc2.createElement("real_" + criterias[j]);

                                }

                                perf_2.appendChild(criteria_2);
                                realVal_2.appendChild(doc2.createTextNode(realValue));
                                perf_2.appendChild(realVal_2);
                                newNode_Att.appendChild(perf_2);
                            }
                        }
                    }
                }

                // write the content into xml file
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(newPerformanceFile));
                TransformerFactory.newInstance().newTransformer().transform(source, result);

                //Save Attibute File
                DOMSource source2 = new DOMSource(doc2);
                StreamResult result2 = new StreamResult(new File(newAttributeFile));
                TransformerFactory.newInstance().newTransformer().transform(source2, result2);
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
    }

    public void createAttributeValFile(String emptyPerformanceFile, String attribute, String[] alternatives, String[] alternativesIds, String[] criterias, String[] criteriaIds, String newPerformanceFile,
            String wpsUrl, File filterFile, String lyrName, File newFilterFile) {

        try {
            if (criteriaIds.length != 0 && alternativesIds.length != 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(emptyPerformanceFile);

                Node mainNode = doc.getElementsByTagName("performanceTable").item(0);

                for (int i = 0; i < alternativesIds.length; i++) {
                    if (alternativesIds[i] != null) {

                        //create main alternative Performance node
                        Element newNode = doc.createElement("alternativePerformances");
                        mainNode.appendChild(newNode);

                        Element alter_node = doc.createElement("alternativeID");
                        alter_node.appendChild(doc.createTextNode(alternativesIds[i]));
                        newNode.appendChild(alter_node);

                        //create main Performance nodes that equal the criteria no.
                        for (int j = 0; j < criteriaIds.length; j++) {
                            if (criteriaIds[j] != null) {

                                Element perf_1 = doc.createElement("performance");

                                Element criteria_1 = doc.createElement("criterionID");
                                criteria_1.appendChild(doc.createTextNode(criterias[j]));
                                perf_1.appendChild(criteria_1);

                                Element realVal_1 = doc.createElement("real");

                                //Get Real Value of Criteria for that Alternative
                                String realValue = getRealWeightOfAlternative(wpsUrl, filterFile, lyrName, attribute, alternatives[i], criterias[j], newFilterFile);

                                realVal_1.appendChild(doc.createTextNode(realValue));

                                perf_1.appendChild(realVal_1);

                                newNode.appendChild(perf_1);
                            }
                        }
                    }
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(newPerformanceFile));
                transformer.transform(source, result);
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
    }

    public File createBuffer(File bufferFile, List<Input> inputs, File result) {

        try {
            if (inputs.size() > 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(bufferFile);

                // Get the query element by tag name directly
                Node Qurey = doc.getElementsByTagName("wfs:Query").item(0);

                for (int i = 0; i < inputs.size(); i++) {
                    if ("number".equals(inputs.get(i).getType())) {
                        //set distance;
                        Node attribute = doc.getElementsByTagName("wps:LiteralData").item(0);
                        attribute.setTextContent(inputs.get(i).getValue());
                    }
                    if ("vector".equals(inputs.get(i).getType())) {
                        /// set layer name
                        NamedNodeMap queryAttr = Qurey.getAttributes();
                        Node lyrName = queryAttr.getNamedItem("typeName");
                        lyrName.setNodeValue(inputs.get(i).getName());
                    }
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(result);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return result;

    }

    public File createClip(File clipFile, List<Input> inputs, File result) {

        try {
            if (!inputs.isEmpty()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(clipFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("wfs:Query");

                for (int i = 0; i < Qurey.getLength(); i++) {
                    /// set layer name
                    NamedNodeMap queryAttr = Qurey.item(i).getAttributes();
                    Node lyrName = queryAttr.getNamedItem("typeName");
                    lyrName.setNodeValue(inputs.get(i).getName());
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(result);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return result;

    }

    public File createSplit(File splitfile, List<Input> inputs, File result) {
        try {
            if (inputs.size() > 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(splitfile);

                NodeList coorList = doc.getElementsByTagName("wps:ComplexData");
                for (int j = 0; j < coorList.getLength(); j++) {
                    if (inputs.get(j).getValue() != "") {
                        //set coor;
                        Node coor = coorList.item(j);
                        Node cdata = null;
                        if (j == 0) {
                            cdata = doc.createCDATASection("POLYGON ((" + inputs.get(j).getValue() + "))");
                        } else if (j == 1) {
                            cdata = doc.createCDATASection("LINESTRING (" + inputs.get(j).getValue() + ")");
                        }
                        coor.appendChild(cdata);
                    }
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(result);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return result;
    }

    public File createIntersection(List<String> layers, String outputContains, File intersectFile, File result) {

        try {
            if (!layers.isEmpty() && outputContains != "") {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(intersectFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("wfs:Query");

                for (int i = 0; i < Qurey.getLength(); i++) {
                    /// set layer name
                    NamedNodeMap queryAttr = Qurey.item(i).getAttributes();
                    Node lyrName = queryAttr.getNamedItem("typeName");
                    lyrName.setNodeValue(layers.get(i));
                }

                NodeList data = doc.getElementsByTagName("wps:LiteralData");
                for (int j = 0; j < data.getLength(); j++) {

                    /// set intersectin output type
                    String intersectValue = data.item(j).getTextContent();
                    if (intersectValue.equals("INTERSECTION")) {
                        data.item(j).setTextContent(outputContains);
                    }
                }
                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(result);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return result;

    }

    public File createIntersect(List<Input> layers, String outputContains, File intersectFile, File result) {
        try {
            if (!layers.isEmpty() && outputContains != "") {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(intersectFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("wfs:Query");

                for (int i = 0; i < Qurey.getLength(); i++) {
                    /// set layer name
                    NamedNodeMap queryAttr = Qurey.item(i).getAttributes();
                    Node lyrName = queryAttr.getNamedItem("typeName");
                    lyrName.setNodeValue(layers.get(i).getName());
                }

                NodeList data = doc.getElementsByTagName("wps:LiteralData");
                for (int j = 0; j < data.getLength(); j++) {

                    /// set intersectin output type
                    String intersectValue = data.item(j).getTextContent();
                    if (intersectValue.equals("INTERSECTION")) {
                        data.item(j).setTextContent(outputContains);
                    }
                }
                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(result);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return result;

    }

    public File createUnion(File unionFile, List<Input> layers, File result) {

        try {
            if (!layers.isEmpty()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(unionFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("wfs:Query");

                for (int i = 0; i < Qurey.getLength(); i++) {
                    /// set layer name
                    NamedNodeMap queryAttr = Qurey.item(i).getAttributes();
                    Node lyrName = queryAttr.getNamedItem("typeName");
                    lyrName.setNodeValue(layers.get(i).getName());
                }
                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(result);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return result;

    }

    public File getNearest(File nearestFile, List<Input> inputs, File result) {
        try {
            if (inputs.size() > 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(nearestFile);

                for (int i = 0; i < inputs.size(); i++) {
                    if ("coor".equals(inputs.get(i).getType())) {
                        //set coor;
                        Node point = doc.getElementsByTagName("wps:ComplexData").item(0);
                        //set point;
                        //Node cdata = doc.createCDATASection("POINT (" + pointX + " " + pointY + ")");
                        Node cdata = doc.createCDATASection("POINT (" + inputs.get(i).getValue() + ")");
                        point.appendChild(cdata);
                    }
                    if ("vector".equals(inputs.get(i).getType())) {
                        // Get the query element by tag name directly
                        Node Qurey = doc.getElementsByTagName("wfs:Query").item(0);

                        /// set layer name
                        NamedNodeMap queryAttr = Qurey.getAttributes();
                        Node lyrName = queryAttr.getNamedItem("typeName");
                        lyrName.setNodeValue(inputs.get(i).getName());
                    }
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(result);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return result;

    }

    public File postRequest(String url, File postedFile, ContentType ct, File responseFile) {

        try {
            //Post Query to geoserver
            Response response = Request.Post(url).bodyFile(postedFile, ct).execute();
            response.saveContent(responseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseFile;
    }

    public File getCapabilites(String url, File responseFile) {
        try {
            //Post Query to geoserver
            Response response = Request.Post(url).execute();
            response.saveContent(responseFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseFile;
    }

    public String JpostRequest(String url, String postedFile, String responseFile) {

        try {
            //Post Query to geoserver
            Response response = Request.Post(url).bodyFile(new File(postedFile), ContentType.TEXT_XML).execute();
            response.saveContent(new File(responseFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseFile;
    }

    public File insertLayersInToolFile(String workspace, String[] layers, File inputFile, File outputFile) {

        try {
            if (layers.length != 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("wfs:Query");

                for (int i = 0; i < Qurey.getLength(); i++) {
                    /// set layer name
                    NamedNodeMap queryAttr = Qurey.item(i).getAttributes();
                    Node lyrName = queryAttr.getNamedItem("typeName");
                    lyrName.setNodeValue(workspace + layers[i]);
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(outputFile);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return inputFile;

    }

    public String J_InsertLayers(String workspace, String[] layers, String inputFile, String outputFile) {

        try {
            if (layers.length != 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("wfs:Query");

                for (int i = 0; i < Qurey.getLength(); i++) {
                    /// set layer name
                    NamedNodeMap queryAttr = Qurey.item(i).getAttributes();
                    Node lyrName = queryAttr.getNamedItem("typeName");
                    lyrName.setNodeValue(workspace + layers[i]);
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(new File(outputFile));
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return inputFile;

    }

    public File insertSingelLayerInToolFile(String workspace, String[] layers, File inputFile, File outputFile) {
        try {
            if (layers.length != 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("wfs:Query");

                for (int i = 1; i < Qurey.getLength(); i++) {
                    /// set layer name
                    NamedNodeMap queryAttr = Qurey.item(i).getAttributes();
                    Node lyrName = queryAttr.getNamedItem("typeName");
                    //lyrName.setNodeValue(workspace + layers);
                    lyrName.setNodeValue(workspace + layers[i-1]);
                }
                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(outputFile);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return inputFile;

    }
public File tryInsertSingelLayerInToolFile(String workspace, String layer, File inputFile, File outputFile) {
        try {
            if (layer != null) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("wfs:Query");

                for (int i = 1; i < Qurey.getLength(); i++) {
                    /// set layer name
                    NamedNodeMap queryAttr = Qurey.item(i).getAttributes();
                    Node lyrName = queryAttr.getNamedItem("typeName");
                    //lyrName.setNodeValue(workspace + layers);
                    lyrName.setNodeValue(workspace + layer);
                }
                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(outputFile);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return inputFile;

    }
    public String J_InsertSingelLayer(String workspace, String[] layers, String inputFile, String outputFile) {
        try {
            if (layers.length != 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("wfs:Query");

                for (int i = 1; i < Qurey.getLength(); i++) {
                    /// set layer name
                    NamedNodeMap queryAttr = Qurey.item(i).getAttributes();
                    Node lyrName = queryAttr.getNamedItem("typeName");
                    lyrName.setNodeValue(workspace + layers[i - 1]);
                    //lyrName.setNodeValue(workspace + layers[i]);
                }
                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(new File(outputFile));
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return inputFile;

    }

    public File insertDistance(File bufferFile, File newfile, String distance) {

        try {
            if (distance != "") {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(bufferFile);

                //set distance;
                Node attribute = doc.getElementsByTagName("wps:LiteralData").item(0);
                attribute.setTextContent(distance);

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(newfile);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return bufferFile;

    }

    public String J_InsertDistance(String bufferFile, String newfile, String distance) {

        try {
            if (distance != "") {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(bufferFile);

                //set distance;
                Node attribute = doc.getElementsByTagName("wps:LiteralData").item(0);
                attribute.setTextContent(distance);

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(new File(newfile));
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return bufferFile;

    }

    public File insertPoint(File file, File newfile, String coor) {

        try {
            if (coor != "") {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(file);

                //set distance;
                Node attribute = doc.getElementsByTagName("wps:ComplexData").item(0);
                Node cdata = doc.createCDATASection("POINT (" + coor + ")");
                attribute.appendChild(cdata);

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(newfile);
                transformer.transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return file;

    }

    public String J_InsertPoint(String file, String newfile, String coor) {
        try {
            if (coor != "") {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(file);

                //set distance;
                Node attribute = doc.getElementsByTagName("wps:ComplexData").item(0);
                Node cdata = doc.createCDATASection("POINT (" + coor + ")");
                attribute.appendChild(cdata);

                // write the content into xml file
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(new File(newfile));
                TransformerFactory.newInstance().newTransformer().transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return file;

    }

    public File insertSplitInputs(File file, File newfile, String[] splitInputs) {
        try {
            if (splitInputs.length > 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(file);

                NodeList coorList = doc.getElementsByTagName("wps:ComplexData");
                for (int j = 0; j <= coorList.getLength(); j++) {
                    //set coor;
                    Node coor = coorList.item(j);
                    Node cdata = null;
                    if (j == 0) {
                        cdata = doc.createCDATASection("LINESTRING ((" + splitInputs[j] + "))");
                    } else if (j == 1) {
                       cdata = doc.createCDATASection("POLYGON ((" + splitInputs[j] + "))"); 
                    }
                    coor.appendChild(cdata);

                }

                // write the content into xml file
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(newfile);
                TransformerFactory.newInstance().newTransformer().transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return file;

    }
     public File insertSplitPolyInput(File file, File newfile, String splitPolyInputs) {
        try {
            if (splitPolyInputs!= null) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(file);

                NodeList coorList = doc.getElementsByTagName("wps:ComplexData");
                NodeList IdentifierList = doc.getElementsByTagName("ows:Identifier");
                 //set coor;
                 for (int i=0;i<=IdentifierList.getLength()-1;i++)
                 {
                     String u=IdentifierList.item(i).getTextContent();
                 if(u.equals("polygon"))
                 {
                     Node coor = coorList.item(0);
                     Node cdata = null;
                     cdata = doc.createCDATASection("POLYGON ((" + splitPolyInputs + "))"); 
                     coor.appendChild(cdata);
                 }
                 
                 }
                    

                // write the content into xml file
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(newfile);
                TransformerFactory.newInstance().newTransformer().transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return file;

    }
   public File insertSplitLineInput(File file, File newfile, String splitLineInputs) {
        try {
            if (splitLineInputs!= null) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(file);

                NodeList coorList = doc.getElementsByTagName("wps:ComplexData");
                //NodeList coorList = doc.getElementsByTagName("wps:ComplexData");
                NodeList IdentifierList = doc.getElementsByTagName("ows:Identifier");
                 //set coor;
                 for (int i=0;i<=IdentifierList.getLength()-1;i++)
                 {
                     String u=IdentifierList.item(i).getTextContent();
                  if(u.equals("line"))
                 {
                     Node coor = coorList.item(1);
                     Node cdata = null;
                     cdata = doc.createCDATASection("LINESTRING ((" + splitLineInputs + "))"); 
                     coor.appendChild(cdata);
                 }
                 }
                 //set coor;
               

                // write the content into xml file
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(newfile);
                TransformerFactory.newInstance().newTransformer().transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return file;

    }
    public String J_InsertSplitInputs(String file, String newfile, String[] splitInputs) {
        try {
            if (splitInputs.length > 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(file);

                NodeList coorList = doc.getElementsByTagName("wps:ComplexData");
                for (int j = 0; j < coorList.getLength(); j++) {
                    //set coor;
                    Node coor = coorList.item(j);
                    Node cdata = null;
                    if (j == 0) {
                        cdata = doc.createCDATASection("POLYGON ((" + splitInputs[j] + "))");
                    } else if (j == 1) {
                        cdata = doc.createCDATASection("LINESTRING (" + splitInputs[j] + ")");
                    }
                    coor.appendChild(cdata);

                }

                // write the content into xml file
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(new File(newfile));
                TransformerFactory.newInstance().newTransformer().transform(source, strResult);

            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (TransformerConfigurationException tce) {
            tce.printStackTrace();
        } catch (TransformerException te) {
            te.printStackTrace();
        }
        return file;

    }

    public NodeList returnNode(File toolfile) {

        NodeList list = null;
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(toolfile);

            //set distance;
            Node first = doc.getElementsByTagName("wps:Execute").item(0);
            list = first.getChildNodes();

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return list;

    }

    public File addNodeList(File toolfile, NodeList list) {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(toolfile);

            //set distance;
            Node first = doc.getElementsByTagName("wps:Input").item(0);
            NodeList lst = first.getChildNodes();
            for (int j = 0; j < lst.getLength(); j++) {

                first.removeChild(lst.item(j));

            }
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                first.appendChild(node);
            }

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ie) {
            ie.printStackTrace();
        }

        return null;
    }
}

