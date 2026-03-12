package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Login screen shown at startup.
 * Admin gets full access. Customer can only Register and Rent/Return.
 */
public class LoginForm extends JFrame {

    // ── Simple hardcoded admin password (can be moved to DB later) ──────────────
    private static final String ADMIN_PASSWORD = "admin123";

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;

    public LoginForm() {
        initUI();
    }

    private void initUI() {
        setTitle("Car Rental System — Login");
        setSize(420, 380);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(18, 22, 35));
        setContentPane(root);

        // ── Header ────────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new GridLayout(3, 1));
        header.setBackground(new Color(12, 15, 25));
        header.setPreferredSize(new Dimension(0, 100));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 180, 160)));

        JLabel icon  = new JLabel("🚗", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 28));

        JLabel title = new JLabel("Car Rental Management System", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(0, 220, 200));

        JLabel sub = new JLabel("Please select your role and log in", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(new Color(100, 120, 150));

        header.add(icon);
        header.add(title);
        header.add(sub);
        root.add(header, BorderLayout.NORTH);

        // ── Form ─────────────────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(18, 22, 35));
        form.setBorder(BorderFactory.createEmptyBorder(28, 44, 10, 44));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        // Role selector
        String[] roles = {"👤  Customer", "🔐  Admin"};
        roleBox = new JComboBox<>(roles);
        roleBox.setBackground(new Color(22, 28, 44));
        roleBox.setForeground(Color.WHITE);
        roleBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        roleBox.setBorder(BorderFactory.createLineBorder(new Color(60, 80, 110)));
        addRow(form, gbc, 0, "Login As:", roleBox);

        // Password (only needed for admin)
        passwordField = new JPasswordField();
        styleField(passwordField);
        addRow(form, gbc, 1, "Password:", passwordField);

        // Password hint
        JLabel hint = new JLabel("  Password required for Admin only");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 10));
        hint.setForeground(new Color(80, 100, 130));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        form.add(hint, gbc);
        gbc.gridwidth = 1;

        root.add(form, BorderLayout.CENTER);

        // Show/hide password field based on role
        roleBox.addActionListener(e -> {
            boolean isAdmin = roleBox.getSelectedIndex() == 1;
            passwordField.setEnabled(isAdmin);
            passwordField.setBackground(isAdmin ? new Color(22, 28, 44) : new Color(14, 18, 28));
        });
        // Default: customer selected, disable password
        passwordField.setEnabled(false);
        passwordField.setBackground(new Color(14, 18, 28));

        // ── Buttons ───────────────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 16));
        btnPanel.setBackground(new Color(18, 22, 35));

        JButton loginBtn = makeBtn("Login →", new Color(0, 140, 120));
        JButton exitBtn  = makeBtn("Exit",    new Color(100, 40, 40));

        loginBtn.addActionListener(e -> doLogin());
        exitBtn.addActionListener(e -> System.exit(0));

        // Allow Enter key to trigger login
        passwordField.addActionListener(e -> doLogin());

        btnPanel.add(loginBtn);
        btnPanel.add(exitBtn);
        root.add(btnPanel, BorderLayout.SOUTH);
    }

    // ── Login logic ──────────────────────────────────────────────────────────────

    private void doLogin() {
        boolean isAdmin = roleBox.getSelectedIndex() == 1;

        if (isAdmin) {
            String password = new String(passwordField.getPassword());
            if (!ADMIN_PASSWORD.equals(password)) {
                JOptionPane.showMessageDialog(this,
                    "Incorrect admin password!\nHint: admin123",
                    "Access Denied", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                return;
            }
        }

        // Login successful — open dashboard with appropriate role
        dispose();
        SwingUtilities.invokeLater(() -> {
            Dashboard dashboard = new Dashboard(isAdmin);
            dashboard.setVisible(true);
        });
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(new Color(170, 185, 210));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
        panel.add(field, gbc);
    }

    private void styleField(JComponent f) {
        f.setBackground(new Color(22, 28, 44));
        f.setForeground(Color.WHITE);
        if (f instanceof JTextField) ((JTextField)f).setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 80, 110)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
