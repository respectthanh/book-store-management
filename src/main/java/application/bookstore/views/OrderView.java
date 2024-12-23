package application.bookstore.views;

import application.bookstore.controllers.ControllerCommon;
import application.bookstore.controllers.OrderController;
import application.bookstore.models.Item;
import application.bookstore.models.ItemOrder;
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

    private final TableView<ItemOrder> tableView = new TableView<>();
    private final TableColumn<ItemOrder, Integer> noCol = new TableColumn<>("Quantity");
    private final TableColumn<ItemOrder, String> isbnCol = new TableColumn<>("ISBN");
    private final TableColumn<ItemOrder, String> titleCol = new TableColumn<>("Title");
    private final TableColumn<ItemOrder, Float> priceCol = new TableColumn<>("Unit Price");
    private final TableColumn<ItemOrder, Float> totalPriceCol = new TableColumn<>("Total Price");
    private final TableColumn<ItemOrder, String> authorCol = new TableColumn<>("Author");
    private final TableColumn selectorCol = new TableColumn<>("");
    private final TableColumn selectorCol_ = new TableColumn<>("");

    private final HBox formPane = new HBox();
    private final TextField nameField = new TextField();
    private final Button createBtn = new CreateButton();
    private final Button clearBtn = new ClearButton();
    private final Label totalValueLabel = new Label("0");
    private final Label totalLabel = new Label("Total: ", totalValueLabel);

    private final Label messageLabel = new Label("");

    private final ItemView existingItemsView;
    private final Parent existingItemsViewPane;
    private final boolean advanced; // if the itemview allows editing

    public OrderView(MainView mainView, Stage mainStage, Tab tab) {
        this(mainView, mainStage, tab, false);
    }

    public OrderView(MainView mainView, Stage mainStage, Tab tab, boolean advanced) {
        this.tab = tab;
        this.advanced = advanced;

        order = new Order();
        existingItemsView = new ItemView(advanced, true);
        existingItemsViewPane = existingItemsView.getView();
        new OrderController(this, mainStage);
    }

    @Override
    public Parent getView() {
        setForm();
        setTableView();

        ControllerCommon.showSuccessMessage(messageLabel, "Click on the checkbox add/remove a item.");

        VBox tables = new VBox();
        tables.setAlignment(Pos.CENTER);
        VBox.setVgrow(existingItemsViewPane, Priority.ALWAYS);
        VBox.setVgrow(tableView, Priority.ALWAYS); // make the tables expand
        tables.getChildren().add(existingItemsViewPane);
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


        selectorCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ItemOrder, CheckBox>, ObservableValue<CheckBox>>() {
            @Override
            public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<ItemOrder, CheckBox> val) {
                ItemOrder itemOrder = val.getValue();
                CheckBox checkBox = new CheckBox();
                checkBox.selectedProperty().setValue(true);
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {
                        if (!new_val){
                            existingItemsView.getTableView().getItems().add(itemOrder.getItem());
                            tableView.getItems().remove(itemOrder);
                            order.getItemsOrdered().remove(itemOrder);
                        }
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        });


        selectorCol_.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Item, CheckBox>, ObservableValue<CheckBox>>() {
            @Override
            public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Item, CheckBox> val) {
                CheckBox checkBox = new CheckBox();
                Item b = val.getValue();
                ItemOrder itemOrder = new ItemOrder(1, val.getValue());
                checkBox.selectedProperty().setValue(false);
                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {
                        if (new_val){
                            itemOrder.setQuantity(1);
                            if (itemOrder.getQuantity() > itemOrder.getItem().getQuantity()){
                                ControllerCommon.showErrorMessage(getResultLabel(), "There are not enough items in stock! Currently there are " + b.getQuantity() + " available.");
                                checkBox.selectedProperty().setValue(false);
                            }
                            else {
                                order.getItemsOrdered().add(itemOrder);
                                tableView.getItems().add(itemOrder);
                                existingItemsView.getTableView().getItems().remove(b);
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

        // we save a copy to not affect other orders or the original items
        ArrayList<Item> copyOfItems =  new ArrayList<>(Item.getItems());
        existingItemsView.getTableView().setItems(FXCollections.observableArrayList(copyOfItems));
        existingItemsView.getTableView().getColumns().add(0, selectorCol_);

        existingItemsView.getTableView().getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setEditable(true);
        tableView.setMinHeight(200);
        tableView.setItems(FXCollections.observableArrayList(order.getItemsOrdered()));

        noCol.setEditable(true);
        noCol.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );
        noCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        isbnCol.setEditable(false);
        isbnCol.setCellValueFactory(
                new PropertyValueFactory<>("itemISBN")
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

    public TableColumn<ItemOrder, Integer> getNoCol() {
        return noCol;
    }

    public Label getResultLabel() {
        return messageLabel;
    }

    public ItemView getExistingItemsView() {
        return existingItemsView;
    }

    public Button getClearBtn() {
        return clearBtn;
    }


    public TextField getNameField() {
        return nameField;
    }

    public TableView<ItemOrder> getTableView() {
        return tableView;
    }


    public Label getMessageLabel() {
        return messageLabel;
    }
}