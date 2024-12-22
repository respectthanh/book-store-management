package application.bookstore.views;

import application.bookstore.controllers.SalesController;
import application.bookstore.models.Order;
import application.bookstore.ui.PrintButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class SalesView extends View {
    private static final PrintButton printButton = new PrintButton("View/Print");
    private final BorderPane mainPane = new BorderPane();
    private final SearchView searchView = new SearchView("Search for an order");
    private final TableView<Order> tableView = new TableView<>();
    private final TableColumn<Order, String> clientNameCol = new TableColumn<>("Client Name");
    private final TableColumn<Order, String> usernameCol = new TableColumn<>("Username");
    private final TableColumn<Order, Float> totalPriceCol = new TableColumn<>("Total");
    private final TableColumn<Order, String> dateCol = new TableColumn<>("Date");
    private final TableColumn<Order, String> orderIDCol = new TableColumn<>("Order ID");

    public SalesView(Stage mainStage) {
        setTableView();
        new SalesController(this, mainStage);
    }


    private void setTableView() {
        tableView.setMinHeight(200);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setEditable(false);
        tableView.setItems(Order.getOrders());
        clientNameCol.setCellValueFactory(
                new PropertyValueFactory<>("clientName")
        );
        usernameCol.setCellValueFactory(
                new PropertyValueFactory<>("username")
        );
        dateCol.setCellValueFactory(
                new PropertyValueFactory<>("date")
        );
        orderIDCol.setCellValueFactory(
                new PropertyValueFactory<>("orderID")
        );
        totalPriceCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getTotal()));

        tableView.getColumns().addAll(clientNameCol, usernameCol, totalPriceCol, dateCol, orderIDCol);
    }


    @Override
    public Parent getView() {
        HBox controls = new HBox();
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(10);
        controls.setPadding(new Insets(10));
        controls.getChildren().addAll(printButton);

        mainPane.setTop(searchView.getSearchPane());
        mainPane.setCenter(tableView);
        mainPane.setBottom(controls);
        return mainPane;
    }

    public SearchView getSearchView() {
        return searchView;
    }

    public TableView<Order> getTableView() {
        return tableView;
    }

    public Button getPrintButton() {
        return printButton;
    }
}
