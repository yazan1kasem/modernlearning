package modern.learning.modernlearning.CalenderClasses;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;

public class NotificationBox extends HBox {
    private TextField ZahlenEingabe;
    private ComboBox<String> Zeit;
    private List<String> Zeitnamen;


    public NotificationBox() {
        ZahlenEingabe = new TextField();
        ZahlenEingabe.setPromptText("Zeit");
        Zeit = new ComboBox<>();
        Zeitnamen = new ArrayList<>();
        Zeitnamen.add("Minuten");
        Zeitnamen.add("Stunden");
        Zeitnamen.add("Tage");
        Zeitnamen.add("Monate");
        EingabeCorrect();
        Zeit.getItems().addAll(Zeitnamen);
        Zeit.setValue(Zeitnamen.get(0));
        this.getChildren().addAll(ZahlenEingabe,Zeit);
        Zeit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("SELECTED " + newValue);
            EingabeCorrect();

        });
        ZahlenEingabe.textProperty().addListener((observable, oldValue, newValue) -> {
            EingabeCorrect();
        });
    }
    private void EingabeCorrect(){

            String selectedZeit = Zeit.getValue();

            try {
                int value = Integer.parseInt(ZahlenEingabe.getText());

                if ("Minuten".equals(selectedZeit) && value > 59) {
                    System.out.println("Minutes cannot be greater than 59");
                } else if ("Stunden".equals(selectedZeit) && value > 23) {
                    System.out.println("Hours cannot be greater than 23");
                } else if ("Tage".equals(selectedZeit) && value > 30) {
                    System.out.println("Days cannot be greater than 30");
                } else if ("Monate".equals(selectedZeit) && (value < 1 || value > 12)) {
                    System.out.println("Month should be between 1 and 12");
                }

            } catch (NumberFormatException e) {
                // Handle the case where newValue is not a valid integer
                System.out.println("Invalid input for " + selectedZeit);
            }



    }







    public TextField getZahlenEingabe() {
        return ZahlenEingabe;
    }

    public void setZahlenEingabe(TextField zahlenEingabe) {
        ZahlenEingabe = zahlenEingabe;
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
