package application.bookstore.models;

import java.io.File;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User extends BaseModel<User> implements Serializable{
    @Serial
    private static final long serialVersionUID = 1234567L;
    public static final String FILE_PATH = BaseModel.FOLDER_PATH+"users.ser";
    private static final File DATA_FILE = new File(FILE_PATH);

    private static final ObservableList<User> users = FXCollections.observableArrayList();

    private String username;
    private String password;
    private Role role;

    public User(String username, String password, Role role) {
        this(username, password);
        this.role = role;
    }

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public static ObservableList<User> getUsers() {
        return getData(DATA_FILE, users);
    }

    public static User getIfExists(User potentialUser) {
        for (User user : getUsers())
            if (user.equals(potentialUser))
                return user;
        return null;
    }

    public boolean usernameExists() {
        for (User u : getUsers())
            if (u.getUsername().equals(this.getUsername()))
                if (u!=this)
                    return true;
        return false;
    }

    public static ArrayList<User> getSearchResults(String searchText) {
        searchText = ".*" + searchText.toLowerCase() + ".*";
        ArrayList<User> searchResults = new ArrayList<>();
        for (User User : getUsers()) { // this one need only a simple search by username
            if (User.getUsername().toLowerCase().matches(searchText))
                searchResults.add(User);
        }
        return searchResults;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User) obj;
            return other.getUsername().equals(getUsername()) && other.getPassword().equals(getPassword());
        }
        return false;
    }

    @Override
    public String toString() {
        return "\nUser{" +
                "\n\t\"username\": " + getUsername() +
                ",\n\t\"password\": " + "******" +
                ",\n\t\"role\": " + getRole() +
                "\n}";
    }

    @Override
    public String isValid() {
        // username and password must contain at least 1 word character and no spaces [a-zA-Z_0-9]
        if (!username.matches("\\w+"))
            return "Username must contain at least 1 lower/upper case letters, numbers or underscore.";
        if (!password.matches("\\w+"))
            return "Password must contain at least 1 lower/upper case letters, numbers or underscore.";
        return "1";
    }

    @Override
    public String saveInFile() {
        if (this.usernameExists())
            return "Username Exists";
        return save(DATA_FILE, users);
    }

    @Override
    public String deleteFromFile() {
        return delete(DATA_FILE, users);
    }

    @Override
    public String  updateInFile(User old) {
        return update(DATA_FILE, users, old);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
