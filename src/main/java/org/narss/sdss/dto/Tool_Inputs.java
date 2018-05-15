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
public class Tool_Inputs {

    private int id;
    private int tool_id;
    private int input_id;

    public Tool_Inputs() {
        id = 0;
        tool_id = 0;
        input_id = 0;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getToolId() {
        return tool_id;
    }

    public void setToolId(int tool_id) {
        this.tool_id = tool_id;
    }

    public int getInputId() {
        return input_id;
    }

    public void setInputId(int input_id) {
        this.input_id = input_id;
    }
}
