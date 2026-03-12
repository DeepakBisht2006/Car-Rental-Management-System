package ui;

import model.Rental;
import service.CarService;
import service.RentalService;

import javax.swing.*;
import java.awt.*;

/**
 * Form for processing a car return.
 * Looks up a rental by ID, shows its details, and confirms the return.
 */
public class ReturnCarForm extends JFrame {

    private final RentalService rentalService = new RentalService();
    private final CarService    carService    = new CarService();

    private JTextField rentalIdField;
    private JTextArea  infoArea;
    private Rental     currentRental = null;

    // ── Constructor ──────────────────────────────────────────────────────────────

    public ReturnCarForm() {
        initUI();
    }

    // ── UI Initialisation ────────────────────────────────────────────────────────

    private void initUI() {
        setTitle("Return a Car");
        setSize(460, 380);
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
        JLabel title = new JLabel("↩️  Return a Car");
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(255, 100, 100));
        header.add(title);
        root.add(header, BorderLayout.NORTH);

        // Centre area
        JPanel centre = new JPanel(new BorderLayout());
        centre.setBackground(new Color(30, 36, 48));
        centre.setBorder(BorderFactory.createEmptyBorder(20, 36, 10, 36));

        // Rental ID input row
        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        inputRow.setOpaque(false);

        JLabel idLabel = new JLabel("Rental ID:");
        idLabel.setForeground(new Color(170, 185, 210));
        idLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        rentalIdField = new JTextField(10);
        rentalIdField.setBackground(new Color(22, 27, 40));
        rentalIdField.setForeground(Color.WHITE);
        rentalIdField.setCaretColor(Color.WHITE);
        rentalIdField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 80, 110)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        rentalIdField.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton lookupBtn = makeBtn("Look Up", new Color(60, 80, 140));
        lookupBtn.addActionListener(e -> lookupRental());
        rentalIdField.addActionListener(e -> lookupRental()); // Enter key shortcut

        inputRow.add(idLabel);
        inputRow.add(rentalIdField);
        inputRow.add(lookupBtn);
        centre.add(inputRow, BorderLayout.NORTH);

        // Info area
        infoArea = new JTextArea(8, 30);
        infoArea.setEditable(false);
        infoArea.setBackground(new Color(18, 22, 35));
        infoArea.setForeground(new Color(170, 200, 230));
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 55, 80)),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        infoArea.setText("\n  Enter a Rental ID and click 'Look Up'\n  to view rental details before confirming the return.");

        JScrollPane scroll = new JScrollPane(infoArea);
        scroll.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        centre.add(scroll, BorderLayout.CENTER);
        root.add(centre, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 14));
        btnPanel.setBackground(new Color(30, 36, 48));

        JButton returnBtn = makeBtn("Confirm Return", new Color(160, 40, 40));
        JButton cancelBtn = makeBtn("Cancel",         new Color(70, 80, 100));

        returnBtn.addActionListener(e -> processReturn());
        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(returnBtn);
        btnPanel.add(cancelBtn);
        root.add(btnPanel, BorderLayout.SOUTH);
    }

    // ── Actions ──────────────────────────────────────────────────────────────────

    /** Looks up rental details and populates the info area. */
    private void lookupRental() {
        currentRental = null;
        String txt = rentalIdField.getText().trim();

        if (txt.isEmpty()) {
            infoArea.setText("  Please enter a Rental ID.");
            return;
        }

        int rentalId;
        try {
            rentalId = Integer.parseInt(txt);
        } catch (NumberFormatException ex) {
            infoArea.setText("  Invalid Rental ID — must be a number.");
            return;
        }

        Rental rental = rentalService.getRentalById(rentalId);

        if (rental == null) {
            infoArea.setText("  Rental ID " + rentalId + " not found.");
            return;
        }

        if (rental.getReturnDate() != null) {
            infoArea.setText("  Rental ID " + rentalId + " has already been returned on " + rental.getReturnDate() + ".");
            return;
        }

        currentRental = rental;
        infoArea.setText(
            "  Rental Details\n" +
            "  ─────────────────────────────────────\n" +
            "  Rental ID    : " + rental.getRentalId()   + "\n" +
            "  Car ID       : " + rental.getCarId()      + "\n" +
            "  Customer ID  : " + rental.getCustomerId() + "\n" +
            "  Rent Date    : " + rental.getRentDate()   + "\n" +
            "  Days Rented  : " + rental.getDays()       + "\n" +
            "  Total Charged: $" + String.format("%.2f", rental.getTotalPrice()) + "\n" +
            "  Return Date  : NOT RETURNED\n\n" +
            "  ✔  Click 'Confirm Return' to process."
        );
    }

    /** Processes the car return — updates DB and restores car availability. */
    private void processReturn() {
        if (currentRental == null) {
            JOptionPane.showMessageDialog(this, "Please look up a valid, unreturned rental first.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Confirm return for Rental ID " + currentRental.getRentalId() + "?\n" +
            "Car ID " + currentRental.getCarId() + " will be marked as available.",
            "Confirm Return", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean rentOk = rentalService.returnCar(currentRental.getRentalId());
        boolean carOk  = carService.updateAvailability(currentRental.getCarId(), "YES");

        if (rentOk && carOk) {
            JOptionPane.showMessageDialog(this, "Car returned successfully! Car is now available.", "Return Successful", JOptionPane.INFORMATION_MESSAGE);
            rentalIdField.setText("");
            infoArea.setText("\n  Return processed. Enter another Rental ID to continue.");
            currentRental = null;
        } else {
            JOptionPane.showMessageDialog(this, "Return failed. Check DB connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helper ───────────────────────────────────────────────────────────────────

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
