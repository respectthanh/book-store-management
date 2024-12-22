package application.bookstore.models;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.logging.Level;

import application.bookstore.auxiliaries.CustomObjectOutputStream;
import application.bookstore.auxiliaries.FileHandler;
import application.bookstore.controllers.ControllerCommon;
import javafx.collections.ObservableList;

public abstract class BaseModel <V extends BaseModel> {
    public static String FOLDER_PATH="data/";

    public static <T extends BaseModel> ObservableList<T> getData(File file, ObservableList<T> data) {
        if (data.size() == 0) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
                while (true) {
                    T temp = (T) inputStream.readObject();
                    if (temp == null)
                        break;
                    data.add(temp);
                }
                inputStream.close();
            } catch (FileNotFoundException ex){
                ControllerCommon.LOGGER.log(Level.INFO, String.format("File %s not found, the file will be created when data is entered.\n", file) + Arrays.toString(ex.getStackTrace()));
            } catch (EOFException eofException) {
                ControllerCommon.LOGGER.log(Level.INFO, String.format("End of %s file reached!", file));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return data;
    }


    public String save(File file, ObservableList<V> data) {
        if (!(isValid().matches("1")))
            return isValid();
        try {
            ObjectOutputStream outputStream;
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            if (file.length() == 0)
                outputStream = new ObjectOutputStream(fileOutputStream);
            else
                outputStream = new CustomObjectOutputStream(fileOutputStream);
            outputStream.writeObject((V) this);
            data.add((V) this);
        } catch (IOException e) {
            ControllerCommon.LOGGER.log(Level.INFO, "Could Not Save to File."+ Arrays.toString(e.getStackTrace()));
            return "Could Not Save to File. Please check Logs.";
        }
        return "1";
    }

    public String delete(File file, ObservableList<V> list) {
        try {
            list.remove((V) this);
            FileHandler.overwriteCurrentListToFile(file, list);
        } catch (Exception e) {
            list.set(list.indexOf(((V) this)), (V) this);
            ControllerCommon.LOGGER.log(Level.INFO, "Could Not Save to File."+ Arrays.toString(e.getStackTrace()));
            return "Could Not Save to File. Please check Logs.";
        }
        return "1";
    }

    public String update(File file, ObservableList<V> list, V old) {
        if (!(isValid().matches("1"))){
            list.set(list.indexOf(old), old);
            return isValid();
        }
        try {
            list.set(list.indexOf(old), (V) this);
            FileHandler.overwriteCurrentListToFile(file, list);
        } catch (Exception e) {
            list.set(list.indexOf(((V) this)), old);
            ControllerCommon.LOGGER.log(Level.INFO, "Could Not Save to File."+ Arrays.toString(e.getStackTrace()));
            return "Could Not Save to File. Please check Logs.";
        }
        return "1";
    }

    public abstract String isValid();
    // return "1" if valid, else return a "message" describing the error


    public abstract String saveInFile();

    public abstract String deleteFromFile();

    public abstract String updateInFile(V old);

}
