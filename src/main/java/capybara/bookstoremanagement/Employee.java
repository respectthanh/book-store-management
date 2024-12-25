package capybara.bookstoremanagement;

public class Employee {
    private int id;
    private String name;
    private String position;
    private double salary;
    private String username;
    private String password;
    private String role;

    public Employee(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Employee(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Employee(int id, String name, String position, double salary) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
    }

    public Employee(int id, String name, String position, double salary, String username, String password) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.username = username;
        this.password = password;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public double getSalary() { return salary; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}