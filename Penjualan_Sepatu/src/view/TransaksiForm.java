package view;

import dao.SepatuDAO;
import dao.TransaksiDAO;
import model.Sepatu;
import model.Transaksi;
import utils.PDFExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.DecimalFormat;

public class TransaksiForm extends JFrame {
    private JComboBox<Sepatu> cbSepatu;
    private JTextField txtJumlah;
    private JLabel lblStok, lblHarga;
    private JTable table;
    private DefaultTableModel tableModel;

    private SepatuDAO sepatuDAO = new SepatuDAO();
    private TransaksiDAO transaksiDAO = new TransaksiDAO();

    public TransaksiForm() {
        setTitle("Transaksi Penjualan Sepatu");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelInput = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInput.add(new JLabel("Sepatu:"));
        cbSepatu = new JComboBox<>();
        panelInput.add(cbSepatu);

        panelInput.add(new JLabel("Stok Tersedia:"));
        lblStok = new JLabel("0");
        panelInput.add(lblStok);

        panelInput.add(new JLabel("Harga per Unit:"));
        lblHarga = new JLabel("Rp 0");
        panelInput.add(lblHarga);

        panelInput.add(new JLabel("Jumlah:"));
        txtJumlah = new JTextField();
        panelInput.add(txtJumlah);

        JButton btnTambah = new JButton("Tambah Transaksi");
        btnTambah.addActionListener(e -> tambahTransaksi());

        JButton btnCetakPDF = new JButton("Cetak PDF");
        btnCetakPDF.addActionListener(e -> cetakPDF());

        JButton btnKembali = new JButton("Kembali");
        btnKembali.addActionListener(e -> {
            new MainMenu().setVisible(true);
            dispose();
        });

        JPanel panelButton = new JPanel();
        panelButton.add(btnTambah);
        panelButton.add(btnCetakPDF);
        panelButton.add(btnKembali);

        tableModel = new DefaultTableModel(new String[]{"ID", "Tanggal", "Sepatu", "Jumlah", "Total"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(panelInput, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);

        loadSepatu();
        loadTransaksi();
        cbSepatu.addActionListener(e -> updateInfoSepatu());
    }

    private void loadSepatu() {
        try {
            cbSepatu.removeAllItems();
            for (Sepatu s : sepatuDAO.getAll()) {
                cbSepatu.addItem(s);
            }
            updateInfoSepatu();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load sepatu: " + e.getMessage());
        }
    }

    private void updateInfoSepatu() {
        Sepatu s = (Sepatu) cbSepatu.getSelectedItem();
        if (s != null) {
            lblStok.setText(String.valueOf(s.getStok()));
            lblHarga.setText(formatRupiah(s.getHarga()));
        }
    }

    private void tambahTransaksi() {
        try {
            Sepatu s = (Sepatu) cbSepatu.getSelectedItem();
            int jumlah = Integer.parseInt(txtJumlah.getText());
            if (jumlah <= 0 || jumlah > s.getStok()) {
                JOptionPane.showMessageDialog(this, "Jumlah tidak valid!");
                return;
            }

            double total = jumlah * s.getHarga();
            Transaksi t = new Transaksi(new Date(), s, jumlah, total);

            transaksiDAO.add(t);
            JOptionPane.showMessageDialog(this, "Transaksi berhasil!");
            loadSepatu();
            loadTransaksi();
            txtJumlah.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal tambah transaksi: " + ex.getMessage());
        }
    }

    private void loadTransaksi() {
        try {
            tableModel.setRowCount(0);
            List<Transaksi> list = transaksiDAO.getAll();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            for (Transaksi t : list) {
                tableModel.addRow(new Object[]{
                        t.getId(),
                        sdf.format(t.getTanggal()),
                        t.getSepatu().getNama(),
                        t.getJumlah(),
                        formatRupiah(t.getTotal())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal load transaksi: " + e.getMessage());
        }
    }

    private String formatRupiah(double angka) {
        DecimalFormat df = new DecimalFormat("#,###");
        return "Rp" + df.format(angka);
    }

    private void cetakPDF() {
        try {
            List<Transaksi> list = transaksiDAO.getAll();
            PDFExporter.exportTransaksi(list);
            JOptionPane.showMessageDialog(this, "Laporan PDF berhasil disimpan sebagai transaksi.pdf");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal ekspor PDF: " + e.getMessage());
        }
    }
}
