package application.bookstore.ui;

import application.bookstore.controllers.ControllerCommon;
import application.bookstore.models.Author;
import application.bookstore.models.Book;
import application.bookstore.models.User;
import application.bookstore.views.AuthorView;
import application.bookstore.views.View;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;
import java.util.Optional;

public class DeleteAuthorDialog extends Alert {



    public DeleteAuthorDialog(AuthorView view, ButtonType deleteBooks, ButtonType deleteOnlyAuthors) {
        super(AlertType.NONE, "Do you want to delete the books related to this author?", deleteBooks, deleteOnlyAuthors);
        setGraphic(getImage());
        Window window = getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(e -> hide());
        Optional<ButtonType> result = showAndWait();
        if (result.isEmpty());
        else if (result.get() == deleteBooks)
            deleteAuthors(view, true);
        else if (result.get()==deleteOnlyAuthors)
            deleteAuthors(view, false);
    }

    private ImageView getImage() {
        ImageView imageView = new ImageView(String.valueOf(CreateButton.class.getResource("/images/edit_icon.png")));
        return imageView;
    }

    private void deleteAuthors(AuthorView view, boolean deleteBooks_){
            List<Author> itemsToDelete = List.copyOf(view.getTableView().getSelectionModel().getSelectedItems());
            for (Author a : itemsToDelete) {
                String res = a.deleteFromFile();
                if (res.matches("1")) {
                    if (deleteBooks_) {
                        List<Book> booksToDelete = FXCollections.observableArrayList();
                        for (Book b : Book.getBooks())
                            if (b.getAuthor().getFullName().matches(a.getFullName()))
                                booksToDelete.add(b);
                        for (Book b : booksToDelete)
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
