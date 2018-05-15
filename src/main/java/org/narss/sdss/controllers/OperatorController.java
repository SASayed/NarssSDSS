/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.controllers;

import org.narss.sdss.dto.Model;
import org.narss.sdss.dto.Input;
import org.narss.sdss.dto.Tool;
import org.narss.sdss.dto.CaseStudy;
import Manager.LayerManager;
import Manager.StyleManager;
import com.lowagie.text.DocumentException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.narss.sdss.daoImpl.CaseStudyDaoImpl;
import org.narss.sdss.daoImpl.ModelDaoImpl;
import org.narss.sdss.daoImpl.Model_Tool_InputsDaoImpl;
import org.narss.sdss.daoImpl.ReportDaoImpl;
import org.narss.sdss.daoImpl.StyleDaoImpl;
import org.narss.sdss.daoImpl.ToolDaoImpl;
import org.narss.sdss.dto.Model_Tool_Inputs;
import org.narss.sdss.dto.Report;
import org.narss.sdss.dto.Style;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;
import org.narss.sdss.water.pollution.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
@Controller
public class OperatorController {

    @Autowired
    private Model model1;
    @Autowired
    private ModelDaoImpl modelDaoImpl;
    @Autowired
    private CaseStudy caseStudy;
    @Autowired
    private LayerManager layerManger;
    @Autowired
    private CaseStudyDaoImpl caseStudyDaoImpl;
     @Autowired
    private ReportDaoImpl reportDaoImpl;
       @Autowired
    private Style style;
      @Autowired
    private StyleDaoImpl styleDaoImpl;
    @Autowired
    private Chlorophyll chl;
    @Autowired
    private SSC ssc;
       
    @Autowired
    private TDS tds;
    @Autowired
    private TSS tss;
    @Autowired
    private Turbidity tur;

    @Autowired
    private ClientAuthentication clientAuthentication;
    @Autowired
    private ZPFSRename zpfs;
    @Autowired
    private SOAPClientSAAJ soapClientForRequest;
    @Autowired
    private SOAPClientSAAJ1 soapClientForResponse;
    @Autowired
    private ReportManger reportmanger;
    @Autowired
    private PostgreSQLJDBC postgreSQLJDBC;
    @Autowired
    private Model_Tool_InputsDaoImpl modelToolInputsDaoImpl;
    @Autowired
    private ToolDaoImpl toolDaoImpl;
     @Autowired
    private StyleManager  styleManger;
    ///unique number 
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    LocalDateTime now = LocalDateTime.now();
    String unique = dtf.format(now);
    int modelId;
    int reportId;
    int styleId;
    private String SOURCE_FOLDER;
    private List<String> fileList;
    List<String> listOfFiles = new ArrayList<String>();

    List<SamplePoint> points = null;
    //ArrayList<Double> results = null;
    double[][] results = null;
    List<Model_Tool_Inputs> modelToolInputsList;
    List<String> toolNames = new ArrayList<String>();
    List<String> inputNames = new ArrayList<String>();
    CaseStudy casReq;
 
    @RequestMapping(value = {"/run"}, method = RequestMethod.GET)
    public String getRunModelPage(HttpServletRequest request) {
        Connection con = postgreSQLJDBC.Connect();
        int caseId = Integer.parseInt(request.getParameter("case"));
       
        
        casReq =  caseStudyDaoImpl.getCaseStudy(caseId); 
        String casename=casReq.getName();
        modelId = casReq.getModel();
        reportId= casReq.getReport();
        styleId=casReq.getStyle();
        model1 = modelDaoImpl.getModelById(modelId);
        modelToolInputsList= modelToolInputsDaoImpl.getModelToolsInputByModelId(modelId);
        toolNames = new ArrayList<String>();
        inputNames = new ArrayList<String>();
        for(int i=0; i<modelToolInputsList.size(); i++)
        {
            toolNames.add(toolDaoImpl.getById(modelToolInputsList.get(i).getToolId()).getName());
            inputNames.add(modelToolInputsList.get(i).getInputValue());
        }
         
        if (casename != "" && casename.startsWith("Water")) {
            return "redirect:/Operator/water";
        }
        return "redirect:/Operator/runModel";
    }

    //--------------------------------------------------------------------------

    @RequestMapping(value = {"/Operator/water"}, method = RequestMethod.GET)
    public ModelAndView getWaterCaseStudyPage(HttpServletRequest request) {

        points = new ArrayList<SamplePoint>();
        Node node = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(Properties.SAMPLES));

            NodeList sampleList = doc.getElementsByTagName("Points").item(0).getChildNodes();
            for (int i = 0; i < sampleList.getLength(); i++) {
                NodeList samplePoints = sampleList.item(i).getChildNodes();
                SamplePoint point = new SamplePoint();
                for (int j = 0; j < samplePoints.getLength(); j++) {
                    node = samplePoints.item(j);
                    if (node.getNodeName().equalsIgnoreCase("ID")) {
                        point.setID(node.getTextContent());
                    } else if (node.getNodeName().equalsIgnoreCase("SampleSite")) {
                        point.setSampleSite(node.getTextContent());
                    } else if (node.getNodeName().equalsIgnoreCase("XOfChlorophyll")) {
                        point.setxOfChlorophyll(Double.parseDouble(node.getTextContent()));
                    } else if (node.getNodeName().equalsIgnoreCase("XOfSSC")) {
                        point.setxOfSSC(Double.parseDouble(node.getTextContent()));
                    } else if (node.getNodeName().equalsIgnoreCase("XOfTDS")) {
                        point.setxOfTDS(Double.parseDouble(node.getTextContent()));
                    } else if (node.getNodeName().equalsIgnoreCase("XOfTSS")) {
                        point.setxOfTSS(Double.parseDouble(node.getTextContent()));
                    } else {
                        point.setxOfTurbidity(Double.parseDouble(node.getTextContent()));
                    }
                }
                node = null;
                java.util.logging.Logger.getLogger(OperatorController.class
                        .getName()).log(Level.SEVERE, null, point);
                points.add(point);
            }
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();

        } catch (SAXException ex) {
            java.util.logging.Logger.getLogger(OperatorController.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(OperatorController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        request.setAttribute("points", points);
        request.setAttribute("chl", chl);
        request.setAttribute("ssc", ssc);
        request.setAttribute("tds", tds);
        request.setAttribute("tss", tss);
        request.setAttribute("tur", tur);
        return new ModelAndView("Operator/water");
    }

    //--------------------------------------------------------------------------

    @RequestMapping(value = {"/Operator/water"}, method = RequestMethod.POST)
    public ModelAndView runWaterCaseStudy(HttpServletRequest request) {
        results = results = new double[16][5];
        double y_Chlorophyll = 0.0;
        double y_SSC = 0.0;
        double y_TDS = 0.0;
        double y_TSS = 0.0;
        double y_Turbidity = 0.0;

        for (int i = 0; i < points.size(); i++) {
            y_Chlorophyll = (points.get(i).getxOfChlorophyll() * chl.getA()) + chl.getA0();
            y_SSC = (points.get(i).getxOfSSC() * ssc.getA()) + ssc.getA0();
            y_TDS = (points.get(i).getxOfTDS() * tds.getA()) + tds.getA0();
            y_TSS = (points.get(i).getxOfTSS() * tss.getA()) + tss.getA0();
            y_Turbidity = (points.get(i).getxOfTurbidity() * tur.getA()) + tur.getA0();
            
            results[i][0] = y_Chlorophyll;
            results[i][1] = y_SSC;
            results[i][2] = y_TDS;
            results[i][3] = y_TSS;
            results[i][4] = y_Turbidity;
        }
        request.setAttribute("points", points);
        request.setAttribute("results", results);
        request.setAttribute("chl", chl);
        request.setAttribute("ssc", ssc);
        request.setAttribute("tds", tds);
        request.setAttribute("tss", tss);
        request.setAttribute("tur", tur);
        return new ModelAndView("Operator/results");
    }

     ////////// operate water report ----------------------------
    @RequestMapping(value = {"/Operator/viewWaterReport"}, method = RequestMethod.GET)
    public ModelAndView ViewWaterReport(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, DocumentException, ParserConfigurationException, SAXException, Exception {
        
      reportmanger.viewreportfile(response,"WATER");
        return new ModelAndView("Operator/results");
    }
    
    //--------------------------------------------------------------------------

    @RequestMapping(value = {"/Operator/runModel"}, method = RequestMethod.GET)
    public ModelAndView getModel(HttpServletRequest request) {
        request.setAttribute("modelId", modelId+"");
        request.setAttribute("modelToolInputsList", modelToolInputsList);
        request.setAttribute("toolNames", toolNames);
        request.setAttribute("inputNames", inputNames);
        int counttool= modelToolInputsDaoImpl.countModelToolsByModelId(modelId);
        request.setAttribute("counttool", counttool-1);
        /*if (caseStudy.getName().equals(Properties.SOIlCASEID)) {
            request.setAttribute("soilCase", true);
        } else {
            request.setAttribute("soilCase", false);
        }*/
        return new ModelAndView("Operator/runModel");
    }

    @RequestMapping(value = {"/Operator/runModel"}, method = RequestMethod.POST)
    public ModelAndView runModel(HttpServletRequest request) {
        File sourceFolder;
        ModelAndView mav = null;
        try {
            System.out.println(" -------------------The Start ----------------");

            ///unique number 
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime now = LocalDateTime.now();
            String unique = dtf.format(now);

            String output_name = casReq.getName().replaceAll("\\s+", "") + unique;
            String zipFileName = output_name + ".zip";
            String threeCharName = casReq.getName().substring(0, 3);

            ////Run Model1 File
            File modelFile = new File(modelDaoImpl.getFileByModelId(casReq.getModel()).toString());
            File WPS_Output = new File(Properties.WPS_OUTPUT + zipFileName);

            //post file
            layerManger.postRequest(Properties.UrlforWPS, modelFile, ContentType.TEXT_XML, WPS_Output);
            //rename output before publish
            //String firstInputName = getFirstInputName(modelFile);
            sourceFolder = zpfs.unZipIt(zipFileName, threeCharName+unique);
            SOURCE_FOLDER = sourceFolder.getName();
            fileList = generateFileList(sourceFolder);
            zpfs.zipIt(SOURCE_FOLDER, SOURCE_FOLDER+".zip", fileList);
            CloseableHttpClient httpclient = clientAuthentication.authenticate(Properties.GEOSERVER_ADMIN, Properties.GEOSERVER_PASS);
            //Upload Shapefile (in zip file form)
            String resultresponse = layerManger.uploadLayer(httpclient, Properties.UrlforUpload, new File(Properties.ZIPFILE+SOURCE_FOLDER+".zip"), "Application/zip"); 
            //zpfs.renameFilesInZip(zipFileName, Properties.extension, firstInputName, output_name);
            /**
                 * *Upload the process output to geoserver**
                 */
                layerManger.publishLayer(Properties.WPS_OUTPUT, Properties.ZIPFILE+SOURCE_FOLDER+".zip");
                //set Authentication
                // CloseableHttpClient httpclient = clientAuthentication.authenticate(Properties.GEOSERVER_ADMIN, Properties.GEOSERVER_PASS);

                //Upload Shapefile (in zip file form)
                //String resultresponse = layerManger.uploadLayer(httpclient, Properties.UrlforUpload, WPS_Output, "Application/zip");
                //prepare MCDA Files
               // String postData = layerManger.descripeLayer("http://localhost:8080/geoserver", "describeFeatureType", Properties.workSpace, threeCharName+unique);
               String postData = layerManger.descripeLayer("http://192.168.1.160:8080/geoserver", "describeFeatureType", Properties.workSpace, Properties.LAYER_NAME);
                Response response = Request.Get(postData).execute();
                response.saveContent(new File(Properties.QUERY_PATH + "result.xml"));
                int noOfAttributes = layerManger.countAttributes(Properties.QUERY_PATH + "result.xml");
                String[] attributes = layerManger.GetLayerAttributes(Properties.QUERY_PATH + "result.xml", noOfAttributes);
                fileList.clear();
                    try{
                        FileUtils.deleteDirectory(new File(Properties.ZIPFILE+SOURCE_FOLDER));
                    }
                    catch(IOException exp) {
                        exp.printStackTrace();
                    }
                mav = new ModelAndView("Operator/prepareMCDA");
               // request.setAttribute("lyrname", threeCharName+unique);
               request.setAttribute("lyrname", Properties.LAYER_NAME);
                request.setAttribute("result", attributes);
                request.setAttribute("Swap_alias", Properties.SWAPS_ALIAS);
                request.setAttribute("Swap_Real", Properties.SWAPS_REAL);
       } catch (Exception io) {
            io.printStackTrace();
       }

        return mav;
    }

    @RequestMapping(value = {"/Operator/prepareMCDA"}, method = RequestMethod.POST)
    public String prepareMCDAFiles(HttpServletRequest request) {

        ModelAndView model1 = null;
        //String workingDirectory = OperatorController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String mcdaFilesPath = Properties.MCDA_QUERY_PATH;
        mcdaFilesPath = mcdaFilesPath.replaceFirst("^/(.:/)", "$1");

        //get request inputs
        Map<String, String[]> parameterMap = request.getParameterMap();
        String alternative = parameterMap.get("alternative")[0];
        String[] criterias = parameterMap.get("criteria");
        String[] preferences = parameterMap.get("preference");
        String[] weights = parameterMap.get("weight");
        String lyrname = parameterMap.get("lyrname")[0];
        
        Style styl= styleDaoImpl.getStyleById(styleId);
        File outfile=new File(Properties.Style_OUTPUT+styl.getName()+".sld");
        
         styleManger.insertattributeInStyleFile(alternative, outfile);
        //Request to get the chosen attribute(alternative) values
        //Will be replaced with DB
        File postedFile = layerManger.newQuery(mcdaFilesPath + "Query.xml", Properties.workSpace + lyrname, alternative, Properties.QUERY_PATH + "newQuery.xml");
        File queryResult = layerManger.postQuery(Properties.UrlforWPS, postedFile, new File(Properties.QUERY_PATH + "QueryResult.xml"));

        //*********Create Alternative File***********//  
        int numOfVals = layerManger.getValuesLength(queryResult, alternative);
        if (numOfVals > 25) {
            numOfVals = 25;
        }
        /// prepare values of the selected attributes to represent the alternative  **/ 
        String[] alternatives_values = layerManger.getAttributeValues(queryResult, alternative, numOfVals);
        styleManger.insertattributevaluesInStyleFile(alternatives_values, outfile);
        styleManger.publishStyle(outfile, styl.getName());
        styl.setFile(outfile);
        styleDaoImpl.updatefile(styl);
        //add style in database 
        /**
         * Create File *
         */
        String[] alternativeIds = layerManger.createAlternativesFile(mcdaFilesPath + "alternatives.xml", alternatives_values, Properties.MCDA_INPUT + "newAlternatives.xml", numOfVals);

        //*********Create Criteria File***********//
        String[] criteriaIds = layerManger.createCriteriaFile(mcdaFilesPath + "criteria.xml", criterias, preferences, Properties.MCDA_INPUT + "newCriterias.xml", criterias.length);

        //*********Create Weights File***********//
        layerManger.createWeightsFile(mcdaFilesPath + "weights.xml", criteriaIds, weights, Properties.MCDA_INPUT + "newWeights.xml");

        //*********Create Performance File***********//
        layerManger.createPerformanceFile(mcdaFilesPath + "performances.xml", mcdaFilesPath + "attributes.xml", Properties.MCDA_INPUT + "attributes.xml", alternative, alternatives_values, alternativeIds, criterias, criteriaIds, Properties.MCDA_INPUT + "newPerformances.xml",
                Properties.UrlforWPS, new File(mcdaFilesPath + "Filter.xml"), Properties.workSpace + lyrname, new File(Properties.QUERY_PATH + "newFilter.xml"));

        //model1 = new ModelAndView("");
        return "redirect:/Operator/runMCDA";
    }

    @RequestMapping(value = {"/Operator/runMCDA"}, method = RequestMethod.GET)
    public ModelAndView getMCDAPage() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        String outputString1 = "";
        String outputString2 = "";
        String outputString3 = "";
        String outputString4 = "";

        //String workingDirectory = OperatorController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
       // String xslPath = workingDirectory.substring(0, workingDirectory.lastIndexOf("WEB-INF/")) + "resources/xsl/";
        String xslPath =Properties.MCDA_XSL_PATH;
       xslPath = xslPath.replaceFirst("^/(.:/)", "$1");

        //View Alternatives Values
        try {
            byte[] encoded1 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT + "newAlternatives.xml"));
            String soapXml1 = new String(encoded1, StandardCharsets.UTF_8);
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(soapXml1)));
            Source xmlSource = new DOMSource(document);

            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Source xslt = new StreamSource(new File(xslPath + "XMCDA.xsl"));

            Transformer transformer = transformerFactory.newTransformer(xslt);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(xmlSource, streamResult);
            outputString1 = stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //--------------View Criteria Values------------------------------------
        try {
            byte[] encoded2 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT + "newCriterias.xml"));
            String soapXml2 = new String(encoded2, StandardCharsets.UTF_8);
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(soapXml2)));
            Source xmlSource = new DOMSource(document);

            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Source xslt = new StreamSource(new File(xslPath + "XMCDA.xsl"));

            Transformer transformer = transformerFactory.newTransformer(xslt);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(xmlSource, streamResult);
            outputString2 = stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------View Weights Values-----------------------------------
        try {
            byte[] encoded3 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT + "newWeights.xml"));
            String soapXml3 = new String(encoded3, StandardCharsets.UTF_8);
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(soapXml3)));
            Source xmlSource = new DOMSource(document);

            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Source xslt = new StreamSource(new File(xslPath + "XMCDA.xsl"));

            Transformer transformer = transformerFactory.newTransformer(xslt);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(xmlSource, streamResult);
            outputString3 = stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //------------------View Performance Table Values-----------------------
        try {
            byte[] encoded4 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT + "newPerformances.xml"));
            String soapXml4 = new String(encoded4, StandardCharsets.UTF_8);
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(soapXml4)));
            Source xmlSource = new DOMSource(document);

            StringWriter stringWriter = new StringWriter();
            StreamResult streamResult = new StreamResult(stringWriter);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Source xslt = new StreamSource(new File(xslPath + "XMCDA.xsl"));

            Transformer transformer = transformerFactory.newTransformer(xslt);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(xmlSource, streamResult);
            outputString4 = stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------
        ModelAndView mav = new ModelAndView("Operator/runMCDA");
        boolean noTicketValue = false;
        boolean noTicketValue1 = false;
        mav.addObject("noTicketValue", noTicketValue);
        mav.addObject("noTicketValue1", noTicketValue1);
        mav.addObject("outputString1", outputString1);
        mav.addObject("outputString2", outputString2);
        mav.addObject("outputString3", outputString3);
        mav.addObject("outputString4", outputString4);
        return mav;
    }

    @RequestMapping(value = {"/Operator/runMCDA"}, method = RequestMethod.POST)
    public ModelAndView runMCDA(HttpServletRequest request) {
        String serviceName = "weightedSum-PyXMCDA.py";
        String ticketValue = "";
        String ticketValue1 = "";
        String submitProblemResponse = "";
        String submitProblemResponse1 = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        //String workingDirectory = HomeController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
       // String xslPath = workingDirectory.substring(0, workingDirectory.lastIndexOf("WEB-INF/")) + "resources/xsl/";
       String xslPath =Properties.MCDA_XSL_PATH;
       xslPath = xslPath.replaceFirst("^/(.:/)", "$1");

        String outputString1 = "";
        String outputString2 = "";
        String outputString3 = "";
        String outputString4 = "";

        String outputStringPlot = "";
        String outputStringMessages = "";
        //Sending Submit Problem Request
        File submitProblrmFile = soapClientForRequest.createSubmitProblemSOAPFile();
        byte[] encoded = soapClientForRequest.createSubmitProblemSOAPRequest(submitProblrmFile.getPath());//"http://192.168.2.92/soap/"
        submitProblemResponse = soapClientForRequest.sendSubmitProblemSOAPRequest(encoded, "http://192.168.2.92/soap/" + serviceName);
        System.out.println("Submit Problem Response\n" + submitProblemResponse);

        //Sending Request Solution Request
        ticketValue = soapClientForRequest.readTicketValue(submitProblemResponse);
        boolean noTicketValue = false;
        if (ticketValue.isEmpty()) {
            noTicketValue = true;
        }
        File requestSolutionFile = soapClientForRequest.createRequestSolutionSOAPFile(ticketValue);
        byte[] encodedSolution = soapClientForRequest.createRequestSolutionSOAPRequest(requestSolutionFile.getPath());
        String requestSolutionResponse = soapClientForRequest.sendRequestSolutionSOAPRequest(encodedSolution, "http://192.168.2.92/soap/" + serviceName);
        System.out.println("Request Solution Response\n" + requestSolutionResponse);

        //Sending Plot Values Request
        File submitProblrmFile1 = soapClientForResponse.createSubmitProblemSOAPFile();
        byte[] encoded1 = soapClientForResponse.createSubmitProblemSOAPRequest(submitProblrmFile1.getPath());
        submitProblemResponse1 = soapClientForResponse.sendSubmitProblemSOAPRequest(encoded1, "http://192.168.2.92/soap/plotAlternativesValues-ITTB.py");
        System.out.println("Submit Problem Response\n" + submitProblemResponse1);

        //Sending Plot Values Solution Request
        ticketValue1 = soapClientForResponse.readTicketValue(submitProblemResponse1);
        boolean noTicketValue1 = false;
        if (ticketValue1.isEmpty()) {
            noTicketValue1 = true;
            //View Alternatives Values
            try {
                byte[] encodedT = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT + "newAlternatives.xml"));
                String soapXml1 = new String(encodedT, StandardCharsets.UTF_8);
                builder = factory.newDocumentBuilder();
                document = builder.parse(new InputSource(new StringReader(soapXml1)));
                Source xmlSource = new DOMSource(document);

                StringWriter stringWriter = new StringWriter();
                StreamResult streamResult = new StreamResult(stringWriter);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();

                Source xslt = new StreamSource(new File(xslPath + "XMCDA.xsl"));

                Transformer transformer = transformerFactory.newTransformer(xslt);
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                transformer.transform(xmlSource, streamResult);
                outputString1 = stringWriter.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //--------------View Criteria Values------------------------------------
            try {
                byte[] encoded2 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT + "newCriterias.xml"));
                String soapXml2 = new String(encoded2, StandardCharsets.UTF_8);
                builder = factory.newDocumentBuilder();
                document = builder.parse(new InputSource(new StringReader(soapXml2)));
                Source xmlSource = new DOMSource(document);

                StringWriter stringWriter = new StringWriter();
                StreamResult streamResult = new StreamResult(stringWriter);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();

                Source xslt = new StreamSource(new File(xslPath + "XMCDA.xsl"));

                Transformer transformer = transformerFactory.newTransformer(xslt);
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                transformer.transform(xmlSource, streamResult);
                outputString2 = stringWriter.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //----------------View Weights Values-----------------------------------
            try {
                byte[] encoded3 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT + "newWeights.xml"));
                String soapXml3 = new String(encoded3, StandardCharsets.UTF_8);
                builder = factory.newDocumentBuilder();
                document = builder.parse(new InputSource(new StringReader(soapXml3)));
                Source xmlSource = new DOMSource(document);

                StringWriter stringWriter = new StringWriter();
                StreamResult streamResult = new StreamResult(stringWriter);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();

                Source xslt = new StreamSource(new File(xslPath + "XMCDA.xsl"));

                Transformer transformer = transformerFactory.newTransformer(xslt);
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                transformer.transform(xmlSource, streamResult);
                outputString3 = stringWriter.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //------------------View Performance Table Values-----------------------
            try {
                byte[] encoded4 = Files.readAllBytes(Paths.get(Properties.MCDA_INPUT + "newPerformances.xml"));
                String soapXml4 = new String(encoded4, StandardCharsets.UTF_8);
                builder = factory.newDocumentBuilder();
                document = builder.parse(new InputSource(new StringReader(soapXml4)));
                Source xmlSource = new DOMSource(document);

                StringWriter stringWriter = new StringWriter();
                StreamResult streamResult = new StreamResult(stringWriter);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();

                Source xslt = new StreamSource(new File(xslPath + "XMCDA.xsl"));

                Transformer transformer = transformerFactory.newTransformer(xslt);
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                transformer.transform(xmlSource, streamResult);
                outputString4 = stringWriter.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            ModelAndView mav = new ModelAndView("Operator/runMCDA");
            mav.addObject("noTicketValue", noTicketValue);
            mav.addObject("noTicketValue1", noTicketValue1);
            mav.addObject("outputString1", outputString1);
            mav.addObject("outputString2", outputString2);
            mav.addObject("outputString3", outputString3);
            mav.addObject("outputString4", outputString4);
            return mav;
        } else {
            File requestSolutionFile1 = soapClientForResponse.createRequestSolutionSOAPFile(ticketValue1);
            byte[] encodedSolution1 = soapClientForResponse.createRequestSolutionSOAPRequest(requestSolutionFile1.getPath());
            String requestSolutionResponse1 = soapClientForResponse.sendRequestSolutionSOAPRequest(encodedSolution1, "http://192.168.2.92/soap/plotAlternativesValues-ITTB.py");
            System.out.println("Request Solution Response\n" + requestSolutionResponse1);

            //View the output Plot
            try {
                byte[] encodedOutputPlot = Files.readAllBytes(Paths.get(Properties.MCDA_OUTPUT + "alternativesValuesPlot.xml"));
                String soapXmlOutputPlot = new String(encodedOutputPlot, StandardCharsets.UTF_8);
                builder = factory.newDocumentBuilder();
                document = builder.parse(new InputSource(new StringReader(soapXmlOutputPlot)));
                Source xmlSource = new DOMSource(document);

                StringWriter stringWriter = new StringWriter();
                StreamResult streamResult = new StreamResult(stringWriter);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();

                Source xslt = new StreamSource(new File(xslPath + "XMCDA.xsl"));

                Transformer transformer = transformerFactory.newTransformer(xslt);
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                transformer.transform(xmlSource, streamResult);
                outputStringPlot = stringWriter.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            //View the output messages
            try {
                byte[] encodedOutputMessage = Files.readAllBytes(Paths.get(Properties.MCDA_OUTPUT + "messages.xml"));
                String soapXmlOutputMessage = new String(encodedOutputMessage, StandardCharsets.UTF_8);
                builder = factory.newDocumentBuilder();
                document = builder.parse(new InputSource(new StringReader(soapXmlOutputMessage)));
                Source xmlSource = new DOMSource(document);

                StringWriter stringWriter = new StringWriter();
                StreamResult streamResult = new StreamResult(stringWriter);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();

                Source xslt = new StreamSource(new File(xslPath + "XMCDA.xsl"));

                Transformer transformer = transformerFactory.newTransformer(xslt);
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                transformer.transform(xmlSource, streamResult);
                outputStringMessages = stringWriter.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ModelAndView mav = new ModelAndView("Operator/outputMCDA");
            mav.addObject("outputStringMessages", outputStringMessages);
            mav.addObject("outputStringPlot", outputStringPlot);
            return mav;
        }
    }

    @RequestMapping(value = {"/Operator/viewReport"}, method = RequestMethod.GET)
    public ModelAndView viewReport(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, DocumentException, ParserConfigurationException, SAXException, Exception {
    Report rep= reportDaoImpl.GetReportById(reportId);
    File report= new File (Properties.Reports+rep.getName()+".pdf");
    reportmanger.build(Properties.MCDA_INPUT + "\\alternativesValues.xml", Properties.Reports, rep.getName(),Properties.MCDA_INPUT + "//attributes.xml", "SDSS Report", rep.getDescription(),rep.getChart_type());
    rep.setPdf_export(report);
    reportDaoImpl.updatePDF(rep); 
    reportmanger.viewreportfile(response,rep.getName());
    return new ModelAndView("Operator/outputMCDA");
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
                    Node model1 = id.getParentNode();
                    NodeList modelItems = model1.getChildNodes();
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

    private File getModelFile(String modelId) {

        File modelFile = null;
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(Properties.MODEL);

            NodeList models_Ids = doc.getElementsByTagName("id");
            //CaseStudy caseStudy= null;

            for (int i = 0; i < models_Ids.getLength(); i++) {
                Node id = models_Ids.item(i);
                if (modelId.equals(id.getTextContent())) {
                    Node model1 = id.getParentNode();
                    NodeList modelItems = model1.getChildNodes();
                    for (int j = 0; j < modelItems.getLength(); j++) {
                        Node node = modelItems.item(j);
                        if ("file".equals(node.getNodeName())) {
                            modelFile = new File(node.getTextContent());
                        }

                    }
                }
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(OperatorController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return modelFile;
    }
    
  
    private String getFirstInputName(File modelFile) {

        String firstinput = "";
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(modelFile);

            // Get the query element by tag name directly
            Node Qurey = doc.getElementsByTagName("wfs:Query").item(0);

            /// set layer name
            NamedNodeMap queryAttr = Qurey.getAttributes();
            Node lyrName = queryAttr.getNamedItem("typeName");
            firstinput = lyrName.getNodeValue().replace("narss:", "");

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        } catch (ParserConfigurationException pc) {
            pc.printStackTrace();
        }
        return firstinput;
    }
    //--------------------------------------------------------------------------
    private String generateZipEntry(String file){
    	return file.substring(SOURCE_FOLDER.lastIndexOf("\\")+1, file.length());
    }
    //--------------------------------------------------------------------------
    private List<String> generateFileList(File node)
    {
        if(node.isFile()){
            listOfFiles.add(generateZipEntry(node.getAbsoluteFile().toString()));
	}
        if(node.isDirectory()){
		String[] subNote = node.list();
		for(String filename : subNote){
			generateFileList(new File(node, filename));
		}
	}
        return listOfFiles;
    }
    //--------------------------------------------------------------------------
}
