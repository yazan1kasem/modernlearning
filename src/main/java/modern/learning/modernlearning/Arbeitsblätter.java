package modern.learning.modernlearning;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.ResourceBundle;

public class Arbeitsblätter implements Initializable {
    EntityManager emf= Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

    @FXML
    private BorderPane Arbeitsblaetter;

    public Arbeitsblätter() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public BorderPane designBorder(String name) {
        BorderPane Arbeitsblatt = new BorderPane();

        // Bild laden und als ImageView erstellen
        Image image = new Image("file:src/main/Media/pdf_icon.png");
        ImageView imageView = new ImageView(image);

        // Text erstellen
        Text text = new Text(name);

        // Bild und Text zur Mitte (Center) der BorderPane hinzufügen
        Arbeitsblatt.setCenter(imageView);
        Arbeitsblatt.setBottom(text);
        BorderPane.setAlignment(text, Pos.CENTER); // Zentriert den Text am unteren Rand

        return Arbeitsblatt;
    }
}
