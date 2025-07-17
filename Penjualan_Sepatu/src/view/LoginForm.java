package view;

import dao.AdminDAO;
import model.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private AdminDAO adminDAO = new AdminDAO();

    public LoginForm() {
        setTitle("Login Admin");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panel.add(txtUsername);

        panel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Login");
        JButton btnExit = new JButton("Keluar");

        btnLogin.addActionListener(this::login);
        btnExit.addActionListener(e -> System.exit(0));

        panel.add(btnLogin);
        panel.add(btnExit);

        add(panel);
    }

    private void login(ActionEvent e) {
        String username = txtUsername.getText();
        String password = String.valueOf(txtPassword.getPassword());
        try {
            Admin admin = adminDAO.login(username, password);
            if (admin != null) {
                JOptionPane.showMessageDialog(this, "Login berhasil!");
                new MainMenu().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username/password salah!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}
