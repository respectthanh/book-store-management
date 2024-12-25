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

public class ManageEmployeesController {
    private String previousView;

    public void setPreviousView(String previousView) {
        this.previousView = previousView;
    }


    @FXML
    private TableView<Employee> tableView;
    @FXML
    private TableColumn<Employee, Integer> colId;
    @FXML
    private TableColumn<Employee, String> colName;
    @FXML
    private TableColumn<Employee, String> colPosition;
    @FXML
    private TableColumn<Employee, Double> colSalary;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        loadEmployees();
    }

    private void loadEmployees() {
        tableView.getItems().clear();
        try {
            ResultSet rs = DatabaseUtil.getAllEmployees();
            while (rs.next()) {
                tableView.getItems().add(new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("position"), rs.getDouble("salary")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddEmployee(ActionEvent event) {
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Add New Employee");
        dialog.setHeaderText("Enter the details of the new employee");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the name, position, and salary labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField positionField = new TextField();
        positionField.setPromptText("Position");
        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Position:"), 0, 1);
        grid.add(positionField, 1, 1);
        grid.add(new Label("Salary:"), 0, 2);
        grid.add(salaryField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to an Employee object when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String name = nameField.getText();
                String position = positionField.getText();
                double salary = Double.parseDouble(salaryField.getText());
                return new Employee(0, name, position, salary);
            }
            return null;
        });

        Optional<Employee> result = dialog.showAndWait();
        result.ifPresent(employee -> {
            try {
                DatabaseUtil.createEmployee(employee.getName(), employee.getPosition(), employee.getSalary());
                loadEmployees();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleEditEmployee(ActionEvent event) {
        Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Dialog<Employee> dialog = new Dialog<>();
            dialog.setTitle("Edit Employee");
            dialog.setHeaderText("Edit the details of the employee");

            // Set the button types
            ButtonType editButtonType = new ButtonType("Save", ButtonType.OK.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

            // Create the name, position, and salary labels and fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField nameField = new TextField(selectedEmployee.getName());
            TextField positionField = new TextField(selectedEmployee.getPosition());
            TextField salaryField = new TextField(String.valueOf(selectedEmployee.getSalary()));

            grid.add(new Label("Name:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Position:"), 0, 1);
            grid.add(positionField, 1, 1);
            grid.add(new Label("Salary:"), 0, 2);
            grid.add(salaryField, 1, 2);

            dialog.getDialogPane().setContent(grid);

            // Convert the result to an Employee object when the Save button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == editButtonType) {
                    String name = nameField.getText();
                    String position = positionField.getText();
                    double salary = Double.parseDouble(salaryField.getText());
                    return new Employee(selectedEmployee.getId(), name, position, salary);
                }
                return null;
            });

            Optional<Employee> result = dialog.showAndWait();
            result.ifPresent(employee -> {
                try {
                    DatabaseUtil.updateEmployee(employee.getId(), employee.getName(), employee.getPosition(), employee.getSalary());
                    loadEmployees();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void handleDeleteEmployee(ActionEvent event) {
        Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this employee?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        DatabaseUtil.deleteEmployee(selectedEmployee.getId());
                        loadEmployees();
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
