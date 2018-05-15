/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.util.List;
import org.narss.sdss.dto.Report;

/**
 *
 * @author Administrator
 */
public interface ReportDAO {
  //public void uploadreport(String Reportname,String ReportDescription,String ReportPath);
   public int add(Report report);
   public Report GetReportById(int id);
  public List<Report> getAll();
  public int update(Report report);
   public int delete(int id);
   public int updatePDF(Report report);
   public Report GetReportpdfById(int id);
   public Report GetReportByName(String name);
}
