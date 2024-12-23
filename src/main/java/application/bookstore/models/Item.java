package application.bookstore.models;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Item extends BaseModel<Item> implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    private static final String FILE_PATH = BaseModel.FOLDER_PATH+"items.ser";
    private static final File DATA_FILE = new File(FILE_PATH);

    private static final ObservableList<Item> items = FXCollections.observableArrayList();

    private String isbn;
    private String title;
    private int quantity;
    private float purchasedPrice;
    private float sellingPrice;
    private Author author;

    public Item(String isbn, String title, int quantity, float purchasedPrice, float sellingPrice, Author author) {
        this.isbn = isbn;
        this.title = title;
        this.purchasedPrice = purchasedPrice;
        this.sellingPrice = sellingPrice;
        this.author = author;
        this.quantity = quantity;
    }

    public static ObservableList<Item> getItems() {
        return getData(DATA_FILE, items);
    }


    public boolean exists() {
        for (Item b : items) {
            if (b.getIsbn().equals(this.getIsbn()))
                return true;
        }
        return false;
    }

    public static ObservableList<Item> getSearchResults(String searchText) {
        ObservableList<Item> searchResults = FXCollections.observableArrayList();
        searchText = ".*" + searchText.toLowerCase() + ".*";
        for (Item item : getItems()) {
            if (item.getTitle().toLowerCase().matches(searchText))
                searchResults.add(item);
            else if (item.getIsbn().toLowerCase().matches(searchText))
                searchResults.add(item);
            else if (item.getAuthor().getFullName().toLowerCase().matches(searchText))
                searchResults.add(item);
        }
        return searchResults;
    }


    @Override
    public Item clone() {
        return new Item(isbn, title, quantity, purchasedPrice, sellingPrice, author.clone());
    }

    @Override
    public String toString() {
        return "\nItem{" +
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
            return "Item with this ISBN exists.";
        return save(DATA_FILE, items);
    }


    @Override
    public String deleteFromFile() {
        return delete(DATA_FILE, items);
    }

    @Override
    public String  updateInFile(Item old) {
        return update(DATA_FILE, items, old);
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