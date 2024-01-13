package modern.learning.modernlearning;

import entities.A_Arbeitsblatt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
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
        imageView.setFitWidth(100); // Hier kannst du die Breite des Icons anpassen
        imageView.setFitHeight(100);

        // Text erstellen
        Text text = new Text(name);

        // Bild und Text zur Mitte (Center) der BorderPane hinzufügen
        Arbeitsblatt.setCenter(imageView);
        Arbeitsblatt.setBottom(text);
        BorderPane.setAlignment(text, Pos.CENTER);

        return Arbeitsblatt;


    }
    private String KlasseID="1" ;
    private String FachID ="MIS";


    public Arbeitsblätter() {

    }

    public void DrawArbeitsblaetter() {
        List<A_Arbeitsblatt> arbeitsblattList = emf.createQuery("select a From A_Arbeitsblatt a",A_Arbeitsblatt.class).getResultList();
        List<A_Arbeitsblatt> filter_arbeitsblatt = arbeitsblattList.stream().filter(a -> a.getA_KF_Bez().getKF_F_Bez().equals(FachID) && a.getA_KF_Bez().getKF_KL_Bez().equals(KlasseID)).collect(Collectors.toList());
        System.out.println(filter_arbeitsblatt.size());
        VBox container = new VBox();
        for (A_Arbeitsblatt arbeitsblatt : filter_arbeitsblatt) {
            BorderPane box = designBorder(arbeitsblatt.getA_Name());
            container.getChildren().add(box);
        }
        Arbeitsblaetter.getChildren().add(container);
    }
    @FXML
    public void zurück(javafx.scene.input.MouseEvent mouseEvent) {
        Stage currentStage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Fach.fxml"));
            Fach fach = fxmlLoader.getController();
            fach.setKlasseId(KlasseID);
            fxmlLoader.setController(fach);
            Scene scene = new Scene(fxmlLoader.load(),currentStage.getWidth(),currentStage.getHeight());
            currentStage.setScene(scene);
        }catch (Exception e) {

        }
    }


    public String getKlasseID() {
        return KlasseID;
    }

    public void setKlasseID(String klasseID) {
        KlasseID = klasseID;
        DrawArbeitsblaetter();
    }



    public void setFachID(String fachID) {
        FachID = fachID;
        DrawArbeitsblaetter();

    }
    public void onPdfIconClicked(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            // Beispiel-URL, die Sie durch die tatsächliche URL der PDF-Datei ersetzen müssen
            URL pdfUrl = new URL("file:src/main/Media/pdf_icon.png");

            // Öffnen Sie einen Dialog, um den Speicherort für das PDF zu wählen
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File selectedFile = fileChooser.showSaveDialog(((Node)mouseEvent.getSource()).getScene().getWindow());

            if (selectedFile != null) {
                // InputStream vom Server
                InputStream in = pdfUrl.openStream();
                ReadableByteChannel rbc = Channels.newChannel(in);

                // FileOutputStream, um die Datei lokal zu speichern
                FileOutputStream fos = new FileOutputStream(selectedFile);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                // Schließen der Streams
                fos.close();
                rbc.close();

                // Bestätigungsnachricht
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "PDF downloaded successfully!", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fehlermeldung anzeigen
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to download PDF.", ButtonType.OK);
            alert.showAndWait();
        }
    }
    //yassin ist ein schwuchtel
}