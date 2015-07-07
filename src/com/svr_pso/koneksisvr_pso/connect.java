/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svr_pso.koneksisvr_pso;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nuhuyanan
 */
public class connect {

    Statement statmen;
    Connection koneksi;
    public connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/peramalan_tma", "root", "nuhuyanan");
            statmen = koneksi.createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(connect.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet getTable(String q) throws SQLException {
        return statmen.executeQuery(q);
    }
    
    public int updateData(String s) throws SQLException{
        return statmen.executeUpdate(s);
    }
            
    public PreparedStatement pS(String s) throws SQLException  {
        return koneksi.prepareStatement(s);
    }

    
    public boolean updateTable(String q) throws SQLException {
        return statmen.execute(q);
    }

    public boolean isExists(String q) throws SQLException {
        ResultSet rs = statmen.executeQuery(q);
        int i = 0;
        while (rs.next()) {
            i++;
        }
        return (i != 0);
    }
}