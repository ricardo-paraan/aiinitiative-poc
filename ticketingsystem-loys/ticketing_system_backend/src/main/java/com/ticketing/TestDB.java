package com.ticketing;
import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/ticketing_system";
        String user = "postgres";
        String password = "12345678";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to PostgreSQL successfully!");

            String query = "SELECT * FROM public.ticket_status_tbl LIMIT 5";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    System.out.println(
                        rs.getInt("status_id") + " - " +
                        rs.getString("status_name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}