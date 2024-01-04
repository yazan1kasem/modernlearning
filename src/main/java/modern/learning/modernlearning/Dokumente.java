package modern.learning.modernlearning;

import entities.D_Dokumente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Dokumente implements Initializable {
    EntityManager emf = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

    @FXML
    private FlowPane Dokumente;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DrawDokumente();
    }

    public BorderPane designBorder(String name) {
        BorderPane Dokument = new BorderPane();

        // Bild laden und als ImageView erstellen
        Image image = new Image("file:src/main/Media/pdf_icon.png");
        ImageView imageView = new ImageView(image);

        // Text erstellen
        Text text = new Text(name);

        // Bild und Text zur Mitte (Center) der BorderPane hinzufügen
        Dokument.setCenter(imageView);
        Dokument.setBottom(text);
        BorderPane.setAlignment(text, Pos.CENTER); // Zentriert den Text am unteren Rand

        return Dokument;
    }

    public Dokumente() {
    }

    public void DrawDokumente() {
        List<D_Dokumente> dokumenteList = emf.createQuery("select d From D_Dokumente d", D_Dokumente.class).getResultList();
        System.out.println(dokumenteList.size());

        VBox container = new VBox();

        for (D_Dokumente dokumente1 : dokumenteList) {
            BorderPane box = designBorder(dokumente1.getD_Name());
            container.getChildren().add(box);
        }

        Dokumente.getChildren().add(container);
    }
}