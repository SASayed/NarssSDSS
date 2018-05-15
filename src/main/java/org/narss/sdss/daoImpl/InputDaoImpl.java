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
import org.narss.sdss.dao.InputDAO;
import org.narss.sdss.dto.Input;
import org.narss.sdss.postgreSQL.PostgreSQLJDBC;

/**
 *
 * @author heba
 */
public class InputDaoImpl implements InputDAO {

    @Override
    public List<Input> getAll() {
        List<Input> inputList = new ArrayList<Input>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM input;");
            while (rs.next()) {
                Input input = new Input();
                input.setId(rs.getInt("id"));
                input.setName(rs.getString("name"));
                input.setType(rs.getString("type"));
                inputList.add(input);

                ////for test to remove later
                System.out.println("ID = " + input.getId());
                System.out.println("NAME = " + input.getName());
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
        return inputList;
    }

    @Override
    public List<Input> getInputs(int toolId) {
        List<Input> inputs = new ArrayList<Input>();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            String sql = "SELECT * FROM tool_inputs WHERE tool_id = ? ";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, toolId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Input input = new Input();
                input.setId(rs.getInt("Input_id"));

                String sql2 = "SELECT * FROM input WHERE id = ? ";
                PreparedStatement ps2 = c.prepareStatement(sql2);
                ps2.setInt(1, input.getId());
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    input.setName(rs2.getString("name"));
                    input.setType(rs2.getString("type"));
                }
                rs2.close();
                ps2.close();
                inputs.add(input);
                // use the data in some way here
                System.out.println("ID = " + input.getId());
                System.out.println("NAME = " + input.getName());
                System.out.println("TYPE = " + input.getType());
            }
            rs.close();
            ps.close();

            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return inputs;
    }

    @Override
    public Input getInputById(int input_Id) {
        Input input = new Input();
        Connection c = null;
        Statement stmt = null;
        try {
            c = PostgreSQLJDBC.Connect();
            String sql = "SELECT * FROM input WHERE id = ? ";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, input_Id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                input.setId(rs.getInt("id"));
                input.setName(rs.getString("name"));
                input.setType(rs.getString("type"));

                ////for test to remove later
                System.out.println("ID = " + input.getId());
                System.out.println("NAME = " + input.getName());
                System.out.println("type = " + input.getType());
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
        return input;
    }
}
