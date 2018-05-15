/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.narss.sdss.dao.ColorDao;
import org.narss.sdss.dto.color;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;

/**
 *
 * @author Administrator
 */
public class ColorDaoImpl implements ColorDao {

    @Override
    public List<color> getAll() {
        List<color> colList = new ArrayList<color>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM color;");
            while (rs.next()) {
                color col = new color();
                col.setId(rs.getInt("id"));
                col.setName(rs.getString("name"));
                col.setCode(rs.getString("code"));
               // dept.setDescription(rs.getString("description"));
                
                colList.add(col);

                ////for test to remove later
                System.out.println("ID = " + col.getId());
                System.out.println("NAME = " + col.getName());
               // System.out.println("Description = " + dept.getDescription());
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return colList;
    }

    @Override
    public color GetColorByCode(String code) {
              color col = new color();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement(); 
            String sql ="SELECT * FROM color where code= ? ;";
             PreparedStatement ps = c.prepareStatement(sql);
                 
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                col.setId(rs.getInt("id"));
                col.setName(rs.getString("name"));
                 col.setCode(rs.getString("code"));
              
           
           }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return col;
    }
    
    
}
