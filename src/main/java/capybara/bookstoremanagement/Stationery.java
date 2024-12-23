package capybara.bookstoremanagement;

public class Stationery extends Item {
    private String brand;
    private String origin;

    public Stationery() {}

    public Stationery(String brand, String name, String origin, double price) {
        super("Stationery", name, price);
        this.brand = brand;
        this.origin = origin;
    }

    public String getBrand() {
        return brand;
    }

    public String getOrigin() {
        return origin;
    }
}
