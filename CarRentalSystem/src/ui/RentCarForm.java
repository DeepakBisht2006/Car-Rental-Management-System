package ui;

import model.Car;
import model.Customer;
import model.Rental;
import service.CarService;
import service.CustomerService;
import service.RentalService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

/**
 * Form for renting a car to a customer.
 * Calculates total cost automatically and updates car availability.
 */
public class RentCarForm extends JFrame {

    private final CarService      carService      = new CarService();
    private final CustomerService customerService = new CustomerService();
    private final RentalService   rentalService   = new RentalService();

    // ── Form fields ──────────────────────────────────────────────────────────────
    private JTextField carIdField;
    private JTextField customerIdField;
    private JTextField daysField;
    private JLabel     carInfoLabel;
    private JLabel     customerInfoLabel;
    private JLabel     totalLabel;

    // ── Constructor ──────────────────────────────────────────────────────────────

    public RentCarForm() {
        initUI();
    }

    // ── UI Initialisation ────────────────────────────────────────────────────────

    private void initUI() {
        setTitle("Rent a Car");
        setSize(480, 440);
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
        JLabel title = new JLabel("🚘  Rent a Car");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(255, 180, 40));
        header.add(title);
        root.add(header, BorderLayout.NORTH);

        // Form panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(30, 36, 48));
        form.setBorder(BorderFactory.createEmptyBorder(20, 36, 10, 36));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 4, 7, 4);

        carIdField      = new JTextField();
        customerIdField = new JTextField();
        daysField       = new JTextField();

        // Car ID row
        addRow(form, gbc, 0, "Car ID:", carIdField);
        // Car info feedback
        carInfoLabel = makeInfoLabel();
        gbc.gridx = 1; gbc.gridy = 1;
        form.add(carInfoLabel, gbc);

        // Customer ID row
        addRow(form, gbc, 2, "Customer ID:", customerIdField);
        customerInfoLabel = makeInfoLabel();
        gbc.gridx = 1; gbc.gridy = 3;
        form.add(customerInfoLabel, gbc);

        // Days row
        addRow(form, gbc, 4, "Number of Days:", daysField);

        // Total price display
        totalLabel = new JLabel("Total Cost: —");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        totalLabel.setForeground(new Color(80, 220, 160));
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(18, 4, 4, 4);
        form.add(totalLabel, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(7, 4, 7, 4);

        root.add(form, BorderLayout.CENTER);

        // Live-lookup on focus loss
        carIdField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) { lookupCar(); }
        });
        customerIdField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) { lookupCustomer(); }
        });
        daysField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) { calcTotal(); }
        });

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 14));
        btnPanel.setBackground(new Color(30, 36, 48));

        JButton rentBtn   = makeBtn("Confirm Rental", new Color(180, 110, 0));
        JButton clearBtn  = makeBtn("Clear",          new Color(70, 80, 100));
        JButton cancelBtn = makeBtn("Cancel",         new Color(140, 40, 40));

        rentBtn.addActionListener(e -> processRental());
        clearBtn.addActionListener(e -> clearForm());
        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(rentBtn);
        btnPanel.add(clearBtn);
        btnPanel.add(cancelBtn);
        root.add(btnPanel, BorderLayout.SOUTH);
    }

    // ── Live-lookup helpers ──────────────────────────────────────────────────────

    private Car currentCar = null;

    private void lookupCar() {
        currentCar = null;
        carInfoLabel.setText("");
        String txt = carIdField.getText().trim();
        if (txt.isEmpty()) return;
        try {
            Car car = carService.getCarById(Integer.parseInt(txt));
            if (car == null) {
                carInfoLabel.setForeground(new Color(255, 80, 80));
                carInfoLabel.setText("Car not found.");
            } else if (!"YES".equals(car.getAvailable())) {
                carInfoLabel.setForeground(new Color(255, 180, 40));
                carInfoLabel.setText(car.getBrand() + " " + car.getModel() + " — NOT available.");
            } else {
                currentCar = car;
                carInfoLabel.setForeground(new Color(80, 220, 160));
                carInfoLabel.setText(car.getBrand() + " " + car.getModel()
                    + "  |  $" + String.format("%.2f", car.getRentPerDay()) + "/day");
                calcTotal();
            }
        } catch (NumberFormatException ex) {
            carInfoLabel.setForeground(new Color(255, 80, 80));
            carInfoLabel.setText("Invalid Car ID.");
        }
    }

    private void lookupCustomer() {
        customerInfoLabel.setText("");
        String txt = customerIdField.getText().trim();
        if (txt.isEmpty()) return;
        try {
            Customer c = customerService.getCustomerById(Integer.parseInt(txt));
            if (c == null) {
                customerInfoLabel.setForeground(new Color(255, 80, 80));
                customerInfoLabel.setText("Customer not found.");
            } else {
                customerInfoLabel.setForeground(new Color(80, 220, 160));
                customerInfoLabel.setText(c.getName() + "  |  " + c.getPhone());
            }
        } catch (NumberFormatException ex) {
            customerInfoLabel.setForeground(new Color(255, 80, 80));
            customerInfoLabel.setText("Invalid Customer ID.");
        }
    }

    private void calcTotal() {
        if (currentCar == null) return;
        try {
            int days = Integer.parseInt(daysField.getText().trim());
            if (days > 0) {
                double total = days * currentCar.getRentPerDay();
                totalLabel.setText(String.format("Total Cost: $%.2f  (%d days × $%.2f/day)",
                    total, days, currentCar.getRentPerDay()));
            }
        } catch (NumberFormatException ignored) {}
    }

    // ── Main action ──────────────────────────────────────────────────────────────

    private void processRental() {
        lookupCar();
        lookupCustomer();

        if (currentCar == null) {
            JOptionPane.showMessageDialog(this, "Please enter a valid, available Car ID.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int customerId;
        int days;
        try {
            customerId = Integer.parseInt(customerIdField.getText().trim());
            days       = Integer.parseInt(daysField.getText().trim());
            if (days <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Customer ID and Days must be valid positive numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (customerService.getCustomerById(customerId) == null) {
            JOptionPane.showMessageDialog(this, "Customer ID not found.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = days * currentCar.getRentPerDay();

        // Show payment QR dialog before confirming rental
        PaymentDialog paymentDialog = new PaymentDialog(
            (javax.swing.JFrame) SwingUtilities.getWindowAncestor(this),
            currentCar.getBrand() + " " + currentCar.getModel(),
            days, total
        );
        paymentDialog.setVisible(true);

        // Only proceed if user confirmed payment
        if (!paymentDialog.isPaymentConfirmed()) {
            JOptionPane.showMessageDialog(this, "Payment cancelled. Rental not confirmed.", "Cancelled", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Rental rental = new Rental(currentCar.getCarId(), customerId, LocalDate.now(), days, total);

        // Persist rental and mark car unavailable
        boolean rentalOk = rentalService.createRental(rental);
        boolean carOk    = carService.updateAvailability(currentCar.getCarId(), "NO");

        if (rentalOk && carOk) {
            int rentalId = getLastRentalId();
            JOptionPane.showMessageDialog(this,
                String.format("Rental Confirmed!\n\nYour Rental ID:  %d\n\n%s %s rented for %d days.\nTotal: ₹%.2f\n\nSave your Rental ID to return the car!",
                    rentalId, currentCar.getBrand(), currentCar.getModel(), days, total),
                "Rental Successful", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Rental failed. Check DB connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getLastRentalId() {
        try (java.sql.Connection conn = database.DBConnection.getConnection();
             java.sql.PreparedStatement ps = conn.prepareStatement(
                 "SELECT rental_id FROM rentals ORDER BY rental_id DESC LIMIT 1")) {
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("rental_id");
        } catch (java.sql.SQLException e) {
            System.err.println("Could not fetch rental ID: " + e.getMessage());
        }
        return -1;
    }

    private void clearForm() {
        carIdField.setText("");
        customerIdField.setText("");
        daysField.setText("");
        carInfoLabel.setText("");
        customerInfoLabel.setText("");
        totalLabel.setText("Total Cost: —");
        currentCar = null;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.35;
        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(new Color(170, 185, 210));
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.weightx = 0.65;
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

    private JLabel makeInfoLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(new Font("SansSerif", Font.ITALIC, 11));
        l.setForeground(new Color(80, 220, 160));
        return l;
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
