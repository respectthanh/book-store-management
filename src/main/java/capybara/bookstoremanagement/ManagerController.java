package capybara.bookstoremanagement;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ManagerController {

    @FXML
    private void handleManageEmployees(ActionEvent event) {
        navigateToViewWithPrevious(event, "manage_employees", "manager_view");
    }

    private void navigateToViewWithPrevious(ActionEvent event, String viewName, String previousView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewName + ".fxml"));
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller instanceof ManageEmployeesController) {
                ((ManageEmployeesController) controller).setPreviousView(previousView);
            }
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1080, 640));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}