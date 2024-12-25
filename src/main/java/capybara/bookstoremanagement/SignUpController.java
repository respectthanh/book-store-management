package capybara.bookstoremanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SignUpController {

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void handleSignUpAction() {
        // String studentId = studentIdField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Passwords do not match.");
            return;
        }

        if (DatabaseUtil.createUser(username, password)) {
            showAlert(Alert.AlertType.INFORMATION, "Sign Up Successful", "User " + username + " has been registered.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Sign Up Failed", "Could not register user.");
        }
    }

    // private boolean addUser(String studentId, String username, String password) {
    //     // Replace with actual database interaction logic
    //     // return DatabaseUtil.addUser(username, password);
    //     return true;
    // }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}