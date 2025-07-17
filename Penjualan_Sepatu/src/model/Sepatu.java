package model;

import java.text.NumberFormat;
import java.util.Locale;

public class Sepatu {
    private int id;
    private String nama;
    private double harga;
    private int stok;

    public Sepatu(int id, String nama, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public int getId() { return id; }
    public String getNama() { return nama; }
    public double getHarga() { return harga; }
    public int getStok() { return stok; }

    public void setStok(int stok) { this.stok = stok; }

    @Override
    public String toString() {
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return nama + " (" + formatRupiah.format(harga) + ")";
    }
}
