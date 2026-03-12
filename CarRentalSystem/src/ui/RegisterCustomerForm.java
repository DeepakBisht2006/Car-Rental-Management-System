package ui;

import model.Customer;
import service.CustomerService;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import database.DBConnection;

/**
 * Form for registering a new customer.
 * After registration, displays the assigned Customer ID prominently.
 */
public class RegisterCustomerForm extends JFrame {

    private final CustomerService customerService = new CustomerService();

    private JTextField nameField;
    private JTextField phoneField;
    private JPanel     resultPanel;
    private JLabel     resultLabel;

    public RegisterCustomerForm() {
        initUI();
    }

    private void initUI() {
        setTitle("Register Customer");
        setSize(440, 360);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(30, 36, 48));
        setContentPane(root);

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(22, 27, 40));
        header.setPreferredSize(new Dimension(0, 55));
        JLabel lbl = new JLabel("Register New Customer");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(new Color(160, 120, 255));
        header.add(lbl);
        root.add(header, BorderLayout.NORTH);

        // Form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(30, 36, 48));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        nameField  = new JTextField();
        phoneField = new JTextField();
        addRow(form, gbc, 0, "Full Name:", nameField);
        addRow(form, gbc, 1, "Phone:",     phoneField);

        root.add(form, BorderLayout.CENTER);

        // Result panel — shown after successful registration
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(new Color(15, 40, 25));
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(0, 180, 100)),
            BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));
        resultPanel.setVisible(false);

        JLabel successIcon = new JLabel("Registration Successful!", SwingConstants.CENTER);
        successIcon.setFont(new Font("SansSerif", Font.BOLD, 13));
        successIcon.setForeground(new Color(80, 220, 120));

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        resultLabel.setForeground(new Color(0, 255, 150));

        JLabel hint = new JLabel("Save this ID — you will need it to rent a car", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(new Color(100, 160, 120));

        resultPanel.add(successIcon, BorderLayout.NORTH);
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        resultPanel.add(hint,        BorderLayout.SOUTH);

        // Bottom area: buttons + result
        JPanel bottomArea = new JPanel(new BorderLayout());
        bottomArea.setBackground(new Color(30, 36, 48));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        btnPanel.setBackground(new Color(30, 36, 48));

        JButton saveBtn   = makeBtn("Register",  new Color(100, 60, 180));
        JButton clearBtn  = makeBtn("Clear",     new Color(70, 80, 100));
        JButton cancelBtn = makeBtn("Close",     new Color(140, 40, 40));

        saveBtn.addActionListener(e -> register());
        clearBtn.addActionListener(e -> {
            nameField.setText("");
            phoneField.setText("");
            resultPanel.setVisible(false);
            pack();
            setSize(440, 360);
        });
        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(cancelBtn);

        bottomArea.add(btnPanel,     BorderLayout.NORTH);
        bottomArea.add(resultPanel,  BorderLayout.SOUTH);
        root.add(bottomArea, BorderLayout.SOUTH);
    }

    private void register() {
        String name  = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = customerService.registerCustomer(new Customer(name, phone));
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Failed to register customer.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Fetch the auto-generated Customer ID from DB
        int newId = getLastInsertedCustomerId();

        // Show the Customer ID prominently
        resultLabel.setText("Your Customer ID:  " + newId);
        resultPanel.setVisible(true);
        setSize(440, 460);
        revalidate();
        repaint();
    }

    /** Retrieves the last inserted customer ID for the current session. */
    private int getLastInsertedCustomerId() {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "SELECT customer_id FROM customers ORDER BY customer_id DESC LIMIT 1")) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("customer_id");
        } catch (SQLException e) {
            System.err.println("[RegisterCustomerForm] Could not fetch ID: " + e.getMessage());
        }
        return -1;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(new Color(170, 185, 210));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        field.setBackground(new Color(22, 27, 40));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 80, 110)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(field, gbc);
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
