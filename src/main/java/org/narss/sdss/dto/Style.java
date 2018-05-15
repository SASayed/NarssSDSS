/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dto;

import java.io.File;

/**
 *
 * @author Administrator
 */
public class Style {


    private int id;
    private String name;
    private String description;
    private String datatype;
    private String lowvalcolor;
    private String midvalcolor;
    private String highvalcolor;
    private File file;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the datatype
     */
    public String getDatatype() {
        return datatype;
    }

    /**
     * @param datatype the datatype to set
     */
    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }
        /**
     * @return the lowvalcolor
     */
    public String getLowvalcolor() {
        return lowvalcolor;
    }

    /**
     * @param lowvalcolor the lowvalcolor to set
     */
    public void setLowvalcolor(String lowvalcolor) {
        this.lowvalcolor = lowvalcolor;
    }

    /**
     * @return the midvalcolor
     */
    public String getMidvalcolor() {
        return midvalcolor;
    }

    /**
     * @param midvalcolor the midvalcolor to set
     */
    public void setMidvalcolor(String midvalcolor) {
        this.midvalcolor = midvalcolor;
    }

    /**
     * @return the highvalcolor
     */
    public String getHighvalcolor() {
        return highvalcolor;
    }

    /**
     * @param highvalcolor the highvalcolor to set
     */
    public void setHighvalcolor(String highvalcolor) {
        this.highvalcolor = highvalcolor;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }
}
