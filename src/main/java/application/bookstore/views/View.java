package application.bookstore.views;

import application.bookstore.models.User;
import javafx.scene.Parent;

public abstract class View {
    static private User currentUser = null;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser_) {
        currentUser = currentUser_;
    }

    public abstract Parent getView();
}
