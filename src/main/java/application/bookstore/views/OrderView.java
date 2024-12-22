package application.bookstore.views;

import application.bookstore.controllers.ControllerCommon;
import application.bookstore.controllers.OrderController;
import application.bookstore.models.Book;
import application.bookstore.models.BookOrder;
import application.bookstore.models.Order;
import application.bookstore.ui.ClearButton;
import application.bookstore.ui.CreateButton;
import application.bookstore.ui.ProfileButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;

public class OrderView extends View {
    private final BorderPane mainPane = new BorderPane();

    private Order order;
    private final Tab tab;

    private final TableView<BookOrder> tableView = new TableView<>();
    private final TableColumn<BookOrder, Integer> noCol = new TableColumn<>("Quantity");
    private final TableColumn<BookOrder, String> isbnCol = new TableColumn<>("ISBN");
    private final TableColumn<BookOrder, String> titleCol = new TableColumn<>("Title");
    private final TableColumn<BookOrder, Float> priceCol = new TableColumn<>("Unit Price");
    private final TableColumn<BookOrder, Float> totalPriceCol = new TableColumn<>("Total Price");
    private final TableColumn<BookOrder, String> authorCol = new TableColumn<>("Author");
    private final TableColumn selectorCol = new TableColumn<>("");
    private final TableColumn selectorCol_ = new TableColumn<>("");

    private final HBox formPane = new HBox();
    private final TextField nameField = new TextField();
    private final Button createBtn = new CreateButton();
    private final Button clearBtn = new ClearButton();
    private final Label totalValueLabel = new Label("0");
    private final Label totalLabel = new Label("Total: ", totalValueLabel);

    private final Label messageLabel = new Label("");

    private final BookView existingBooksView;
    private final Parent existingBooksViewPane;
    private final boolean advanced; // if the bookview allows editing

    public OrderView(MainView mainView, Stage mainStage, Tab tab) {
        this(mainView, mainStage, tab, false);
    }

    public OrderView(MainView mainView, Stage mainStage, Tab tab, boolean advanced) {
        this.tab = tab;
        this.advanced = advanced;

        order = new Order();
        existingBooksView = new BookView(advanced, true);
        existingBooksViewPane = existingBooksView.getView();
        new OrderController(this, mainStage);
    }

    @Override
    public Parent getView() {
        setForm();
        setTableView();

        ControllerCommon.showSuccessMessage(messageLabel, "Click on the checkbox add/remove a book.");

        VBox tables = new VBox();
        tables.setAlignment(Pos.CENTER);
        VBox.setVgrow(existingBooksViewPane, Priority.ALWAYS);
        VBox.setVgrow(tableView, Priority.ALWAYS); // make the tables expand
        tables.getChildren().add(existingBooksViewPane);
        tables.getChildren().add(tableView);

        messageLabel.setTextAlignment(TextAlignment.CENTER);
        VBox controls = new VBox();
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(5);
        controls.getChildren().addAll(formPane, messageLabel);

        mainPane.setCenter(tables);
        mainPane.setBottom(controls);

        return mainPane;
    }

    private void setForm() {
        formPane.setPadding(new Insets(20));
        formPane.setSpacing(20);
        formPane.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Client Full Name: ", nameField);
        nameLabel.setContentDisplay(ContentDisplay.TOP);
        totalLabel.setContentDisplay(ContentDisplay.RIGHT);
        Pane spacer = new Pane();
        spacer.setMinWidth(totalLabel.getWidth() + 40);
        formPane.getChildren().addAll(spacer, clearBtn, nameLabel, createBtn, totalLabel);
    }

    private void setTableView() {
        selectorCol.setGraphic(new ImageView(String.valueOf(ProfileButton.class.getResource("/images/selector.png"))));
        selectorCol.setMinWidth(30);
        selectorCol.setMaxWidth(30);;
        selectorCol_.setGraphic(new ImageView(String.valueOf(ProfileButton.class.getResource("/images/selector.png"))));
        selectorCol_.setMinWidth(30);
        selectorCol_.setMaxWidth(30);


        selectorCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<BookOrder, CheckBox>, ObservableValue<CheckBox>>() {
            @Override
            public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<BookOrder, CheckBox> val) {
                BookOrder bookOrder = val.getValue();
                CheckBox checkBox = new CheckBox();
                checkBox.selectedProperty().setValue(true);
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {
                        if (!new_val){
                            existingBooksView.getTableView().getItems().add(bookOrder.getBook());
                            tableView.getItems().remove(bookOrder);
                            order.getBooksOrdered().remove(bookOrder);
                        }
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        });


        selectorCol_.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Book, CheckBox>, ObservableValue<CheckBox>>() {
            @Override
            public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Book, CheckBox> val) {
                CheckBox checkBox = new CheckBox();
                Book b = val.getValue();
                BookOrder bookOrder = new BookOrder(1, val.getValue());
                checkBox.selectedProperty().setValue(false);
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {
                        if (new_val){
                            bookOrder.setQuantity(1);
                            if (bookOrder.getQuantity() > bookOrder.getBook().getQuantity()){
                                ControllerCommon.showErrorMessage(getResultLabel(), "There are not enough books in stock! Currently there are " + b.getQuantity() + " available.");
                                checkBox.selectedProperty().setValue(false);
                            }
                            else {
                                order.getBooksOrdered().add(bookOrder);
                                tableView.getItems().add(bookOrder);
                                existingBooksView.getTableView().getItems().remove(b);
                                getTotalValueLabel().setText(((Float) getOrder().getTotal()).toString());
                            }
                        }
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        });


        selectorCol.setSortable(false);
        selectorCol_.setSortable(false);

        // we save a copy to not affect other orders or the original books
        ArrayList<Book> copyOfBooks =  new ArrayList<>(Book.getBooks());
        existingBooksView.getTableView().setItems(FXCollections.observableArrayList(copyOfBooks));
        existingBooksView.getTableView().getColumns().add(0, selectorCol_);

        existingBooksView.getTableView().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setEditable(true);
        tableView.setMinHeight(200);
        tableView.setItems(FXCollections.observableArrayList(order.getBooksOrdered()));

        noCol.setEditable(true);
        noCol.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );
        noCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        isbnCol.setEditable(false);
        isbnCol.setCellValueFactory(
                new PropertyValueFactory<>("bookISBN")
        );
        titleCol.setEditable(false);
        titleCol.setCellValueFactory(
                new PropertyValueFactory<>("title")
        );
        priceCol.setEditable(false);
        priceCol.setCellValueFactory(
                new PropertyValueFactory<>("unitPrice")
        );
        totalPriceCol.setEditable(false);
        totalPriceCol.setCellValueFactory(
                new PropertyValueFactory<>("totalPrice")
        );
        authorCol.setEditable(false);
        authorCol.setCellValueFactory(
                new PropertyValueFactory<>("author")
        );
        tableView.getColumns().addAll(selectorCol, noCol, isbnCol, titleCol, priceCol, totalPriceCol, authorCol);
    }


    public Label getTotalValueLabel() {
        return totalValueLabel;
    }

    public Button getCreateBtn() {
        return createBtn;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order=order;
    }

    public TableColumn<BookOrder, Integer> getNoCol() {
        return noCol;
    }

    public Label getResultLabel() {
        return messageLabel;
    }

    public BookView getExistingBooksView() {
        return existingBooksView;
    }

    public Button getClearBtn() {
        return clearBtn;
    }


    public TextField getNameField() {
        return nameField;
    }

    public TableView<BookOrder> getTableView() {
        return tableView;
    }


    public Label getMessageLabel() {
        return messageLabel;
    }
}
