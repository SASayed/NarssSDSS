/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.util.List;
import org.narss.sdss.dto.Tool;

/**
 *
 * @author Sayed
 */
public interface ToolDAO {

    public List<Tool> getAll();
    public Tool getByName(String toolname);
    public Tool getById(int id);
    public int getNoOfInputsLayers(String toolname);
}
