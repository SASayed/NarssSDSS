/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.daoImpl;

import Manager.LayerManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.narss.sdss.dao.GeoLayerDAO;
import org.narss.sdss.dto.GeoLayer;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;

/**
 *
 * @author Sayed
 */
public class GeoLayerDAOImpl implements GeoLayerDAO{

    @Override
    public int addGeoLayer(GeoLayer geoLayer, Connection connection) {
        String sql = "INSERT INTO geolayer(id, name,type) VALUES("+geoLayer.getId()+",'"+geoLayer.getName()+"','"+geoLayer.getType()+"')";
        int rowCount = 0;
        try
        {
            Statement st = connection.createStatement();
            rowCount = st.executeUpdate(sql);
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(GeoLayerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rowCount;
    }
    //--------------------------------------------------------------------------
    @Override
    public List<GeoLayer> getAllGeoLayers(Connection connection) {
        String sql = "SELECT * FROM geolayer";
        Statement st = null;
        List<GeoLayer> geoLayers = new ArrayList<GeoLayer>();
        try{
            st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                GeoLayer geoLayer = new GeoLayer();
                geoLayer.setId(rs.getInt("id"));
                geoLayer.setName(rs.getString("name"));
                geoLayer.setType(rs.getString("type"));
                geoLayers.add(geoLayer);
            }
            rs.close();
            st.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(GeoLayerDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return geoLayers;
    }
    //--------------------------------------------------------------------------
    public static void main(String[] args)
    {
        GeoLayerDAOImpl g = new GeoLayerDAOImpl();
        Connection conn = PostgreSQLJDBC.Connect();
        LayerManager layerManger = new LayerManager();
        List<String> geoLayersNames = layerManger.getGeoLayers("http://localhost:8080/geoserver/rest/", "narss", "json");
        List<GeoLayer> geoLayers = new ArrayList<GeoLayer>();
        for(int i=0; i<geoLayersNames.size(); i++)
        {
            GeoLayer geoLayer = new GeoLayer();
            geoLayer.setId(i+1);
            geoLayer.setName(geoLayersNames.get(i));
            geoLayers.add(geoLayer);
            g.addGeoLayer(geoLayer, conn);
        }
    }
}