package application.bookstore.models;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Book extends BaseModel<Book> implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private static final String FILE_PATH = BaseModel.FOLDER_PATH+"books.ser";
    private static final File DATA_FILE = new File(FILE_PATH);

    private static final ObservableList<Book> books = FXCollections.observableArrayList();

    private String isbn;
    private String title;
    private int quantity;
    private float purchasedPrice;
    private float sellingPrice;
    private Author author;

    public Book(String isbn, String title, int quantity, float purchasedPrice, float sellingPrice, Author author) {
        this.isbn = isbn;
        this.title = title;
        this.purchasedPrice = purchasedPrice;
        this.sellingPrice = sellingPrice;
        this.author = author;
        this.quantity = quantity;
    }

    public static ObservableList<Book> getBooks() {
        return getData(DATA_FILE, books);
    }


    public boolean exists() {
        for (Book b : books) {
            if (b.getIsbn().equals(this.getIsbn()))
                return true;
        }
        return false;
    }

    public static ObservableList<Book> getSearchResults(String searchText) {
        ObservableList<Book> searchResults = FXCollections.observableArrayList();
        searchText = ".*" + searchText.toLowerCase() + ".*";
        for (Book book : getBooks()) {
            if (book.getTitle().toLowerCase().matches(searchText))
                searchResults.add(book);
            else if (book.getIsbn().toLowerCase().matches(searchText))
                searchResults.add(book);
            else if (book.getAuthor().getFullName().toLowerCase().matches(searchText))
                searchResults.add(book);
        }
        return searchResults;
    }


    @Override
    public Book clone() {
        return new Book(isbn, title, quantity, purchasedPrice, sellingPrice, author.clone());
    }

    @Override
    public String toString() {
        return "\nBook{" +
                "\n\t\"isbn\": " + getIsbn() +
                ",\n\t\"title\": " + getTitle() +
                ",\n\t\"quantity\": " + getQuantity() +
                ",\n\t\"purchasedPrice\": " + getPurchasedPrice() +
                ",\n\t\"sellingPrice\": " + getSellingPrice() +
                ",\n\t\"author\": " + getAuthor() +
                "\n}";
    }

    @Override
    public String isValid() {
        if (!isbn.matches("\\d{13}"))
            return "ISBN must contain exactly 13 digits with no spaces/dashes.";
        if (sellingPrice < 0)
            return "Selling Price cannot be negative.";
        if (purchasedPrice < 0)
            return "Purchased Price cannot be negative.";
        if (!title.matches("([a-zA-Z0-9_]{1,30}\\s*)+"))
            return "Title must contain 1 to 30 lower/upper case letters numbers spaces or underscore.";
        if (quantity < 0)
            return "Quantity cannot be negative.";
        return "1";
    }

    @Override
    public String saveInFile() {
        if (exists())
            return "Book with this ISBN exists.";
        return save(DATA_FILE, books);
    }


    @Override
    public String deleteFromFile() {
        return delete(DATA_FILE, books);
    }

    @Override
    public String  updateInFile(Book old) {
        return update(DATA_FILE, books, old);
    }

    public float getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(float purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
