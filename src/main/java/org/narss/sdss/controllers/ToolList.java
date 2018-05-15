/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.controllers;

import org.narss.sdss.dto.Tool;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class ToolList {
    
    private List<Tool> toolList;

    public List<Tool> getToolList() {
        return toolList;
    }

    public void setToolList(List<Tool> toolList) {
        this.toolList = toolList;
    }
}
