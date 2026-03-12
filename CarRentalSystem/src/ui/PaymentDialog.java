package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * Payment dialog shown before confirming a rental.
 * Displays a UPI QR code with the exact amount to pay.
 * After scanning and paying, user clicks "I Have Paid" to confirm rental.
 */
public class PaymentDialog extends JDialog {

    // ── UPI ID to receive payment (change to your own UPI ID) ───────────────────
    private static final String UPI_ID   = "carrental@upi";
    private static final String UPI_NAME = "Car Rental Services";

    private boolean paymentConfirmed = false;

    // ── Constructor ──────────────────────────────────────────────────────────────

    public PaymentDialog(JFrame parent, String carName, int days, double totalAmount) {
        super(parent, "Payment", true); // modal dialog
        initUI(carName, days, totalAmount);
    }

    // ── UI ───────────────────────────────────────────────────────────────────────

    private void initUI(String carName, int days, double totalAmount) {
        setSize(420, 620);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(new Color(18, 22, 35));
        setContentPane(root);

        // ── Header ────────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new GridLayout(3, 1));
        header.setBackground(new Color(12, 16, 28));
        header.setPreferredSize(new Dimension(0, 90));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 200, 120)));

        JLabel icon = new JLabel("💳  Scan & Pay", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.BOLD, 18));
        icon.setForeground(new Color(0, 230, 140));

        JLabel sub = new JLabel("Complete payment to confirm your rental", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        sub.setForeground(new Color(100, 130, 160));

        JLabel upiLabel = new JLabel("UPI ID: " + UPI_ID, SwingConstants.CENTER);
        upiLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));
        upiLabel.setForeground(new Color(80, 160, 120));

        header.add(icon);
        header.add(sub);
        header.add(upiLabel);
        root.add(header, BorderLayout.NORTH);

        // ── Centre: QR + details ─────────────────────────────────────────────────
        JPanel centre = new JPanel(new BorderLayout(0, 12));
        centre.setBackground(new Color(18, 22, 35));
        centre.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));

        // Rental summary box
        JPanel summaryBox = new JPanel(new GridLayout(3, 2, 6, 6));
        summaryBox.setBackground(new Color(24, 30, 46));
        summaryBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 60, 90)),
            BorderFactory.createEmptyBorder(10, 16, 10, 16)
        ));

        addSummaryRow(summaryBox, "Car:",       carName);
        addSummaryRow(summaryBox, "Duration:",  days + " day" + (days > 1 ? "s" : ""));
        addSummaryRow(summaryBox, "Amount Due:", String.format("₹ %.2f", totalAmount));

        centre.add(summaryBox, BorderLayout.NORTH);

        // QR Code image
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBackground(new Color(18, 22, 35));

        JLabel qrLabel = new JLabel("Loading QR Code...", SwingConstants.CENTER);
        qrLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        qrLabel.setForeground(new Color(100, 130, 160));
        qrLabel.setPreferredSize(new Dimension(220, 220));
        qrPanel.add(qrLabel, BorderLayout.CENTER);

        // White border around QR to simulate scanner frame
        JPanel qrFrame = new JPanel(new BorderLayout());
        qrFrame.setBackground(new Color(18, 22, 35));
        qrFrame.add(qrPanel, BorderLayout.CENTER);

        JLabel scanHint = new JLabel("Scan with any UPI app  •  Google Pay  •  PhonePe  •  Paytm", SwingConstants.CENTER);
        scanHint.setFont(new Font("SansSerif", Font.PLAIN, 10));
        scanHint.setForeground(new Color(80, 100, 130));
        scanHint.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        centre.add(qrFrame,   BorderLayout.CENTER);
        centre.add(scanHint,  BorderLayout.SOUTH);
        root.add(centre, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────────────
        JPanel btnArea = new JPanel(new BorderLayout());
        btnArea.setBackground(new Color(18, 22, 35));
        btnArea.setBorder(BorderFactory.createEmptyBorder(0, 24, 20, 24));

        // Amount badge
        JLabel amountBadge = new JLabel(String.format("Total: ₹ %.2f", totalAmount), SwingConstants.CENTER);
        amountBadge.setFont(new Font("SansSerif", Font.BOLD, 20));
        amountBadge.setForeground(new Color(0, 230, 140));
        amountBadge.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 12, 0));
        btnRow.setOpaque(false);

        JButton paidBtn   = makeBtn("✅  I Have Paid",  new Color(0, 140, 80));
        JButton cancelBtn = makeBtn("✖  Cancel",        new Color(140, 40, 40));

        paidBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm that you have paid ₹" + String.format("%.2f", totalAmount) + "?",
                "Confirm Payment", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                paymentConfirmed = true;
                dispose();
            }
        });

        cancelBtn.addActionListener(e -> {
            paymentConfirmed = false;
            dispose();
        });

        btnRow.add(paidBtn);
        btnRow.add(cancelBtn);

        btnArea.add(amountBadge, BorderLayout.NORTH);
        btnArea.add(btnRow,      BorderLayout.CENTER);
        root.add(btnArea, BorderLayout.SOUTH);

        // ── Load QR code asynchronously so UI doesn't freeze ─────────────────────
        loadQRCode(qrLabel, totalAmount);
    }

    // ── QR Code loader ───────────────────────────────────────────────────────────

    /**
     * Loads a QR code image from the free QR Server API in a background thread.
     * The QR encodes a UPI deep link with the exact payment amount.
     */
    private void loadQRCode(JLabel qrLabel, double amount) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    // UPI payment deep link encoded into the QR
                    String upiData = String.format(
                        "upi://pay?pa=%s&pn=%s&am=%.2f&cu=INR&tn=CarRental",
                        UPI_ID, UPI_NAME.replace(" ", "%20"), amount
                    );

                    // Use free QR code generation API
                    String apiUrl = "https://api.qrserver.com/v1/create-qr-code/"
                        + "?size=200x200"
                        + "&margin=10"
                        + "&color=000000"
                        + "&bgcolor=FFFFFF"
                        + "&data=" + java.net.URLEncoder.encode(upiData, "UTF-8");

                    BufferedImage img = ImageIO.read(new URL(apiUrl));
                    if (img != null) {
                        return new ImageIcon(img);
                    }
                } catch (IOException e) {
                    System.err.println("[PaymentDialog] Could not load QR: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    ImageIcon qrIcon = get();
                    if (qrIcon != null) {
                        qrLabel.setIcon(qrIcon);
                        qrLabel.setText("");
                    } else {
                        // Fallback if no internet
                        showOfflineQR(qrLabel, amount);
                    }
                } catch (Exception e) {
                    showOfflineQR(qrLabel, amount);
                }
            }
        };
        worker.execute();
    }

    /** Fallback when there's no internet — shows a styled placeholder. */
    private void showOfflineQR(JLabel label, double amount) {
        label.setText("<html><center>"
            + "<b style='color:#00E68A;font-size:14px'>QR Unavailable</b><br><br>"
            + "<span style='color:#8AA0C0'>No internet connection.<br><br>"
            + "Please pay manually to:<br></span>"
            + "<b style='color:#FFFFFF'>" + UPI_ID + "</b><br><br>"
            + "<span style='color:#8AA0C0'>Amount:</span> "
            + "<b style='color:#00E68A'>₹" + String.format("%.2f", amount) + "</b>"
            + "</center></html>");
        label.setBackground(new Color(24, 30, 46));
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(new Color(40, 60, 90)));
    }

    // ── Result getter ────────────────────────────────────────────────────────────

    /** Returns true if the user confirmed payment. */
    public boolean isPaymentConfirmed() {
        return paymentConfirmed;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    private void addSummaryRow(JPanel panel, String key, String value) {
        JLabel keyLabel = new JLabel(key);
        keyLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        keyLabel.setForeground(new Color(120, 140, 170));

        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        valLabel.setForeground(new Color(220, 230, 245));

        panel.add(keyLabel);
        panel.add(valLabel);
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(bg.darker()); }
            @Override public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }
}
