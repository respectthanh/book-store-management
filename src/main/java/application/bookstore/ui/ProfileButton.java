package application.bookstore.ui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class ProfileButton extends Button {
    public ProfileButton() {
        super.setText("Profile");
        super.setGraphic(getImage());
        super.setTextFill(Color.BLACK);
        super.setStyle("-fx-background-color: white; -fx-border-color: black");
    }

    private ImageView getImage() {
        ImageView imageView = new ImageView(String.valueOf(ProfileButton.class.getResource("/images/profile_icon.png")));
        return imageView;
    }
}
