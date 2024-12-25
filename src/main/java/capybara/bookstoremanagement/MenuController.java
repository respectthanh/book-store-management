package capybara.bookstoremanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class MenuController {

    @FXML
    private void handleManageBooks(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("manage_books.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1080, 640));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageCustomers(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("manage_customers.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1080, 640));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageEmployees(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Enter Admin Password");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(passwordField, 0, 0);

        alert.getDialogPane().setContent(grid);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String password = passwordField.getText();
            if (password.equals("admin123")) { // Replace with your actual admin password
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("manage_employees.fxml"));
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root, 1080, 640));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Show an alert if the password is incorrect
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Authentication Failed");
                errorAlert.setHeaderText("Invalid Password");
                errorAlert.setContentText("The password you entered is incorrect.");
                errorAlert.showAndWait();
            }
        }
    }

    @FXML
    private void handleManageOrders(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("manage_orders.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1080, 640));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageItems(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("manage_items.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1080, 640));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
