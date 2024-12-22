package application.bookstore.views;

import application.bookstore.controllers.AuthorController;
import application.bookstore.models.Author;
import application.bookstore.models.Role;
import application.bookstore.ui.CreateButton;
import application.bookstore.ui.DeleteButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

public class AuthorView extends View {
    private final BorderPane mainPane = new BorderPane();

    private final SearchView searchView = new SearchView("Search for an author");

    private final TableView<Author> tableView = new TableView<>();
    private final TableColumn<Author, String> firstNameCol = new TableColumn<>("First name");
    private final TableColumn<Author, String> lastNameCol = new TableColumn<>("Last name");

    private final HBox formPane = new HBox();
    private final TextField firstNameField = new TextField();
    private final TextField lastNameField = new TextField();
    private final Button saveBtn = new CreateButton();
    private final Button deleteBtn = new DeleteButton();

    private final Label messageLabel = new Label("");

    public AuthorView() {
        setTableView();
        setForm();
        new AuthorController(this);
    }

    @Override
    public Parent getView() {
        mainPane.setTop(searchView.getSearchPane());
        mainPane.setCenter(tableView);
        if ((super.getCurrentUser().getRole() == Role.ADMIN) || (super.getCurrentUser().getRole() == Role.MANAGER)) {
            messageLabel.setTextAlignment(TextAlignment.CENTER);
            VBox controls = new VBox();
            controls.setAlignment(Pos.CENTER);
            controls.setSpacing(5);
            controls.getChildren().addAll(formPane, messageLabel);
            mainPane.setBottom(controls);
        }
        return mainPane;
    }

    private void setForm() {
        formPane.setPadding(new Insets(20));
        formPane.setSpacing(20);
        formPane.setAlignment(Pos.CENTER);
        Label firstNameLabel = new Label("First name: ", firstNameField);
        firstNameLabel.setContentDisplay(ContentDisplay.TOP);
        Label lastNameLabel = new Label("Last name: ", lastNameField);
        lastNameLabel.setContentDisplay(ContentDisplay.TOP);
        formPane.getChildren().addAll(firstNameLabel, lastNameLabel, saveBtn, deleteBtn);
    }

    private void setTableView() {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setEditable(false);
        tableView.setItems(Author.getAuthors());

        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("firstName")
        );
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("lastName")
        );
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        tableView.getColumns().addAll(firstNameCol, lastNameCol);
    }


    public SearchView getSearchView() {
        return searchView;
    }

    public TableColumn<Author, String> getFirstNameCol() {
        return firstNameCol;
    }

    public TableColumn<Author, String> getLastNameCol() {
        return lastNameCol;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public TableView<Author> getTableView() {
        return tableView;
    }

    public TextField getFirstNameField() {
        return firstNameField;
    }

    public TextField getLastNameField() {
        return lastNameField;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }
}
