package modern.learning.modernlearning.CalenderClasses;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;

public class NotificationBox extends HBox {

    private ComboBox<String> Zeit;
    private List<String> Zeitnamen;


    public NotificationBox() {

        Zeit = new ComboBox<>();
        Zeitnamen = new ArrayList<>();
        Zeitnamen.add("15 Minuten");
        Zeitnamen.add("1 Stunde");
        Zeitnamen.add("1 Tag");
        Zeitnamen.add("2 Tage");
        Zeitnamen.add("4 Tage");
        Zeitnamen.add("1 Woche");
        Zeit.getItems().addAll(Zeitnamen);
        Zeit.setValue(Zeitnamen.get(0));
        this.getChildren().addAll(Zeit);
        Zeit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("SELECTED " + newValue);
        });

    }




    public ComboBox<String> getZeit() {
        return Zeit;
    }

    public void setZeit(ComboBox<String> zeit) {
        Zeit = zeit;
    }

    public List<String> getZeitnamen() {
        return Zeitnamen;
    }

    public void setZeitnamen(List<String> zeitnamen) {
        Zeitnamen = zeitnamen;
    }
}
