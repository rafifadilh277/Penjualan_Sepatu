package dao;

import model.Sepatu;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SepatuDAO {
    public List<Sepatu> getAll() throws SQLException {
        List<Sepatu> list = new ArrayList<>();
        String sql = "SELECT * FROM sepatu";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Sepatu s = new Sepatu(
                        rs.getInt("id_sepatu"),
                        rs.getString("nama"),
                        rs.getDouble("harga"),
                        rs.getInt("stok")
                );
                list.add(s);
            }
        }
        return list;
    }

public void add(Sepatu s) throws SQLException {
    String sql = "INSERT INTO sepatu(nama, harga, stok) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, s.getNama());
        ps.setDouble(2, s.getHarga());
        ps.setInt(3, s.getStok());
        ps.executeUpdate();
    }
}
public void update(Sepatu s) throws Exception {
    String sql = "UPDATE sepatu SET nama=?, harga=?, stok=? WHERE id_sepatu=?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, s.getNama());
        ps.setDouble(2, s.getHarga());
        ps.setInt(3, s.getStok());
        ps.setInt(4, s.getId());
        ps.executeUpdate();
    }
}
public void delete(int id) throws Exception {
    String sql = "DELETE FROM sepatu WHERE id_sepatu=?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, id);
        ps.executeUpdate();
    }
}
}
