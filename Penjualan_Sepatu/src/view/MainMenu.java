    package view;

    import javax.swing.*;
    import java.awt.*;

    public class MainMenu extends JFrame {
        public MainMenu() {
            setTitle("Main Menu");
            setSize(400, 300);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

            JButton btnSepatu = new JButton("Kelola Sepatu");
            btnSepatu.addActionListener(e -> {
                new SepatuForm().setVisible(true);
                dispose();
            });

            JButton btnTransaksi = new JButton("Transaksi");
            btnTransaksi.addActionListener(e -> {
                new TransaksiForm().setVisible(true);
                dispose();
            });

            JButton btnLogout = new JButton("Logout");
            btnLogout.addActionListener(e -> {
                new LoginForm().setVisible(true);
                dispose();
            });

            JButton btnKeluar = new JButton("Keluar");
            btnKeluar.addActionListener(e -> System.exit(0));

            panel.add(btnSepatu);
            panel.add(btnTransaksi);
            panel.add(btnLogout);
            panel.add(btnKeluar);

            add(panel);
        }
    }
