package application.bookstore.controllers;

import application.bookstore.models.Author;
import application.bookstore.models.Role;
import application.bookstore.ui.DeleteAuthorDialog;
import application.bookstore.views.AuthorView;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.ArrayList;
import java.util.List;

public class AuthorController {
    private final AuthorView view;

    public AuthorController(AuthorView view) {
        this.view = view;
        Role currentRole = view.getCurrentUser().getRole();

        setSearchListener();

        if (currentRole == Role.MANAGER || currentRole == Role.ADMIN) {
            setSaveListener();
            setDeleteListener();
            setEditListener();
            view.getTableView().setEditable(true);
        }
    }

    private void setSearchListener() {
        view.getSearchView().getClearBtn().setOnAction(e -> {
            view.getSearchView().getSearchField().setText("");
            view.getTableView().setItems(FXCollections.observableArrayList(Author.getAuthors()));
        });
        view.getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = view.getSearchView().getSearchField().getText();
            ArrayList<Author> searchResults = Author.getSearchResults(searchText);
            view.getTableView().setItems(FXCollections.observableArrayList(searchResults));
        });
    }

    private void setSaveListener() {
        view.getSaveBtn().setOnAction(e -> {
            Author author = new Author(view.getFirstNameField().getText(), view.getLastNameField().getText());
            String res = author.saveInFile();
            if (res.matches("1")) {
                    ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Author created successfully!");
                    view.getFirstNameField().setText("");
                    view.getLastNameField().setText("");
            } else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Author creation failed!\n" + res);
        });
    }

    private void setDeleteListener() {
        view.getDeleteBtn().setOnAction(e -> {
            ButtonType answer = new ButtonType("Delete Books");
            ButtonType somethingElse = new ButtonType("Delete Authors Only");
            new DeleteAuthorDialog(view, answer, somethingElse);
            // deletion is handled inside the dialog
        });
    }

    private void setEditListener() {
        view.getFirstNameCol().setOnEditCommit(e -> {
            Author authorToEdit = e.getRowValue();
            Author editedAuthor = new Author(e.getNewValue(), authorToEdit.getLastName());
            String res = editedAuthor.updateInFile(authorToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });

        view.getLastNameCol().setOnEditCommit(e -> {
            Author authorToEdit = e.getRowValue();
            Author editedAuthor = new Author(authorToEdit.getFirstName(), e.getNewValue());
            String res = editedAuthor.updateInFile(authorToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });
    }
}
