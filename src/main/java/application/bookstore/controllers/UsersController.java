package application.bookstore.controllers;
import application.bookstore.models.User;
import application.bookstore.views.UsersView;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;

public class UsersController {
    private final UsersView view;

    public UsersController(UsersView view) {
        this.view = view;
        setSearchListener();
        setSaveListener();
        setDeleteListener();
        setEditListener();
    }

    private void setSearchListener() {
        view.getSearchView().getClearBtn().setOnAction(e -> {
            view.getSearchView().getSearchField().setText("");
            view.getTableView().setItems(FXCollections.observableArrayList(User.getUsers()));
        });
        view.getSearchView().getSearchBtn().setOnAction(e -> {
            String searchText = view.getSearchView().getSearchField().getText();
            ArrayList<User> searchResults = User.getSearchResults(searchText);
            view.getTableView().setItems(FXCollections.observableArrayList(searchResults));
        });
    }

    private void setSaveListener() {
        view.getSaveBtn().setOnAction(e -> {
            User user = new User(view.getUserNameField().getText(), view.getPasswordField().getText(), view.getRoleComboBox().getValue());
            String res = user.saveInFile();
            if (res.matches("1")) {
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "User created successfully!");
                view.getUserNameField().setText("");
                view.getPasswordField().setText("");
            } else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "User creation failed!\n" + res);
        });
    }

    private void setDeleteListener() {
        view.getDeleteBtn().setOnAction(e -> {
            List<User> itemsToDelete = List.copyOf(view.getTableView().getSelectionModel().getSelectedItems());
            for (User u : itemsToDelete) {
                String res = u.deleteFromFile();
                if (res.matches("1"))
                    ControllerCommon.showSuccessMessage(view.getMessageLabel(), "User removed successfully");
                else {
                    ControllerCommon.showErrorMessage(view.getMessageLabel(), "User deletion failed\n" + res);
                    break;
                }
            }
        });
    }

    private void setEditListener() {

        view.getUsernameCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            User editedUser = new User(e.getNewValue(), userToEdit.getPassword(), userToEdit.getRole());
            if (!editedUser.getUsername().equals(userToEdit.getUsername())){
                if (editedUser.usernameExists()){
                    User.getUsers().set(User.getUsers().indexOf(userToEdit), userToEdit);
                    ControllerCommon.showErrorMessage(view.getMessageLabel(), "Username Exists!");
                }
                else {
                    String res = editedUser.updateInFile(userToEdit);
                    if (res.matches("1"))
                        ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
                    else
                        ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
                }
            }
        });

        view.getPasswordCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            User editedUser = new User(userToEdit.getUsername(), e.getNewValue(), userToEdit.getRole());
            String res = editedUser.updateInFile(userToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });

        view.getRoleCol().setOnEditCommit(e -> {
            User userToEdit = e.getRowValue();
            User editedUser = new User(userToEdit.getUsername(), userToEdit.getPassword(), e.getNewValue());
            String res = editedUser.updateInFile(userToEdit);
            if (res.matches("1"))
                ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Edit Successful!");
            else
                ControllerCommon.showErrorMessage(view.getMessageLabel(), "Edit value invalid!\n" + res);
        });
    }
}
