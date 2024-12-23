package application.bookstore.views;

import application.bookstore.controllers.ItemController;
import application.bookstore.models.Author;
import application.bookstore.models.Item;
import application.bookstore.models.Role;
import application.bookstore.ui.CreateButton;
import application.bookstore.ui.DeleteButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;


public class ItemView extends View {
    private final BorderPane mainPane = new BorderPane();

    private final SearchView searchView = new SearchView("Search for a item");

    private final TableView<Item> tableView = new TableView<>();
    private final TableColumn<Item, String> isbnCol = new TableColumn<>("ISBN");
    private final TableColumn<Item, String> titleCol = new TableColumn<>("Title");
    private final TableColumn<Item, Integer> quantityCol = new TableColumn<>("Quantity");
    private final TableColumn<Item, Float> purchasedPriceCol = new TableColumn<>("Purchased Price");
    private final TableColumn<Item, Float> sellingPriceCol = new TableColumn<>("Selling Price");
    private final TableColumn<Item, String> authorCol = new TableColumn<>("Author");

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

    public ItemView() {
        this(true, false);
    }

    public ItemView(boolean allowEdit, boolean customSearch) {
        this.allowEdit = allowEdit;
        setTableView();
        setForm();
        new ItemController(this, customSearch);
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
        tableView.setItems(Item.getItems());

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




    public TableView<Item> getTableView() {
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

    public TableColumn<Item, String> getIsbnCol() {
        return isbnCol;
    }

    public TableColumn<Item, String> getTitleCol() {
        return titleCol;
    }

    public TableColumn<Item, Float> getPurchasedPriceCol() {
        return purchasedPriceCol;
    }

    public TableColumn<Item, Float> getSellingPriceCol() {
        return sellingPriceCol;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }

    public TextField getQuantityField() {
        return quantityField;
    }

    public TableColumn<Item, Integer> getQuantityCol() {
        return quantityCol;
    }

    public TableColumn<Item, String> getAuthorCol() {
        return authorCol;
    }

    public boolean isAllowEdit() {
        return allowEdit;
    }

    public SearchView getSearchView() {
        return searchView;
    }
}