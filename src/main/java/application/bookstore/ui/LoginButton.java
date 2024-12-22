package application.bookstore.ui;

import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class LoginButton extends Button {
    public LoginButton() {
        super.setText("Login");
        super.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        super.setTextFill(Color.BLACK);
        super.setStyle("-fx-background-color: white; -fx-border-color: white; -fx-border-radius: 2px;");
    }
}
