package application.bookstore.models;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.ArrayList;

public class Author extends BaseModel<Author> implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 1234567L;
    public static final String FILE_PATH = BaseModel.FOLDER_PATH+"authors.ser";
    public static final File DATA_FILE = new File(FILE_PATH);

    private static final ObservableList<Author> authors = FXCollections.observableArrayList();

    private String firstName;
    private String lastName;

    public Author(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }

    public static ObservableList<Author> getAuthors() {
        return getData(DATA_FILE, authors);
    }

    public boolean exists() {
        for (Author a : authors) {
            if (a.getFullName().equals(this.getFullName()))
                return true;
        }
        return false;
    }

    public static ArrayList<Author> getSearchResults(String searchText) {
        // this only works if users searches by:
        // firstname/lastname only (even if truncated)
        // firstname (full) *space/s* lastname (even if truncated)
        searchText = ".*" + searchText.toLowerCase().replaceAll(" ", "") + ".*";
        ArrayList<Author> searchResults = new ArrayList<>();
        for (Author author : getAuthors())
            if (author.getFullName().toLowerCase().replaceAll(" ", "").matches(searchText))
                searchResults.add(author);
        return searchResults;
    }

    @Override
    public Author clone() {
        return new Author(firstName, lastName);
    }


    @Override
    public String toString() {
        return getFullName();
    }

    // toString for logging
    // since Author is widely used as an object in combo-boxes/table-views the original toString is used by them
    public String toString_() {
        return "\nAuthor{" +
                "\n\t\"firstName\": " + getFirstName() +
                ",\n\t\"lastName\": " + getLastName() +
                "\n}";
    }

    @Override
    public String isValid() {
        // firstname and last name must contain only letters
        if (!getFirstName().matches("[a-zA-Z]{1,30}"))
            return "First Name must contain only letters.";
        if (!getLastName().matches("[a-zA-Z]{1,30}")) {
            return "Last Name must contain only letters.";
        }
        return "1";
    }

    @Override
    public String saveInFile() {
        if (exists())
            return "Author with this Full Name exists.";
        return save(DATA_FILE, authors);
    }

    @Override
    public String deleteFromFile() {
        return delete(DATA_FILE, authors);
    }

    @Override
    public String  updateInFile(Author old) {
        return update(DATA_FILE, authors, old);
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

}
