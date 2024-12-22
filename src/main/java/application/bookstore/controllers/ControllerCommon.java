package application.bookstore.controllers;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.*;

public class ControllerCommon {

    public static final Logger LOGGER = Logger.getLogger( ControllerCommon.class.getName() );

    public static void showSuccessMessage(Label l, String t) {
        l.setText(t);
        l.setTextFill(Color.DARKGREEN);
        scheduleLabelReset(l, 5000);
    }

    public static void showErrorMessage(Label l, String t) {
        l.setText(t);
        l.setTextFill(Color.RED);
        scheduleLabelReset(l, 5000);
    }


    static void scheduleLabelReset(Label l, int time) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    l.setText("");
                });
            }
        }, time);
    }

    public static class ConfirmationDialog extends Stage {
        Button okButton;

        ConfirmationDialog(String message) {
            super.initModality(Modality.WINDOW_MODAL);
            okButton = new Button("OK");
            VBox vbox = new VBox(new Text(message), okButton);
            vbox.setAlignment(Pos.CENTER);
            vbox.setSpacing(20);
            vbox.setPadding(new Insets(20));
            super.setScene(new Scene(vbox));
            super.show();
        }
    }
}
