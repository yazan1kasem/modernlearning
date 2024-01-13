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
    @FXML
    public void handleClassSelection(ActionEvent event) {
        // Hier kommt der Code, den Sie ausführen möchten,
        // wenn auf einen Klassen-Button geklickt wird.
        // Sie können beispielsweise die ausgewählte Klasse erhalten
        // und entsprechende Aktionen durchführen.
        // Zum Beispiel:
        // Object source = event.getSource();
        // if (source instanceof Button) {
        //    Button clickedButton = (Button) source;
        //    String selectedClass = clickedButton.getText();
        //    // Führen Sie hier Aktionen für die ausgewählte Klasse durch.
        // }
    }
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

    public void Fach2(MouseEvent mouseEvent)
    {Stage currentStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        try {
            Node source = (Node) mouseEvent.getSource();
            String klassenid = source.getId();
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Fach.fxml"));
            Fach fach = new Fach(klassenid);
            fxmlLoader.setController(fach);
            Scene scene = new Scene(fxmlLoader.load(),currentStage.getWidth(),currentStage.getHeight());
            currentStage.setScene(scene);
        }catch (Exception e) {}
    }
}