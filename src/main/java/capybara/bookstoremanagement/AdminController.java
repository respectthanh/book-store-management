package capybara.bookstoremanagement;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class AdminController {

    @FXML
    private void handleManageEmployees(ActionEvent event) {
        navigateToViewWithPrevious(event, "manage_employees", "admin_view");
    }

    @FXML
    private void handleManageItems(ActionEvent event) {
        navigateToViewWithPrevious(event, "manage_items", "admin_view");
    }

    @FXML
    private void handleManageCustomers(ActionEvent event) {
        navigateToViewWithPrevious(event, "manage_customers", "admin_view");
    }

    @FXML
    private void handleManageOrders(ActionEvent event) {
        navigateToViewWithPrevious(event, "manage_orders", "admin_view");
    }

    // private void navigateToView(ActionEvent event, String viewName) {
    //     try {
    //         Parent root = FXMLLoader.load(getClass().getResource(viewName + ".fxml"));
    //         Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
    //         stage.setScene(new Scene(root, 640, 540));
    //         stage.show();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    private void navigateToViewWithPrevious(ActionEvent event, String viewName, String previousView) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewName + ".fxml"));
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller instanceof ManageItemsController) {
                ((ManageItemsController) controller).setPreviousView(previousView);
            } else if (controller instanceof ManageCustomersController) {
                ((ManageCustomersController) controller).setPreviousView(previousView);
            } else if (controller instanceof ManageEmployeesController) {
                ((ManageEmployeesController) controller).setPreviousView(previousView);
            } else if (controller instanceof ManageOrdersController) {
                ((ManageOrdersController) controller).setPreviousView(previousView);
            }
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1080, 640));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}