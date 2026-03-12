package ui;

import model.Car;
import service.CarService;

import javax.swing.*;
import java.awt.*;

/**
 * Form for adding a new car to the database.
 */
public class AddCarForm extends JFrame {

    private final CarService carService = new CarService();

    // ── Form fields ──────────────────────────────────────────────────────────────
    private JTextField brandField;
    private JTextField modelField;
    private JTextField rentField;

    // ── Constructor ──────────────────────────────────────────────────────────────

    public AddCarForm() {
        initUI();
    }

    // ── UI Initialisation ────────────────────────────────────────────────────────

    private void initUI() {
        setTitle("Add New Car");
        setSize(420, 320);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(30, 36, 48));
        setContentPane(root);

        // Header
        root.add(makeHeader("➕  Add New Car"), BorderLayout.NORTH);

        // Form area
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(30, 36, 48));
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);

        brandField = new JTextField();
        modelField = new JTextField();
        rentField  = new JTextField();

        addFormRow(form, gbc, 0, "Brand:",          brandField);
        addFormRow(form, gbc, 1, "Model:",           modelField);
        addFormRow(form, gbc, 2, "Rent Per Day ($):", rentField);

        root.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        btnPanel.setBackground(new Color(30, 36, 48));

        JButton saveBtn   = makeBtn("Save Car",   new Color(0, 140, 120));
        JButton clearBtn  = makeBtn("Clear",      new Color(70, 80, 100));
        JButton cancelBtn = makeBtn("Cancel",     new Color(140, 40, 40));

        saveBtn.addActionListener(e -> saveCar());
        clearBtn.addActionListener(e -> clearFields());
        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(cancelBtn);
        root.add(btnPanel, BorderLayout.SOUTH);
    }

    // ── Actions ──────────────────────────────────────────────────────────────────

    /** Validates input and saves the car to the database. */
    private void saveCar() {
        String brand = brandField.getText().trim();
        String model = modelField.getText().trim();
        String rentText = rentField.getText().trim();

        // Basic validation
        if (brand.isEmpty() || model.isEmpty() || rentText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double rentPerDay;
        try {
            rentPerDay = Double.parseDouble(rentText);
            if (rentPerDay <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Rent Per Day must be a positive number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Car car = new Car(brand, model, rentPerDay, "YES");
        boolean success = carService.addCar(car);

        if (success) {
            JOptionPane.showMessageDialog(this, "Car added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add car. Check DB connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        brandField.setText("");
        modelField.setText("");
        rentField.setText("");
        brandField.requestFocus();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(new Color(170, 185, 210));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        styleField(field);
        panel.add(field, gbc);
    }

    private void styleField(JTextField f) {
        f.setBackground(new Color(22, 27, 40));
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 80, 110)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
    }

    private JPanel makeHeader(String text) {
        JPanel p = new JPanel();
        p.setBackground(new Color(22, 27, 40));
        p.setPreferredSize(new Dimension(0, 55));
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(new Color(0, 220, 200));
        p.add(lbl);
        return p;
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
