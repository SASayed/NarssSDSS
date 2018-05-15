/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.util.List;
import org.narss.sdss.dto.Input;
import org.narss.sdss.dto.Tool_Inputs;

/**
 *
 * @author Sayed
 */
public interface Tool_InputDAO {

    public List<Tool_Inputs> getByIds(int toolId, int inputId);

    public List<Input> getInputs(int toolId);

}
