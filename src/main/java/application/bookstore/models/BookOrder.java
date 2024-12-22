package application.bookstore.models;

import java.io.Serial;
import java.io.Serializable;

// BookOrder keeps the data of the book that is being ordered as well as the quantity being sold
// This is similar to the Book class, but it also keeps the quantity
// However this class does not use arraylists to save the data since that is a responsibility of Order
// The BookOrders of an Order are only kept temporary inside the OrderView's second table list
// that list will be cleared when the order is finished
// this is the reason why we need to make this class (and the classes of the objets used here) cloneable
public class BookOrder implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 1234567L;

    private transient Book book; // I keep the book only to have it easier to work between the OrderView tables
    // this will not be saved on the order file

    private int quantity; // note quantity here is not the stock but the number of this book being sold on this order
    private String bookISBN; // some data is here mainly for analysis
    private String title;
    private float unitPrice;
    private float purchasedPrice;
    private Author author;

    public BookOrder(int quantity, Book b) {
        book = b;
        this.quantity = quantity;
        bookISBN = b.getIsbn();
        title = b.getTitle();
        unitPrice = b.getSellingPrice();
        author = b.getAuthor();
        purchasedPrice = b.getPurchasedPrice();
    }

    @Override
    public String toString() {
        return String.format("%-6s pcs %-20s Unit: %-6.2f Total: %.2f", quantity, title, unitPrice, getTotalPrice());
    }

    // we will need to clone them later on before we remove them from the OrderView table
    @Override
    public BookOrder clone() {
        return new BookOrder(quantity, book.clone());
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBookISBN() {
        return bookISBN;
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public float getTotalPrice() {
        return quantity * unitPrice;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Book getBook() {
        return book;
    }

    public float getPurchasedPrice() {
        return purchasedPrice;
    }

    public void setPurchasedPrice(float purchasedPrice) {
        this.purchasedPrice = purchasedPrice;
    }

    public void setBook(Book book){
        this.book=book;
    }
}
