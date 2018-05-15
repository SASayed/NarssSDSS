/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.narss.sdss.dao.CaseStudyDAO;
import org.narss.sdss.dto.CaseStudy;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;

/**
 *
 * @author Sayed
 */
public class CaseStudyDaoImpl implements CaseStudyDAO{

    @Override
    public int addCaseStudy(CaseStudy caseStudy, Connection connection) {
        int numOfRows = 0;
        List<String> namesList = new ArrayList<String>();
        String testSQL = "SELECT * FROM casestudy";
        ResultSet rs = null;
        String sql = "INSERT INTO casestudy "
                + "(id, name, description, model, mcda,report,styl) VALUES ("+ caseStudy.getId() + ",'" + caseStudy.getName()
                + "','"+caseStudy.getDescription()+"', "+caseStudy.getModel()+", "+caseStudy.getMcda()+", "+caseStudy.getReport()+", "+caseStudy.getStyle()+")";
        try {
                Statement stm = null;
                stm = connection.createStatement();
                rs = stm.executeQuery(testSQL);
                while(rs.next())
                {
                    namesList.add(rs.getString("name"));
                }
                rs.close();
                stm.close();
        }
        catch (SQLException exp) {
            Logger.getLogger(MCDADaoImpl.class.getName()).log(Level.SEVERE, null, exp);
        }
        try {
            Statement st = connection.createStatement();
            if(!namesList.isEmpty())
            {
                for(String name: namesList)
                {
                    if(name.equalsIgnoreCase(caseStudy.getName()))
                        return numOfRows;
                }
            }
            numOfRows = st.executeUpdate(sql);
            st.close();
            connection.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CaseStudyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return numOfRows;
    }

    @Override
    public List<CaseStudy> getAllCaseStudy(Connection connection) {
        List<CaseStudy> caseStudyList = new ArrayList<CaseStudy>();
        String sql = "SELECT * FROM casestudy";
        try{
            Statement st = null;
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                CaseStudy caseStudy = new CaseStudy();
                caseStudy.setId(rs.getInt("id"));
                caseStudy.setName(rs.getString("name"));
                caseStudy.setDescription(rs.getString("description"));
                caseStudy.setModel(rs.getInt("model"));
                caseStudy.setMcda(rs.getInt("mcda"));
                caseStudy.setReport(rs.getInt("report"));
                caseStudy.setStyle(rs.getInt("styl"));
                caseStudyList.add(caseStudy);
            } 
        } catch (SQLException ex) {
            Logger.getLogger(CaseStudyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return caseStudyList;
    }
    
    @Override
    public CaseStudy getCaseStudy(String name,Connection connection)
    {
        String sql = "SELECT * FROM casestudy WHERE name = '" + name + "'" ;
        CaseStudy caseStudy = new CaseStudy();
        try {
            Statement st = null;
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                caseStudy.setId(rs.getInt("id"));
                caseStudy.setName(rs.getString("name"));
                caseStudy.setDescription(rs.getString("description"));
                caseStudy.setModel(rs.getInt("model"));
                caseStudy.setMcda(rs.getInt("mcda"));
                caseStudy.setReport(rs.getInt("report"));
                caseStudy.setStyle(rs.getInt("styl"));
            } 
        } catch (SQLException ex) {
            Logger.getLogger(CaseStudyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return caseStudy;
    }
    //--------------------------------------------------------------------------
    @Override
    public CaseStudy getCaseStudy(int id)
    {
        Connection conn = PostgreSQLJDBC.Connect();
        String sql = "SELECT * FROM casestudy WHERE id = " + id ;
        CaseStudy caseStudy = new CaseStudy();
        try {
            Statement st = null;
            st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                caseStudy.setId(rs.getInt("id"));
                caseStudy.setName(rs.getString("name"));
                caseStudy.setDescription(rs.getString("description"));
                caseStudy.setModel(rs.getInt("model"));
                caseStudy.setMcda(rs.getInt("mcda"));
                caseStudy.setReport(rs.getInt("report"));
                caseStudy.setStyle(rs.getInt("styl"));
            } 
        } catch (SQLException ex) {
            Logger.getLogger(CaseStudyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return caseStudy;
    }
    //--------------------------------------------------------------------------
    @Override
    public int updateCaseStudy(CaseStudy caseStudy, int id, Connection connection)
    {
        int numOfRows = 0;
        
        String sql = "UPDATE casestudy SET name = '" + caseStudy.getName() +"', description = '" + caseStudy.getDescription() + "', model = " + caseStudy.getModel() + ", mcda = " + caseStudy.getMcda()+ ", report = " + caseStudy.getReport()
               + ", styl = " + caseStudy.getStyle()+ " WHERE id = " + id;
        
        try {
            Statement st = null;
            st = connection.createStatement();
            numOfRows = st.executeUpdate(sql);
            st.close();
            connection.commit();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(CaseStudyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return numOfRows;
    }
    //--------------------------------------------------------------------------
    @Override
    public int deleteCaseStudy(String name, Connection connection)
    {
        int numOfRows = 0;
        String sql = "DELETE FROM casestudy WHERE name = '" + name + "'";
        try {
            Statement st = null;
            st = connection.createStatement();
            numOfRows = st.executeUpdate(sql);
            st.close();
            //connection.commit();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(CaseStudyDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return numOfRows;
    }
}