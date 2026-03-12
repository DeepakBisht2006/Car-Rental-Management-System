package ui;

import database.DBConnection;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main Dashboard — buttons shown depend on whether user is Admin or Customer.
 * ADMIN    → Add Car, View Cars, Register Customer, Rent Car, Return Car, Exit
 * CUSTOMER → Register Customer, Rent Car, Return Car, Exit
 */
public class Dashboard extends JFrame {

    private final boolean isAdmin;

    public Dashboard(boolean isAdmin) {
        this.isAdmin = isAdmin;
        initUI();
    }

    private void initUI() {
        setTitle("Car Rental System — " + (isAdmin ? "Admin Panel" : "Customer Portal"));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(560, isAdmin ? 460 : 460);
        setLocationRelativeTo(null);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(Dashboard.this,
                    "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    DBConnection.closeConnection();
                    System.exit(0);
                }
            }
        });

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(30, 36, 48));
        setContentPane(root);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(22, 27, 40));
        header.setPreferredSize(new Dimension(0, 90));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0,
            isAdmin ? new Color(0, 180, 160) : new Color(100, 80, 200)));

        JLabel titleLabel = new JLabel(
            isAdmin ? "🔐  Admin Panel" : "👤  Customer Portal", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(isAdmin ? new Color(0, 220, 200) : new Color(160, 130, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel subLabel = new JLabel(
            isAdmin ? "Full system access" : "Register, Rent, and Return cars",
            SwingConstants.CENTER);
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subLabel.setForeground(new Color(140, 160, 190));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
        logoutBtn.setBackground(new Color(50, 55, 70));
        logoutBtn.setForeground(new Color(180, 190, 210));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> { dispose(); new LoginForm().setVisible(true); });

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topRight.setOpaque(false);
        topRight.add(logoutBtn);

        JPanel headerText = new JPanel(new GridLayout(2, 1));
        headerText.setOpaque(false);
        headerText.add(titleLabel);
        headerText.add(subLabel);

        header.add(headerText, BorderLayout.CENTER);
        header.add(topRight, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // Buttons
        JPanel btnPanel = new JPanel();
        if (isAdmin) {
            btnPanel = new JPanel(new GridLayout(4, 1, 16, 16));
            btnPanel.setBackground(new Color(30, 36, 48));
            btnPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 20, 80));
            btnPanel.add(makeButton("Add Car",          new Color(0, 140, 120),  e -> new AddCarForm().setVisible(true)));
            btnPanel.add(makeButton("View Cars",        new Color(30, 90, 170),  e -> new ViewCarsForm().setVisible(true)));
            btnPanel.add(makeButton("View Customers",   new Color(100, 60, 180), e -> new ViewCustomersForm().setVisible(true)));
            btnPanel.add(makeButton("Exit",             new Color(60, 65, 80),   e -> { DBConnection.closeConnection(); System.exit(0); }));
        } else {
            btnPanel = new JPanel(new GridLayout(4, 1, 16, 16));
            btnPanel.setBackground(new Color(30, 36, 48));
            btnPanel.setBorder(BorderFactory.createEmptyBorder(30, 80, 20, 80));
            btnPanel.add(makeButton("View Available Cars",  new Color(30, 90, 170),  e -> new ViewCarsForm().setVisible(true)));
            btnPanel.add(makeButton("Register as Customer", new Color(100, 60, 180), e -> new RegisterCustomerForm().setVisible(true)));
            btnPanel.add(makeButton("Rent a Car",           new Color(180, 110, 0),  e -> new RentCarForm().setVisible(true)));
            btnPanel.add(makeButton("Return a Car",         new Color(160, 40, 40),  e -> new ReturnCarForm().setVisible(true)));
        }
        root.add(btnPanel, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel(
            "Connected: car_rental_system @ localhost:3306  |  Role: " + (isAdmin ? "Administrator" : "Customer"),
            SwingConstants.CENTER);
        footer.setFont(new Font("Monospaced", Font.PLAIN, 10));
        footer.setForeground(new Color(70, 80, 100));
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        root.add(footer, BorderLayout.SOUTH);
    }

    private JButton makeButton(String text, Color bg, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(14, 10, 14, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(action);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }
}
