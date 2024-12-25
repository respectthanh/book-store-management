package capybara.bookstoremanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ManageBooksController {

    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, Integer> colId;
    @FXML
    private TableColumn<Book, String> colBookId;
    @FXML
    private TableColumn<Book, String> colTitle;
    @FXML
    private TableColumn<Book, String> colAuthor;
    @FXML
    private TableColumn<Book, Double> colPrice;

    private String previousView;
    private String previousViewOfManageItems;

    public void setPreviousView(String previousView) {
        this.previousView = previousView;
    }

    public void setPreviousViewOfManageItems(String previousViewOfManageItems) {
        this.previousViewOfManageItems = previousViewOfManageItems;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colBookId.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        loadBooks();
    }

    private void loadBooks() {
        tableView.getItems().clear();
        try {
            ResultSet rs = DatabaseUtil.getAllBooks();
            while (rs.next()) {
                tableView.getItems().add(new Book(rs.getInt("id"), rs.getString("bookId"), rs.getString("title"), rs.getString("author"), rs.getDouble("price")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddBook(ActionEvent event) {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add New Book");
        dialog.setHeaderText("Enter the details of the new book");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonType.OK.getButtonData());
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the title, author, and price labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Author:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a Book object when the Add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String bookId = DatabaseUtil.generateUniqueBookId();
                    String title = titleField.getText();
                    String author = authorField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    return new Book(0, bookId, title, author, price);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            try {
                DatabaseUtil.createBook(book.getBookId(), book.getTitle(), book.getAuthor(), book.getPrice());
                loadBooks();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void handleEditBook(ActionEvent event) {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Dialog<Book> dialog = new Dialog<>();
            dialog.setTitle("Edit Book");
            dialog.setHeaderText("Edit the details of the book");

            // Set the button types
            ButtonType editButtonType = new ButtonType("Save", ButtonType.OK.getButtonData());
            dialog.getDialogPane().getButtonTypes().addAll(editButtonType, ButtonType.CANCEL);

            // Create the title, author, and price labels and fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            TextField titleField = new TextField(selectedBook.getTitle());
            TextField authorField = new TextField(selectedBook.getAuthor());
            TextField priceField = new TextField(String.valueOf(selectedBook.getPrice()));

            grid.add(new Label("Title:"), 0, 0);
            grid.add(titleField, 1, 0);
            grid.add(new Label("Author:"), 0, 1);
            grid.add(authorField, 1, 1);
            grid.add(new Label("Price:"), 0, 2);
            grid.add(priceField, 1, 2);

            dialog.getDialogPane().setContent(grid);

            // Convert the result to a Book object when the Save button is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == editButtonType) {
                    String title = titleField.getText();
                    String author = authorField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    return new Book(selectedBook.getId(), selectedBook.getBookId(), title, author, price);
                }
                return null;
            });

            Optional<Book> result = dialog.showAndWait();
            result.ifPresent(book -> {
                try {
                    DatabaseUtil.updateBook(book.getId(), book.getTitle(), book.getAuthor(), book.getPrice());
                    loadBooks();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void handleDeleteBook(ActionEvent event) {
        Book selectedBook = tableView.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this book?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    try {
                        DatabaseUtil.deleteBook(selectedBook.getId());
                        loadBooks();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @FXML
    private void handleReturnToMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(previousView + ".fxml"));
            Parent root = loader.load();
            ManageItemsController controller = loader.getController();
            controller.setPreviousView(previousViewOfManageItems);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root,1080, 640));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}