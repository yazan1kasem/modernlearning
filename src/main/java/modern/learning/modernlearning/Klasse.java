package modern.learning.modernlearning;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;

public class Klasse {

    public void zurück(MouseEvent mouseEvent) {
        Stage currentStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        currentStage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Klasse.fxml"));
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Kalender.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            currentStage.setTitle("Modern learning");
            currentStage.getIcons().add(new Image("file:src/main/Media/SkillBuildersLogo.png"));
            currentStage.setMinHeight(640);
            currentStage.setMinWidth(1000);
            currentStage.setWidth(scene.getWidth());

            currentStage.setScene(scene);

            currentStage.show();
        }catch (Exception e) {}

    }
    public void Dokumente(MouseEvent mouseEvent) {
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        currentStage.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Klasse.fxml"));
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Dokumente.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            currentStage.setTitle("Modern learning");
            currentStage.getIcons().add(new Image("file:src/main/Media/SkillBuildersLogo.png"));
            currentStage.setMinHeight(640);
            currentStage.setMinWidth(1000);
            currentStage.setWidth(scene.getWidth());

            currentStage.setScene(scene);

            currentStage.show();
        } catch (Exception e) {
        }
    }

    public void Fach(MouseEvent mouseEvent)
    {Stage currentStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        try {
            Node source = (Node) mouseEvent.getSource();
            String klassenid = source.getId();
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Fach.fxml"));

            Scene scene = new Scene(fxmlLoader.load(),currentStage.getWidth(),currentStage.getHeight());
            Fach fach = fxmlLoader.getController();
            fach.setKlasseId(klassenid);
            currentStage.setScene(scene);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}