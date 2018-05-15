/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.sql.Connection;
import java.util.List;
import org.narss.sdss.dto.Model_Tool_Inputs;

/**
 *
 * @author Sayed
 */
public interface Model_Tool_InputsDAO {
    
    public int add(Model_Tool_Inputs modelToolInputs);
    public List<Model_Tool_Inputs> getModelToolsInputByModelId(int modelId);
    public int delete(int modelid);
    public int deleteTool(int toolId,int modelId);
    public int countModelToolsByModelId(int modelId);
    public boolean checkExistOfLayer(int modelId,int tool, String Layer);
    public List<Model_Tool_Inputs> getModelToolsInputByToolid(int toolId,int modelId);
}
