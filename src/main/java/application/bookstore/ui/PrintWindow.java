package application.bookstore.ui;

import application.bookstore.controllers.OrderController;
import application.bookstore.models.Order;
import application.bookstore.views.View;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrintWindow {
    private final Scene secondScene;
    private final Button okButton;
    private final Label dataLabel;
    private final Stage newWindow;
    private final Stage mainStage;
    private final View mainView;
    private final VBox v;
    private final OrderController controller;


    public PrintWindow(Stage mainStage, View mainView, Order data) {
        this(mainStage, mainView, data, null);
    }

    public PrintWindow(Stage mainStage, View mainView, Order data, OrderController controller) {
        this.mainStage = mainStage;
        this.mainView = mainView;
        this.controller = controller;

        dataLabel = new Label(data.toString());

        HBox h = new HBox();
        h.setAlignment(Pos.CENTER);
        h.setSpacing(20);
        okButton = new Button("Ok");
        Button printButton = new Button("Print");
        h.getChildren().addAll(okButton, printButton);

        v = new VBox();
        v.setAlignment(Pos.CENTER);
        v.getChildren().addAll(dataLabel, h);
        v.setSpacing(25);

        secondScene = new Scene(v, 500, 350);

        newWindow = new Stage();
        newWindow.setTitle("Print Order");
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(mainStage);

        okButton.setOnMousePressed(ev -> {
            newWindow.close();
            if (controller != null) controller.resetFields();
        });
        printButton.setOnMousePressed(ev -> {
            data.print();
            newWindow.close();
            if (controller != null)
                controller.resetFields();
        });

        addKeyBind();

        newWindow.show();
    }

    private void addKeyBind() {
        v.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                newWindow.close();
                if (controller != null)
                    controller.resetFields();
            }
        });

    }
}
