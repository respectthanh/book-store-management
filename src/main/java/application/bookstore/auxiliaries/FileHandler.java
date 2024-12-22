package application.bookstore.auxiliaries;

import application.bookstore.models.BaseModel;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileHandler {

    public static <T extends BaseModel> void overwriteCurrentListToFile(File file, ObservableList<T> data) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
        if (data.size() == 0) {
        } else {
            for (T entity : data)
                outputStream.writeObject(entity);
        }
    }
}
