/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class Tool {

    private int id;
    private String name;
    private File file;
    private List<Input> inputs;
    private int countOfInputLayers;
    private int countOfOutputLayers;
    //private Boolean hasSubmodel;

    public Tool() {
        id = 0;
        name = "";
        file = new File("");
        inputs = new ArrayList<Input>();
        countOfInputLayers = 0;
        countOfOutputLayers = 0;
        //hasSubmodel=false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void setInputs(List<Input> list) {
        this.inputs = list;
    }

    public int getCountOfInputLayers() {
        return countOfInputLayers;
    }

    public void setCountOfInputLayers(int count) {
        this.countOfInputLayers = count;
    }

    public int getCountOfoutputLayer() {
        return countOfOutputLayers;
    }

    public void setCountOfoutputLayer(int count) {
        this.countOfOutputLayers = count;
    }
   /* public void setSubmodel(Boolean submodel) {
        this.hasSubmodel = submodel;
    }
    public Boolean getSubmodel() {
        return hasSubmodel;
    }*/
}
