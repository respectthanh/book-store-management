package capybara.bookstoremanagement;

public class Book extends Item {
    private String isbn;
    private String author;

    public Book() {}

    public Book(String isbn, String name, String author, double price) {
        super("Book", name, price);
        this.isbn = isbn;
        this.author = author;
    }

    public String getISBN() {
        return isbn;
    }

    public String getAuthor() {
        return author;
    }
}
