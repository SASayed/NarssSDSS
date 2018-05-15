/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.daoImpl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.narss.sdss.dao.MCDADao;
import org.narss.sdss.dto.MCDA;

/**
 *
 * @author Sayed
 */
public class MCDADaoImpl implements MCDADao{

    @Override
    public int addMCDA(MCDA mcda, Connection connection) {
        int numOfRows = 0;
        List<String> namesList = new ArrayList<String>();
        ResultSet rs = null;
        String testSQL = "SELECT * FROM mcda";
        String sql = "INSERT INTO MCDA "
                + "(id,name,description,algorithm,model) VALUES ("+mcda.getId()+",'" +mcda.getName()+"','"+mcda.getDescription()+"','"+mcda.getAlgorithm()+"',"+mcda.getModel()+")";
        try {
                Statement stm = null;
                stm = connection.createStatement();
                rs = stm.executeQuery(testSQL);
                rs.next();
                do 
                {
                    namesList.add(rs.getString("name"));
                } while(rs.next());
                rs.close();
                stm.close();
        }
        catch (SQLException exp) {
            Logger.getLogger(MCDADaoImpl.class.getName()).log(Level.SEVERE, null, exp);
        }
        
        try {
            Statement st = null;
            st = connection.createStatement();
            for(String name: namesList)
            {
                if(name.equalsIgnoreCase(mcda.getName()))
                    return numOfRows;
            }
            numOfRows = st.executeUpdate(sql);
            st.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(MCDADaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return numOfRows;
    }
    //--------------------------------------------------------------------------
    @Override
    public List<MCDA> getAllMCDAs(Connection connection) {
        List<MCDA> mcdaList = new ArrayList<MCDA>();
        String sql = "SELECT * FROM mcda";
        try{
            Statement st = null;
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                MCDA mcda = new MCDA();
                mcda.setId(rs.getInt("id"));
                mcda.setName(rs.getString("name"));
                mcda.setDescription(rs.getString("description"));
                mcda.setAlgorithm(rs.getString("algorithm"));
                mcda.setModel(rs.getInt("model"));
                mcdaList.add(mcda);
            } 
        } catch (SQLException ex) {
            Logger.getLogger(MCDADaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mcdaList;
    }
    //--------------------------------------------------------------------------
    @Override
    public MCDA getMCDAByName(Connection connection, String name)
    {
        String sql = "SELECT * FROM mcda WHERE name = '"+name+"'";
        MCDA mcda = new MCDA();
        try{
            connection.setAutoCommit(false);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                mcda.setId(rs.getInt("id"));
                mcda.setName(rs.getString("name"));
                mcda.setDescription(rs.getString("description"));
                mcda.setAlgorithm(rs.getString("algorithm"));
                mcda.setModel(rs.getInt("model"));
            }
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(MCDADaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mcda;
    }
    //--------------------------------------------------------------------------
    @Override
    public MCDA getMCDAById(Connection connection, int id) {
        String sql = "SELECT * FROM mcda WHERE id = " + id;
        MCDA mcda = new MCDA();
        try{
            connection.setAutoCommit(false);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next())
            {
                mcda.setId(rs.getInt("id"));
                mcda.setName(rs.getString("name"));
                mcda.setDescription(rs.getString("description"));
                mcda.setAlgorithm(rs.getString("algorithm"));
                mcda.setModel(rs.getInt("model"));
            }
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(MCDADaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mcda;
    }
    //--------------------------------------------------------------------------
    @Override
    public int updateMCDA(MCDA mcda, int id, Connection connection)
    {
        int numOfRows = 0;
        
        String sql = "UPDATE mcda SET name = '" + mcda.getName() +"', description = '" + mcda.getDescription() + "', algorithm = '" + mcda.getAlgorithm() + "', model = " + mcda.getModel()
                + " WHERE id = " + id;
        
        /*String sql = "UPDATE mcda SET (name,description,algorithm,model) = ('"+mcda.getName()+"','"+mcda.getDescription()+"','"+mcda.getAlgorithm()+"',"+mcda.getModel()+")"
                      + "WHERE id = " + id;*/
        try {
            Statement st = null;
            st = connection.createStatement();
            numOfRows = st.executeUpdate(sql);
            st.close();
                //connection.commit();
                connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(MCDADaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return numOfRows;
    }
    //--------------------------------------------------------------------------
    @Override
    public int deleteMCDA(String name, Connection connection)
    {
        int numOfRows = 0;
        String sql = "DELETE FROM mcda WHERE name = '" + name + "'";
        try {
            Statement st = null;
            st = connection.createStatement();
            numOfRows = st.executeUpdate(sql);
            st.close();
                //connection.commit();
                connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(MCDADaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
       return numOfRows;
    }
}