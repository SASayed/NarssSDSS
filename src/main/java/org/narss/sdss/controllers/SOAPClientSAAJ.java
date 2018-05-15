/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.soap.*;

/**
 *
 * @author wh_sayed
 */
public class SOAPClientSAAJ {
    
    //--------------------------------------------------------------------------
    public File createSubmitProblemSOAPFile()
    {
        File soapFile = new File(Properties.SOAP_REQUESTS+"submitProblemSOAPFile.txt");
        String allXmlInput = "";
        String xmlContent = "";
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "    <SOAP-ENV:Envelope\n" +
                    "      SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"\n" +
                    "      xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"\n" +
                    "      xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\"\n" +
                    "      xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                    "      xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">\n" +
                    "    <SOAP-ENV:Body>\n" +
                    "    <submitProblem SOAP-ENC:root=\"1\">";
        String footer = "</submitProblem>\n" +
                        "    </SOAP-ENV:Body>\n" +
                        "    </SOAP-ENV:Envelope>";
        try{

            byte[] encoded1 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT+"newAlternatives.xml"));
            String soapXml1 = new String(encoded1, StandardCharsets.UTF_8);
            soapXml1 = "<alternatives xsi:type=\"xsd:string\">" + "<![CDATA[" + soapXml1 + "]]>" + "</alternatives>";
            
            byte[] encoded2 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT+"newWeights.xml"));
            String soapXml2 = new String(encoded2, StandardCharsets.UTF_8);
            soapXml2 = "<criteriaWeights xsi:type=\"xsd:string\">" + "<![CDATA[" + soapXml2 + "]]>" + "</criteriaWeights>";
            
            byte[] encoded3 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT+"newPerformances.xml"));
            String soapXml3 = new String(encoded3, StandardCharsets.UTF_8);
            soapXml3 = "<performanceTable xsi:type=\"xsd:string\">" + "<![CDATA[" + soapXml3 + "]]>" + "</performanceTable>";
            
            byte[] encoded4 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT+"newCriterias.xml"));
            String soapXml4 = new String(encoded4, StandardCharsets.UTF_8);
            soapXml4 = "<criteria xsi:type=\"xsd:string\">" + "<![CDATA[" + soapXml4 + "]]>"  + "</criteria>";
            
            allXmlInput = soapXml1+soapXml2+soapXml3+soapXml4;
            xmlContent = header+allXmlInput+footer;
            
            FileWriter writer = new FileWriter(soapFile);
            writer.write(xmlContent);
            writer.flush();
            writer.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        return soapFile;
    }
    //--------------------------------------------------------------------------
    public byte[] createSubmitProblemSOAPRequest(String filePath)
    {
        try{
            byte[] encoded = Files.readAllBytes(Paths.get(filePath));
            String soapXml = new String(encoded, StandardCharsets.UTF_8);
            System.out.println("Request: " + soapXml);
            return encoded;
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    //--------------------------------------------------------------------------
    public String sendSubmitProblemSOAPRequest(byte[] request, String url)
    {
        try{
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            java.net.URL endpoint = new URL(url);
            SOAPConnection connection = soapConnectionFactory.createConnection();
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(request));
            message.saveChanges();
            
            SOAPMessage response = connection.call(message, endpoint);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.writeTo(out);
            String strMsg = new String(out.toByteArray());
            return strMsg;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    //--------------------------------------------------------------------------
    public String readTicketValue(String response)
    {
        try{
            int index1 = response.indexOf("<ticket");
            String temp1 = response.substring(index1, response.length());
            int index2 = temp1.indexOf("</ticket>");
            String temp2 = temp1.substring(0, index2);
            String ticketValue = temp2.substring(temp2.lastIndexOf(">")+1, temp2.length());
            return ticketValue;
        } catch(Exception e) {
            return "";
        }
    }
    //--------------------------------------------------------------------------
    public File createRequestSolutionSOAPFile(String ticketValue)
    {
        File soapFile = new File(Properties.SOAP_REQUESTS+"requestSolutionSOAPFile.txt");
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<SOAP-ENV:Envelope\n" +
                        "  SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"\n" +
                        "  xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\"\n" +
                        "  xmlns:xsi=\"http://www.w3.org/1999/XMLSchema-instance\"\n" +
                        "  xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" +
                        "  xmlns:xsd=\"http://www.w3.org/1999/XMLSchema\">\n" +
                        "<SOAP-ENV:Body>\n" +
                        "<requestSolution SOAP-ENC:root=\"1\">";
        
        String footer = "</requestSolution>\n" +
                        "</SOAP-ENV:Body>\n" +
                        "</SOAP-ENV:Envelope>";
        
        String boody = "<ticket xsi:type=\"xsd:string\">"+ticketValue+"</ticket>";
        
        String xmlContent = header+boody+footer;
        
        try {
            FileWriter wiFileWriter = new FileWriter(soapFile);
            wiFileWriter.write(xmlContent);
            wiFileWriter.flush();
            wiFileWriter.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return soapFile;
    }
    //--------------------------------------------------------------------------
    public byte[] createRequestSolutionSOAPRequest(String filePath)
    {
        try{
            byte[] encoded = Files.readAllBytes(Paths.get(filePath));
            String soapXml = new String(encoded, StandardCharsets.UTF_8);
            System.out.println("Request: " + soapXml);
            return encoded;
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    //--------------------------------------------------------------------------
    public String sendRequestSolutionSOAPRequest(byte[] request, String url)
    {
        SOAPMessage response = null;
        ByteArrayOutputStream out = null;
        String strMsg = "";
        int serviceStatus = 1;
        try{
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            java.net.URL endpoint = new URL(url);
            SOAPConnection connection = soapConnectionFactory.createConnection();
            MessageFactory factory = MessageFactory.newInstance();
            SOAPMessage message = factory.createMessage(new MimeHeaders(), new ByteArrayInputStream(request));
            message.saveChanges();
            
            while(serviceStatus == 1)
            {
                response = connection.call(message, endpoint);
                out = new ByteArrayOutputStream();
                response.writeTo(out);
                strMsg = new String(out.toByteArray());
                strMsg = strMsg.replaceAll("&lt;", "<");
                strMsg = strMsg.replaceAll("&gt;", ">");

                int index1 = strMsg.indexOf("<service-status");
                String temp1 = strMsg.substring(index1, strMsg.length());
                int index2 = temp1.indexOf("</service-status>");
                String temp2 = temp1.substring(0, index2);
                String status = temp2.substring(temp2.lastIndexOf(">")+1, temp2.length());
                serviceStatus = Integer.parseInt(status);
            }
            
            int i = response.getSOAPBody().getChildNodes().item(0).getTextContent().indexOf("</xmcda:XMCDA>");
            File xmlFile = new File(Properties.MCDA_INPUT+"alternativesValues.xml");
            FileWriter wiFileWriter = new FileWriter(xmlFile);
            wiFileWriter.write(response.getSOAPBody().getChildNodes().item(0).getTextContent().substring(0,i+"</xmcda:XMCDA>".length()));
            wiFileWriter.flush();
            wiFileWriter.close();
            return strMsg;
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}