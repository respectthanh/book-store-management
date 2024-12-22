package application.bookstore.views;

import application.bookstore.controllers.MainController;
import application.bookstore.models.Role;
import application.bookstore.ui.LogoutButton;
import application.bookstore.ui.ProfileButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainView extends View {

    public static int width = 1300;
    public static int height = 800;

    private final MenuBar menuBar = new MenuBar();

    private final Menu booksMenu = new Menu("Books");
    private final MenuItem menuItemViewBooks = new MenuItem("View Books");
    private final MenuItem menuItemViewAuthors = new MenuItem("View Authors");

    private final Menu salesMenu = new Menu("_Sales");
    private final MenuItem menuItemNewOrder = new MenuItem("New Order");
    private final MenuItem menuItemViewSales = new MenuItem("View Sales");
    private final MenuItem statsMenu = new MenuItem("View Sales Analysis");

    private final Menu controlMenu = new Menu("Administration");
    private final MenuItem menuItemProfile = new MenuItem("My Profile");
    private final MenuItem menuItemChangePassword = new MenuItem("Change Password");
    private final MenuItem menuItemSettings = new MenuItem("App Settings");
    private final MenuItem manageUsers = new MenuItem("Manage Users");
    private final MenuItem menuItemLogout = new MenuItem("Logout");

    private final LogoutButton logoutButton = new LogoutButton();
    private final ProfileButton userProfileButton = new ProfileButton();

    private final TabPane tabPane = new TabPane();

    public MainView(Stage mainStage) {
        new MainController(this, mainStage);
    }

    @Override
    public Parent getView() {
        BorderPane mainPane = new BorderPane();

        Role currentRole = (getCurrentUser() != null ? getCurrentUser().getRole() : null);
        if (currentRole != null) {
            booksMenu.getItems().addAll(menuItemViewBooks, menuItemViewAuthors);
            salesMenu.getItems().add(menuItemNewOrder);
            menuBar.getMenus().addAll(booksMenu, salesMenu);
            menuBar.getMenus().add(controlMenu);
            controlMenu.getItems().addAll(menuItemProfile, menuItemChangePassword);
            if (currentRole == Role.ADMIN) {
                controlMenu.getItems().addAll(manageUsers, menuItemSettings);
            }
            if (currentRole == Role.MANAGER || currentRole == Role.ADMIN) {
                salesMenu.getItems().addAll(menuItemViewSales, statsMenu);
            }
            controlMenu.getItems().add(menuItemLogout);

        }

        VBox topPane = new VBox();
        topPane.getChildren().addAll(menuBar);

        HBox bottomControls = new HBox();
        bottomControls.setAlignment(Pos.CENTER);
        bottomControls.setPadding(new Insets(10, 10, 10, 10));
        final Pane spacer = new Pane();
        final Pane spacer_ = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(spacer_, Priority.ALWAYS);
        bottomControls.getChildren().addAll(userProfileButton, spacer_, new Text("Hi "+getCurrentUser().getUsername() + ", welcome to our bookstore."), spacer, logoutButton);
        VBox b = new VBox(bottomControls);
        b.setMinHeight(30);

        mainPane.setTop(topPane);
        mainPane.setCenter(tabPane);
        mainPane.setBottom(b);

        return mainPane;
    }


    public MenuBar getMenuBar() {
        return menuBar;
    }

    public Menu getBooksMenu() {
        return booksMenu;
    }

    public MenuItem getMenuItemViewBooks() {
        return menuItemViewBooks;
    }

    public MenuItem getMenuItemViewAuthors() {
        return menuItemViewAuthors;
    }

    public Menu getSalesMenu() {
        return salesMenu;
    }

    public MenuItem getMenuItemNewOrder() {
        return menuItemNewOrder;
    }

    public Menu getControlMenu() {
        return controlMenu;
    }

    public MenuItem getManageUsers() {
        return manageUsers;
    }

    public MenuItem getStatsMenu() {
        return statsMenu;
    }

    public MenuItem getMenuItemProfile() {
        return menuItemProfile;
    }

    public MenuItem getMenuItemChangePassword() {
        return menuItemChangePassword;
    }

    public MenuItem getMenuItemSettings() {
        return menuItemSettings;
    }

    public MenuItem getMenuItemLogout() {
        return menuItemLogout;
    }

    public LogoutButton getLogoutButton() {
        return logoutButton;
    }

    public ProfileButton getUserProfileButton() {
        return userProfileButton;
    }

    public TabPane getTabPane() {
        return tabPane;
    }

    public MenuItem getMenuItemViewSales() {
        return menuItemViewSales;
    }
}
