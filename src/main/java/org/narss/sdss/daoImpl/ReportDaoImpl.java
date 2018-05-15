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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.narss.sdss.controllers.Properties;
import org.narss.sdss.dao.ReportDAO;
import org.narss.sdss.dto.Report;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;

/**
 *
 * @author heba
 */
public class ReportDaoImpl implements ReportDAO{
  
  
    @Override
    public int add(Report report)
   {  Report rep = new Report();
        rep=GetReportByName(report.getName());
       int numOfRows = 0;
        Connection conn = PostgreSQLJDBC.Connect();
        PreparedStatement ps = null;
        if (conn != null)
        { 
         try
         {
            if (!report.getName().equals(rep.getName()))
                   {
      System.out.println("Inserting records into the table...");
      String sql = "INSERT INTO report (name,description,department,chart_type)"+"VALUES(?,?,?,?)";
      ps = conn.prepareStatement(sql);
      ps.setString(1, report.getName());
      ps.setString(2, report.getDescription());
      ps.setInt(3,report.getDept()); 
      ps.setString(4,report.getChart_type());
      numOfRows=ps.executeUpdate();
      System.out.println("Inserted records into the table...");

                   }
            else 
                   {   
                       numOfRows = 0;
                     }
         
         }catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
} return numOfRows;
   }
        
    @Override
    public int update(Report report)
    {
        int numOfRows = 0;
        Connection c = PostgreSQLJDBC.Connect();
        if (c != null) {
            try { 
             String sql = "UPDATE  report SET description=?,department=?,chart_type=? WHERE id=?" ;
             PreparedStatement pstmt = c.prepareStatement(sql) ;
            
             pstmt.setString(1, report.getDescription());
             pstmt.setInt(2, report.getDept());
             pstmt.setString(3, report.getChart_type());
             pstmt.setInt(4, report.getId());
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
    public int updatePDF(Report report)
    {
        int numOfRows = 0;
        Connection c = PostgreSQLJDBC.Connect();
        if (c != null) {
            try {
                File file = report.getPdf_export();
                FileInputStream fis = new FileInputStream(file);
             String sql = "UPDATE  report SET pdf_export=? WHERE id=?" ;
             PreparedStatement pstmt = c.prepareStatement(sql) ;
             pstmt.setBinaryStream(1, fis, (int) file.length());
             pstmt.setInt(2, report.getId());
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
   public List<Report> getAll() {
        List<Report> reportList = new ArrayList<Report>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM report;");
            while (rs.next()) {
                Report rprt = new Report();
                rprt.setId(rs.getInt("id"));
                rprt.setName(rs.getString("name"));
                rprt.setDescription(rs.getString("description"));
                rprt.setDept(rs.getInt("department"));
                reportList.add(rprt);

                ////for test to remove later
                System.out.println("ID = " + rprt.getId());
                System.out.println("NAME = " + rprt.getName());
                System.out.println("Description = " + rprt.getDescription());
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
        return reportList;
    }

 @Override
    public Report GetReportByName(String name) {
         Report report = new Report();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement(); 
            String sql ="SELECT * FROM report where name= ? ;";
             PreparedStatement ps = c.prepareStatement(sql);
                 
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                report.setId(rs.getInt("id"));
                report.setName(rs.getString("name"));
                report.setDept(rs.getInt("department"));
                report.setDescription(rs.getString("description"));
                report.setChart_type(rs.getString("chart_type"));
               
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return report;
       
    }

    @Override
    public Report GetReportById(int id) {
         Report report = new Report();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement(); 
            String sql ="SELECT * FROM report where id= ? ;";
             PreparedStatement ps = c.prepareStatement(sql);
                 
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                report.setId(rs.getInt("id"));
                report.setName(rs.getString("name"));
                report.setDept(rs.getInt("department"));
                report.setDescription(rs.getString("description"));
                report.setChart_type(rs.getString("chart_type"));
           
           }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return report;
       
    }
     @Override
    public Report GetReportpdfById(int id) {
         Report report = new Report();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement(); 
            String sql ="SELECT * FROM report where id= ? ;";
             PreparedStatement ps = c.prepareStatement(sql);
                 
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                report.setId(rs.getInt("id"));
                report.setName(rs.getString("name"));
                report.setDept(rs.getInt("department"));
                report.setDescription(rs.getString("description"));
                report.setChart_type(rs.getString("chart_type"));
                InputStream inputStream = rs.getBinaryStream("pdf_export");
                File reportfilepdf = new File(Properties.Reports+rs.getString("name")+".pdf");
                FileOutputStream outputStream = new FileOutputStream(reportfilepdf);
                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                report.setPdf_export(reportfilepdf);
               
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return report;
       
    }


    @Override
 public int delete(int id) {
        String SQL = "DELETE FROM report WHERE id = ?";
 
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
 

/*  static void SelectSpecificReport(String RName) 
   {
      Statement stmt = null;
     // DataBeanList DataBeanList = new DataBeanList();
     // ArrayList<DataBean> dataList = DataBeanList.getDataBeanList();
      JRBeanCollectionDataSource beanColDataSource =
         new JRBeanCollectionDataSource(dataList);
String pdfFileName ="C:\\Users\\Administrator\\Desktop\\";
      Map parameters = new HashMap();
       try {
           System.out.println("select records into the table...");
          System.out.println("Creating statement...");
      stmt = conn.createStatement();
     String selectsql = "SELECT REPORT_ID,NAME,DESCRIPTION,REPORT_FILE FROM sdi.report where NAME='report6'";
    // ps.setString(1, RName); 
     ResultSet rs = stmt.executeQuery(selectsql);
      
        rs.next();
        String ReportID=rs.getString("REPORT_ID");
        String ReportName=rs.getString("NAME");
         Blob last = rs.getBlob("REPORT_FILE");
         //Display values
         int blobLength = (int) last.length();  
        byte[] blobAsBytes = last.getBytes(1, blobLength);
    
     ByteArrayInputStream bis = new ByteArrayInputStream(blobAsBytes);
  
     JasperPrint jasperPrint = JasperFillManager.fillReport(bis,
            parameters, beanColDataSource);
 
//JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(bis, parameters, beanColDataSource);

JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFileName+ReportName+ReportID+".pdf");
String sql = "INSERT INTO report (PDF_EXPORT)"+"VALUES(?)";
      ps = conn.prepareStatement(sql);
//JasperViewer.viewReport(jprint,true);

//JasperViewer.viewReport(jasperPrint);
 
      rs.close();
      bis.close();
           System.out.println("Show records into the table...");
       } catch (SQLException | JRException | IOException ex) {
           Logger.getLogger(DBClass.class.getName()).log(Level.SEVERE, null, ex);
       } 
     }  */