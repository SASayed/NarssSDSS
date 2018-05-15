/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.daoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.narss.sdss.controllers.Properties;
import org.narss.sdss.dao.Model_Tool_InputsDAO;
import org.narss.sdss.dto.Model_Tool_Inputs;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;

/**
 *
 * @author Sayed
 */
public class Model_Tool_InputsDaoImpl implements Model_Tool_InputsDAO {

    @Override
    public int add(Model_Tool_Inputs modelToolInputs) {
        int numOfRows = 0;
        Connection c = PostgreSQLJDBC.Connect();
        Statement stmt = null;
        if (c != null) {
            try {
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String sql = "INSERT INTO model_tool_input ( model,tool, input,layername) "
                        + "VALUES (" + modelToolInputs.getModelId() + ", " + modelToolInputs.getToolId() + ", " + modelToolInputs.getInputId()+ ",'" + modelToolInputs.getInputValue() + "');";
                numOfRows = stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        return numOfRows;
    }

   @Override
    public List<Model_Tool_Inputs> getModelToolsInputByModelId(int modelId) {
        Connection c = PostgreSQLJDBC.Connect();
        List<Model_Tool_Inputs> modelToolInputsList = new ArrayList<Model_Tool_Inputs>();
        Statement stmt = null;
        if (c != null) {
            try {
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String sql = "SELECT * FROM model_tool_input WHERE model = " + modelId;
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                  
                    Model_Tool_Inputs modelToolInputs = new Model_Tool_Inputs();
                    modelToolInputs.setId(rs.getInt("id"));
                    modelToolInputs.setModelId(rs.getInt("model"));
                    modelToolInputs.setToolId(rs.getInt("tool"));
                    modelToolInputs.setInputId(rs.getInt("input"));
                    modelToolInputs.setInputValue(rs.getString("layername"));
                    modelToolInputsList.add(modelToolInputs);
                }
                rs.close();
                stmt.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        return modelToolInputsList;
    }
  @Override
    public int countModelToolsByModelId(int modelId) {
        Connection c = PostgreSQLJDBC.Connect();
      int count=0;
        Statement stmt = null;
        if (c != null) {
            try {
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String sql = "SELECT COUNT(tool) AS count FROM model_tool_input WHERE model = " + modelId;
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                 count= rs.getInt("count");
                }
                rs.close();
                stmt.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        return count;
    }

    @Override
    public int delete(int modelid) {
        String SQL = "DELETE FROM model_tool_input WHERE model = ?";
 
        int affectedrows = 0;
 
        try (Connection conn = PostgreSQLJDBC.Connect();
        PreparedStatement pstmt = conn.prepareStatement(SQL)) {
 
            pstmt.setInt(1, modelid);
           
 
            affectedrows = pstmt.executeUpdate();
 
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }
    //--------------------------------------------------------------------------
    @Override
    public int deleteTool(int toolId,int modelId) {
        String SQL = "DELETE FROM model_tool_input WHERE tool = ? and model=?";
 
        int affectedrows = 0;
 
        try (Connection conn = PostgreSQLJDBC.Connect();
        PreparedStatement pstmt = conn.prepareStatement(SQL)) {
 
            pstmt.setInt(1, toolId);
             pstmt.setInt(2, modelId);
           
 
            affectedrows = pstmt.executeUpdate();
 
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }
   
    //--------------------------------------------------------------------------
    @Override
    public List<Model_Tool_Inputs> getModelToolsInputByToolid(int toolId, int modelId) {
        Connection c = PostgreSQLJDBC.Connect();
        List<Model_Tool_Inputs> modelToolInputsList = new ArrayList<Model_Tool_Inputs>();
        Statement stmt = null;
        if (c != null) {
            try {
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String sql = "SELECT * FROM model_tool_input WHERE tool = " + toolId +"and model ="+ modelId;
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next())
                {
                    Model_Tool_Inputs modelToolInputs = new Model_Tool_Inputs();
                    modelToolInputs.setId(rs.getInt("id"));
                    modelToolInputs.setModelId(rs.getInt("model"));
                    modelToolInputs.setToolId(rs.getInt("tool"));
                    modelToolInputs.setInputId(rs.getInt("input"));
                    modelToolInputs.setInputValue(rs.getString("layername"));
                    modelToolInputsList.add(modelToolInputs);
                }
                rs.close();
                stmt.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        return modelToolInputsList;
    }
     @Override
    public boolean checkExistOfLayer(int modelId,int tool, String Layer) {
        Connection c = PostgreSQLJDBC.Connect();
        boolean exist =false;
       
        Statement stmt = null;
        if (c != null) {
            try {
                c.setAutoCommit(false);
                stmt = c.createStatement();
               String sql = "SELECT * FROM model_tool_input WHERE tool = " + tool +" and model ="+ modelId+" and layername ="+ "'"+Layer+"'";
                ResultSet rs = stmt.executeQuery(sql);
               if(rs.next()) {
                exist=true;
                
               }
               rs.close();
                stmt.close();
                c.close();} catch (Exception e) {
             
            }
        }
        return exist;
    }

}
