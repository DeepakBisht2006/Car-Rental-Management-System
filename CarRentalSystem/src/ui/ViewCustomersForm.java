package ui;

import model.Customer;
import service.CustomerService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Displays all registered customers in a JTable.
 */
public class ViewCustomersForm extends JFrame {

    private final CustomerService customerService = new CustomerService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel countLabel;

    public ViewCustomersForm() {
        initUI();
        loadData();
    }

    private void initUI() {
        setTitle("All Customers");
        setSize(560, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(30, 36, 48));
        setContentPane(root);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(22, 27, 40));
        header.setPreferredSize(new Dimension(0, 55));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(100, 60, 180)));

        JLabel title = new JLabel("👥  All Registered Customers", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(160, 120, 255));
        header.add(title, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        // Table
        String[] cols = {"Customer ID", "Name", "Phone"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        scroll.getViewport().setBackground(new Color(22, 27, 40));
        root.add(scroll, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(22, 27, 40));
        footer.setBorder(BorderFactory.createEmptyBorder(6, 16, 8, 16));

        countLabel = new JLabel();
        countLabel.setForeground(new Color(140, 160, 190));
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.add(countLabel, BorderLayout.WEST);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);

        JButton refreshBtn = makeBtn("Refresh", new Color(100, 60, 180));
        JButton closeBtn   = makeBtn("Close",   new Color(80, 40, 40));
        refreshBtn.addActionListener(e -> loadData());
        closeBtn.addActionListener(e -> dispose());

        btnRow.add(refreshBtn);
        btnRow.add(closeBtn);
        footer.add(btnRow, BorderLayout.EAST);
        root.add(footer, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Customer> customers = customerService.getAllCustomers();
        for (Customer c : customers) {
            tableModel.addRow(new Object[]{
                c.getCustomerId(),
                c.getName(),
                c.getPhone()
            });
        }
        countLabel.setText("Total Customers: " + customers.size());
    }

    private void styleTable(JTable t) {
        t.setBackground(new Color(22, 27, 40));
        t.setForeground(Color.WHITE);
        t.setGridColor(new Color(40, 50, 70));
        t.setRowHeight(28);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setSelectionBackground(new Color(100, 60, 180));
        t.setSelectionForeground(Color.WHITE);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);

        JTableHeader hdr = t.getTableHeader();
        hdr.setBackground(new Color(18, 22, 35));
        hdr.setForeground(new Color(160, 120, 255));
        hdr.setFont(new Font("SansSerif", Font.BOLD, 13));
        hdr.setPreferredSize(new Dimension(0, 32));

        DefaultTableCellRenderer centre = new DefaultTableCellRenderer();
        centre.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < t.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(centre);
        }
    }

    private JButton makeBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
