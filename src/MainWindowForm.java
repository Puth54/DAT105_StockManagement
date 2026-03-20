import Product.Management;
import Product.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;

public class MainWindowForm extends JFrame {

    // attribute
    private JPanel main_form;
    private JPanel management_button;
    private JButton btnAdd;
    private JButton btnDecrease;
    private JButton btnEdit;
    private JButton btnIncrease;
    private JScrollPane basic_information;
    private JTable basicData;
    private JButton btnFullData;

    private JFrame frame;
    private Management management;

    // constructor
    public MainWindowForm() {
        frame = new JFrame("Stock Management System");
        management = new Management();

        setupTable();
        updateTable();

        frame.setContentPane(main_form);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // ================= ADD =================
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddWindowForm add_form = new AddWindowForm(MainWindowForm.this, management);
                add_form.setVisible(true);
                updateTable();
            }
        });

        // ================= INCREASE =================
        btnIncrease.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog(null, "Enter Product ID: ");
                if (id == null || id.trim().isEmpty()) return;

                String qtyStr = JOptionPane.showInputDialog(null, "Enter increased quantity: ");
                if (qtyStr == null) return;

                try {
                    int qty = Integer.parseInt(qtyStr);
                    management.increaseProductQuantity(id, qty);
                    updateTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ================= DECREASE =================
        btnDecrease.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog(null, "Enter Product ID: ");
                if (id == null || id.trim().isEmpty()) return;

                String qtyStr = JOptionPane.showInputDialog(null, "Enter decreased quantity: ");
                if (qtyStr == null) return;

                try {
                    int qty = Integer.parseInt(qtyStr);
                    management.decreaseProductQuantity(id, qty);
                    updateTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ================= EDIT =================
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog(null, "Enter Product ID: ");
                if (id == null || id.trim().isEmpty()) return;

                if (!management.checkProductId(id)) {
                    JOptionPane.showMessageDialog(null, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                EditBasicData edit_form = new EditBasicData(MainWindowForm.this, management, id);
                edit_form.setVisible(true);
            }
        });

        // ================= FULL DATA =================
        btnFullData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog("Enter Product ID:");
                if (id == null || id.trim().isEmpty()) return;

                Product p = management.findProduct(id);
                if (p == null) {
                    JOptionPane.showMessageDialog(null, "Product not found", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(null,
                        p.toString(),
                        "Full Product Data",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // ================= SETUP TABLE =================
    private void setupTable() {
        String[] columnNames = {"ID", "Name", "Price", "Quantity", "Max", "Min", "Status"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        basicData.setModel(model);

        int statusColumnIndex = 6;

        basicData.getColumnModel().getColumn(statusColumnIndex)
                .setCellRenderer(new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value,
                                                                   boolean isSelected, boolean hasFocus,
                                                                   int row, int column) {

                        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                        setOpaque(true);

                        if (value != null) {
                            String status = value.toString();

                            if (status.equalsIgnoreCase("Available")) {
                                c.setBackground(new Color(40, 167, 69));
                                c.setForeground(Color.WHITE);
                            } else if (status.equalsIgnoreCase("Low Stock")) {
                                c.setBackground(new Color(220, 53, 69));
                                c.setForeground(Color.WHITE);
                            } else {
                                c.setBackground(table.getBackground());
                                c.setForeground(table.getForeground());
                            }

                            if (isSelected) {
                                c.setBackground(c.getBackground().darker());
                            }
                        }

                        setHorizontalAlignment(JLabel.CENTER);
                        return c;
                    }
                });
    }

    // ================= UPDATE TABLE =================
    public void updateTable() {
        DefaultTableModel model = (DefaultTableModel) basicData.getModel();
        model.setRowCount(0);

        for (Product p : management.getProducts()) {
            Object[] rowData = {
                    p.getProductId(),
                    p.getProductName(),
                    p.getProductPrice(),
                    p.getProductQuantity(),
                    p.getProductMax(),
                    p.getProductMin(),
                    p.getProductStatus() ? "Available" : "Low Stock"
            };
            model.addRow(rowData);
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindowForm();
            }
        });
    }
}
