package application.bookstore.controllers;

import application.bookstore.models.Author;
import application.bookstore.models.Book;
import application.bookstore.models.User;
import application.bookstore.views.BookView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class BookController {
    private final BookView view;

    public BookController(BookView bookView, boolean customSearch) {
        this.view = bookView;

        setComboBoxListener();
        if (!customSearch)
            setSearchListener();

        if (bookView.isAllowEdit()) {
            setSaveListener();
            setDeleteListener();
            setEditListener();
            bookView.getTableView().setEditable(true);
        }
    }

    private void setSearchListener() {
        view.getSearchView().getClearBtn().setOnAction(e -> {
            view.getSearchView().getSearchField().setText("");
            view.getTableView().setItems(FXCollections.observableArrayList(Book.getBooks()));
        });
        view.getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = view.getSearchView().getSearchField().getText();
            ObservableList<Book> searchResults = Book.getSearchResults(searchText);
            view.getTableView().setItems(searchResults);
        });
    }

    private void setComboBoxListener() {
        view.getAuthorsComboBox().setOnMouseClicked(e -> {
            view.getAuthorsComboBox().getItems().setAll(Author.getAuthors());
            // set default selected the first author
            if (!Author.getAuthors().isEmpty())
                view.getAuthorsComboBox().setValue(Author.getAuthors().get(0));
        });
    }

    private void setSaveListener() {
        view.getSaveBtn().setOnAction(e -> {
            String isbn = view.getIsbnField().getText();
            String title = view.getTitleField().getText();
            int quantity = Integer.parseInt(view.getQuantityField().getText());
            float purchasedPrice = Float.parseFloat(view.getPurchasedPriceField().getText());
            float sellingPrice = Float.parseFloat(view.getSellingPriceField().getText());
            Author author = view.getAuthorsComboBox().getValue();
            Book book = new Book(isbn, title, quantity, purchasedPrice, sellingPrice, author);

            String res = book.saveInFile();
            if (res.matches("1")) {
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Book created successfully!");
                resetFields();
            } else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Book creation failed!\n" + res);
        });
    }

    private void setDeleteListener() {
        view.getDeleteBtn().setOnAction(e -> {
            List<Book> itemsToDelete = List.copyOf(view.getTableView().getSelectionModel().getSelectedItems());
            for (Book b : itemsToDelete) {
                String res = b.deleteFromFile();
                if (res.matches("1")) {
                    ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Book removed successfully");
                } else {
                    ControllerCommon.showErrorMessage(view.getMessageLabel(), "Book deletion failed\n"+res);
                    break;
                }
            }
        });
    }

    private void setEditListener() {
        view.getIsbnCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            Book editedBook = bookToEdit.clone();
            editedBook.setIsbn(e.getNewValue());
            if (!editedBook.getIsbn().equals(bookToEdit.getIsbn())) {
                if (editedBook.exists()) {
                    Book.getBooks().set(Book.getBooks().indexOf(bookToEdit), bookToEdit);
                    ControllerCommon.showErrorMessage(view.getMessageLabel(), "Book with this ISBN Exists!");
                } else {
                    String res = editedBook.updateInFile(bookToEdit);
                    if (res.matches("1"))
                        ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
                    else
                        ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
                }
            }
        });

        view.getTitleCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            Book editedBook = bookToEdit.clone();
            editedBook.setTitle(e.getNewValue());
            String res = editedBook.updateInFile(bookToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });

        view.getQuantityCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            Book editedBook = bookToEdit.clone();
            editedBook.setQuantity(e.getNewValue());
            String res = editedBook.updateInFile(bookToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });

        view.getPurchasedPriceCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            Book editedBook = bookToEdit.clone();
            editedBook.setPurchasedPrice(e.getNewValue());
            String res = editedBook.updateInFile(bookToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });

        view.getSellingPriceCol().setOnEditCommit(e -> {
            Book bookToEdit = e.getRowValue();
            Book editedBook = bookToEdit.clone();
            editedBook.setSellingPrice(e.getNewValue());
            String res = editedBook.updateInFile(bookToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });


    }

    private void resetFields() {
        view.getIsbnField().setText("");
        view.getTitleField().setText("");
        view.getPurchasedPriceField().setText("");
        view.getSellingPriceField().setText("");
        view.getQuantityField().setText("");
    }
}
