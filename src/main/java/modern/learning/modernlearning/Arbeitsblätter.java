package modern.learning.modernlearning;

import entities.A_Arbeitsblatt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Arbeitsblätter implements Initializable {
    EntityManager emf= Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

    @FXML
    private FlowPane Arbeitsblaetter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DrawArbeitsblaetter();
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
        private String KlasseID = "5AHBGM";
        private String FachID = "Mathematik";

   public Arbeitsblätter(String klasseID, String fachID) {
        KlasseID = klasseID;
        FachID = fachID;
    }
   public Arbeitsblätter() {

   }

    public void DrawArbeitsblaetter() {
        System.out.println("Arbeitsblaetter: " + Arbeitsblaetter); // Debug statement

        List<A_Arbeitsblatt> arbeitsblattList = emf.createQuery("select a From A_Arbeitsblatt a",A_Arbeitsblatt.class).getResultList();
        List<A_Arbeitsblatt> filter_arbeitsblatt = arbeitsblattList.stream().filter(a -> a.getA_KF_Bez().getKF_F_Bez().equals(FachID) && a.getA_KF_Bez().getKF_KL_Bez().equals(KlasseID)).collect(Collectors.toList());
        System.out.println(filter_arbeitsblatt.size());

        VBox container = new VBox();

        for (A_Arbeitsblatt arbeitsblatt : filter_arbeitsblatt) {
            BorderPane box = designBorder(arbeitsblatt.getA_Name());
            container.getChildren().add(box);
        }

        if (Arbeitsblaetter != null) {
            Arbeitsblaetter.getChildren().add(container);
        } else {
            System.err.println("Arbeitsblaetter is null!"); // Debug statement
        }


    }
    @FXML
    public void zurück(javafx.scene.input.MouseEvent mouseEvent) {
        Stage currentStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Fach.fxml"));
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
}
