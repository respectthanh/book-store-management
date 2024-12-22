package application.bookstore.views;

import application.bookstore.controllers.BookController;
import application.bookstore.models.Author;
import application.bookstore.models.Book;
import application.bookstore.models.Role;
import application.bookstore.ui.CreateButton;
import application.bookstore.ui.DeleteButton;
import javafx.collections.FXCollections;
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
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;


public class BookView extends View {
    private final BorderPane mainPane = new BorderPane();

    private final SearchView searchView = new SearchView("Search for a book");

    private final TableView<Book> tableView = new TableView<>();
    private final TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
    private final TableColumn<Book, String> titleCol = new TableColumn<>("Title");
    private final TableColumn<Book, Integer> quantityCol = new TableColumn<>("Quantity");
    private final TableColumn<Book, Float> purchasedPriceCol = new TableColumn<>("Purchased Price");
    private final TableColumn<Book, Float> sellingPriceCol = new TableColumn<>("Selling Price");
    private final TableColumn<Book, String> authorCol = new TableColumn<>("Author");

    private final HBox formPane = new HBox();
    private final TextField isbnField = new TextField();
    private final TextField titleField = new TextField();
    private final TextField quantityField = new TextField();
    private final TextField purchasedPriceField = new TextField();
    private final TextField sellingPriceField = new TextField();
    private final ComboBox<Author> authorsComboBox = new ComboBox<>();
    private final Button saveBtn = new CreateButton();
    private final Button deleteBtn = new DeleteButton();

    private final Label messageLabel = new Label("");

    private final boolean allowEdit;

    public BookView() {
        this(true, false);
    }

    public BookView(boolean allowEdit, boolean customSearch) {
        this.allowEdit = allowEdit;
        setTableView();
        setForm();
        new BookController(this, customSearch);
    }



    @Override
    public Parent getView() {
        tableView.setMinHeight(200);
        mainPane.setTop(searchView.getSearchPane());
        mainPane.setCenter(tableView);
        if (((getCurrentUser().getRole() == Role.ADMIN) || (getCurrentUser().getRole() == Role.MANAGER)) && allowEdit) {
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
        Label isbnLabel = new Label("ISBN: ", isbnField);
        isbnLabel.setContentDisplay(ContentDisplay.TOP);
        Label titleLabel = new Label("Title: ", titleField);
        titleLabel.setContentDisplay(ContentDisplay.TOP);
        Label quantityLabel = new Label("Quantity: ", quantityField);
        quantityLabel.setContentDisplay(ContentDisplay.TOP);
        quantityField.setMaxWidth(100);
        Label purchasedPriceLabel = new Label("Purchased price", purchasedPriceField);
        purchasedPriceLabel.setContentDisplay(ContentDisplay.TOP);
        purchasedPriceField.setMaxWidth(130);
        Label sellingPriceLabel = new Label("Selling price", sellingPriceField);
        sellingPriceLabel.setContentDisplay(ContentDisplay.TOP);
        sellingPriceField.setMaxWidth(100);
        Label authorLabel = new Label("Author", authorsComboBox);
        authorsComboBox.getItems().setAll(Author.getAuthors());
        // set default selected the first author
        if (!Author.getAuthors().isEmpty())
            authorsComboBox.setValue(Author.getAuthors().get(0));
        authorLabel.setContentDisplay(ContentDisplay.TOP);
        formPane.getChildren().addAll(isbnLabel, titleLabel, quantityLabel, purchasedPriceLabel, sellingPriceLabel,
                authorLabel, saveBtn, deleteBtn);
    }

    private void setTableView() {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setEditable(false);
        tableView.setItems(Book.getBooks());

        isbnCol.setCellValueFactory(
                new PropertyValueFactory<>("isbn")
        );
        isbnCol.setCellFactory(TextFieldTableCell.forTableColumn());

        titleCol.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());

        quantityCol.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );
        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        purchasedPriceCol.setCellValueFactory(
                new PropertyValueFactory<>("purchasedPrice")
        );
        purchasedPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        sellingPriceCol.setCellValueFactory(
                new PropertyValueFactory<>("sellingPrice")
        );
        sellingPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));

        authorCol.setCellValueFactory(
                new PropertyValueFactory<>("author")
        );

        // currently not editable
//        ArrayList<String> authors = new ArrayList<String>();
//        for (Author a:Author.getAuthors())
//            authors.add(a.getFullName());
//        authorCol.setCellFactory(ComboBoxTableCell.forTableColumn(authors));

        tableView.getColumns().addAll(isbnCol, titleCol, quantityCol, purchasedPriceCol, sellingPriceCol, authorCol);
    }




    public TableView<Book> getTableView() {
        return tableView;
    }

    public TextField getIsbnField() {
        return isbnField;
    }

    public TextField getTitleField() {
        return titleField;
    }

    public TextField getPurchasedPriceField() {
        return purchasedPriceField;
    }

    public TextField getSellingPriceField() {
        return sellingPriceField;
    }

    public ComboBox<Author> getAuthorsComboBox() {
        return authorsComboBox;
    }

    public Button getSaveBtn() {
        return saveBtn;
    }

    public Button getDeleteBtn() {
        return deleteBtn;
    }

    public TableColumn<Book, String> getIsbnCol() {
        return isbnCol;
    }

    public TableColumn<Book, String> getTitleCol() {
        return titleCol;
    }

    public TableColumn<Book, Float> getPurchasedPriceCol() {
        return purchasedPriceCol;
    }

    public TableColumn<Book, Float> getSellingPriceCol() {
        return sellingPriceCol;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public TextField getQuantityField() {
        return quantityField;
    }

    public TableColumn<Book, Integer> getQuantityCol() {
        return quantityCol;
    }

    public TableColumn<Book, String> getAuthorCol() {
        return authorCol;
    }

    public boolean isAllowEdit() {
        return allowEdit;
    }

    public SearchView getSearchView() {
        return searchView;
    }
}
