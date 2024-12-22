package application.bookstore.ui;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class PrintButton extends Button {

    public PrintButton() {
        this("Print");
    }

    public PrintButton(String text) {
        super.setText(text);
        super.setGraphic(getImage());
    }


    private ImageView getImage() {
        ImageView imageView = new ImageView(String.valueOf(PrintButton.class.getResource("/images/print_icon.png")));
        return imageView;
    }
}
