package application.bookstore.controllers;

import application.bookstore.ui.ChangePasswordDialog;
import application.bookstore.views.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.Window;


public class MainController {
    private final MainView mainView;
    private final Stage mainStage;

    public MainController(MainView mainView, Stage mainStage) {
        this.mainView = mainView;
        this.mainStage = mainStage;

        // default startup tab
        Tab startupTab = new Tab("New Order");
        startupTab.setContent(new OrderView(mainView, mainStage, startupTab).getView());
        mainView.getTabPane().getTabs().add(startupTab); // add the tab

        setKeyBinds();
        setListener();
        setLogoutListener();
    }

    private Tab openTab(String tabLabel, Parent tabView) {
        Tab tab = new Tab(tabLabel); // create tab
        tab.setContent(tabView);

        // check if tab is open
        for (Tab t : mainView.getTabPane().getTabs())
            if (t.getText().equals(tab.getText())) {
                mainView.getTabPane().getSelectionModel().select(t);// if it is open, navigate to it (bring it up front)
                mainStage.sizeToScene();
                return t;
            }

        mainView.getTabPane().getTabs().add(tab); // add the tab
        mainView.getTabPane().getSelectionModel().select(tab); // bring it up front
        return tab;
    }

    private void setKeyBinds() {
        mainView.getMenuItemNewOrder().setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
    }

    private void setListener() {

        mainView.getMenuItemViewAuthors().setOnAction((e) -> openTab("Authors", new AuthorView().getView()));

        mainView.getMenuItemViewBooks().setOnAction((e) -> openTab("Books", new BookView().getView()));

        mainView.getMenuItemViewSales().setOnAction(e -> openTab("Sales", new SalesView(mainStage).getView()));

        mainView.getStatsMenu().setOnAction(e -> openTab("Stats", new StatsView().getView()));

        mainView.getMenuItemChangePassword().setOnAction(e -> new ChangePasswordDialog(mainStage, mainView));

        mainView.getManageUsers().setOnAction(e -> openTab("Users", new UsersView().getView()));

        // Multiple new order tabs can be opened at the same time
        mainView.getMenuItemNewOrder().setOnAction(e -> {
            Tab order = new Tab("New Order");
            OrderView o = new OrderView(mainView, mainStage, order);
            order.setContent(o.getView());
            mainView.getTabPane().getTabs().add(order);
            mainView.getTabPane().getSelectionModel().select(order);
        });

    }

    private void setLogoutListener() {
        mainView.getMenuItemLogout().setOnAction(e -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView, mainStage);
            Scene scene = new Scene(loginView.getView(), MainView.width, MainView.height);
            mainStage.setScene(scene);
        });
        mainView.getLogoutButton().setOnMouseClicked(e -> {
            LoginView loginView = new LoginView();
            new LoginController(loginView, mainStage);
            Scene scene = new Scene(loginView.getView(), MainView.width, MainView.height);
            mainStage.setScene(scene);
        });
    }

}
