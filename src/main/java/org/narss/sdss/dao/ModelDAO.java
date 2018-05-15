/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.io.File;
import java.sql.Connection;
import java.util.List;
import org.narss.sdss.dto.Model;

/**
 *
 * @author Sayed
 */
public interface ModelDAO {
    public int add(Model model);
    public List<Model> getAll();
    public Model getModelById(int id);
    public Model getModelByName( String name );
    public int update(Model model,String filename);
    public int update_resultlayer(Model model, String layer);
    public File getFileByModelId(int modelId);
    public int delete(int id);
}
