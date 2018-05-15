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
import java.util.ArrayList;
import java.util.Arrays;
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
import org.narss.sdss.controllers.Properties;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
public class StyleManager {
    String RESTURL = "http://localhost:8080/geoserver";
    String RESTUSER = Properties.GEOSERVER_ADMIN;
    String RESTPW = Properties.GEOSERVER_PASS;

    GeoServerRESTReader reader;
      GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(RESTURL, RESTUSER, RESTPW);
      
     public  boolean publishStyle(File fileToUpload,String name) {
       // File zipFile = new File(fileLoc + fileToUpload.substring(fileToUpload.lastIndexOf('/')+1, fileToUpload.length()));
        boolean published = false;
        try {
            
            published = publisher.publishStyleInWorkspace("narss", fileToUpload, name);
           // published = publisher.publishShp("narss", "publicStore", fileToUpload, zipFile);
        } catch (IllegalArgumentException il) {
            il.printStackTrace();
        }
        return published;
    }
        
     public File insertColorInStyleFile( String[] Colors, File inputFile, File outputFile) {

        try {
            if (Colors.length != 0) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("CssParameter");
 
                for (int i = 0; i < Qurey.getLength(); i++) {
                    /// set layer name
                    Node queryAttr = Qurey.item(i);
                    
                    queryAttr.setTextContent(Colors[i]);
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
        return outputFile;

    }
        public File insertattributeInStyleFile( String attribute, File inputFile) {

        try {
            if (attribute != null) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("ogc:PropertyName");
 
                for (int i = 0; i < Qurey.getLength(); i++) {
                    /// set layer name
                    Node queryAttr = Qurey.item(i);
                    
                    queryAttr.setTextContent(attribute);
                }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(inputFile);
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
      
        public File insertattributevaluesInStyleFile( String[] attributevalues, File inputFile) {
//w2ft hnaa feh coding
        try {
            
           
            if (attributevalues.length != 0) {
               
                Double[] integers = new Double[attributevalues.length]; 
                 // Creates the integer array.
              for (int i = 0; i < integers.length; i++)
              {
                integers[i] = Double.parseDouble(attributevalues[i]);
              }
              Arrays.sort(integers);
               
                Double min= integers[0];
                
            
                Double max= integers[integers.length-1];
                Double mid = (max+min)/2 ;
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(inputFile);

                // Get the query element by tag name directly
                NodeList Qurey = doc.getElementsByTagName("ogc:Literal");
 
              //  for (int i = 0; i < Qurey.getLength(); i++) {
                    /// set layer name
                    Node queryAttr = Qurey.item(0);
                    Node queryAttr1 = Qurey.item(1);
                    Node queryAttr2 = Qurey.item(2);
                    Node queryAttr3 = Qurey.item(3);
                    queryAttr.setTextContent(mid.toString());
                    queryAttr1.setTextContent(mid.toString());
                    queryAttr2.setTextContent(max.toString());
                    queryAttr3.setTextContent(max.toString());
               // }

                // write the content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult strResult = new StreamResult(inputFile);
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
}
