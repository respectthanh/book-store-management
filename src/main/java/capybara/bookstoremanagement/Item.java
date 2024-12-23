package capybara.bookstoremanagement;

public class Item {
    public String type;
    public String name;
    public double price;

    public Item() {}

    public Item(String type, String name, double price) {
        this.type = type;
        this.name = name;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
