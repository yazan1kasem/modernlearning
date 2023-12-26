package modern.learning.modernlearning.CalenderClasses;

import entities.K_Kalender;
import entities.N_Notifications;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.ResourceBundle;

public class NotificationBox extends HBox {
    private TextField ZahlenEingabe;
    private ComboBox<String> Zeit;
    private List<String> Zeitnamen;
    public static ToggleSwitch EinAus;



    public NotificationBox() {
        ZahlenEingabe = new TextField();
        ZahlenEingabe.setPromptText("Zeit");
        Zeit = new ComboBox<>();
        Zeitnamen = new ArrayList<>();
        Zeitnamen.add("Minuten");
        Zeitnamen.add("Stunden");
        Zeitnamen.add("Tage");
        Zeitnamen.add("Monate");
        Zeit.getItems().addAll(Zeitnamen);
        Zeit.setValue(Zeitnamen.get(0));
        EinAus= new ToggleSwitch();
        EinAus.getStyleClass().add("selected-button");
        EinAus.setAlignment(Pos.CENTER);
        EinAus.setSelected(true);
        clicked();
        EinAus.setOnMouseClicked(ANAUSEVENT -> {
            clicked();
        });
        this.getChildren().addAll(ZahlenEingabe,Zeit,EinAus);
        this.setAlignment(Pos.CENTER_LEFT);

    }

    public N_Notifications GehtDataNotification(K_Kalender kalender){
        N_Notifications n_notifications = new N_Notifications();
        if(Zeit.getValue().equals("Minuten")){
            n_notifications.setN_Vorzeit(kalender.getK_vonDatum().minusMinutes(Integer.parseInt( ZahlenEingabe.getText())));
        }
        if(Zeit.getValue().equals("Stunden")){
            n_notifications.setN_Vorzeit(kalender.getK_vonDatum().minusHours(Integer.parseInt( ZahlenEingabe.getText())));

        }
        if(Zeit.getValue().equals("Tage")){
            n_notifications.setN_Vorzeit(kalender.getK_vonDatum().minusDays(Integer.parseInt( ZahlenEingabe.getText())));

        }
        if(Zeit.getValue().equals("Monate")){
            n_notifications.setN_Vorzeit(kalender.getK_vonDatum().minusMonths(Integer.parseInt( ZahlenEingabe.getText())));

        }

        n_notifications.setN_K_ID(kalender);
        addBenachrichtigung(n_notifications);
        return n_notifications;
    }
    private void addBenachrichtigung(N_Notifications Benachrichtigung){
        Benachrichtigung benachrichtigung = new Benachrichtigung(Benachrichtigung.getN_K_ID().getK_Title(),Benachrichtigung.getN_Vorzeit(),Benachrichtigung.getN_id());
    }

    public void clicked(){
        if(!EinAus.isSelected()){
            ZahlenEingabe.setDisable(true);
            Zeit.setDisable(true);
        }else{
            ZahlenEingabe.setDisable(false);
            Zeit.setDisable(false);
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

    public ToggleSwitch getEinAus() {
        return EinAus;
    }

    public void setEinAus(ToggleSwitch einAus) {
        EinAus = einAus;
    }
}
