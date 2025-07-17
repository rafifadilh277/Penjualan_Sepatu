package utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import model.Transaksi;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.text.DecimalFormat;

public class PDFExporter {

    public static void exportTransaksi(List<Transaksi> transaksiList) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("transaksi.pdf"));
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph title = new Paragraph("Laporan Transaksi Penjualan Sepatu", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new int[]{2, 3, 5, 2, 3});
        table.setWidthPercentage(100);

        addHeader(table, "ID");
        addHeader(table, "Tanggal");
        addHeader(table, "Sepatu");
        addHeader(table, "Jumlah");
        addHeader(table, "Total");

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        DecimalFormat rp = new DecimalFormat("'Rp'#,##0");

        for (Transaksi t : transaksiList) {
            table.addCell(String.valueOf(t.getId()));
            table.addCell(sdf.format(t.getTanggal()));
            table.addCell(t.getSepatu().getNama());
            table.addCell(String.valueOf(t.getJumlah()));
            table.addCell(rp.format(t.getTotal()));
        }

        document.add(table);
        document.close();
    }

    private static void addHeader(PdfPTable table, String title) {
        PdfPCell header = new PdfPCell();
        header.setPhrase(new Phrase(title));
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(header);
    }
}
