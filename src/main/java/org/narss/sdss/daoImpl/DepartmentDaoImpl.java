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
import org.narss.sdss.dao.DepartmentDAO;
import org.narss.sdss.dto.Department;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;

/**
 *
 * @author HEBA
 */
public class DepartmentDaoImpl implements DepartmentDAO {

    @Override
    public List<Department> getAll() {
         List<Department> deptList = new ArrayList<Department>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM department;");
            while (rs.next()) {
                Department dept = new Department();
                dept.setId(rs.getInt("id"));
                dept.setName(rs.getString("name"));
               // dept.setDescription(rs.getString("description"));
                
                deptList.add(dept);

                ////for test to remove later
                System.out.println("ID = " + dept.getId());
                System.out.println("NAME = " + dept.getName());
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
        return deptList;
    }
    @Override
    public Department GetDeptById(int id) {
         Department dept = new Department();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement(); 
            String sql ="SELECT * FROM department where id= ? ;";
             PreparedStatement ps = c.prepareStatement(sql);
                 
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                
                dept.setId(rs.getInt("id"));
                dept.setName(rs.getString("name"));
              
           
           }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return dept;
       
    }
}
