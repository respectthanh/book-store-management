package application.bookstore.views;

import application.bookstore.controllers.UsersController;
import application.bookstore.models.Role;
import application.bookstore.models.User;
import application.bookstore.ui.CreateButton;
import application.bookstore.ui.DeleteButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class UsersView extends View {
    private final BorderPane mainPane = new BorderPane();

    private final SearchView searchView = new SearchView("Search for a user");

    private final TableView<User> tableView = new TableView<>();
    private final TableColumn<User, String> usernameCol = new TableColumn<>("Username");
    private final TableColumn<User, String> passwordCol = new TableColumn<>("Password");
    private final TableColumn<User, Role> roleCol = new TableColumn<>("Role");

    private final HBox formPane = new HBox();
    private final TextField userNameField = new TextField();
    private final TextField passwordField = new TextField();
    private final ComboBox<Role> roleComboBox = new ComboBox<>();
    private final Button saveBtn = new CreateButton();
    private final Button deleteBtn = new DeleteButton();

    private final Label messageLabel = new Label("");

    public UsersView() {
        setTableView();
        setForm();
        new UsersController(this);
    }

    @Override
    public Parent getView() {
        // user controls
        messageLabel.setTextAlignment(TextAlignment.CENTER);
        VBox controls = new VBox();
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(5);
        controls.getChildren().addAll(formPane, messageLabel);
        //
        mainPane.setTop(searchView.getSearchPane());
        mainPane.setCenter(tableView);
        mainPane.setBottom(controls);
        return mainPane;
    }

    private void setForm() {
        formPane.setPadding(new Insets(20));
        formPane.setSpacing(20);
        formPane.setAlignment(Pos.CENTER);
        Label usernameLabel = new Label("Username: ", userNameField);
        usernameLabel.setContentDisplay(ContentDisplay.TOP);
        Label passwordLabel = new Label("Password: ", passwordField);
        passwordLabel.setContentDisplay(ContentDisplay.TOP);
        Label roleLabel = new Label("Role", roleComboBox);
        roleComboBox.getItems().setAll(Role.ADMIN, Role.MANAGER, Role.LIBRARIAN);
        roleComboBox.setValue(Role.MANAGER);
        formPane.getChildren().addAll(usernameLabel, passwordLabel, roleLabel, saveBtn, deleteBtn);
    }

    private void setTableView() {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setEditable(true);
        tableView.setItems(User.getUsers());

        usernameCol.setCellValueFactory(
                new PropertyValueFactory<>("username")
        );
        usernameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordCol.setCellValueFactory(
                new PropertyValueFactory<>("password")
        );
        passwordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        roleCol.setCellFactory(ComboBoxTableCell.forTableColumn(Role.values()));

        tableView.getColumns().addAll(usernameCol, passwordCol, roleCol);
    }


    public ComboBox<Role> getRoleComboBox() {
        return roleComboBox;
    }

    public TextField getUserNameField() {
        return userNameField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public TableColumn<User, String> getPasswordCol() {
        return passwordCol;
    }

    public TableColumn<User, Role> getRoleCol() {
        return roleCol;
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public TableColumn<User, String> getUsernameCol() {
        return usernameCol;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public TableView<User> getTableView() {
        return tableView;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }
}
