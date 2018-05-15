/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.daoImpl;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.narss.sdss.dao.StyleDAO;
import org.narss.sdss.dto.Style;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;

/**
 *
 * @author Administrator
 */
public class StyleDaoImpl implements StyleDAO{
    @Override
    public int add(Style style)
   {  
       Style styl = new Style();
       styl=GetStyleByName(style.getName());
       int numOfRows = 0;
        Connection conn = PostgreSQLJDBC.Connect();
        PreparedStatement ps = null;
        if (conn != null && styl.getName()==null)
        { 
         try
         {
            
      System.out.println("Inserting records into the table...");
      String sql = "INSERT INTO style (name,description,datatype,file,highvaluescolor,medvaluescolor,lowvaluescolor) VALUES(?,?,?,?,?,?,?)";
      ps = conn.prepareStatement(sql);
      ps.setString(1, style.getName());
      ps.setString(2, style.getDescription());
      ps.setString(3,style.getDatatype());
      
      FileInputStream fis = new FileInputStream(style.getFile());
      ps.setBinaryStream(4, fis, (int) style.getFile().length());
      // ps.setString(4, style.getFile());
       ps.setString(5, style.getHighvalcolor());
      ps.setString(6, style.getMidvalcolor());
      ps.setString(7,style.getLowvalcolor());
      numOfRows=ps.executeUpdate();
      System.out.println("Inserted records into the table...");
 
           
         
         }catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
               
            }
} 
        else 
                   {   
                       numOfRows = 0;
                     }
        return numOfRows;
   }
    public Style GetStyleByName(String name)
    {
        
         Style styl = new Style();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement(); 
            String sql ="SELECT * FROM style where name= ? ;";
             PreparedStatement ps = c.prepareStatement(sql);
                 
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                styl.setId(rs.getInt("id"));
                styl.setName(rs.getString("name"));
                styl.setDescription(rs.getString("description"));
                styl.setDatatype(rs.getString("datatype"));
            styl.setHighvalcolor(rs.getString("highvaluescolor"));
                styl.setMidvalcolor(rs.getString("medvaluescolor"));
                styl.setLowvalcolor(rs.getString("lowvaluescolor"));
               //file
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
         
        }
        System.out.println("Operation done successfully");
        return styl;
    }
    public List<Style> getAll() {
        List<Style> styleList = new ArrayList<Style>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM style;");
            while (rs.next()) {
                Style styl = new Style();
                styl.setId(rs.getInt("id"));
                styl.setName(rs.getString("name"));
                styl.setDescription(rs.getString("description"));
                styl.setDatatype(rs.getString("datatype"));
                styleList.add(styl);

            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            
        }
        System.out.println("Operation done successfully");
        return styleList;
    }
     @Override
    public int update(Style style)
    {
        int numOfRows = 0;
        Connection c = PostgreSQLJDBC.Connect();
        if (c != null) {
            try { 
             String sql = "UPDATE  style SET description=?,datatype=?,file=? ,highvaluescolor=?,medvaluescolor=?,lowvaluescolor=? WHERE id=?" ;
             PreparedStatement pstmt = c.prepareStatement(sql) ;
             
             pstmt.setString(1, style.getDescription());
            
             pstmt.setString(2, style.getDatatype());
             FileInputStream fis = new FileInputStream(style.getFile());
              pstmt.setBinaryStream(3, fis, (int) style.getFile().length());
              pstmt.setString(4, style.getHighvalcolor());
              pstmt.setString(5, style.getMidvalcolor());
              pstmt.setString(6, style.getLowvalcolor());
             pstmt.setInt(7, style.getId());
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
    public int updatefile(Style style)
    {
        int numOfRows = 0;
        Connection c = PostgreSQLJDBC.Connect();
        if (c != null) {
            try { 
             String sql = "UPDATE  style SET file=?  WHERE id=?" ;
             PreparedStatement pstmt = c.prepareStatement(sql) ;
             
            
            
             
             FileInputStream fis = new FileInputStream(style.getFile());
              pstmt.setBinaryStream(1, fis, (int) style.getFile().length());
             
             pstmt.setInt(2, style.getId());
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
 public int delete(int id) {
        String SQL = "DELETE FROM style WHERE id = ?";
 
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
 @Override
    public Style getStyleById(int id) {
         Style styl = new Style();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement(); 
            String sql ="SELECT * FROM style where id= ? ;";
             PreparedStatement ps = c.prepareStatement(sql);
                 
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                 styl.setId(rs.getInt("id"));
                styl.setName(rs.getString("name"));
                styl.setDescription(rs.getString("description"));
                styl.setDatatype(rs.getString("datatype"));
                 styl.setHighvalcolor(rs.getString("highvaluescolor"));
                styl.setMidvalcolor(rs.getString("medvaluescolor"));
                styl.setLowvalcolor(rs.getString("lowvaluescolor"));
           
           }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return styl;
       
    }
}

