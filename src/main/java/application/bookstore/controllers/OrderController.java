package application.bookstore.controllers;

import application.bookstore.models.Book;
import application.bookstore.models.BookOrder;
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
        setChooseBookListener();
        setRemoveBookListener();
        setCreateListener();
        setClearListener();
        setSearchListener();
    }

    private ObservableList<Book> uniqueBooks(ObservableList<Book> originalBooks){
        ObservableList<Book> uBooks = FXCollections.observableArrayList();
        for (Book b:originalBooks) {
            boolean flag = false;
            for (BookOrder bo : orderView.getTableView().getItems()) {
                if (bo.getBook().getIsbn().matches(b.getIsbn())) {
                    flag = true;
                    break;
                }
            }
            if (!flag)
                uBooks.add(b);
        }
        return uBooks;
    }

    private void setSearchListener(){
        orderView.getExistingBooksView().getSearchView().getClearBtn().setOnAction(e -> {
            orderView.getExistingBooksView().getSearchView().getSearchField().setText("");
            orderView.getExistingBooksView().getTableView().setItems(uniqueBooks(Book.getBooks()));
        });
        orderView.getExistingBooksView().getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = orderView.getExistingBooksView().getSearchView().getSearchField().getText();
            orderView.getExistingBooksView().getTableView().setItems(uniqueBooks(Book.getSearchResults(searchText)));
        });
    }


    private void setChooseBookListener() {
        //implemented inside checkbox in OrderView
    }

    private void setRemoveBookListener() {
        //implemented inside checkbox in OrderView
    }

    private void setClearListener() {
        orderView.getClearBtn().setOnMousePressed(e -> clearOrder());
    }

    private void setEditListener() {
        orderView.getNoCol().setOnEditCommit(e -> {
            BookOrder orderToEdit = e.getRowValue();
            int oldVal = orderToEdit.getQuantity();
            orderToEdit.setQuantity(e.getNewValue());
            if (orderToEdit.getQuantity() > 0) {
                if (orderToEdit.getQuantity() <= orderToEdit.getBook().getQuantity()) {
                    orderView.getTotalValueLabel().setText(((Float) orderView.getOrder().getTotal()).toString());
                    int index=orderView.getTableView().getItems().indexOf(orderToEdit);
                    orderView.getTableView().getItems().set(index, orderToEdit);
                } else {
                    orderToEdit.setQuantity(oldVal);
                    orderView.getTableView().getItems().set(orderView.getTableView().getItems().indexOf(orderToEdit), orderToEdit);
                    ControllerCommon.showErrorMessage(orderView.getResultLabel(), "There are not enough books in stock! Currently there are " + orderToEdit.getBook().getQuantity() + " available.");
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
        for (BookOrder b : orderView.getTableView().getItems()) {
            Book updatedBook = b.getBook().clone();
            updatedBook.setQuantity(b.getBook().getQuantity() - b.getQuantity());
            updatedBook.updateInFile(b.getBook());
            b.setBook(updatedBook);
        } // change stock quantity
    }


    private void removeFromOrder(BookOrder b){
        orderView.getExistingBooksView().getTableView().getItems().add(b.getBook());
        orderView.getTableView().getItems().remove(b);
        orderView.getTotalValueLabel().setText(((Float) orderView.getOrder().getTotal()).toString());
    }

    private void clearOrder() {
        orderView.getOrder().getBooksOrdered().clear();
        orderView.getNameField().setText(""); // reset fields
        List<BookOrder> elementsToRemove = List.copyOf(orderView.getTableView().getItems());
        for (BookOrder b : elementsToRemove) {
            removeFromOrder(b);
        }
    }

    public void resetFields() {
        orderView.setOrder(new Order()); // create a new order
        clearOrder(); // clear previous order
    }
}
