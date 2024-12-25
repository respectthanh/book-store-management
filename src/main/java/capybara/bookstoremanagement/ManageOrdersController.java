package capybara.bookstoremanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class ManageOrdersController {
    private String previousView;

    public void setPreviousView(String previousView) {
        this.previousView = previousView;
    }


    @FXML
    private TableView<Order> tableView;
    @FXML
    private TableColumn<Order, Integer> colId;
    @FXML
    private TableColumn<Order, String> colCustomer;
    @FXML
    private TableColumn<Order, String> colBooks;
    @FXML
    private TableColumn<Order, String> colQuantities;
    @FXML
    private TableColumn<Order, Double> colTotalPrice;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        colBooks.setCellValueFactory(new PropertyValueFactory<>("booksFormatted"));
        colQuantities.setCellValueFactory(new PropertyValueFactory<>("quantitiesFormatted"));
        colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        loadOrders();
    }

    private void loadOrders() {
        tableView.getItems().clear();
        try {
            ResultSet rs = DatabaseUtil.getAllOrders();
            while (rs.next()) {
                Map<String, Integer> books = new HashMap<>();
                books.put(rs.getString("bookId"), rs.getInt("quantity"));
                tableView.getItems().add(new Order(rs.getInt("id"), rs.getString("customer"), books, rs.getDouble("totalPrice")));
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

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        VBox vbox = new VBox(10);

        TextField customerField = new TextField();
        customerField.setPromptText("Customer");
        vbox.getChildren().add(new Label("Customer:"));
        vbox.getChildren().add(customerField);

        // Create a map to store book fields
        Map<TextField, TextField> bookFields = new HashMap<>();
        final int[] row = {1};

        TextField bookIdField = new TextField();
        bookIdField.setPromptText("Book ID");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Label totalPriceLabel = new Label("Total Price: €0,00");
        vbox.getChildren().add(totalPriceLabel);

        bookIdField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(bookFields, totalPriceLabel));
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(bookFields, totalPriceLabel));

        GridPane bookGrid = new GridPane();
        bookGrid.setHgap(10);
        bookGrid.setVgap(10);
        bookGrid.add(new Label("Book ID:"), 0, row[0]);
        bookGrid.add(bookIdField, 1, row[0]);
        bookGrid.add(new Label("Quantity:"), 2, row[0]);
        bookGrid.add(quantityField, 3, row[0]);

        bookFields.put(bookIdField, quantityField);

        Button addBookButton = new Button("Add Book");
        vbox.getChildren().add(bookGrid);
        vbox.getChildren().add(addBookButton);

        addBookButton.setOnAction(e -> {
            row[0]++;
            TextField newBookIdField = new TextField();
            newBookIdField.setPromptText("Book ID");
            TextField newQuantityField = new TextField();
            newQuantityField.setPromptText("Quantity");

            newBookIdField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(bookFields, totalPriceLabel));
            newQuantityField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(bookFields, totalPriceLabel));

            bookGrid.add(new Label("Book ID:"), 0, row[0]);
            bookGrid.add(newBookIdField, 1, row[0]);
            bookGrid.add(new Label("Quantity:"), 2, row[0]);
            bookGrid.add(newQuantityField, 3, row[0]);

            bookFields.put(newBookIdField, newQuantityField);
        });

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        dialog.getDialogPane().setContent(scrollPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String customer = customerField.getText();
                Map<String, Integer> books = new HashMap<>();
                for (Map.Entry<TextField, TextField> entry : bookFields.entrySet()) {
                    String bookId = entry.getKey().getText();
                    int quantity = Integer.parseInt(entry.getValue().getText());
                    books.put(bookId, quantity);
                }
                double totalPrice = calculateTotalPrice(books);
                return new Order(0, customer, books, totalPrice);
            }
            return null;
        });

        Optional<Order> result = dialog.showAndWait();
        result.ifPresent(order -> {
            try {
                DatabaseUtil.createOrder(order.getCustomer(), order.getBooks(), order.getTotalPrice());
                loadOrders();
                Bill bill = generateBillForCustomer(order.getCustomer());
                displayBill(bill);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleEditOrder(ActionEvent event) {
        Order selectedOrder = tableView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            Dialog<Order> dialog = new Dialog<>();
            dialog.setTitle("Edit Order");
            dialog.setHeaderText("Edit the details of the order");

            ButtonType editButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

            VBox vbox = new VBox(10);

            TextField customerField = new TextField(selectedOrder.getCustomer());
            vbox.getChildren().add(new Label("Customer:"));
            vbox.getChildren().add(customerField);

            Map<TextField, TextField> bookFields = new HashMap<>();
            final int[] row = {1};
            GridPane bookGrid = new GridPane();
            bookGrid.setHgap(10);
            bookGrid.setVgap(10);
            for (Map.Entry<String, Integer> entry : selectedOrder.getBooks().entrySet()) {
                TextField bookIdField = new TextField(entry.getKey());
                TextField quantityField = new TextField(entry.getValue().toString());
                bookGrid.add(new Label("Book ID:"), 0, row[0]);
                bookGrid.add(bookIdField, 1, row[0]);
                bookGrid.add(new Label("Quantity:"), 2, row[0]);
                bookGrid.add(quantityField, 3, row[0]);
                bookFields.put(bookIdField, quantityField);
                row[0]++;
            }

            Button addBookButton = new Button("Add Book");
            vbox.getChildren().add(bookGrid);
            vbox.getChildren().add(addBookButton);

            Label totalPriceLabel = new Label(String.format("Total Price: €%.2f", selectedOrder.getTotalPrice()).replace('.', ','));
            vbox.getChildren().add(totalPriceLabel);

            addBookButton.setOnAction(e -> {
                row[0]++;
                TextField newBookIdField = new TextField();
                newBookIdField.setPromptText("Book ID");
                TextField newQuantityField = new TextField();
                newQuantityField.setPromptText("Quantity");

                newBookIdField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(bookFields, totalPriceLabel));
                newQuantityField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice(bookFields, totalPriceLabel));

                bookGrid.add(new Label("Book ID:"), 0, row[0]);
                bookGrid.add(newBookIdField, 1, row[0]);
                bookGrid.add(new Label("Quantity:"), 2, row[0]);
                bookGrid.add(newQuantityField, 3, row[0]);

                bookFields.put(newBookIdField, newQuantityField);
            });

            ScrollPane scrollPane = new ScrollPane(vbox);
            scrollPane.setFitToWidth(true);
            dialog.getDialogPane().setContent(scrollPane);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == editButtonType) {
                    String customer = customerField.getText();
                    Map<String, Integer> books = new HashMap<>();
                    for (Map.Entry<TextField, TextField> entry : bookFields.entrySet()) {
                        String bookId = entry.getKey().getText();
                        int quantity = Integer.parseInt(entry.getValue().getText());
                        books.put(bookId, quantity);
                    }
                    double totalPrice = calculateTotalPrice(books);
                    return new Order(selectedOrder.getId(), customer, books, totalPrice);
                }
                return null;
            });

            Optional<Order> result = dialog.showAndWait();
            result.ifPresent(order -> {
                try {
                    DatabaseUtil.updateOrder(order.getId(), order.getCustomer(), order.getBooks(), order.getTotalPrice());
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
            Parent root = FXMLLoader.load(getClass().getResource(previousView + ".fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1080, 640));
            stage.show();
        } catch (IOException e) {
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
                displayBill(bill);
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Bill generateBillForCustomer(String customer) throws SQLException {
        ResultSet rs = DatabaseUtil.getOrdersByCustomer(customer);
        Map<String, Integer> books = new HashMap<>();
        Map<String, String> bookTitles = new HashMap<>();
        double totalPrice = 0.0;

        while (rs.next()) {
            String bookId = rs.getString("bookId");
            int quantity = rs.getInt("quantity");
            books.put(bookId, books.getOrDefault(bookId, 0) + quantity);
            bookTitles.put(bookId, DatabaseUtil.getBookTitleById(bookId));
            totalPrice += DatabaseUtil.getBookPriceById(bookId) * quantity;
        }

        return new Bill(customer, books, bookTitles, totalPrice);
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

    private void calculateTotalPrice(Map<TextField, TextField> bookFields, Label totalPriceLabel) {
        double totalPrice = 0.0;
        for (Map.Entry<TextField, TextField> entry : bookFields.entrySet()) {
            try {
                String bookId = entry.getKey().getText();
                int quantity = Integer.parseInt(entry.getValue().getText());
                double price = DatabaseUtil.getBookPriceById(bookId);
                totalPrice += price * quantity;
            } catch (NumberFormatException | SQLException e) {
                // Handle exception
            }
        }
        totalPriceLabel.setText(String.format("Total Price: €%.2f", totalPrice).replace('.', ','));
    }

    private double calculateTotalPrice(Map<String, Integer> books) {
        double totalPrice = 0.0;
        for (Map.Entry<String, Integer> entry : books.entrySet()) {
            try {
                double price = DatabaseUtil.getBookPriceById(entry.getKey());
                totalPrice += price * entry.getValue();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return totalPrice;
    }

    private double parsePrice(String priceString) {
        try {
            NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);
            Number number = format.parse(priceString);
            return number.doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}