package modern.learning.modernlearning;

import entities.A_Arbeitsblatt;
import entities.KF_Klassefach;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Arbeitsblätter implements Initializable {
    EntityManager emf = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

    @FXML
    private FlowPane Arbeitsblaetter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DrawArbeitsblaetter();

    }


    private String KlasseID;
    private String FachID;


    public Arbeitsblätter() {

    }

    public void DrawArbeitsblaetter() {
        if (KlasseID != null && FachID != null) {
            List<A_Arbeitsblatt> arbeitsblattList = emf.createQuery("select a From A_Arbeitsblatt a", A_Arbeitsblatt.class).getResultList();
            List<A_Arbeitsblatt> filter_arbeitsblatt = arbeitsblattList.stream().filter(a -> a.getA_KF_Bez().getKF_F_Bez().equals(KlasseID) && a.getA_KF_Bez().getKF_KL_Bez().equals(FachID)).collect(Collectors.toList());


            for (A_Arbeitsblatt arbeitsblatt : filter_arbeitsblatt) {
                BorderPane box = designBorder(arbeitsblatt.getA_Name());
                Arbeitsblaetter.getChildren().add(box);
            }
        }
    }

    public BorderPane designBorder(String name) {
        BorderPane Arbeitsblatt = new BorderPane();

        // Bild laden und als ImageView erstellen
        Image image = new Image("file:src/main/Media/pdf_icon.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100); // Hier kannst du die Breite des Icons anpassen
        imageView.setFitHeight(100);
        Arbeitsblatt.setOnMouseClicked(event ->
        {
            String savePath = showSaveDialog(name);
            try {
                downloadPDF(name, savePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Arbeitsblatt.setOnMouseEntered(enteredBorder -> {
            Arbeitsblatt.setCursor(Cursor.HAND);
        });

        // Text erstellen
        Text text = new Text(name);

        // Bild und Text zur Mitte (Center) der BorderPane hinzufügen
        Arbeitsblatt.setCenter(imageView);
        Arbeitsblatt.setBottom(text);
        BorderPane.setAlignment(text, Pos.CENTER);

        return Arbeitsblatt;
    }


    public void downloadPDF(String pdfUrl, String savePath) throws IOException {
        URL url = new URL("file:src/main/Files/" + pdfUrl + ".pdf");
        URLConnection connection = url.openConnection();

        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {

        }
    }

    public String showSaveDialog(String filename) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Speicherort wählen");
        fileChooser.setInitialFileName(filename + ".pdf");
        FileChooser.ExtensionFilter fileExtensions =new FileChooser.ExtensionFilter("Datei typ","*.pdf");
        fileChooser.getExtensionFilters().add(fileExtensions);
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        } else {
            return null;
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

    @FXML
    public void zurück(javafx.scene.input.MouseEvent mouseEvent) {
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Fach.fxml"));

            Parent root= fxmlLoader.load();

            Fach fach=fxmlLoader.getController();
            fach.setKlasseId(KlasseID);
            currentStage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
