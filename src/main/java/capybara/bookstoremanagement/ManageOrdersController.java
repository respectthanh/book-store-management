package capybara.bookstoremanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ManageOrdersController {

    @FXML
    private TableView<Order> tableView;
    @FXML
    private TableColumn<Order, Integer> colId;
    @FXML
    private TableColumn<Order, String> colCustomer;
    @FXML
    private TableColumn<Order, String> colBookId;
    @FXML
    private TableColumn<Order, Integer> colQuantity;
    @FXML
    private TableColumn<Order, Double> colTotalPrice;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        loadOrders();
    }

    private void loadOrders() {
        tableView.getItems().clear();
        try {
            ResultSet rs = DatabaseUtil.getAllOrders();
            while (rs.next()) {
                tableView.getItems().add(new Order(rs.getInt("id"), rs.getString("customerPhone"), rs.getString("bookId"), rs.getInt("quantity"), rs.getDouble("totalPrice")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddOrder(ActionEvent event) {
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Add New Order");
        dialog.setHeaderText("Enter the details of the new order");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the customer, bookId, quantity, and email labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField customerField = new TextField();
        customerField.setPromptText("Customer");
        TextField bookIdField = new TextField();
        bookIdField.setPromptText("Book ID");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        Label totalPriceLabel = new Label("Total Price: $0.00");

        grid.add(new Label("Customer:"), 0, 0);
        grid.add(customerField, 1, 0);
        grid.add(new Label("Book ID:"), 0, 1);
        grid.add(bookIdField, 1, 1);
        grid.add(new Label("Quantity:"), 0, 2);
        grid.add(quantityField, 1, 2);
        grid.add(totalPriceLabel, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Calculate total price when bookId and quantity are input
        bookIdField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(bookIdField, quantityField, totalPriceLabel));
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(bookIdField, quantityField, totalPriceLabel));

        // Convert the result to an Order object when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String customer = customerField.getText();
                String bookId = bookIdField.getText();
                int quantity;
                double totalPrice;
                try {
                    quantity = Integer.parseInt(quantityField.getText());
                    totalPrice = Double.parseDouble(totalPriceLabel.getText().replace("Total Price: $", "").replace(",", ""));
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for quantity and total price.");
                    return null;
                }
                return new Order(0, customer, bookId, quantity, totalPrice);
            }
            return null;
        });

        Optional<Order> result = dialog.showAndWait();
        result.ifPresent(order -> {
            try {
                DatabaseUtil.createOrder(order.getCustomerPhone(), order.getBookId(), order.getQuantity(), order.getTotalPrice());
                loadOrders();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void calculateTotalPrice(TextField bookIdField, TextField quantityField, Label totalPriceLabel) {
        try {
            String bookId = bookIdField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = DatabaseUtil.getBookPriceById(bookId);
            double totalPrice = price * quantity;
            totalPriceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
        } catch (NumberFormatException | SQLException e) {
            totalPriceLabel.setText("Total Price: $0.00");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleEditOrder(ActionEvent event) {
        Order selectedOrder = tableView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            Dialog<Order> dialog = new Dialog<>();
            dialog.setTitle("Edit Order");
            dialog.setHeaderText("Edit the details of the order");

            // Set the button types
            ButtonType editButtonType = new ButtonType("Save", ButtonType.OK.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

            // Create the customer, bookId, quantity, and email labels and fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField customerField = new TextField(selectedOrder.getCustomerPhone());
            TextField bookIdField = new TextField(selectedOrder.getBookId());
            TextField quantityField = new TextField(String.valueOf(selectedOrder.getQuantity()));
            Label totalPriceLabel = new Label(String.format("Total Price: $%.2f", selectedOrder.getTotalPrice()));

            grid.add(new Label("Customer:"), 0, 0);
            grid.add(customerField, 1, 0);
            grid.add(new Label("Book ID:"), 0, 1);
            grid.add(bookIdField, 1, 1);
            grid.add(new Label("Quantity:"), 0, 2);
            grid.add(quantityField, 1, 2);
            grid.add(totalPriceLabel, 1, 3);

            dialog.getDialogPane().setContent(grid);

            // Calculate total price when bookId and quantity are input
            bookIdField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(bookIdField, quantityField, totalPriceLabel));
            quantityField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(bookIdField, quantityField, totalPriceLabel));

            // Convert the result to an Order object when the Save button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == editButtonType) {
                    String customer = customerField.getText();
                    String bookId = bookIdField.getText();
                    int quantity;
                    double totalPrice;
                    try {
                        quantity = Integer.parseInt(quantityField.getText());
                        totalPrice = Double.parseDouble(totalPriceLabel.getText().replace("Total Price: $", "").replace(",", ""));
                    } catch (NumberFormatException e) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for quantity and total price.");
                        return null;
                    }
                    return new Order(selectedOrder.getId(), customer, bookId, quantity, totalPrice);
                }
                return null;
            });

            Optional<Order> result = dialog.showAndWait();
            result.ifPresent(order -> {
                try {
                    DatabaseUtil.updateOrder(order.getId(), order.getCustomerPhone(), order.getBookId(), order.getQuantity(), order.getTotalPrice());
                    loadOrders();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void handleDeleteOrder(ActionEvent event) {
        Order selectedOrder = tableView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this order?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        DatabaseUtil.deleteOrder(selectedOrder.getId());
                        loadOrders();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @FXML
    private void handleReturnToMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 640, 540));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}