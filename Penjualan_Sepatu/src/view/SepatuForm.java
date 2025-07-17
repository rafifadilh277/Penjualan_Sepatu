package view;

import dao.SepatuDAO;
import model.Sepatu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.text.DecimalFormat;

public class SepatuForm extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNama, txtHarga, txtStok;
    private SepatuDAO sepatuDAO = new SepatuDAO();

    public SepatuForm() {
        setTitle("Kelola Sepatu");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nama", "Harga", "Stok"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput.setBorder(BorderFactory.createTitledBorder("Data Sepatu"));

        panelInput.add(new JLabel("Nama Sepatu:"));
        txtNama = new JTextField();
        panelInput.add(txtNama);

        panelInput.add(new JLabel("Harga Sepatu:"));
        txtHarga = new JTextField();
        panelInput.add(txtHarga);

        panelInput.add(new JLabel("Stok Sepatu:"));
        txtStok = new JTextField();
        panelInput.add(txtStok);

        JButton btnTambah = new JButton("Tambah");
        btnTambah.addActionListener(e -> tambahSepatu());
        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(e -> editSepatu());
        JButton btnHapus = new JButton("Hapus");
        btnHapus.addActionListener(e -> hapusSepatu());
        JButton btnKembali = new JButton("Kembali");
        btnKembali.addActionListener(e -> {
            new MainMenu().setVisible(true);
            dispose();
        });

        JPanel panelButton = new JPanel();
        panelButton.add(btnTambah);
        panelButton.add(btnEdit);
        panelButton.add(btnHapus);
        panelButton.add(btnKembali);

        add(scrollPane, BorderLayout.CENTER);
        add(panelInput, BorderLayout.NORTH);
        add(panelButton, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                loadSelectedSepatu();
            }
        });

        loadData();
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            List<Sepatu> list = sepatuDAO.getAll();
            for (Sepatu s : list) {
                tableModel.addRow(new Object[]{
                        s.getId(),
                        s.getNama(),
                        formatRupiah(s.getHarga()), 
                        s.getStok()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load data sepatu: " + e.getMessage());
        }
    }

    private String formatRupiah(double angka) {
        DecimalFormat df = new DecimalFormat("#,###");
        return "Rp" + df.format(angka);
    }

    private void tambahSepatu() {
        try {
            String nama = txtNama.getText().trim();
            String hargaStr = txtHarga.getText().trim().replace(".", "").replace(",", "");
            String stokStr = txtStok.getText().trim();

            if (nama.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
                return;
            }

            double harga = Double.parseDouble(hargaStr);
            int stok = Integer.parseInt(stokStr);

            if (harga <= 0 || stok < 0) {
                JOptionPane.showMessageDialog(this, "Harga harus > 0 dan stok tidak boleh negatif!");
                return;
            }

            Sepatu s = new Sepatu(0, nama, harga, stok);
            sepatuDAO.add(s);

            JOptionPane.showMessageDialog(this, "Sepatu berhasil ditambahkan!");
            clearFields();
            loadData();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan stok harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal tambah sepatu: " + e.getMessage());
        }
    }

    private void editSepatu() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih sepatu yang akan diedit!");
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nama = txtNama.getText().trim();
            String hargaStr = txtHarga.getText().trim().replace(".", "").replace(",", "");
            String stokStr = txtStok.getText().trim();

            if (nama.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
                return;
            }

            double harga = Double.parseDouble(hargaStr);
            int stok = Integer.parseInt(stokStr);

            if (harga <= 0 || stok < 0) {
                JOptionPane.showMessageDialog(this, "Harga harus > 0 dan stok tidak boleh negatif!");
                return;
            }

            Sepatu s = new Sepatu(id, nama, harga, stok);
            sepatuDAO.update(s);

            JOptionPane.showMessageDialog(this, "Sepatu berhasil diupdate!");
            clearFields();
            loadData();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal edit sepatu: " + e.getMessage());
        }
    }

    private void hapusSepatu() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih sepatu yang akan dihapus!");
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus sepatu ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                sepatuDAO.delete(id);
                JOptionPane.showMessageDialog(this, "Sepatu berhasil dihapus!");
                clearFields();
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal hapus sepatu: " + e.getMessage());
        }
    }

    private void loadSelectedSepatu() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            txtNama.setText((String) tableModel.getValueAt(selectedRow, 1));
            String hargaText = tableModel.getValueAt(selectedRow, 2).toString().replace("Rp", "").replace(".", "").replace(",", "");
            txtHarga.setText(hargaText);
            txtStok.setText(tableModel.getValueAt(selectedRow, 3).toString());
        }
    }

    private void clearFields() {
        txtNama.setText("");
        txtHarga.setText("");
        txtStok.setText("");
        table.clearSelection();
    }
}
