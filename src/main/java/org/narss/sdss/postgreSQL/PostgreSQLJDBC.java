/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.narss.sdss.postgreSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import org.narss.sdss.controllers.Properties;

/**
 *
 * @author Administrator
 */
public class PostgreSQLJDBC {

    public static Connection Connect() {
        Connection c = null;
        try {
            Class.forName(Properties.DRIVER);
            c = DriverManager
                    .getConnection(Properties.DATABASE_URL+Properties.DATABASE_NAME,
                            Properties.DATABASE_USERNAME, Properties.DATABASE_PASSWORD);
            System.out.println("Opened database successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        return c;
    }
}
