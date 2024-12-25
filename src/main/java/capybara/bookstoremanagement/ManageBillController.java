package capybara.bookstoremanagement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
    public void initialize() {
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colBooks.setCellValueFactory(new PropertyValueFactory<>("booksFormatted"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        loadBills();
    }

    private void loadBills() {
        tableView.getItems().clear();
        try {
            ResultSet rs = DatabaseUtil.getAllBills();
            while (rs.next()) {
                Map<String, Integer> books = new HashMap<>();
                books.put(rs.getString("bookId"), rs.getInt("quantity"));
                tableView.getItems().add(new Bill(rs.getString("customer"), books, rs.getDouble("totalPrice")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReturnToMenu(ActionEvent event) {
        // Implement navigation back to the main menu
    }
}
