/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.sql.Connection;
import java.util.List;
import org.narss.sdss.dto.MCDA;

/**
 *
 * @author Sayed
 */
public interface MCDADao {
    public int addMCDA(MCDA mcda, Connection connection);
    public List<MCDA> getAllMCDAs(Connection connection);
    public MCDA getMCDAByName(Connection connection, String name);
    public MCDA getMCDAById(Connection connection, int id);
    public int updateMCDA(MCDA mcda, int id, Connection connection);
    public int deleteMCDA(String name, Connection connection);
}
