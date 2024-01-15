package modern.learning.modernlearning;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.xml.transform.Source;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Fach {
    private String klasseId;


    @FXML
    public void zurück(javafx.scene.input.MouseEvent mouseEvent) {
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Klasse.fxml"));
            Parent root= fxmlLoader.load();

            currentStage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        }

    public void setKlasseId(String klasseId) {
        this.klasseId = klasseId;
    }

    public Fach() {

    }

    public void Arbeitsblaetter(MouseEvent mouseEvent) {
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Arbeitsblätter.fxml"));
            Parent root= fxmlLoader.load();
            Arbeitsblätter arbeitsblätter = fxmlLoader.getController();
            Node source=(Node) mouseEvent.getSource();
            arbeitsblätter.setFachID(source.getId());
            arbeitsblätter.setKlasseID(klasseId);

            currentStage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

