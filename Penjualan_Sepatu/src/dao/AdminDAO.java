package dao;

import model.Admin;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminDAO {

    public Admin login(String username, String password) throws Exception {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                            rs.getInt("id_admin"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            throw new Exception("Gagal login: " + e.getMessage());
        }
    }
}
