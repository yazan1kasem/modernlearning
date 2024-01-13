package modern.learning.modernlearning;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class Fach {
    private String klasseId;


    @FXML
    public void zurück(javafx.scene.input.MouseEvent mouseEvent) {
            Stage currentStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            currentStage.close();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Klasse.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                currentStage.setTitle("Modern learning");
                currentStage.getIcons().add(new Image("file:src/main/Media/SkillBuildersLogo.png"));
                currentStage.setMinHeight(640);
                currentStage.setMinWidth(1000);
                currentStage.setWidth(scene.getWidth());

                currentStage.setScene(scene);

                currentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public void setKlasseId(String klasseId) {
        this.klasseId = klasseId;
    }

    public Fach() {

    }

    public void Arbeitsblaetter(MouseEvent mouseEvent) {
        Stage currentStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();{
        try {
            Node source = (Node) mouseEvent.getSource();
            String fachid = source.getId();
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Arbeitsblätter.fxml"));

            Scene scene = new Scene(fxmlLoader.load(),currentStage.getWidth(),currentStage.getHeight());
            Arbeitsblätter arbeitsblätter = fxmlLoader.getController();
            arbeitsblätter.setFachID(fachid);
            arbeitsblätter.setKlasseID(klasseId);
            currentStage.setScene(scene);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    }
}

