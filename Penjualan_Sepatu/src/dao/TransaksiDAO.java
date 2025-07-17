package dao;

import model.Sepatu;
import model.Transaksi;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaksiDAO {
    public void add(Transaksi t) throws SQLException {
        String sqlInsert = "INSERT INTO transaksi(tanggal, id_sepatu, jumlah, total) VALUES (?, ?, ?, ?)";
        String sqlUpdateStok = "UPDATE sepatu SET stok = stok - ? WHERE id_sepatu = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
             PreparedStatement psUpdateStok = conn.prepareStatement(sqlUpdateStok)) {

            conn.setAutoCommit(false);

            psInsert.setDate(1, new java.sql.Date(t.getTanggal().getTime()));
            psInsert.setInt(2, t.getSepatu().getId());
            psInsert.setInt(3, t.getJumlah());
            psInsert.setDouble(4, t.getTotal());
            psInsert.executeUpdate();

            psUpdateStok.setInt(1, t.getJumlah());
            psUpdateStok.setInt(2, t.getSepatu().getId());
            psUpdateStok.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            throw new SQLException("Gagal menyimpan transaksi: " + e.getMessage(), e);
        }
    }

    public List<Transaksi> getAll() throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT t.id_transaksi, t.tanggal, t.jumlah, t.total, " +
                     "s.id_sepatu, s.nama, s.harga, s.stok " +
                     "FROM transaksi t JOIN sepatu s ON t.id_sepatu = s.id_sepatu";
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
                Transaksi t = new Transaksi(
                        rs.getInt("id_transaksi"),
                        rs.getDate("tanggal"),
                        s,
                        rs.getInt("jumlah"),
                        rs.getDouble("total")
                );
                list.add(t);
            }
        }
        return list;
    }
}
