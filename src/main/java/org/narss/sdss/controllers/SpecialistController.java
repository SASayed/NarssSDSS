/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.controllers;

//imports of graphic library
import org.narss.sdss.dto.Input;
import org.narss.sdss.dto.Tool;
import org.narss.sdss.dto.CaseStudy;
import org.narss.sdss.dto.Question;
import org.narss.sdss.dto.MCDA;
import Manager.LayerManager;
import Graphic2D.Ellipse;
import Manager.Layer;
import Manager.ToolManager;
import Manager.StyleManager;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.FileUtils;
import static org.apache.commons.io.IOUtils.copy;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.narss.sdss.daoImpl.CaseStudyDaoImpl;
import org.narss.sdss.daoImpl.ColorDaoImpl;
import org.narss.sdss.daoImpl.DepartmentDaoImpl;
import org.narss.sdss.daoImpl.GeoLayerDAOImpl;
import org.narss.sdss.daoImpl.InputDaoImpl;
import org.narss.sdss.daoImpl.MCDADaoImpl;
import org.narss.sdss.daoImpl.ModelDaoImpl;
import org.narss.sdss.daoImpl.Model_Tool_InputsDaoImpl;
import org.narss.sdss.daoImpl.QuestionDaoImpl;
import org.narss.sdss.daoImpl.ReportDaoImpl;
import org.narss.sdss.daoImpl.StyleDaoImpl;
import org.narss.sdss.daoImpl.ToolDaoImpl;
import org.narss.sdss.dto.Department;
import org.narss.sdss.dto.GeoLayer;
import org.narss.sdss.dto.Model;
import org.narss.sdss.dto.Model_Tool_Inputs;
import org.narss.sdss.dto.Report;
import org.narss.sdss.dto.Style;
import org.narss.sdss.dto.color;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
@Controller
public class SpecialistController {

    @Autowired
    private Model model;
    @Autowired
    private GeoLayerDAOImpl geoLayerDAOImpl;
    @Autowired
    private CaseStudy caseStudy;
    @Autowired
    private MCDA mcda;
    @Autowired
    private MCDADaoImpl mcdaDaoImpl;
    @Autowired
    private ModelDaoImpl modelDaoImpl;
    @Autowired
    private ToolDaoImpl toolDaoImpl;
    @Autowired
    private InputDaoImpl inputDaoImpl;
    @Autowired
    private Model_Tool_Inputs model_Tool_Inputs;
    @Autowired
    private Model_Tool_InputsDaoImpl model_Tool_InputsDaoImpl;
    @Autowired
    private QuestionDaoImpl questionDaoImpl;
    @Autowired
    private CaseStudyDaoImpl caseStudyDaoImpl;
    @Autowired
    private Tool tool;
    @Autowired
    private SOAPClientSAAJ soapClientForRequest;
    @Autowired
    private SOAPClientSAAJ1 soapClientForResponse;
    @Autowired
    private LayerManager layerManger;
    @Autowired
    private StyleManager  styleManger;
    @Autowired
    private ClientAuthentication clientAuthentication;
    @Autowired
    private ZPFSRename zpfs;
    @Autowired
    private ToolManager ToolManger;
    @Autowired
    private PostgreSQLJDBC postgreSQLJDBC;
    @Autowired
    private Report report;
    @Autowired
    private Style style;
    
    @Autowired
    private Department department;
     @Autowired
    private color color;
    @Autowired
    private ReportDaoImpl reportDaoImpl;
    @Autowired
    private StyleDaoImpl styleDaoImpl;
    @Autowired
    private DepartmentDaoImpl departmentDaoImpl;
    @Autowired
    private ColorDaoImpl colorDaoImpl;
    
    @Autowired
    private ReportManger reportmanger;
    
    private List<Report> reportList;
    private List<Style> styleList;
    private List<color> colorList;
    
    private List<Department> deptList;

    private List<Layer> geoLayers;

    private List<MCDA> mcdaList;

    private List<Model> modelList;

    private List<CaseStudy> casesList;

    private List<Question> questionsList;

    private List<Tool> tools = new ArrayList<Tool>();
    private int counttool=0;
    private List<String> toolname=new ArrayList<String>();;
    private boolean isMultipart;
    private String filePath;
    private File file;
    
    private String SOURCE_FOLDER;
    private List<String> fileList;
    List<String> listOfFiles = new ArrayList<String>();

   // String workingDirectory = OperatorController.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   // String modellingPath = workingDirectory.substring(0, workingDirectory.lastIndexOf("WEB-INF/")) + "resources/geoModelling/";

    ///unique number 
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("ddmmyyyhhmmss");
    LocalDateTime now = LocalDateTime.now();
    String unique = dtf.format(now);

    /* public final Connection connect() {
        Connection conn = null;
        try {
            Class.forName(Properties.DRIVER);
            conn = DriverManager.getConnection(Properties.DATABASE_URL + Properties.DATABASE_NAME, Properties.DATABASE_USERNAME, Properties.DATABASE_PASSWORD);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return conn;
    }
     */
    @RequestMapping(value = {"/specialist/exploreData"}, method = RequestMethod.GET)
    public ModelAndView exploreDataPage(HttpServletRequest request) {
        //layerManger.getLayerExtent("narss:slope");
        //get capabilites
        layerManger.getCapabilites("http://192.168.1.160:8080/geoserver/wms?service=wms&version=1.1.1&request=GetCapabilities", new File(Properties.CAPABILITIES));
        ModelAndView mav = new ModelAndView("specialist/exploreData");
        return mav;
    }

    @RequestMapping(value = {"/specialist/modelling"}, method = RequestMethod.GET)
    public ModelAndView getModelPage(HttpServletRequest request) {
        if (!tools.isEmpty()) {
            for (int i = 0; i < tools.size(); i++) {
                //tools = ToolManger.addTool(tools.get(i));
                //System.out.print(tools.get(i).getName());
            }
        }
        modelList = modelDaoImpl.getAll();
        request.setAttribute("modelList", modelList);
        request.setAttribute("tools", tools);
        //layerManger.getLayerExtent("narss:slope");
        //**  get capabilites  **/
        //List<String> geoLayers = layerManger.getGeoLayers("http://localhost:8080/geoserver/rest/", "narss", "json");
        //request.setAttribute("geoLayers", geoLayers);
        ModelAndView mav = new ModelAndView("specialist/modelling");
        return mav;
    }

    @RequestMapping(value = {"/intersect"}, method = RequestMethod.GET)
    public String intersectTool(HttpServletRequest request) {
        Tool lastTool = null;
        ////check on the previous tool output
        if (tools.size() > 0) {
            lastTool = tools.get(tools.size() - 1);
            int toolInputs_no = toolDaoImpl.getNoOfInputsLayers("Intersect");
            if (lastTool.getCountOfoutputLayer() <= toolInputs_no) {
                tools.add(toolDaoImpl.getByName("Intersect"));
            } 
            toolname.add("Intersect");
           //toolname="Intersect";
            counttool++;
        } else {
            tools.add(toolDaoImpl.getByName("Intersect"));
            
            toolname.add("Intersect");
            counttool++;
        }
        return "redirect:/specialist/editmodel";
    }

    @RequestMapping(value = {"/union"}, method = RequestMethod.GET)
    public String unionTool(HttpServletRequest request) {
        Tool lastTool = null;
        ////check on the previous tool output
        if (tools.size() > 0) {
            lastTool = tools.get(tools.size() - 1);
            int toolInputs_no = toolDaoImpl.getNoOfInputsLayers("Union");
            if (lastTool.getCountOfoutputLayer() <= toolInputs_no) {
                tools.add(toolDaoImpl.getByName("Union"));
            }
             toolname.add("union");
            counttool++;
        } else {
            tools.add(toolDaoImpl.getByName("Union"));
        toolname.add("union");
            counttool++;
        }
        return "redirect:/specialist/editmodel";
    }

    @RequestMapping(value = {"/buffer"}, method = RequestMethod.GET)
    public String bufferTool(HttpServletRequest request) {
        Tool lastTool = null;
        ////check on the previous tool output
        if (tools.size() > 0) {
            lastTool = tools.get(tools.size() - 1);
            int toolInputs_no = toolDaoImpl.getNoOfInputsLayers("Buffer");
            if (lastTool.getCountOfoutputLayer() <= toolInputs_no) {
                tools.add(toolDaoImpl.getByName("Buffer"));
            }
            toolname.add("Buffer");
            counttool++;
        } else {
            tools.add(toolDaoImpl.getByName("Buffer"));
            toolname.add("Buffer");
            counttool++;
        }
        return "redirect:/specialist/editmodel";
    }

    @RequestMapping(value = {"/near"}, method = RequestMethod.GET)
    public String nearestTool(HttpServletRequest request) {
        Tool lastTool = null;
        ////check on the previous tool output
        if (tools.size() > 0) {
            lastTool = tools.get(tools.size() - 1);
            int toolInputs_no = toolDaoImpl.getNoOfInputsLayers("Nearest");
            if (lastTool.getCountOfoutputLayer() <= toolInputs_no) {
                tools.add(toolDaoImpl.getByName("Nearest"));
            toolname.add("Nearest");
            counttool++;
            }
        } else {
            tools.add(toolDaoImpl.getByName("Nearest"));
          toolname.add("Nearest");
            counttool++;
        }
        return "redirect:/specialist/editmodel";
    }

    @RequestMapping(value = {"/clip"}, method = RequestMethod.GET)
    public String clipTool(HttpServletRequest request) {
        Tool lastTool = null;
        ////check on the previous tool output
        if (tools.size() > 0) {
            lastTool = tools.get(tools.size() - 1);
            int toolInputs_no = toolDaoImpl.getNoOfInputsLayers("Clip");
            if (lastTool.getCountOfoutputLayer() <= toolInputs_no) {
                tools.add(toolDaoImpl.getByName("Clip"));
            toolname.add("Clip");
            counttool++;
            }
        } else {
            tools.add(toolDaoImpl.getByName("Clip"));
        toolname.add("Clip");
            counttool++;
        }
       //  request.setAttribute("toollist", tools);
        return "redirect:/specialist/editmodel";
    }

    @RequestMapping(value = {"/split"}, method = RequestMethod.GET)
    public String splitTool(HttpServletRequest request) {
        Tool lastTool = null;
        ////check on the previous tool output
        if (tools.size() > 0) {
            lastTool = tools.get(tools.size() - 1);
            int toolInputs_no = toolDaoImpl.getNoOfInputsLayers("Split");
            if (lastTool.getCountOfoutputLayer() <= toolInputs_no) {
                tools.add(toolDaoImpl.getByName("Split"));
           toolname.add("Split");
            counttool++; }
        } else {
            tools.add(toolDaoImpl.getByName("Split"));
            toolname.add("Split");
            counttool++; 
        }
        request.setAttribute("toollist", tools);
        return "redirect:/specialist/editmodel";
    }

    @RequestMapping(value = {"/identity"}, method = RequestMethod.GET)
    public String identityTool(HttpServletRequest request) {
        Tool lastTool = null;
        ////check on the previous tool output
        if (tools.size() > 0) {
            lastTool = tools.get(tools.size() - 1);
            int toolInputs_no = toolDaoImpl.getNoOfInputsLayers("Identity");
            if (lastTool.getCountOfoutputLayer() <= toolInputs_no) {
                tools.add(toolDaoImpl.getByName("Identity"));
            toolname.add("Identity");
            counttool++; 
            }
        } else {
            tools.add(toolDaoImpl.getByName("Identity"));
             toolname.add("Identity");
            counttool++; 
        }
        //request.setAttribute("toollist", tools);
        return "redirect:/specialist/editmodel";
    }

    @RequestMapping(value = {"/spatialJoin"}, method = RequestMethod.GET)
    public String spatialJoinTool(HttpServletRequest request) {
        Tool lastTool = null;
        ////check on the previous tool output
        if (tools.size() > 0) {
            lastTool = tools.get(tools.size() - 1);
            int toolInputs_no = toolDaoImpl.getNoOfInputsLayers("SpatialJoin");
            if (lastTool.getCountOfoutputLayer() <= toolInputs_no) {
                tools.add(toolDaoImpl.getByName("SpatialJoin"));
            }
             toolname.add("SpatialJoin");
            counttool++; 
        } else {
            tools.add(toolDaoImpl.getByName("SpatialJoin"));
          toolname.add("SpatialJoin");
            counttool++; 
        }
        return "redirect:/specialist/editmodel";
    }

    @RequestMapping(value = {"/removeTool"}, method = RequestMethod.GET)
    public String removeTool(HttpServletRequest request) {
        int tool_hir = Integer.parseInt(request.getParameter("tool"));
        int toolid = Integer.parseInt(request.getParameter("toolid"));
        int modelid = Integer.parseInt(request.getParameter("modelid"));
        if (!tools.isEmpty()) 
        {
           model_Tool_InputsDaoImpl.deleteTool(toolid,modelid);
            tools.remove(tool_hir);
            counttool--;
        }
        
        return "redirect:/specialist/editmodel";
    }


    @RequestMapping(value = {"/viewModel"}, method = RequestMethod.GET)
    public String viewModel(HttpServletRequest request) {
        Ellipse eclipse = new Ellipse();
        return "redirect:/specialist/editmodel";
    }

    @RequestMapping(value = {"/saveModel"}, method = RequestMethod.POST)
    public String saveModel(HttpServletRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        String name = parameterMap.get("modelName")[0];
        String desc = parameterMap.get("desc")[0];
        boolean hasMCDA = true;
        HttpSession session = request.getSession(true);
        //edit in 27/2
        name=name.replaceAll("\\s+","");//

       int noofrows;
        model.setName(name);
        model.setDescription(desc);
        model.setHasMcda(hasMCDA);
       
       noofrows=modelDaoImpl.add(model);
       if (noofrows==0)
       {
              
              session.setAttribute("error", "Error!!!!! Another Model with the Same Name Already Exist"); 
                   
       }
       return "redirect:/specialist/modelling";
       
        
    }
    
    @RequestMapping(value = {"/specialist/editmodel"}, method = RequestMethod.GET)
    public ModelAndView editmodel(HttpServletRequest request) {
     
        HttpSession sess = request.getSession(true);
        String modelid = null;
        String modelname=null;
         List<String> modelids = new ArrayList<String>();
      //  List<String> geoLayers = layerManger.getGeoLayers("http://localhost:8080/geoserver/rest/", "narss", "json");
      Connection conn = PostgreSQLJDBC.Connect();
       List<GeoLayer> geolayer= geoLayerDAOImpl.getAllGeoLayers(conn);
      try
       { 
           String uri = request.getQueryString();
           String[] queries= uri.split("&");
           if (queries[1].contains("modid="))
           {
               modelid= queries[1].replace("modid=","");
               model.setId(Integer.parseInt(modelid));
               sess.setAttribute("modelid", modelid);
               modelids.add(modelid);
               tools.clear();
               counttool=0;
           }
           if (queries[0].contains("modname="))
           {
               modelname= queries[0].replace("modname=","");
               model.setName(modelname);
               sess.setAttribute("modelname", modelname);
           }
       }
       catch (NullPointerException ex) { 
           
           model.setId(Integer.parseInt((String) sess.getAttribute("modelid")));
           model.setName((String) sess.getAttribute("modelname"));
          
        }
        modelList = modelDaoImpl.getAll();
        List<Model_Tool_Inputs> modelToolInputsList = new ArrayList<Model_Tool_Inputs>();
        request.setAttribute("modelList", modelList);
        int mdlid=Integer.parseInt((String) sess.getAttribute("modelid"));
        modelToolInputsList= model_Tool_InputsDaoImpl.getModelToolsInputByModelId(mdlid);
   List<String> Layername = new ArrayList<String>();
   if(!modelToolInputsList.isEmpty()&&tools.isEmpty())
   {
       for (int l=1;l<modelToolInputsList.size();l++)
       { 
           int toolid=modelToolInputsList.get(l).getToolId();
           Tool tol=toolDaoImpl.getById(toolid);
           tools.add(tol);
           counttool=tools.size();
       }
       for (int i=0;i<modelToolInputsList.size();i++)
       {
           String layername = modelToolInputsList.get(i).getInputValue();
           Layername.add(layername);
       }
   }
 request.setAttribute("tools", tools);
      request.setAttribute("toollist", counttool);
      request.setAttribute("Layername", Layername);
    //**  get capabilites  **/
      //request.setAttribute("Layers", geoLayers);
    request.setAttribute("geoLayers", geolayer);
 ModelAndView mav = new ModelAndView("specialist/editmodel");
 return mav;
    }
	
    @RequestMapping(value = {"/specialist/editmodel"}, method = RequestMethod.POST)
    public String editModel(HttpServletRequest request)
    { 
        File sourceFolder;
        model_Tool_InputsDaoImpl.delete(model.getId()); 
       Map<String, String[]> parameterMap = request.getParameterMap();
      String FirstInputLayer = null ;
   String tools_Files[] = new String[tools.size()];
   boolean exist= false;
   for (int i = 0; i < tools.size(); i++) 
   {
       //for loop to all tools 
       LocalDateTime now = LocalDateTime.now();
       String uni = dtf.format(now) + i;
       List<Input> inputList = inputDaoImpl.getInputs(tools.get(i).getId());
            File toolFile;
           for (int j = 0; j < inputList.size(); j++) {
                String inp_id = inputList.get(j).getId() + "";
                 String tool_id = tools.get(i).getId() + "";
                String prev_inp_id = "";
                if (j > 0) 
                {
                    prev_inp_id = inputList.get(j - 1).getId() + "";
                }
                if (!prev_inp_id.equals(inp_id)) {
                //edit
               String[] input_val = parameterMap.get(tool_id + "," + inp_id +","+i);
               if (input_val != null) {
                        FirstInputLayer =input_val[0];
                        model_Tool_Inputs.setModelId(model.getId());
                        model_Tool_Inputs.setInputId(inputList.get(j).getId());
                        model_Tool_Inputs.setToolId(tools.get(i).getId());
                 
                      for (int z = 0; z < input_val.length; z++) 
                      {
                       File newtoolnamefile = new File(Properties.QUERY_PATH + tools.get(i).getName()+ uni + ".xml");
                          toolFile = tools.get(i).getFile();
                          int input_Id = inputList.get(j).getId();
                            ///start of function
                          tools_Files[i] = newtoolnamefile.toString();
                          
                          if (i == 0 && z<=1) 
                           {
                               if ("vector".equals(inputList.get(j).getType())) {
                                    layerManger.insertLayersInToolFile(Properties.workSpace, input_val, toolFile, newtoolnamefile);
                                    
                                }
                                else if ("number".equals(inputList.get(j).getType()))
                                {
                               layerManger.insertDistance(newtoolnamefile, newtoolnamefile, input_val[z]);
                                }
                                else if ("coor".equals(inputList.get(j).getType()))
                                {
                                    layerManger.insertPoint(newtoolnamefile, newtoolnamefile, input_val[z]);
                                }
                  //split tool case
                         else if ("splitCoorline".equals(inputList.get(j).getType())) { 
                     
                    layerManger.insertSplitLineInput(toolFile,newtoolnamefile, input_val[z]);
                           }
                     else if("splitCoor".equals(inputList.get(j).getType()))
                     {
                         layerManger.insertSplitPolyInput(newtoolnamefile,newtoolnamefile, input_val[z]);
                     }
            model_Tool_Inputs.setInputValue(input_val[z]);
            model_Tool_InputsDaoImpl.add(model_Tool_Inputs);
            }
                          
                          else if (i != 0)
                          {
                             model_Tool_Inputs.setInputValue(input_val[z]);
                             exist= model_Tool_InputsDaoImpl.checkExistOfLayer(model.getId(), tools.get(i).getId(),input_val[z]);
                             if (exist==false){
                    
                             if ("vector".equals(inputList.get(j).getType())) {
                        layerManger.tryInsertSingelLayerInToolFile(Properties.workSpace, input_val[z], toolFile, newtoolnamefile);
                        
                        model_Tool_InputsDaoImpl.add(model_Tool_Inputs);
                    }         
                    //buffer case
                    if ("number".equals(inputList.get(j).getType())) {
                        layerManger.insertDistance(toolFile, newtoolnamefile, input_val[z]);
                        model_Tool_InputsDaoImpl.add(model_Tool_Inputs);
                    } 
                 //nearest case
                    if ("coor".equals(inputList.get(j).getType())) {
                        layerManger.insertPoint(toolFile, newtoolnamefile, input_val[z]);
                        model_Tool_InputsDaoImpl.add(model_Tool_Inputs);
                    }
                             }
                          }  

             
        }
                    } 
                }
            }
        }
        model.createModelRunFile(tools_Files);//end of function
        String[] form_Action = parameterMap.get("Form_Action");
            if (form_Action != null && tools_Files.length != 0) {
                if ("editModel".equals(form_Action[0]))
                {
                 // modelDaoImpl.update(model, tools_Files[tools_Files.length-1]);

                }
                else if ("testModel".equals(form_Action[0])) {
                    
                    modelDaoImpl.update(model, tools_Files[tools_Files.length-1]);
                 //Run Model1 File
                    String name =model.getName();
                    String threeCharName = name.substring(0, 3);
                    String zipFileName = name+ model.getId()+ ".zip";
                    File modelFile = model.getFile();
                    File WPS_Output = new File(Properties.WPS_OUTPUT + zipFileName);
                    Date dNow = new Date();
                   SimpleDateFormat ft = new SimpleDateFormat("ddmmyyyhhmmss");
                   String datetime = ft.format(dNow);
                    //post file
                    layerManger.postRequest(Properties.UrlforWPS, modelFile, ContentType.TEXT_XML, WPS_Output);
                    sourceFolder = zpfs.unZipIt(zipFileName, threeCharName+datetime);
                    SOURCE_FOLDER = sourceFolder.getName();
                    fileList = generateFileList(sourceFolder);
                    zpfs.zipIt(SOURCE_FOLDER, SOURCE_FOLDER+".zip", fileList);
                    //Extract zip rename output before publish
                    //zpfs.renameFilesInZip(zipFileName, Properties.extension,FirstInputLayer,name+datetime);
                    //Upload the process output to geoserver** //post file
                    //layerManger.postRequest(Properties.UrlforWPS, modelFile, ContentType.TEXT_XML, new File(Properties.ZIPFILE+SOURCE_FOLDER+".zip"));
                    //set Authentication
                    CloseableHttpClient httpclient = clientAuthentication.authenticate(Properties.GEOSERVER_ADMIN, Properties.GEOSERVER_PASS);
                    //Upload Shapefile (in zip file form)
                    String resultresponse = layerManger.uploadLayer(httpclient, Properties.UrlforUpload, new File(Properties.ZIPFILE+SOURCE_FOLDER+".zip"), "Application/zip"); 
                    //publish layer 
                    Connection conn = PostgreSQLJDBC.Connect();
                    //if()
                    layerManger.publishLayer(Properties.WPS_OUTPUT, Properties.ZIPFILE+SOURCE_FOLDER+".zip");  
                   // {
                 
                    modelDaoImpl.update_resultlayer(model,Properties.LAYER_NAME);
                    List<GeoLayer> geoLayers = geoLayerDAOImpl.getAllGeoLayers(conn);
                    GeoLayer geoLayer = new GeoLayer();
                   // geoLayer.setId(geoLayers.size()+1);
                    geoLayer.setName(threeCharName+datetime);
                    Connection con = PostgreSQLJDBC.Connect();
                    geoLayerDAOImpl.addGeoLayer(geoLayer, con);
                    //}
                    fileList.clear();
                    try{
                        FileUtils.deleteDirectory(new File(Properties.ZIPFILE+SOURCE_FOLDER));
                    }
                    catch(IOException exp) {
                        exp.printStackTrace();
                    }
                }
               return "redirect:/specialist/modelling";
        }
     return "redirect:/specialist/editmodel";
    }
    /*public void updateModel(String modelId, File runfile) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(Properties.MODEL);

            NodeList models_Ids = doc.getElementsByTagName("id");
            //CaseStudy caseStudy= null;

            for (int i = 0; i < models_Ids.getLength(); i++) {
                Node id = models_Ids.item(i);
                if (modelId.equals(id.getTextContent())) {
                    Node mdl = id.getParentNode();

                    //New Model1 run file
                    Boolean firstRun = true;
                    Element modelFile = doc.createElement("file");
                    String fileClearName = runfile.getAbsolutePath().substring(runfile.getAbsolutePath().lastIndexOf("\\") + 1);
                    modelFile.appendChild(doc.createTextNode(fileClearName));

                    //replace the run file
                    NodeList modelItems = mdl.getChildNodes();
                    for (int x = 0; x < modelItems.getLength(); x++) {
                        if ("file".equals(modelItems.item(x).getNodeName())) {
                            mdl.replaceChild(modelFile, modelItems.item(x));
                            firstRun = false;
                        } //remove old Tools
                        else if ("tool".equals(modelItems.item(x).getNodeName())) {
                            mdl.removeChild(modelItems.item(x));
                            x--;
                        }
                    }

                    if (firstRun) {
                        mdl.appendChild(modelFile);
                    }
                    //Save new Tools
                    List<Tool> toolsList = model.getTools();
                    for (int t = 0; t < toolsList.size(); t++) {
                        Tool toolReq = toolsList.get(t);
                        Element tool = doc.createElement("tool");
                        tool.setAttribute("id", toolReq.getId());
                        tool.setAttribute("name", toolReq.getName());
                        tool.appendChild(doc.createTextNode(toolReq.getName()));

                        ///add <toolInput> tsg to <tool> tag
                        List<Input> toolInputs = toolReq.getInputs();
                        for (int j = 0; j < toolInputs.size(); j++) {
                            Input input = toolInputs.get(j);
                            Element toolInput = doc.createElement("toolInput");
                            toolInput.setAttribute("name", input.getName());
                            toolInput.setAttribute("value", input.getValue());
                            toolInput.setAttribute("type", input.getType());
                            toolInput.setAttribute("submodel", input.getSubmodel());
                            tool.appendChild(toolInput);
                        }
                        mdl.appendChild(tool);
                    }
                }
            }
            // write the content into xml file
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(doc), new StreamResult(Properties.MODEL));

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (TransformerConfigurationException tf) {
            tf.printStackTrace();
        } catch (TransformerException tr) {
            tr.printStackTrace();
        }
    }*/
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/caseStudy"}, method = RequestMethod.GET)
    public ModelAndView getCaseStudyPage(HttpServletRequest request) {

        Connection con = postgreSQLJDBC.Connect();
        casesList = caseStudyDaoImpl.getAllCaseStudy(con);
        List<Model> modelsList = modelDaoImpl.getAll();
        List<Report> reportList = reportDaoImpl.getAll();
         List<Style> styleList = styleDaoImpl.getAll();
       
        List<MCDA> mcdaList2 = mcdaDaoImpl.getAllMCDAs(con);
        request.setAttribute("modelsList", modelsList);
        request.setAttribute("mcdasList2", mcdaList2);
        request.setAttribute("reportList", reportList);
        request.setAttribute("styleList", styleList);
        request.setAttribute("casesList", casesList);
        ModelAndView mav = new ModelAndView("/specialist/caseStudy");
        return mav;
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/caseStudy"}, method = RequestMethod.POST)
    public String createCaseStudy(HttpServletRequest request) {
        Connection con = postgreSQLJDBC.Connect();
        HttpSession session = request.getSession(true);
        //get request inputs
        Map<String, String[]> parameterMap = request.getParameterMap();
        String caseName = parameterMap.get("casename")[0];
        String desc = parameterMap.get("description")[0];
        String caseModel = parameterMap.get("model")[0];
        String caseMCDA = parameterMap.get("mcda")[0];
        String casereport = parameterMap.get("report")[0];
        String casestyle = parameterMap.get("style")[0];
        Model model = modelDaoImpl.getModelByName(caseModel);
        MCDA mcda = mcdaDaoImpl.getMCDAByName(con, caseMCDA);
      //  Report rep=reportDaoImpl.GetReportByName(casereport);
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();

        int date = year +  month +  day +  hour + minute +  second;

        caseStudy.setId(date);
        caseStudy.setName(caseName);
        caseStudy.setDescription(desc);
        caseStudy.setModel(model.getId());
        caseStudy.setMcda(mcda.getId());
        caseStudy.setReport(Integer.parseInt(casereport));
        caseStudy.setStyle(Integer.parseInt(casestyle));
        int numOfRows = caseStudyDaoImpl.addCaseStudy(caseStudy, con);
        if(numOfRows == 0)
        {
            session.setAttribute("error", "Error!!!!! Another Case Study with the Same Name Already Exist");
            return "redirect:/specialist/caseStudy";
        }
        return "redirect:/specialist/caseStudy";
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/questions"}, method = RequestMethod.GET)
    public ModelAndView getQuestionsPage(HttpServletRequest request) {
        Connection conn = postgreSQLJDBC.Connect();
        questionsList = questionDaoImpl.getAllQuestions(conn);
        request.setAttribute("questionsList", questionsList);
        ModelAndView mav = new ModelAndView("specialist/questions");
        return mav;
    }

   

 
    @RequestMapping(value = {"/specialist/MCDA"}, method = RequestMethod.GET)
    public ModelAndView getcreateMCDAPage(HttpServletRequest request) {

        Connection con = postgreSQLJDBC.Connect();
        mcdaList = mcdaDaoImpl.getAllMCDAs(con);
        List<Model> modelsList = modelDaoImpl.getAll();

        ModelAndView mav = new ModelAndView("/specialist/MCDA");
        request.setAttribute("modelsList", modelsList);
        request.setAttribute("mcdaList", mcdaList);
        return mav;
    }

    @RequestMapping(value = {"/specialist/MCDA"}, method = RequestMethod.POST)
    public String saveMCDA(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Connection con = PostgreSQLJDBC.Connect();
        String mcdaName = request.getParameter("mcdaName");
        String mcdaDescription = request.getParameter("mcdaDescription");
        String mcdaServiceName = request.getParameter("serviceName");
        String modelName = request.getParameter("modelName");
        Model model = modelDaoImpl.getModelByName(modelName);
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();

        int date = year +  month +  day +  hour + minute +  second;
        mcda.setId(date);
        mcda.setName(mcdaName);
        mcda.setDescription(mcdaDescription);
        mcda.setAlgorithm(mcdaServiceName);
        mcda.setModel(model.getId());
           int numOfRows = mcdaDaoImpl.addMCDA(mcda, con);
        if(numOfRows == 0)
        {
            session.setAttribute("error", "Error!!!!! Another MCDA with the Same Name Already Exist");
            return "redirect:/specialist/MCDA";
        }
        return "redirect:/specialist/MCDA";
    }

    @RequestMapping(value = {"/specialist/MCDA/testMCDA"}, method = RequestMethod.GET)
    public ModelAndView testMCDA(HttpServletRequest request) {
        Connection con = PostgreSQLJDBC.Connect();
        String mcdaName = request.getParameter("name");
        mcdaName = mcdaName.trim();
        MCDA mcda = mcdaDaoImpl.getMCDAByName(con, mcdaName);
        String layerName = modelDaoImpl.getModelById(mcda.getModel()).getResult_layer();
        String postData = layerManger.descripeLayer("http://192.168.1.160:8080/geoserver", "describeFeatureType", Properties.workSpace, layerName);
        Response response = null;
        try {
            response = Request.Get(postData).execute();
        } catch (IOException io) {
            io.printStackTrace();
        }
        try {
            response.saveContent(new File(Properties.QUERY_PATH + "resultTest.xml"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int noOfAttributes = layerManger.countAttributes(Properties.QUERY_PATH + "resultTest.xml");
        String[] attributes = layerManger.GetLayerAttributes(Properties.QUERY_PATH + "resultTest.xml", noOfAttributes);
        ModelAndView mav = new ModelAndView("specialist/prepareMCDA");
        request.setAttribute("result", attributes);
        request.setAttribute("resultLayer", layerName);
        return mav;
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/MCDA/testMCDA"}, method = RequestMethod.POST)
    public String prepareMCDAFiles(HttpServletRequest request) {

       // String mcdaFilesPath = workingDirectory.substring(0, workingDirectory.lastIndexOf("WEB-INF/")) + "resources/XmlMCDA/";
       String mcdaFilesPath = Properties.MCDA_QUERY_PATH;
       // System.out.println(mcdaFilesPath);
        mcdaFilesPath = mcdaFilesPath.replaceFirst("^/(.:/)", "$1");

        //get request inputs
        Map<String, String[]> parameterMap = request.getParameterMap();
        String alternative = parameterMap.get("alternative")[0];
        String[] criterias = parameterMap.get("criteria");
        String[] preferences = parameterMap.get("preference");
        String[] weights = parameterMap.get("weight");
        String lyrname = parameterMap.get("resultLayer")[0];

        //Request to get the chosen attribute(alternative) values
        File postedFile = layerManger.newQuery(mcdaFilesPath + "Query.xml", Properties.workSpace + lyrname, alternative, Properties.QUERY_PATH + "newQuery.xml");
        File queryResult = layerManger.postQuery(Properties.UrlforWPS, postedFile, new File(Properties.QUERY_PATH + "QueryResult.xml"));

        //*********Create Alternative File***********//  
        //int numOfVals = layerManger.getValuesLength(queryResult, alternative);
        /// prepare values of the selected attributes to represent the alternative  **/ 
        String[] alternatives_values = layerManger.getAttributeValues(queryResult, alternative, Properties.NUMBER_OF_ATTRIBUTES);

        /**
         * Create File *
         */
        String[] alternativeIds = layerManger.createAlternativesFile(mcdaFilesPath + "alternatives.xml", alternatives_values, Properties.MCDA_INPUT + "newAlternatives.xml", Properties.NUMBER_OF_ATTRIBUTES);

        //*********Create Criteria File***********//
        String[] criteriaIds = layerManger.createCriteriaFile(mcdaFilesPath + "criteria.xml", criterias, preferences, Properties.MCDA_INPUT + "newCriterias.xml", criterias.length);

        //*********Create Weights File***********//
        layerManger.createWeightsFile(mcdaFilesPath + "weights.xml", criteriaIds, weights, Properties.MCDA_INPUT + "newWeights.xml");

        //*********Create Performance File***********//
        layerManger.createPerformanceFile(mcdaFilesPath + "performances.xml", mcdaFilesPath + "attributes.xml", Properties.MCDA_INPUT + "attributes.xml", alternative, alternatives_values, alternativeIds, criterias, criteriaIds, Properties.MCDA_INPUT + "newPerformances.xml",
                Properties.UrlforWPS, new File(mcdaFilesPath + "Filter.xml"), Properties.workSpace + lyrname, new File(Properties.QUERY_PATH + "newFilter.xml"));

        //model = new ModelAndView("");
        return "redirect:/specialist/MCDA/runMCDA";
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/MCDA/editMCDA"}, method = RequestMethod.GET)
    public ModelAndView editMCDA(HttpServletRequest request)
    {
        Connection con = PostgreSQLJDBC.Connect();
        String mcdaName = request.getParameter("name");
        mcdaName = mcdaName.trim();
        MCDA mcda = mcdaDaoImpl.getMCDAByName(con, mcdaName);
        int id = mcda.getModel();
        Model m = modelDaoImpl.getModelById(id);
        String mdlName = m.getName();
        List<MCDA> mcdas = mcdaDaoImpl.getAllMCDAs(con);
        List<Model> modelsList = modelDaoImpl.getAll();
        request.setAttribute("modelsList", modelsList);
        request.setAttribute("mcdaList", mcdas);
        request.setAttribute("mcda", mcda);
        request.setAttribute("mdlName", mdlName);
        ModelAndView mav = new ModelAndView("specialist/editMCDA");
        return mav;
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/MCDA/editMCDA"}, method = RequestMethod.POST)
    public String updateMCDA(HttpServletRequest request) {
        Connection con = PostgreSQLJDBC.Connect();
        String mcdaName = request.getParameter("mcdaNameHidden");
        String mcdaDescription = request.getParameter("mcdaDescription");
        String mcdaServiceName = request.getParameter("serviceName");
        String modelName = request.getParameter("modelName");
        Model model = modelDaoImpl.getModelByName(modelName);
        int id = Integer.parseInt(request.getParameter("mcdaIdHidden"));
        MCDA mcda = new MCDA();
        mcda.setId(id);
        mcda.setName(mcdaName);
        mcda.setDescription(mcdaDescription);
        mcda.setAlgorithm(mcdaServiceName);
        mcda.setModel(model.getId());
        mcdaDaoImpl.updateMCDA(mcda, id, con);
        return "redirect:/specialist/MCDA";
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/MCDA/deleteMCDA"}, method = RequestMethod.GET)
    public String deleteMCDA(HttpServletRequest request) {
        Connection con = PostgreSQLJDBC.Connect();
        String mcdaName = request.getParameter("name");
        mcdaName = mcdaName.trim();
        mcdaDaoImpl.deleteMCDA(mcdaName, con);
        return "redirect:/specialist/MCDA";
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/questions/deleteQuestion"}, method = RequestMethod.GET)
    public String deleteQuestion(HttpServletRequest request) {
        Connection con = PostgreSQLJDBC.Connect();
        String questionName = request.getParameter("name");
        questionName = questionName.trim();
        questionDaoImpl.deleteQuestion(questionName, con);
        return "redirect:/specialist/questions";
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/caseStudy/editCaseStudy"}, method = RequestMethod.GET)
    public ModelAndView editCaseStudy(HttpServletRequest request)
    {
        Connection con = PostgreSQLJDBC.Connect();
        String caseName = request.getParameter("name");
        caseName = caseName.trim();
        CaseStudy caseStudy = caseStudyDaoImpl.getCaseStudy(caseName, con);
        int mcdaId = caseStudy.getMcda();
        int modelId = caseStudy.getModel();
        Model mdl = modelDaoImpl.getModelById(modelId);
       int reportId = caseStudy.getReport();
       int styleId = caseStudy.getStyle();
        String reportName = reportDaoImpl.GetReportById(reportId).getName();
        int reportid = reportDaoImpl.GetReportById(reportId).getId();
        List<CaseStudy> caseList = caseStudyDaoImpl.getAllCaseStudy(con);
        List<MCDA> mcdas = mcdaDaoImpl.getAllMCDAs(con);
        MCDA mcda= mcdaDaoImpl.getMCDAById(con,mcdaId);
        List<Model> modelsList = modelDaoImpl.getAll();
        List<Report> reportsList = reportDaoImpl.getAll();
        
         List<Style> stylesList = styleDaoImpl.getAll();
        String styleName = styleDaoImpl.getStyleById(styleId).getName();
        int styleid = styleDaoImpl.getStyleById(styleId).getId();
        
        request.setAttribute("caseList", caseList);
        request.setAttribute("modelsList", modelsList);
        request.setAttribute("mcdaList", mcdas);
        request.setAttribute("mcdaId", mcdaId);
        request.setAttribute("mcdaName", mcda.getName());
        
        request.setAttribute("reportList", reportsList);
        request.setAttribute("reportName", reportName);
        request.setAttribute("reportId", reportid);
        
         request.setAttribute("styleList", stylesList);
         request.setAttribute("styleName", styleName);
        request.setAttribute("styleId", styleid);
        
        request.setAttribute("caseStudy", caseStudy);
     
        ModelAndView mav = new ModelAndView("specialist/editCaseStudy");
        return mav;
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/caseStudy/editCaseStudy"}, method = RequestMethod.POST)
    public String updateCaseStudy(HttpServletRequest request) {
        Connection con = PostgreSQLJDBC.Connect();
         Map<String, String[]> parameterMap = request.getParameterMap();
        String caseStudyName = request.getParameter("casenameHidden");
      String caseStudyDescription = request.getParameter("description");
        String mcdaServiceName = parameterMap.get("mcda")[0];
        String modelName = parameterMap.get("model")[0];
        String report = parameterMap.get("report")[0];
        String style = parameterMap.get("style")[0];
        Model model = modelDaoImpl.getModelByName(modelName);
      
        MCDA mcda = mcdaDaoImpl.getMCDAById(con,Integer.parseInt(mcdaServiceName));
        int id = Integer.parseInt(request.getParameter("caseIdHidden"));
        CaseStudy caseStudy = new CaseStudy();
        caseStudy.setId(id);
        caseStudy.setName(caseStudyName);
        caseStudy.setDescription(caseStudyDescription);
        caseStudy.setMcda(mcda.getId());
        caseStudy.setModel(model.getId());
        caseStudy.setReport(Integer.parseInt(report));
        caseStudy.setStyle(Integer.parseInt(style));
        caseStudyDaoImpl.updateCaseStudy(caseStudy, id, con);
        return "redirect:/specialist/caseStudy";
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/caseStudy/deleteCaseStudy"}, method = RequestMethod.GET)
    public String deleteCaseStudy(HttpServletRequest request) {
        Connection con = PostgreSQLJDBC.Connect();
        String caseName = request.getParameter("name");
        caseName = caseName.trim();
        caseStudyDaoImpl.deleteCaseStudy(caseName, con);
        return "redirect:/specialist/caseStudy";
    }
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/MCDA/runMCDA"}, method = RequestMethod.GET)
    public ModelAndView getMCDAPage() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        String outputString1 = "";
        String outputString2 = "";
        String outputString3 = "";
        String outputString4 = "";

      //  String xslPath = workingDirectory.substring(0, workingDirectory.lastIndexOf("WEB-INF/")) + "resources/xsl/";
        String xslPath=Properties.MCDA_XSL_PATH;
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

            Source xslt = new StreamSource(new File(xslPath + "XMCDATest.xsl"));

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

            Source xslt = new StreamSource(new File(xslPath + "XMCDATest.xsl"));

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

            Source xslt = new StreamSource(new File(xslPath + "XMCDATest.xsl"));

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

            Source xslt = new StreamSource(new File(xslPath + "XMCDATest.xsl"));

            Transformer transformer = transformerFactory.newTransformer(xslt);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(xmlSource, streamResult);
            outputString4 = stringWriter.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //----------------------------------------------------------------------
        ModelAndView mav = new ModelAndView("specialist/runMCDA");
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

    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/specialist/MCDA/runMCDA"}, method = RequestMethod.POST)
    public ModelAndView runMCDA(HttpServletRequest request) {
        String serviceName = "weightedSum-PyXMCDA.py";
        String ticketValue = "";
        String ticketValue1 = "";
        String submitProblemResponse = "";
        String submitProblemResponse1 = "";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
      //  String xslPath = workingDirectory.substring(0, workingDirectory.lastIndexOf("WEB-INF/")) + "resources/xsl/";
       String xslPath = Properties.MCDA_XSL_PATH;
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
        submitProblemResponse = soapClientForRequest.sendSubmitProblemSOAPRequest(encoded,  "http://192.168.2.92/soap/" + serviceName);
        System.out.println("Submit Problem Response\n" + submitProblemResponse);

        //Sending Request Solution Request
        ticketValue = soapClientForRequest.readTicketValue(submitProblemResponse);
        boolean noTicketValue = false;
        if (ticketValue.isEmpty()) {
            noTicketValue = true;
        }
        File requestSolutionFile = soapClientForRequest.createRequestSolutionSOAPFile(ticketValue);
        byte[] encodedSolution = soapClientForRequest.createRequestSolutionSOAPRequest(requestSolutionFile.getPath());
        String requestSolutionResponse = soapClientForRequest.sendRequestSolutionSOAPRequest(encodedSolution,  "http://192.168.2.92/soap/" + serviceName);
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
            ModelAndView mav = new ModelAndView("specialist/runMCDA");
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

                Source xslt = new StreamSource(new File(xslPath + "XMCDATest.xsl"));

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

                Source xslt = new StreamSource(new File(xslPath + "XMCDATest.xsl"));

                Transformer transformer = transformerFactory.newTransformer(xslt);
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                transformer.transform(xmlSource, streamResult);
                outputStringMessages = stringWriter.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ModelAndView mav1 = new ModelAndView("specialist/outputMCDA");
            mav1.addObject("outputStringMessages", outputStringMessages);
            mav1.addObject("outputStringPlot", outputStringPlot);
            return mav1;
        }
    }
	//---------------------Report Module ------------------------------------
    @RequestMapping(value = {"/specialist/report"}, method = RequestMethod.GET)
    public ModelAndView getReportPage(HttpServletRequest request) {

        
        reportList = reportDaoImpl.getAll();
        deptList = departmentDaoImpl.getAll();

        request.setAttribute("reportList", reportList);
        request.setAttribute("deptList", deptList);
        ModelAndView mav = new ModelAndView("/specialist/report");
        return mav;
    }
	
	@RequestMapping(value = {"/specialist/saveReport"}, method = RequestMethod.POST)
    public String saveReport(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
    
        int noofrows;
         HttpSession session = request.getSession(true);
        Map<String, String[]> parameterMap = request.getParameterMap();
        final String name = parameterMap.get("reportName")[0];
         final String desc = parameterMap.get("reportDescription")[0];
         final String depart = parameterMap.get("DepartmentName")[0];
           final String chart = parameterMap.get("chart")[0];
       report.setName(name);
        report.setDescription(desc);
        report.setChart_type(chart);
        report.setDept(Integer.parseInt(depart));
      noofrows= reportDaoImpl.add(report);
       if (noofrows==0)
       {
              
              session.setAttribute("error", "Error!!!!! Another Report with the Same Name Already Exist"); 
                   
       }
        return "redirect:/specialist/report";  
    }

	
	@RequestMapping(value = {"/DeleteReport"}, method = RequestMethod.GET)
    public String DeleteReport(HttpServletRequest request) 
    {
        int reportid = Integer.parseInt(request.getParameter("report"));
        reportDaoImpl.delete(reportid);
        
       return "redirect:/specialist/report";   
    }
	
	/*private String getFileName(final Part part) {
		final String partHeader = part.getHeader("content-disposition");
		LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename")) {
				return content.substring(
						content.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}*/
	
	
    @RequestMapping(value = {"/specialist/editreport"}, method = RequestMethod.GET)
    public ModelAndView getEditReport(HttpServletRequest request) {

        int reportid = Integer.parseInt(request.getParameter("report"));
        Report rep=reportDaoImpl.GetReportById(reportid);
         HttpSession sess = request.getSession(true);
         sess.setAttribute("reportid", reportid);
        reportList = reportDaoImpl.getAll();
        deptList = departmentDaoImpl.getAll();

       
        request.setAttribute("reportList", reportList);
        request.setAttribute("deptList", deptList);
        
        request.setAttribute("Name", rep.getName());
        request.setAttribute("deptid", rep.getDept());
        request.setAttribute("DepName", departmentDaoImpl.GetDeptById(rep.getDept()).getName());
        request.setAttribute("Description",rep.getDescription() );
        request.setAttribute("Chart", rep.getChart_type());
        ModelAndView mav = new ModelAndView("/specialist/editreport");
        return mav;
    }
	
    @RequestMapping(value = {"/EditReport"}, method = RequestMethod.POST)
    public String EditReport(HttpServletRequest request)
    {
        
        Map<String, String[]> parameterMap = request.getParameterMap();
        HttpSession sess = request.getSession(true);

       // String name = parameterMap.get("Name")[0];
        String depart = parameterMap.get("DepartmentName")[0];
        String desc = parameterMap.get("Description")[0];
        String Chart = parameterMap.get("Chart")[0];  
        report.setId(Integer.parseInt(sess.getAttribute("reportid").toString()));
       // report.setName(name);
        report.setDescription(desc);
        report.setDept(Integer.parseInt(depart));
        report.setChart_type(Chart);
        reportDaoImpl.update(report);
       return "redirect:/specialist/report";   
    }
	
    @RequestMapping(value = {"/testReport"}, method = RequestMethod.GET)
    public String testReport(HttpServletRequest request,HttpServletResponse response) throws IOException, Exception
    { 
     int reportid = Integer.parseInt(request.getParameter("report"));
     Report rep= reportDaoImpl.GetReportById(reportid);
     reportmanger.build(Properties.Reports + "\\alternativesValues.xml", Properties.Reports, rep.getName(),Properties.Reports + "//attributes.xml", "SDSS Report", rep.getDescription(),rep.getChart_type());
     reportmanger.viewreportfile(response,rep.getName());
     return "redirect:/specialist/report";   
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
    @RequestMapping(value = {"/GenerateReport"}, method = RequestMethod.POST)
    public ModelAndView GenerateReportPage(HttpServletRequest request,HttpServletResponse response) throws SAXException, Exception {
            
        String reportoutputpath = Properties.Reports;
        String reportname = "TestCase1";
        
        try {
                reportmanger.build(Properties.MCDA_INPUT + "\\alternativesValues.xml", reportoutputpath, reportname,Properties.MCDA_INPUT + "//attributes.xml", "", "","Bar_Chart");
                reportmanger.viewreportfile(response,reportname);
           
        } catch (Exception ex) {
          
        }
        return new ModelAndView("specialist/runMCDA");
    }

    
    //--------------------------------------------------------------------------
    @RequestMapping(value = {"/deleteModel"}, method = RequestMethod.GET)
    public String deleteModel(HttpServletRequest request) {
        
        int modelid = Integer.parseInt(request.getParameter("modid"));
        modelDaoImpl.delete(modelid);
        
        return "redirect:/specialist/modelling";
    }
    
    //////////////style///////////
      @RequestMapping(value = {"/specialist/style"}, method = RequestMethod.GET)
    public ModelAndView getStylePage(HttpServletRequest request)
    {
        styleList = styleDaoImpl.getAll();
        colorList=colorDaoImpl.getAll();
        request.setAttribute("colorList", colorList);
      
       
        request.setAttribute("styleList", styleList);
        
        
        ModelAndView mav = new ModelAndView("specialist/style");
        return mav;
    }
    @RequestMapping(value = {"/specialist/saveStyle"}, method = RequestMethod.POST)
    public String saveStyle(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
    
        int noofrows;
        HttpSession session = request.getSession(true);
        Map<String, String[]> parameterMap = request.getParameterMap();
        final String name = parameterMap.get("styleName")[0];
        final String desc = parameterMap.get("styleDescription")[0];
        final String dataType = parameterMap.get("dataType")[0];
        final String highcolor = parameterMap.get("highcolor")[0];
        final String medColor = parameterMap.get("medcolor")[0];
       String name_= name.replaceAll("\\s+","");
        final String lowColor = parameterMap.get("lowcolor")[0];
        String[] ColorArray = {lowColor,medColor,highcolor};
        File stylefile;
        File outfile = null;
        if ("Polygon".equals(dataType))
        { 
       stylefile=new File(Properties.Style_INPUT+"polygon_attributebased.sld");
       outfile=new File(Properties.Style_OUTPUT+name_+".sld");
        styleManger.insertColorInStyleFile(ColorArray, stylefile,outfile );
       // styleManger.publishStyle(outfile,name);
        
        }
        else if("Line".equals(dataType))
        {
         // stylefile=new File(Properties.Style_INPUT+"line_attributebased.sld");
          //outfile=new File(Properties.Style_OUTPUT+name_+".sld");
       // styleManger.insertColorInStyleFile(ColorArray, stylefile,outfile );
        }
        else if("Point".equals(dataType))
        {
           stylefile=new File(Properties.Style_INPUT+"point_attribute.sld");
          outfile=new File(Properties.Style_OUTPUT+name_+".sld");
          styleManger.insertColorInStyleFile(ColorArray, stylefile,outfile );
        }
       
       // String name_= name.replaceAll("\\s+","");
        style.setName(name_);
        style.setDescription(desc);
        style.setDatatype(dataType);
        style.setHighvalcolor(highcolor);
        style.setMidvalcolor(medColor);
        style.setLowvalcolor(lowColor);
        style.setFile(outfile);
        noofrows= styleDaoImpl.add(style);
       if (noofrows==0)
       {
              
              session.setAttribute("error", "Error!!!!! Another Style with the Same Name Already Exist"); 
                   
       } 
       
      return "redirect:/specialist/style";  
    }
  
     @RequestMapping(value = {"/specialist/editStyle"}, method = RequestMethod.GET)
    public ModelAndView getEditStyle(HttpServletRequest request) {

         int styleid = Integer.parseInt(request.getParameter("style"));
       HttpSession sess = request.getSession(true);
         sess.setAttribute("styleid", styleid);
        styleList = styleDaoImpl.getAll();
        colorList=colorDaoImpl.getAll();
        request.setAttribute("colorList", colorList);

        request.setAttribute("StyleList", styleList);;
         Style sty= styleDaoImpl.getStyleById(styleid);
        request.setAttribute("styleName", sty.getName());
        request.setAttribute("dataType", sty.getDatatype());
        request.setAttribute("styleDescription",sty.getDescription());
        
        request.setAttribute("highColor",colorDaoImpl.GetColorByCode(sty.getHighvalcolor()).getName());
        request.setAttribute("medcolor", colorDaoImpl.GetColorByCode(sty.getMidvalcolor()).getName());
        request.setAttribute("lowcolor",colorDaoImpl.GetColorByCode(sty.getLowvalcolor()).getName());
       
        ModelAndView mav = new ModelAndView("/specialist/editstyle");
        return mav;
    }
	
    @RequestMapping(value = {"/EditStyle"}, method = RequestMethod.POST)
    public String editStyle(HttpServletRequest request)
    {
        int noofrows;
       HttpSession sess = request.getSession(true);
        Map<String, String[]> parameterMap = request.getParameterMap();
        final String name = parameterMap.get("styleName")[0];
        final String desc = parameterMap.get("styleDescription")[0];
       final String dataType = parameterMap.get("dataType")[0];
        final String highcolor = parameterMap.get("highcolor")[0];
        final String medColor = parameterMap.get("medcolor")[0];
      final String lowColor = parameterMap.get("lowcolor")[0];
       String[] ColorArray = {lowColor,medColor,highcolor};
        File stylefile=null;
         File outfile=null;
       if ("Polygon".equals(dataType))
        { 
         stylefile=new File(Properties.Style_INPUT+"polygon_attributebased.sld");
         outfile=new File(Properties.Style_OUTPUT+name+".sld");
        styleManger.insertColorInStyleFile(ColorArray, stylefile,outfile );
       // styleManger.publishStyle(outfile,name);
        
        }
        else if("Line".equals(dataType))
        {
         // stylefile=new File(Properties.Style_INPUT+"line_attributebased.sld");
         // outfile=new File(Properties.Style_OUTPUT+name+".sld");
      //  styleManger.insertColorInStyleFile(ColorArray, stylefile,outfile );
        }
        else if("Point".equals(dataType))
        {
           stylefile=new File(Properties.Style_INPUT+"point_attribute.sld");
          outfile=new File(Properties.Style_OUTPUT+name+".sld");
          styleManger.insertColorInStyleFile(ColorArray, stylefile,outfile );
        }
       style.setId(Integer.parseInt(sess.getAttribute("styleid").toString()));
       style.setName(name);
       style.setDescription(desc);
       style.setDatatype(dataType);
       style.setHighvalcolor(highcolor);
       style.setMidvalcolor(medColor);
       style.setLowvalcolor(lowColor);
       style.setFile(outfile);
       noofrows= styleDaoImpl.update(style);
      return "redirect:/specialist/style"; 
    }
    	@RequestMapping(value = {"/deleteStyle"}, method = RequestMethod.GET)
    public String deleteStyle(HttpServletRequest request) 
    {
        int styleid = Integer.parseInt(request.getParameter("style"));
        styleDaoImpl.delete(styleid);
        
       return "redirect:/specialist/style";   
    }
}
