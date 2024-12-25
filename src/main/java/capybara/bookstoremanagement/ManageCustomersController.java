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

public class ManageCustomersController {
    private String previousView;

    public void setPreviousView(String previousView) {
        this.previousView = previousView;
    }


    @FXML
    private TableView<Customer> tableView;
    @FXML
    private TableColumn<Customer, Integer> colId;
    @FXML
    private TableColumn<Customer, String> colName;
    @FXML
    private TableColumn<Customer, String> colEmail;
    @FXML
    private TableColumn<Customer, String> colPhone;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadCustomers();
    }

    private void loadCustomers() {
        tableView.getItems().clear();
        try {
            ResultSet rs = DatabaseUtil.getAllCustomers();
            while (rs.next()) {
                tableView.getItems().add(new Customer(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("phone")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddCustomer(ActionEvent event) {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");
        dialog.setHeaderText("Enter the details of the new customer");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the name, email, and phone labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Phone:"), 0, 2);
        grid.add(phoneField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a Customer object when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                return new Customer(0, name, email, phone);
            }
            return null;
        });

        Optional<Customer> result = dialog.showAndWait();
        result.ifPresent(customer -> {
            try {
                DatabaseUtil.createCustomer(customer.getName(), customer.getEmail(), customer.getPhone());
                loadCustomers();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleEditCustomer(ActionEvent event) {
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            Dialog<Customer> dialog = new Dialog<>();
            dialog.setTitle("Edit Customer");
            dialog.setHeaderText("Edit the details of the customer");

            // Set the button types
            ButtonType editButtonType = new ButtonType("Save", ButtonType.OK.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

            // Create the name, email, and phone labels and fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nameField = new TextField(selectedCustomer.getName());
            TextField emailField = new TextField(selectedCustomer.getEmail());
            TextField phoneField = new TextField(selectedCustomer.getPhone());

            grid.add(new Label("Name:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Email:"), 0, 1);
            grid.add(emailField, 1, 1);
            grid.add(new Label("Phone:"), 0, 2);
            grid.add(phoneField, 1, 2);

            dialog.getDialogPane().setContent(grid);

            // Convert the result to a Customer object when the Save button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == editButtonType) {
                    String name = nameField.getText();
                    String email = emailField.getText();
                    String phone = phoneField.getText();
                    return new Customer(selectedCustomer.getId(), name, email, phone);
                }
                return null;
            });

            Optional<Customer> result = dialog.showAndWait();
            result.ifPresent(customer -> {
                try {
                    DatabaseUtil.updateCustomer(customer.getId(), customer.getName(), customer.getEmail(), customer.getPhone());
                    loadCustomers();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void handleDeleteCustomer(ActionEvent event) {
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        DatabaseUtil.deleteCustomer(selectedCustomer.getId());
                        loadCustomers();
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
}
