/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.daoImpl;

import org.narss.sdss.dao.UsersDAO;
import org.narss.sdss.dto.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;
/**
 *
 * @author Administrator
 */
public class UserDaoImpl implements UsersDAO{

    @Override
    public Boolean SelectSpecificUser(String name,String password,String role) {
        User user = new User();
        Connection c = null;
        Statement stmt = null;
        Boolean exist=false;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement(); 
            String sql ="SELECT * FROM usersdss where name= ? and password=? and role=?;";
             PreparedStatement ps = c.prepareStatement(sql);
                 
            ps.setString(1, name);
            ps.setString(2, password);
            ps.setString(3, role);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) 
            {
                user.setId(rs.getInt("id"));
                exist=true;
             
            }
            rs.close();
            stmt.close();
            c.close();
            
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
      }
        System.out.println("Operation done successfully");
       
        return exist;
    }

    @Override
    public List<User> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
