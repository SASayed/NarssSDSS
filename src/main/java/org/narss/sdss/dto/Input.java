/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dto;


/**
 *
 * @author Administrator
 */
public class Input {

    private int id;
    private String name;
    private String type;
    private String placeholder;
    private String value;
    private String submodel;

    public Input() {
        id = 0;
        name = "";
        type = "";
        placeholder = "";
        value = "";
        submodel = "";
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSubmodel() {
        return submodel;
    }

    public void setSubmodel(String modelId) {
        this.submodel = modelId;
    }

}
