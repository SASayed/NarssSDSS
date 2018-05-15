/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.narss.sdss.dto.GeoLayer;

/**
 *
 * @author Sayed
 */
public interface GeoLayerDAO {
    public int addGeoLayer(GeoLayer geoLayer, Connection connection);
    public List<GeoLayer> getAllGeoLayers(Connection connection);
}
