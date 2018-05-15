/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.util.List;
import org.narss.sdss.dto.Input;

/**
 *
 * @author Administrator
 */
public interface InputDAO {
    
    public List<Input> getAll();
    public List<Input> getInputs(int toolId);
    public Input getInputById(int input_Id);
}
