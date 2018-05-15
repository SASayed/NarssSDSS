package org.narss.sdss.controllers;

import org.narss.sdss.dto.CaseStudy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.narss.sdss.daoImpl.CaseStudyDaoImpl;
import org.narss.sdss.daoImpl.UserDaoImpl;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("userObj")
public class HomeController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CaseStudyDaoImpl caseStudyDaoImpl;
    @Autowired
    private UserDaoImpl userDaoImpl;
    
    private List<CaseStudy> casesList;
    @Autowired
    private PostgreSQLJDBC postgreSQLJDBC;
  /*  public final Connection connect()
    {
        Connection conn = null;
        try
        {
            Class.forName(Properties.DRIVER);
            conn = DriverManager.getConnection(Properties.DATABASE_URL+Properties.DATABASE_NAME, Properties.DATABASE_USERNAME, Properties.DATABASE_PASSWORD);
        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }
        return conn;
    }
    */
    @RequestMapping(value = {"/index", "/", "/login"}, method = RequestMethod.GET)
    public ModelAndView getHomePage(HttpServletRequest request) {
        ModelAndView mav;
        if (request.getSession().getAttribute("username") == null) {
            mav = new ModelAndView("login");
        } else {
            mav = new ModelAndView("sdss_home");
        }
        return mav;
    }

    @RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
    public String getLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("username");
        return "redirect:/";
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public String postLogin(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        String path;
        Boolean exist;
        String name=parameterMap.get("username")[0].toLowerCase();
        String password=parameterMap.get("password")[0].toLowerCase();
        String role=parameterMap.get("role")[0];
        exist=userDaoImpl.SelectSpecificUser(name, password, role);
        if (exist) {
            if (role.equals("specialist")) {
                path = "redirect:/specialist";
            } else if (role.equals("decisionmaker")) {
                path = "redirect:/decision_maker";
            } else if (role.equals("operator")) {
                path = "redirect:/Operator";
            } else {
                request.setAttribute("msg", "Please select a valid role");
                return "redirect:/login";
            }
            request.getSession().setAttribute("username", name);
        } else {
            request.setAttribute("msg", "Please enter a valid credentials");
            return "redirect:/login";
        }
        return path;
    }

    @RequestMapping(value = {"/decision_maker"}, method = RequestMethod.GET)
    public ModelAndView getDecisionMakerPage(HttpServletRequest request) {

        Connection con = postgreSQLJDBC.Connect();
        casesList = caseStudyDaoImpl.getAllCaseStudy(con);
        request.setAttribute("casesList", casesList);
        ModelAndView mav = new ModelAndView("decision_maker");
        return mav;
    }

    @RequestMapping(value = {"/specialist"}, method = RequestMethod.GET)
    public ModelAndView getSpecialistPage() {
        ModelAndView mav = new ModelAndView("specialist");
        return mav;
    }

    @RequestMapping(value = {"/Operator"}, method = RequestMethod.GET)
    public ModelAndView getOperatorPage(HttpServletRequest request) {
        Connection con = postgreSQLJDBC.Connect();
        List<CaseStudy> caseList = caseStudyDaoImpl.getAllCaseStudy(con);
        request.setAttribute("caseList", caseList);
        ModelAndView mav = new ModelAndView("operator");
        return mav;
    }
}