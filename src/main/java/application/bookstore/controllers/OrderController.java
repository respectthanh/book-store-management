package application.bookstore.controllers;

import application.bookstore.models.Item;
import application.bookstore.models.ItemOrder;
import application.bookstore.models.Order;
import application.bookstore.ui.PrintWindow;
import application.bookstore.views.OrderView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.List;

public class OrderController {
    private final OrderView orderView;
    private final Stage mainStage;

    public OrderController(OrderView orderView, Stage mainStage) {
        this.mainStage = mainStage;
        this.orderView = orderView;

        setEditListener();
        setChooseItemListener();
        setRemoveItemListener();
        setCreateListener();
        setClearListener();
        setSearchListener();
    }

    private ObservableList<Item> uniqueItems(ObservableList<Item> originalItems){
        ObservableList<Item> uItems = FXCollections.observableArrayList();
        for (Item b:originalItems) {
            boolean flag = false;
            for (ItemOrder bo : orderView.getTableView().getItems()) {
                if (bo.getItem().getIsbn().matches(b.getIsbn())) {
                    flag = true;
                    break;
                }
            }
            if (!flag)
                uItems.add(b);
        }
        return uItems;
    }

    private void setSearchListener(){
        orderView.getExistingItemsView().getSearchView().getClearBtn().setOnAction(e -> {
            orderView.getExistingItemsView().getSearchView().getSearchField().setText("");
            orderView.getExistingItemsView().getTableView().setItems(uniqueItems(Item.getItems()));
        });
        orderView.getExistingItemsView().getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = orderView.getExistingItemsView().getSearchView().getSearchField().getText();
            orderView.getExistingItemsView().getTableView().setItems(uniqueItems(Item.getSearchResults(searchText)));
        });
    }


    private void setChooseItemListener() {
        //implemented inside checkbox in OrderView
    }

    private void setRemoveItemListener() {
        //implemented inside checkbox in OrderView
    }

    private void setClearListener() {
        orderView.getClearBtn().setOnMousePressed(e -> clearOrder());
    }

    private void setEditListener() {
        orderView.getNoCol().setOnEditCommit(e -> {
            ItemOrder orderToEdit = e.getRowValue();
            int oldVal = orderToEdit.getQuantity();
            orderToEdit.setQuantity(e.getNewValue());
            if (orderToEdit.getQuantity() > 0) {
                if (orderToEdit.getQuantity() <= orderToEdit.getItem().getQuantity()) {
                    orderView.getTotalValueLabel().setText(((Float) orderView.getOrder().getTotal()).toString());
                    int index=orderView.getTableView().getItems().indexOf(orderToEdit);
                    orderView.getTableView().getItems().set(index, orderToEdit);
                } else {
                    orderToEdit.setQuantity(oldVal);
                    orderView.getTableView().getItems().set(orderView.getTableView().getItems().indexOf(orderToEdit), orderToEdit);
                    ControllerCommon.showErrorMessage(orderView.getResultLabel(), "There are not enough items in stock! Currently there are " + orderToEdit.getItem().getQuantity() + " available.");
                }
            } else {
                orderToEdit.setQuantity(oldVal);
                orderView.getTableView().getItems().set(orderView.getTableView().getItems().indexOf(orderToEdit), orderToEdit);
                ControllerCommon.showErrorMessage(orderView.getResultLabel(), "Edit value invalid!\n"+"Quantity cannot be negative.");
            }
        });

    }

    private void setCreateListener() {
        orderView.getCreateBtn().setOnMousePressed(e -> {
            orderView.getOrder().completeOrder(orderView.getCurrentUser().getUsername(), orderView.getNameField().getText());
            String saveResult = orderView.getOrder().saveInFile();
            if (saveResult.matches("1")) {
                changeStock();
                new PrintWindow(mainStage, orderView, orderView.getOrder(), this);
                ControllerCommon.showSuccessMessage(orderView.getResultLabel(), "Order created successfully");
            } else {
                ControllerCommon.showErrorMessage(orderView.getResultLabel(), "Order  creation failed!\n" + saveResult);
            }

        });
    }

    private void changeStock(){
        for (ItemOrder b : orderView.getTableView().getItems()) {
            Item updatedItem = b.getItem().clone();
            updatedItem.setQuantity(b.getItem().getQuantity() - b.getQuantity());
            updatedItem.updateInFile(b.getItem());
            b.setItem(updatedItem);
        } // change stock quantity
    }


    private void removeFromOrder(ItemOrder b){
        orderView.getExistingItemsView().getTableView().getItems().add(b.getItem());
        orderView.getTableView().getItems().remove(b);
        orderView.getTotalValueLabel().setText(((Float) orderView.getOrder().getTotal()).toString());
    }

    private void clearOrder() {
        orderView.getOrder().getItemsOrdered().clear();
        orderView.getNameField().setText(""); // reset fields
        List<ItemOrder> elementsToRemove = List.copyOf(orderView.getTableView().getItems());
        for (ItemOrder b : elementsToRemove) {
            removeFromOrder(b);
        }
    }

    public void resetFields() {
        orderView.setOrder(new Order()); // create a new order
        clearOrder(); // clear previous order
    }
}