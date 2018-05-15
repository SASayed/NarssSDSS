/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.daoImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import org.narss.sdss.dao.ModelDAO;
import org.narss.sdss.dto.Model;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;

/**
 *
 * @author Sayed
 */
public class ModelDaoImpl implements ModelDAO {

    @Override
    public int add(Model model) {
       Model mdl = new Model();
	   mdl=getModelByName(model.getName());
        
        int numOfRows = 0;
        Connection c = PostgreSQLJDBC.Connect();
        Statement stmt = null;
        if (c != null) {
            try {
               
                   if (!model.getName().equals(mdl.getName()))
                   {
                       c.setAutoCommit(false);
                       stmt = c.createStatement();
                       String sql = "INSERT INTO MODEL ( name,description,has_mcda) "
                        + "VALUES ('" + model.getName() + "', '" + model.getDescription() + "'," + model.getHasMcda() + ");";
                numOfRows = stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                c.close(); 
                   }
                   else 
                   {   
                       numOfRows = 0;
                     }
                
                
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        return numOfRows;
    }

    @Override
    public List<Model> getAll() {
        List<Model> mdlList = new ArrayList<Model>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM model;");
            while (rs.next()) {
                Model mdl = new Model();
                mdl.setId(rs.getInt("id"));
                mdl.setName(rs.getString("name"));
                mdl.setDescription(rs.getString("description"));
                mdl.setHasMcda(rs.getBoolean("has_mcda"));
                mdl.setResult_layer(rs.getString("result_layer"));
                mdlList.add(mdl);

                ////for test to remove later
                System.out.println("ID = " + mdl.getId());
                System.out.println("NAME = " + mdl.getName());
                System.out.println("AGE = " + mdl.getDescription());
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return mdlList;
    }

    @Override
    public Model getModelById(int id) {
        //List<Model> mdlList = new ArrayList<Model>();
        Model mdl = new Model();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "SELECT * FROM model where id = " + id;
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                mdl.setId(rs.getInt("id"));
                mdl.setName(rs.getString("name"));
                mdl.setDescription(rs.getString("description"));
                mdl.setHasMcda(rs.getBoolean("has_mcda"));
                mdl.setResult_layer(rs.getString("result_layer"));
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return mdl;
    }

    //--------------------------------------------------------------------------

    public Model getModelByName(String name) {
        Model mdl = new Model();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement();
            String sql = "SELECT * FROM model where name= ? ;";
            PreparedStatement ps = c.prepareStatement(sql);

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                mdl.setId(rs.getInt("id"));
                mdl.setName(rs.getString("name"));
                mdl.setDescription(rs.getString("description"));
                mdl.setHasMcda(rs.getBoolean("has_mcda"));
                mdl.setResult_layer(rs.getString("result_layer"));
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return mdl;
    }

    @Override
    public int update(Model model, String filename) {
        int numOfRows = 0;
        Connection c = PostgreSQLJDBC.Connect();
        if (c != null) {
            try {

                String sql = "UPDATE  MODEL SET file = ? WHERE id=?";
                PreparedStatement pstmt = c.prepareStatement(sql);
                File file = new File(filename);
                FileInputStream fis = new FileInputStream(file);
                pstmt.setBinaryStream(1, fis, (int) file.length());
                pstmt.setInt(2, model.getId());
                numOfRows = pstmt.executeUpdate();
                pstmt.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        return numOfRows;
    }
    
    @Override
    public int update_resultlayer(Model model, String layer) {
        int numOfRows = 0;
        Connection c = PostgreSQLJDBC.Connect();
        if (c != null) {
            try {

                String sql = "UPDATE  MODEL SET result_layer = ? WHERE id=?";
                PreparedStatement pstmt = c.prepareStatement(sql);
               
                pstmt.setString(1,layer );
                pstmt.setInt(2, model.getId());
                numOfRows = pstmt.executeUpdate();
                pstmt.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        return numOfRows;
    }
    
    @Override
    public File getFileByModelId(int modelId)
    {
        File toolfile = null;
        Model mdl = new Model();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            c.setAutoCommit(false);
            stmt = c.createStatement();
            String sql = "SELECT * FROM model where id = " + modelId;
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                mdl.setId(rs.getInt("id"));
                mdl.setName(rs.getString("name"));
                mdl.setDescription(rs.getString("description"));
                mdl.setHasMcda(rs.getBoolean("has_mcda"));
                mdl.setResult_layer(rs.getString("result_layer"));
                InputStream inputStream = rs.getBinaryStream("file");
                toolfile = new File(Properties.QUERY_PATH+mdl.getName()+".xml");
                FileOutputStream outputStream = new FileOutputStream(toolfile);
                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                mdl.setFile(toolfile);
            }
            rs.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return toolfile;
    }
    //--------------------------------------------------------------------------
    @Override
	public int delete(int id) {
        String SQL = "DELETE FROM MODEL WHERE id = ?";
 
        int affectedrows = 0;
 
        try (Connection conn = PostgreSQLJDBC.Connect();
        PreparedStatement pstmt = conn.prepareStatement(SQL)) {
 
            pstmt.setInt(1, id);
 
            affectedrows = pstmt.executeUpdate();
 
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return affectedrows;
    }
}
