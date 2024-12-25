package capybara.bookstoremanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ManageBillController {

    @FXML
    private TableView<Bill> tableView;
    @FXML
    private TableColumn<Bill, String> colCustomer;
    @FXML
    private TableColumn<Bill, String> colBooks;
    @FXML
    private TableColumn<Bill, Double> colTotalPrice;
    @FXML
    private TableColumn<Bill, String> colTimeCreated;

    @FXML
    public void initialize() {
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colBooks.setCellValueFactory(new PropertyValueFactory<>("booksFormatted"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        colTimeCreated.setCellValueFactory(new PropertyValueFactory<>("timeCreated"));

        loadBills();
    }

    private void loadBills() {
        tableView.getItems().clear();
        try {
            ResultSet rs = DatabaseUtil.getAllBills();
            while (rs.next()) {
                Map<String, Integer> books = new HashMap<>();
                Map<String, String> bookTitles = new HashMap<>();
                String customer = rs.getString("customer");
                String timeCreated = rs.getString("timeCreated");
                double totalPrice = rs.getDouble("totalPrice");

                // Populate books and bookTitles maps
                ResultSet orderRs = DatabaseUtil.getOrdersByCustomerAndTime(customer, timeCreated);
                while (orderRs.next()) {
                    String bookId = orderRs.getString("bookId");
                    int quantity = orderRs.getInt("quantity");
                    books.put(bookId, quantity);
                    bookTitles.put(bookId, DatabaseUtil.getBookTitleById(bookId));
                }

                tableView.getItems().add(new Bill(customer, books, bookTitles, totalPrice, timeCreated));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleGenerateBill(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Generate Bill");
        dialog.setHeaderText("Enter the customer name to generate the bill");
        dialog.setContentText("Customer:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(customer -> {
            try {
                Bill bill = generateBillForCustomer(customer);
                saveBillToFile(bill);
                displayBill(bill);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Bill generateBillForCustomer(String customer) throws SQLException {
        ResultSet rs = DatabaseUtil.getMostRecentOrdersByCustomer(customer);
        Map<String, Integer> books = new HashMap<>();
        Map<String, String> bookTitles = new HashMap<>();
        double totalPrice = 0.0;
        String timeCreated = null;

        while (rs.next()) {
            String bookId = rs.getString("bookId");
            int quantity = rs.getInt("quantity");
            books.put(bookId, books.getOrDefault(bookId, 0) + quantity);
            bookTitles.put(bookId, DatabaseUtil.getBookTitleById(bookId));
            totalPrice += DatabaseUtil.getBookPriceById(bookId) * quantity;
            if (timeCreated == null) {
                timeCreated = rs.getString("timeCreated");
            }
        }

        return new Bill(customer, books, bookTitles, totalPrice, timeCreated);
    }

    private void saveBillToFile(Bill bill) throws IOException {
        File billsDir = new File("bills");
        if (!billsDir.exists()) {
            billsDir.mkdir();
        }
        String sanitizedTimeCreated = bill.getTimeCreated().replace(":", "-").replace(" ", "_");
        String fileName = "bills/" + bill.getCustomer() + "_" + sanitizedTimeCreated + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("Customer: " + bill.getCustomer() + "\n");
            writer.write("Time Created: " + bill.getTimeCreated() + "\n");
            writer.write("Books:\n");
            for (Map.Entry<String, Integer> entry : bill.getBooks().entrySet()) {
                writer.write(" - " + entry.getKey() + ": " + entry.getValue() + "\n");
            }
            writer.write("Total Price: " + bill.getTotalPrice() + "\n");
        }
    }

    private void displayBill(Bill bill) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bill_view.fxml"));
        Parent root = loader.load();

        BillViewController controller = loader.getController();
        controller.setBill(bill);

        Stage stage = new Stage();
        stage.setTitle("Bill for " + bill.getCustomer());
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleReturnToMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/capybara/bookstoremanagement/main_menu.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 640, 480));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
