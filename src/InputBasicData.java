import Product.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputBasicData extends JFrame {

    private JPanel inputBasicData;
    private JTextField typeId;
    private JTextField typeName;
    private JTextField typePrice;
    private JTextField typeCurQty;
    private JTextField typeMax;
    private JTextField typeMin;
    private JButton btnConfirm;
    private JButton btnCancel;

    private Management management;
    private String productType;
    private MainWindowForm mainWindowForm;
    private AddWindowForm addWindowForm;

    public InputBasicData(String productType, MainWindowForm mainWindowForm,
                          Management management, AddWindowForm addWindowForm) {

        this.productType = productType;
        this.mainWindowForm = mainWindowForm;
        this.management = management;
        this.addWindowForm = addWindowForm;

        setTitle("Input Basic Data - " + productType);
        setContentPane(inputBasicData);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        // Cancel
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        // Confirm
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleConfirm();
            }
        });
    }

    private void handleConfirm() {

        String idStr = typeId.getText().trim();
        String nameStr = typeName.getText().trim();
        String priceStr = typePrice.getText().trim();
        String qtyStr = typeCurQty.getText().trim();
        String maxStr = typeMax.getText().trim();
        String minStr = typeMin.getText().trim();

        // ===== VALIDATION =====
        if (idStr.isEmpty() || nameStr.isEmpty() || priceStr.isEmpty()
                || qtyStr.isEmpty() || maxStr.isEmpty() || minStr.isEmpty()) {

            showError("Please fill in all basic information fields.");
            return;
        }

        // format ID
        if (!idStr.matches("P\\d{4}")) {
            showError("Product ID must be Pxxxx (e.g. P0001)");
            return;
        }

        // duplicate ID
        if (management.checkProductId(idStr)) {
            showError("This Product ID has already been used!");
            return;
        }

        double price;
        int qty, max, min;

        try {
            price = Double.parseDouble(priceStr);
            qty = Integer.parseInt(qtyStr);
            max = Integer.parseInt(maxStr);
            min = Integer.parseInt(minStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid number format! Please enter valid numbers.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ===== LOGIC VALIDATION =====
        if (price < 0) {
            showError("Price cannot be negative!");
            return;
        }

        if (qty < 0) {
            showError("Quantity cannot be negative!");
            return;
        }

        if (min < 0) {
            showError("Min cannot be less than 0!");
            return;
        }

        if (max < 0) {
            showError("Max cannot be negative!");
            return;
        }

        if (max < min) {
            showError("Max cannot be less than Min!");
            return;
        }

        if (qty > max) {
            showError("Quantity cannot be greater than Max!");
            return;
        }

        Product newProduct = null;

        // ===== SPECIFIC DATA =====
        try {
            if (productType.equals("Pencil")) {

                String color = JOptionPane.showInputDialog(this, "Enter Color:");
                if (color == null) return;

                String grade = JOptionPane.showInputDialog(this, "Enter Grade:");
                if (grade == null) return;

                newProduct = new Pencil(idStr, nameStr, price, qty, max, min, color, grade);

            } else if (productType.equals("Pen")) {

                String color = JOptionPane.showInputDialog(this, "Enter Color:");
                if (color == null) return;

                String tipStr = JOptionPane.showInputDialog(this, "Enter Tip Size:");
                if (tipStr == null) return;
                double tip = Double.parseDouble(tipStr);

                String penType = JOptionPane.showInputDialog(this, "Enter Pen Type:");
                if (penType == null) return;

                newProduct = new Pen(idStr, nameStr, price, qty, max, min, color, tip, penType);

            } else if (productType.equals("Notebook")) {

                String size = JOptionPane.showInputDialog(this, "Enter Size:");
                if (size == null) return;

                int gsm = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter GSM:"));
                int pages = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Pages:"));

                newProduct = new Notebook(idStr, nameStr, price, qty, max, min, size, gsm, pages);

            } else if (productType.equals("Report Paper")) {

                String size = JOptionPane.showInputDialog(this, "Enter Size:");
                if (size == null) return;

                int gsm = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter GSM:"));
                int sheets = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Sheets:"));

                newProduct = new ReportPaper(idStr, nameStr, price, qty, max, min, size, gsm, sheets);

            } else if (productType.equals("General Stationery")) {

                String type = JOptionPane.showInputDialog(this, "Enter Stationery Type:");
                if (type == null) return;

                newProduct = new GeneralStationery(idStr, nameStr, price, qty, max, min, type);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid number format in specific data!",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ===== ADD PRODUCT =====
        if (newProduct != null) {
            boolean isAdded = management.addProduct(newProduct);

            if (isAdded) {
                mainWindowForm.updateTable();

                JOptionPane.showMessageDialog(this,
                        "Product added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                dispose();
                addWindowForm.dispose();
            }
        }
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this,
                msg,
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
    }
}
