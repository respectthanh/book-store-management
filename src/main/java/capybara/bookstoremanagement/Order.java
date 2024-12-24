package capybara.bookstoremanagement;

import java.sql.SQLException;

public class Order {
    private int id;
    private String customerPhone;
    private String bookId;
    private int quantity;
    private double totalPrice;

    public Order(int id, String customerPhone, String bookId, int quantity, double totalPrice) {
        this.id = id;
        this.customerPhone = customerPhone;
        this.bookId = bookId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getBookId() {
        return bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Customer getCustomerInfo() {
        try {
            return DatabaseUtil.getCustomerByPhone(customerPhone);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Book getBookInfo() {
        try {
            return DatabaseUtil.getBookById(bookId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}