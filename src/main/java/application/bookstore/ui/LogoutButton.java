package application.bookstore.ui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class LogoutButton extends Button {
    public LogoutButton() {
        super.setText("Logout");
        super.setGraphic(getImage());
        super.setTextFill(Color.BLACK);
        super.setStyle("-fx-background-color: white; -fx-border-color: black");
    }

    private ImageView getImage() {
        ImageView imageView = new ImageView(String.valueOf(LogoutButton.class.getResource("/images/logout_icon.png")));
        return imageView;
    }
}
