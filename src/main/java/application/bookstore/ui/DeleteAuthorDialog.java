package application.bookstore.ui;

import java.util.List;
import java.util.Optional;

import application.bookstore.controllers.ControllerCommon;
import application.bookstore.models.Author;
import application.bookstore.models.Item;
import application.bookstore.views.AuthorView;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Window;

public class DeleteAuthorDialog extends Alert {



    public DeleteAuthorDialog(AuthorView view, ButtonType deleteItems, ButtonType deleteOnlyAuthors) {
        super(AlertType.NONE, "Do you want to delete the items related to this author?", deleteItems, deleteOnlyAuthors);
        setGraphic(getImage());
        Window window = getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> hide());
        Optional<ButtonType> result = showAndWait();
        if (result.isEmpty());
        else if (result.get() == deleteItems)
            deleteAuthors(view, true);
        else if (result.get()==deleteOnlyAuthors)
            deleteAuthors(view, false);
    }

    private ImageView getImage() {
        ImageView imageView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/edit_icon.png")));
        return imageView;
    }

    private void deleteAuthors(AuthorView view, boolean deleteItems_){
            List<Author> items_ToDelete = List.copyOf(view.getTableView().getSelectionModel().getSelectedItems());
            for (Author a : items_ToDelete) {
                String res = a.deleteFromFile();
                if (res.matches("1")) {
                    if (deleteItems_) {
                        List<Item> itemsToDelete = FXCollections.observableArrayList();
                        for (Item b : Item.getItems())
                            if (b.getAuthor().getFullName().matches(a.getFullName()))
                                itemsToDelete.add(b);
                        for (Item b : itemsToDelete)
                            b.deleteFromFile();
                    }
                    ControllerCommon.showSuccessMessage(view.getMessageLabel(), "Author removed successfully");
                }
                else {
                    ControllerCommon.showErrorMessage(view.getMessageLabel(), "Author deletion failed\n" + res);
                    break;
                }
            }
    }
}