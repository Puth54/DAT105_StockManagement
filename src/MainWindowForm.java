import Product.Management;
import Product.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;                          // สำหรับใช้ใน Renderer
import java.awt.Color;                              // สำหรับตั้งค่าสี

public class MainWindowForm extends JFrame{

    // attribute
    private JPanel main_form;
    private JPanel management_button;   // JPanel ของปุ่ม Management ทั้งหมด
    private JButton btnAdd;
    private JButton btnDecrease;
    private JButton btnEdit;
    private JButton btnIncrease;
    private JScrollPane basic_information;   // JScrollPanel ของตาราง
    private JTable basicData;
    private JButton btnFullData;

    private JFrame frame;

    private Management management;

    // constructor + การทำงานใน Main
    public MainWindowForm() {
        frame = new JFrame("Stock Management System");
        management = new Management(); // สร้าง obj ของ Management เพื่อเชื่อมกับ ArrayList (Attribute ของ Management)

        setupTable();
        updateTable();

        frame.setContentPane(main_form);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null); // ให้อยู่กึ่งกลางหน้าจอ
        frame.setVisible(true);

        // ปุ่ม Add
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddWindowForm add_form = new AddWindowForm(MainWindowForm.this, management);
                add_form.setVisible(true);

                updateTable();
            }
        });

        // ปุ่ม increase
        btnIncrease.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog(null,"Enter Product ID: ");
                if (id == null) return;
                int qty = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter increased quantity: "));
                management.increaseProductQuantity(id, qty);

                updateTable();
            }
        });

        // ปุ่ม decrease
        btnDecrease.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog(null,"Enter Product ID: ");
                if (id == null) return;
                int qty = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter decreased quantity: "));
                management.decreaseProductQuantity(id, qty);

                updateTable();
            }
        });

        // ปุ่ม edit
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog(null,"Enter Product ID: ");
                EditBasicData edit_form = new EditBasicData(MainWindowForm.this, management, id);
                edit_form.setVisible(true);
            }
        });

        // ปุ่ม full data
        btnFullData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog("Enter Product ID:");
        if (id == null) return;
        Product p = management.findProduct(id);
        if (p == null) {
            JOptionPane.showMessageDialog(null,"Product not found","Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(null,
                p.toString(),
                "Full Product Data",
                JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    // setup ค่าเริ่มต้นของตาราง basicData
    private void setupTable() {
        String[] columnNames = {"ID", "Name", "Price", "Quantity", "Max", "Min", "Status"}; // array ของ column ตาราง
        DefaultTableModel model = new DefaultTableModel(null, columnNames); // DefaultTableModel ใช้ตั้งค่าตาราง
        basicData.setModel(model);

        // --- ส่วนที่เพิ่มเข้ามาเพื่อจัดการสี ---
        int statusColumnIndex = 6; // คอลัมน์ Status คือ Index ที่ 6
        basicData.getColumnModel().getColumn(statusColumnIndex).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                // เรียกความสามารถพื้นฐานมาใช้ก่อน
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // บังคับให้ JLabel ยอมรับสีพื้นหลัง
                setOpaque(true);

                if (value != null) {
                    String status = value.toString();

                    if (status.equalsIgnoreCase("Available")) {
                        c.setBackground(new Color(40, 167, 69)); // สีเขียวเข้ม
                        c.setForeground(Color.WHITE);           // ตัวอักษรขาว
                    } else if (status.equalsIgnoreCase("Low Stock")) {
                        c.setBackground(new Color(220, 53, 69));  // สีแดงเข้ม
                        c.setForeground(Color.WHITE);           // ตัวอักษรขาว
                    } else {
                        // แถวอื่นๆ ให้เป็นสีพื้นหลังตารางปกติ
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                    }

                    // กรณีที่กดเลือกแถว (Selection) ให้สีเข้มขึ้นเพื่อให้รู้ว่าเลือกอยู่
                    if (isSelected) {
                        c.setBackground(c.getBackground().darker());
                    }
                }

                // จัดข้อความให้อยู่กึ่งกลาง
                setHorizontalAlignment(JLabel.CENTER);

                return c;
            }
        });
    }

    // ตั้งค่าข้อมูลในตาราง โดยดึงข้อมูลจาก ArrayList
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


    // Main Method ============================================================================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindowForm();
            }
        });
    }
}
