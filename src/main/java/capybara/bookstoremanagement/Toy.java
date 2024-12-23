package capybara.bookstoremanagement;

public class Toy extends Item {
    private String brand;
    private int suitableAge;
    private String origin;

    public Toy() {}

    public Toy(String brand, String name, String origin, int suitableAge, double price) {
        super("Toy", name, price);
        this.brand = brand;
        this.origin = origin;
        this.suitableAge = suitableAge;
    }

    public String getBrand() {
        return brand;
    }

    public String getOrigin() {
        return origin;
    }
    
    public int getSuitableAge() {
        return suitableAge;
    }
}
