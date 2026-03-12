package ui;

import model.Car;
import service.CarService;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Displays all cars (available and unavailable) in a sortable JTable.
 * Rows are colour-coded: green = available, red = rented out.
 */
public class ViewCarsForm extends JFrame {

    private final CarService carService = new CarService();

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel countLabel;

    // ── Constructor ──────────────────────────────────────────────────────────────

    public ViewCarsForm() {
        initUI();
        loadData();
    }

    // ── UI Initialisation ────────────────────────────────────────────────────────

    private void initUI() {
        setTitle("View All Cars");
        setSize(680, 440);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(30, 36, 48));
        setContentPane(root);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(22, 27, 40));
        header.setPreferredSize(new Dimension(0, 55));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(30, 90, 170)));

        JLabel title = new JLabel("🔍  All Cars", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setForeground(new Color(80, 160, 255));
        header.add(title, BorderLayout.CENTER);
        root.add(header, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Brand", "Model", "Rent/Day ($)", "Available"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel) {
            // Colour-code rows based on availability
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                String avail = (String) getValueAt(row, 4);
                if (!isRowSelected(row)) {
                    c.setBackground("YES".equals(avail)
                        ? new Color(20, 60, 40)    // green tint
                        : new Color(60, 20, 20));  // red tint
                }
                c.setForeground(Color.WHITE);
                return c;
            }
        };

        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        scroll.getViewport().setBackground(new Color(22, 27, 40));
        root.add(scroll, BorderLayout.CENTER);

        // Footer toolbar
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(22, 27, 40));
        footer.setBorder(BorderFactory.createEmptyBorder(6, 16, 8, 16));

        countLabel = new JLabel();
        countLabel.setForeground(new Color(140, 160, 190));
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.add(countLabel, BorderLayout.WEST);

        JButton refreshBtn = makeBtn("⟳  Refresh", new Color(30, 90, 170));
        JButton closeBtn   = makeBtn("Close",       new Color(80, 40, 40));
        refreshBtn.addActionListener(e -> loadData());
        closeBtn.addActionListener(e -> dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);
        btnRow.add(refreshBtn);
        btnRow.add(closeBtn);
        footer.add(btnRow, BorderLayout.EAST);
        root.add(footer, BorderLayout.SOUTH);
    }

    // ── Data loading ─────────────────────────────────────────────────────────────

    private void loadData() {
        tableModel.setRowCount(0);
        List<Car> cars = carService.getAllCars();

        long available = 0;
        for (Car car : cars) {
            tableModel.addRow(new Object[]{
                car.getCarId(),
                car.getBrand(),
                car.getModel(),
                String.format("%.2f", car.getRentPerDay()),
                car.getAvailable()
            });
            if ("YES".equals(car.getAvailable())) available++;
        }
        countLabel.setText(String.format("Total: %d car(s)  |  Available: %d  |  Rented: %d",
            cars.size(), available, cars.size() - available));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────────

    private void styleTable(JTable t) {
        t.setBackground(new Color(22, 27, 40));
        t.setForeground(Color.WHITE);
        t.setGridColor(new Color(40, 50, 70));
        t.setRowHeight(28);
        t.setFont(new Font("SansSerif", Font.PLAIN, 13));
        t.setSelectionBackground(new Color(0, 90, 160));
        t.setSelectionForeground(Color.WHITE);
        t.setShowHorizontalLines(true);
        t.setShowVerticalLines(false);

        JTableHeader hdr = t.getTableHeader();
        hdr.setBackground(new Color(18, 22, 35));
        hdr.setForeground(new Color(100, 180, 255));
        hdr.setFont(new Font("SansSerif", Font.BOLD, 13));
        hdr.setPreferredSize(new Dimension(0, 32));

        // Centre-align all columns
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
