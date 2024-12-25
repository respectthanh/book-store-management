package capybara.bookstoremanagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class DatabaseUtil {

    private static final String URL = "jdbc:sqlite:books.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTables() {
        String createBooksTable = "CREATE TABLE IF NOT EXISTS books ("
                                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + "bookId TEXT NOT NULL UNIQUE, "
                                + "title TEXT NOT NULL, "
                                + "author TEXT NOT NULL, "
                                + "price REAL NOT NULL"
                                + ");";

        String createAccountsTable = "CREATE TABLE IF NOT EXISTS accounts ("
                                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                + "username TEXT NOT NULL UNIQUE, "
                                + "password TEXT NOT NULL, "
                                + "role TEXT NOT NULL CHECK(role IN ('admin', 'manager', 'employee'))"
                                + ");";

        String createCustomersTable = "CREATE TABLE IF NOT EXISTS customers ("
                                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                    + "name TEXT NOT NULL, "
                                    + "email TEXT NOT NULL, "
                                    + "phone TEXT NOT NULL"
                                    + ");";

        String createEmployeesTable = "CREATE TABLE IF NOT EXISTS employees ("
                                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                    + "name TEXT NOT NULL, "
                                    + "position TEXT NOT NULL, "
                                    + "salary REAL NOT NULL"
                                    + ");";

        String createOrdersTable = "CREATE TABLE IF NOT EXISTS orders ("
                                 + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                 + "customer TEXT NOT NULL, "
                                 + "bookId TEXT NOT NULL, "
                                 + "quantity INTEGER NOT NULL, "
                                 + "totalPrice REAL NOT NULL, "
                                 + "FOREIGN KEY(bookId) REFERENCES books(bookId)"
                                 + ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createBooksTable);
            stmt.execute(createAccountsTable);
            stmt.execute(createCustomersTable);
            stmt.execute(createEmployeesTable);
            stmt.execute(createOrdersTable);
            System.out.println("Tables 'books', 'accounts', 'customers', 'employees', and 'orders' created or already exist.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM accounts WHERE username = ? AND password = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean createUser(String username, String password) {
        String sql = "INSERT INTO accounts(username, password) VALUES(?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean createUser(String username, String password, String role) {
        String sql = "INSERT INTO accounts(username, password, role) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void updateUser(String username, String password, String role, int id) throws SQLException {
        String sql = "UPDATE employees SET username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
    }

    public static void deleteUser(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public static ResultSet getAllUser() throws SQLException {
        String sql = "SELECT * FROM accounts";
        Connection conn = connect();
        return conn.createStatement().executeQuery(sql);
    }

    public static void createBook(String bookId, String title, String author, double price) throws SQLException {
        String sql = "INSERT INTO books(bookId, title, author, price) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();
        }
    }

    public static void updateBook(int id, String title, String author, double price) throws SQLException {
        String sql = "UPDATE books SET title = ?, author = ?, price = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
    }

    public static void deleteBook(int id) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public static ResultSet getAllBooks() throws SQLException {
        String sql = "SELECT * FROM books";
        Connection conn = connect();
        return conn.createStatement().executeQuery(sql);
    }

    public static boolean isBookIdExists(String bookId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM books WHERE bookId = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public static String generateUniqueBookId() throws SQLException {
        Random random = new Random();
        String bookId;
        do {
            bookId = String.format("978-0-%05d-%03d-%d", 
                                   random.nextInt(100000), 
                                   random.nextInt(1000), 
                                   random.nextInt(10));
        } while (isBookIdExists(bookId));
        return bookId;
    }

    public static void createCustomer(String name, String email, String phone) throws SQLException {
        String sql = "INSERT INTO customers(name, email, phone) VALUES(?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
        }
    }

    public static void updateCustomer(int id, String name, String email, String phone) throws SQLException {
        String sql = "UPDATE customers SET name = ?, email = ?, phone = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
    }

    public static void deleteCustomer(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public static ResultSet getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers";
        Connection conn = connect();
        return conn.createStatement().executeQuery(sql);
    }public static void createEmployee(String name, String position, double salary) throws SQLException {
        String sql = "INSERT INTO employees(name, position, salary) VALUES(?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setDouble(3, salary);
            pstmt.executeUpdate();
        }
    }

    public static void updateEmployee(int id, String name, String position, double salary) throws SQLException {
        String sql = "UPDATE employees SET name = ?, position = ?, salary = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, position);
            pstmt.setDouble(3, salary);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
    }

    public static void deleteEmployee(int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public static ResultSet getAllEmployees() throws SQLException {
        String sql = "SELECT * FROM employees";
        Connection conn = connect();
        return conn.createStatement().executeQuery(sql);
    }

    public static void createOrder(String customer, String bookId, int quantity, double totalPrice) throws SQLException {
        String sql = "INSERT INTO orders(customer, bookId, quantity, totalPrice) VALUES(?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer);
            pstmt.setString(2, bookId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, totalPrice);
            pstmt.executeUpdate();
        }
    }

    public static void updateOrder(int id, String customer, String bookId, int quantity, double totalPrice) throws SQLException {
        String sql = "UPDATE orders SET customer = ?, bookId = ?, quantity = ?, totalPrice = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer);
            pstmt.setString(2, bookId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, totalPrice);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
        }
    }

    public static void deleteOrder(int id) throws SQLException {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public static ResultSet getAllOrders() throws SQLException {
        String sql = "SELECT * FROM orders";
        Connection conn = connect();
        return conn.createStatement().executeQuery(sql);
    }

    public static double getBookPrice(String bookTitle) throws SQLException {
        String sql = "SELECT price FROM books WHERE title = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookTitle);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            } else {
                throw new SQLException("Book not found");
            }
        }
    }public static double getBookPriceById(String bookId) throws SQLException {
        String sql = "SELECT price FROM books WHERE bookId = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            } else {
                throw new SQLException("Book not found");
            }
        }
    }

    public static String getUserRole(String username) {
        String sql = "SELECT role FROM accounts WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            } else {
                throw new SQLException("User not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}