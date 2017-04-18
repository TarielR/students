package dao;

import java.sql.*;

public class TestJDBC {
    public static void main(String[] args) {
        System.out.println("Copyright 2017, Tariel");
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("org.h2.Driver");
            String url = "jdbc:h2:file:E:/proj/WebApp/lib.mv.db";
            con = DriverManager.getConnection(url, "sa", "");
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM STUDENTS");
            while (rs.next()) {
                String str = rs.getString(1) + ": " + rs.getString(2) + " " + rs.getString(3);
                System.out.println(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Эта часть позволяет нам закрыть все открытые ресуры
            // В противном случае возмжожны проблемы. Поэтому будьте
            // всегда аккуратны при работе с коннектами
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }
}
