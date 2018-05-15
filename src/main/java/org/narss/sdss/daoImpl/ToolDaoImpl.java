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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.narss.sdss.controllers.Properties;
import org.narss.sdss.dao.ToolDAO;
import org.narss.sdss.dto.Tool;
import org.narss.sdss.dto.Input;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;

/**
 *
 * @author Sayed
 */
public class ToolDaoImpl implements ToolDAO {

    

// Use it to add new tool
        /* int numOfRows = 0;
        Connection c = PostgreSQLJDBC.Connect();
        PreparedStatement stmt = null;
        if (c != null) {
            try {
                c.setAutoCommit(false);
                String sql = "INSERT INTO tool (name,file) "
                        + "VALUES (? , ?);";
                File file = new File("C:/geoModelling/Aggregate.xml");
                FileInputStream fis = new FileInputStream(file);
                stmt = c.prepareStatement(sql);
                stmt.setString(1, "Aggregate");
                stmt.setBinaryStream(2, fis, (int) file.length());
                numOfRows = stmt.executeUpdate();
                stmt.close();
                fis.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
        System.out.println("Operation done successfully");*/
 /*List<Input> inputs = new ArrayList<Input>();
        
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            String sql = "SELECT * FROM tool_inputs WHERE tool_id = ? ";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, 5);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Input input = new Input();
                input.setId(rs.getInt("Input_id"));

                String sql2 = "SELECT * FROM input WHERE id = ? ";
                PreparedStatement ps2 = c.prepareStatement(sql2);
                ps2.setInt(1, input.getId());
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    input.setName(rs2.getString("name"));
                    input.setType(rs2.getString("type"));
                }
                rs2.close();
                ps2.close();
                inputs.add(input);
                // use the data in some way here
                System.out.println("ID = " + input.getId());
                System.out.println("NAME = " + input.getName());
                System.out.println("TYPE = " + input.getType());
            }
            rs.close();
            ps.close();

            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        // return inputs;*/
        /*Tool tool = new Tool();
        Connection c = null;
        Statement stmt = null;
        try {
            // prepare file to save the tool xml
            File toolFile = new File(Properties.SPECIALIST + "Buffer" + ".xml");
            FileOutputStream fos = new FileOutputStream(toolFile);

            c = PostgreSQLJDBC.Connect();
            String sql = "SELECT * FROM tool WHERE name = ? ";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, "Buffer");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tool.setId(rs.getInt("id"));
                tool.setName(rs.getString("name"));
                tool.setCountOfInputLayers(rs.getInt("countOfInputs"));
                tool.setCountOfoutputLayer(rs.getInt("countOfOutputs"));
                InputStream is = rs.getBinaryStream("file");
                byte[] fileBytes = new byte[is.available()];
                while (is.read(fileBytes) > 0) {
                    fos.write(fileBytes);
                }
                tool.setFile(toolFile);
            }
            //get tool inputs
            Tool_InputDaoImpl toolInputImpl = new Tool_InputDaoImpl();
            List<Input> inputs = toolInputImpl.getInputs(tool.getId());
            tool.setInputs(inputs);
            rs.close();
            ps.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
   */

    @Override
    public List<Tool> getAll() {
        List<Tool> toolList = new ArrayList<Tool>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tool;");
            while (rs.next()) {
                Tool tool = new Tool();
                tool.setId(rs.getInt("id"));
                tool.setName(rs.getString("name"));
                InputStream inputStream = rs.getBinaryStream("file");
                File toolfile = new File("c:/ttt.xml");
                FileOutputStream outputStream = new FileOutputStream(toolfile);
                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                tool.setFile(toolfile);
                toolList.add(tool);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return toolList;
    }

    @Override
    public Tool getByName(String toolname) {
        Tool tool = new Tool();
        Connection c = null;
        Statement stmt = null;
        try {
            // prepare file to save the tool xml
            File toolFile = new File(Properties.SPECIALIST + toolname + ".xml");
            FileOutputStream fos = new FileOutputStream(toolFile);

            c = PostgreSQLJDBC.Connect();
            String sql = "SELECT * FROM tool WHERE name = ? ";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, toolname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tool.setId(rs.getInt("id"));
                tool.setName(rs.getString("name"));
                tool.setCountOfInputLayers(rs.getInt("countOfInputs"));
                tool.setCountOfoutputLayer(rs.getInt("countOfOutputs"));
                InputStream is = rs.getBinaryStream("file");
                byte[] fileBytes = new byte[is.available()];
                while (is.read(fileBytes) > 0) {
                    fos.write(fileBytes);
                }
                tool.setFile(toolFile);
            }
            //get tool inputs
            Tool_InputDaoImpl toolInputImpl = new Tool_InputDaoImpl();
            List<Input> inputs = toolInputImpl.getInputs(tool.getId());
            tool.setInputs(inputs);
            rs.close();
            ps.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return tool;
    }
    
    @Override
    public Tool getById(int id) {
        Tool tool = new Tool();
        Connection c = null;
        Statement stmt = null;
        try {
            
        
            c = PostgreSQLJDBC.Connect();
            String sql = "SELECT * FROM tool WHERE id = ? ";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                tool.setId(rs.getInt("id"));
                tool.setName(rs.getString("name"));
                tool.setCountOfInputLayers(rs.getInt("countOfInputs"));
                tool.setCountOfoutputLayer(rs.getInt("countOfOutputs"));
                 File toolFilename = new File(Properties.SPECIALIST + rs.getString("name") + ".xml");
                 FileOutputStream fos = new FileOutputStream(toolFilename);
                InputStream is = rs.getBinaryStream("file");
                byte[] fileBytes = new byte[is.available()];
                while (is.read(fileBytes) > 0) {
                    fos.write(fileBytes);
                }
                tool.setFile(toolFilename);
            }
             
            //get tool inputs
            Tool_InputDaoImpl toolInputImpl = new Tool_InputDaoImpl();
            List<Input> inputs = toolInputImpl.getInputs(tool.getId());
            tool.setInputs(inputs);
            rs.close();
            ps.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
           
        }
        System.out.println("Operation done successfully");
        return tool;
    }
    
    @Override
    public int getNoOfInputsLayers(String toolname) {
        int outputs = 0;
        Connection c = null;
        Statement stmt = null;
        try {

            c = PostgreSQLJDBC.Connect();
            String sql = "SELECT * FROM tool WHERE name = ? ";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, toolname);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                outputs = rs.getInt("countOfInputs");
            }
            rs.close();
            ps.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return outputs;
    }

}
