package application.bookstore.controllers;

import application.bookstore.models.Author;
import application.bookstore.models.Item;
import application.bookstore.models.User;
import application.bookstore.views.ItemView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ItemController {
    private final ItemView view;

    public ItemController(ItemView itemView, boolean customSearch) {
        this.view = itemView;

        setComboBoxListener();
        if (!customSearch)
            setSearchListener();

        if (itemView.isAllowEdit()) {
            setSaveListener();
            setDeleteListener();
            setEditListener();
            itemView.getTableView().setEditable(true);
        }
    }

    private void setSearchListener() {
        view.getSearchView().getClearBtn().setOnAction(e -> {
            view.getSearchView().getSearchField().setText("");
            view.getTableView().setItems(FXCollections.observableArrayList(Item.getItems()));
        });
        view.getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = view.getSearchView().getSearchField().getText();
            ObservableList<Item> searchResults = Item.getSearchResults(searchText);
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
            Item item = new Item(isbn, title, quantity, purchasedPrice, sellingPrice, author);

            String res = item.saveInFile();
            if (res.matches("1")) {
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Item created successfully!");
                resetFields();
            } else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Item creation failed!\n" + res);
        });
    }

    private void setDeleteListener() {
        view.getDeleteBtn().setOnAction(e -> {
            List<Item> itemsToDelete = List.copyOf(view.getTableView().getSelectionModel().getSelectedItems());
            for (Item b : itemsToDelete) {
                String res = b.deleteFromFile();
                if (res.matches("1")) {
                    ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Item removed successfully");
                } else {
                    ControllerCommon.showErrorMessage(view.getMessageLabel(), "Item deletion failed\n"+res);
                    break;
                }
            }
        });
    }

    private void setEditListener() {
        view.getIsbnCol().setOnEditCommit(e -> {
            Item itemToEdit = e.getRowValue();
            Item editedItem = itemToEdit.clone();
            editedItem.setIsbn(e.getNewValue());
            if (!editedItem.getIsbn().equals(itemToEdit.getIsbn())) {
                if (editedItem.exists()) {
                    Item.getItems().set(Item.getItems().indexOf(itemToEdit), itemToEdit);
                    ControllerCommon.showErrorMessage(view.getMessageLabel(), "Item with this ISBN Exists!");
                } else {
                    String res = editedItem.updateInFile(itemToEdit);
                    if (res.matches("1"))
                        ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
                    else
                        ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
                }
            }
        });

        view.getTitleCol().setOnEditCommit(e -> {
            Item itemToEdit = e.getRowValue();
            Item editedItem = itemToEdit.clone();
            editedItem.setTitle(e.getNewValue());
            String res = editedItem.updateInFile(itemToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });

        view.getQuantityCol().setOnEditCommit(e -> {
            Item itemToEdit = e.getRowValue();
            Item editedItem = itemToEdit.clone();
            editedItem.setQuantity(e.getNewValue());
            String res = editedItem.updateInFile(itemToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });

        view.getPurchasedPriceCol().setOnEditCommit(e -> {
            Item itemToEdit = e.getRowValue();
            Item editedItem = itemToEdit.clone();
            editedItem.setPurchasedPrice(e.getNewValue());
            String res = editedItem.updateInFile(itemToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });

        view.getSellingPriceCol().setOnEditCommit(e -> {
            Item itemToEdit = e.getRowValue();
            Item editedItem = itemToEdit.clone();
            editedItem.setSellingPrice(e.getNewValue());
            String res = editedItem.updateInFile(itemToEdit);
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