/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.controllers;

import com.lowagie.text.DocumentException;
import java.io.FileNotFoundException;
import org.narss.sdss.dto.CaseStudy;
import org.narss.sdss.dto.Question;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import org.narss.sdss.daoImpl.CaseStudyDaoImpl;
import org.narss.sdss.daoImpl.ModelDaoImpl;
import org.narss.sdss.daoImpl.QuestionDaoImpl;
import org.narss.sdss.daoImpl.ReportDaoImpl;
import org.narss.sdss.daoImpl.StyleDaoImpl;
import org.narss.sdss.dto.Model;
import org.narss.sdss.dto.Report;
import org.narss.sdss.dto.Style;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.SAXException;

/**
 *
 * @author Administrator
 */
@Controller
public class DecisionMakerController {

    @Autowired
    private Question question;
    @Autowired
    private QuestionDaoImpl questionDaoImpl;
    @Autowired
    private CaseStudyDaoImpl caseStudyDaoImpl;
     @Autowired
    private CaseStudy caseStudy;
     @Autowired
    private Report report;
     @Autowired
    private Model model;
     @Autowired
    private ReportDaoImpl reportDaoImpl;
     @Autowired
    private ModelDaoImpl modelDaoImpl;
    @Autowired
    private ReportManger reportmanger;
    @Autowired
    private PostgreSQLJDBC postgreSQLJDBC;
    private List<CaseStudy> casesList;
    @Autowired
    private Style style;
      @Autowired
    private StyleDaoImpl styleDaoImpl;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    LocalDateTime now = LocalDateTime.now();
    String unique = dtf.format(now);

    //view report in DS
    @RequestMapping(value = {"/decision_maker/viewReport"}, method = RequestMethod.GET)
    public String viewReportdec(HttpServletRequest request, HttpServletResponse response) throws SAXException, Exception {
        
        int caseid = Integer.parseInt(request.getParameter("caseid"));
        caseStudy=caseStudyDaoImpl.getCaseStudy(caseid);
        report=reportDaoImpl.GetReportpdfById(caseStudy.getReport());
       
        // report=reportDaoImpl.getreportpdf(caseStudy.getReport());
        String path="";
        //must retrieve pdf from database 
       // reportmanger.viewreportfile(response,report.getName());
    // reportmanger.build(Properties.MCDA_INPUT + "\\alternativesValues.xml", Properties.Reports, report.getName(),Properties.MCDA_INPUT + "//attributes.xml", "SDSS Report", report.getDescription(),report.getChart_type());
     reportmanger.viewreportfile(response,report.getName());
        return  path = "redirect:/decision_maker";
    }
@RequestMapping(value = {"/decision_maker/ViewatReport"}, method = RequestMethod.GET)
    public String ViewatReport(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, DocumentException, ParserConfigurationException, SAXException, Exception {
       
      reportmanger.viewreportfile(response,"WATER");
         return  "redirect:/decision_maker";
    }
//send question 

    @RequestMapping(value = {"/sendQuestion"}, method = RequestMethod.POST)
    public String sendQuestion(HttpServletRequest request) {
        Connection con = postgreSQLJDBC.Connect();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String questionText = parameterMap.get("Question")[0];
        question.setQuestion(questionText);
        String questiondesc = parameterMap.get("desc")[0];
        question.setQuestionDescription(questiondesc);
        String questionScopeName = parameterMap.get("ScopeName")[0];
        question.setScopeName(questionScopeName);
        String questioncategoriesName = parameterMap.get("categoriesName")[0];
        question.setCategoryName(questioncategoriesName);
        question.setId("Question" + "_" + unique);

        //save model
        questionDaoImpl.addQuestion(question, con);
        return "redirect:/decision_maker";
    }
    //view Map in DS
    @RequestMapping(value = {"/decision_maker/viewMap"}, method = RequestMethod.GET)
    public String viewMapdec(HttpServletRequest request, HttpServletResponse response) throws SAXException, Exception {
        String path="";
        int caseid = Integer.parseInt(request.getParameter("caseid"));
        caseStudy=caseStudyDaoImpl.getCaseStudy(caseid);
       //style alamindemostyle
        model=modelDaoImpl.getModelById(caseStudy.getModel());
        String layername=model.getResult_layer();
        request.setAttribute("layername", layername);
        return  path = "redirect:/decision_maker";
    }
}
