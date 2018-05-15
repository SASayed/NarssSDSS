/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
 *
 * @author heba
 */
public class Report {
   int Id;
    String Name;
    String Desc;
    private String Chart_type;
    int Dept;
   private File pdf_export;
   
   public Report() {
         Id=0;
         Name="";
         Desc="";
   
        pdf_export= new File("");
         
    }
 
    public Report(int Id, String Name, String Desc, int Dept) {
        this.Id = Id;
        this.Name = Name;
        this.Desc = Desc;
        this.Dept = Dept;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDescription() {
        return Desc;
    }

    public void setDescription(String Desc) {
        this.Desc = Desc;
    }

    public int getDept() {
        return Dept;
    }

    public void setDept(int Dept) {
        this.Dept = Dept;
    }

    /**
     * @return the pdf_export
     */
    public File getPdf_export() {
        return pdf_export;
    }

    /**
     * @param pdf_export the pdf_export to set
     */
    public void setPdf_export(File pdf_export) {
        this.pdf_export = pdf_export;
    } 

    /**
     * @return the Chart_type
     */
    public String getChart_type() {
        return Chart_type;
    }

    /**
     * @param Chart_type the Chart_type to set
     */
    public void setChart_type(String Chart_type) {
        this.Chart_type = Chart_type;
    }
}
