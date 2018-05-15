/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dto;

/**
 *
 * @author wh_sayed
 */
public class MCDA {
    private int id;
    private String name;
    private String description;
    private String algorithm;
    private int model;

    public MCDA(){
       id = 0;
        name = "";
        description = "";
        algorithm = "";
       model = 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
    
    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }
}
