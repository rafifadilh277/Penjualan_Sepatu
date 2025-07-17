package model;

import java.util.Date;

public class Transaksi {
    private int id;
    private Date tanggal;
    private Sepatu sepatu;
    private int jumlah;
    private double total;

    public Transaksi(int id, Date tanggal, Sepatu sepatu, int jumlah, double total) {
        this.id = id;
        this.tanggal = tanggal;
        this.sepatu = sepatu;
        this.jumlah = jumlah;
        this.total = total;
    }

    public Transaksi(Date tanggal, Sepatu sepatu, int jumlah, double total) {
        this.tanggal = tanggal;
        this.sepatu = sepatu;
        this.jumlah = jumlah;
        this.total = total;
    }

    public int getId() { return id; }
    public Date getTanggal() { return tanggal; }
    public Sepatu getSepatu() { return sepatu; }
    public int getJumlah() { return jumlah; }
    public double getTotal() { return total; }
}
