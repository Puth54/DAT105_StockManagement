package Product;

import java.io.Serializable;

public abstract class Product implements Serializable {

    // attribute
    private String productId;
    private String productName;
    private double productPrice;
    private int productQuantity;
    private int productMax;
    private int productMin;
    private boolean productStatus;

    // constructor
    public Product(String productId, String productName, double productPrice, int productQuantity, int productMax, int productMin) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productMax = productMax;
        this.productMin = productMin;
    }

    // method getter
    public String getProductId() {
        return productId;
    }
    public String getProductName() {
        return productName;
    }
    public double getProductPrice() {
        return productPrice;
    }
    public int getProductQuantity() {
        return productQuantity;
    }
    public int getProductMax() {
        return productMax;
    }
    public int getProductMin() {
        return productMin;
    }
    public boolean getProductStatus() {
        return productStatus;
    }

    // method setter

    public void setProductName(String newName) {
        this.productName = newName;
    }
    public void setProductPrice(double newPrice) {
        this.productPrice = newPrice;
    }

    public void setProductQuantity(int newQty) {
        this.productQuantity = newQty;
    }
    public void setProductMax(int newMax) {
        this.productMax = newMax;
    }
    public void setProductMin(int newMin) {
        this.productMin = newMin;
    }

    // others method
    public void updateStatus() {
        // ถ้า qty ปัจจุบันน้อยกว่า min
        if (this.productQuantity <= this.productMin) {
            this.productStatus = false; // สถานะเป็น False (ของหมด/ต้องสั่งเพิ่ม)
        } else {
            this.productStatus = true;  // สถานะเป็น True (ของยังมีพอขาย)
        }
    }

    // abstract method
    public abstract String toString();
}
