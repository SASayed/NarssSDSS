/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.postgreSQL;

import org.narss.sdss.dto.Model;
import java.sql.Connection;
import java.sql.Statement;

/**
 *
 * @author Administrator
 */
public class ModelJDBC {
   // public static void main(String args[]) {}
    public void insertModel(Model model) {
        Connection c = PostgreSQLJDBC.Connect();
        Statement stmt = null;
        if (c != null) {
            try {
                c.setAutoCommit(false);
                stmt = c.createStatement();
                String sql = "INSERT INTO MODEL ( name,description,has_mcda) "
                        + "VALUES ('"+model.getName()+"', '"+model.getDescription()+"',"+model.getHasMcda()+");";
                stmt.executeUpdate(sql);
                stmt.close();
                c.commit();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }
    }
}
