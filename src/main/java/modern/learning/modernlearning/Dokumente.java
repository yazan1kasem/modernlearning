package modern.learning.modernlearning;

import entities.D_Dokumente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.ResourceBundle;

public class Dokumente implements Initializable {
    @FXML
    private FlowPane Dokumente;

    private EntityManager emf;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        emf = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();
        drawDokumente();
    }

    private BorderPane createDocumentPane(D_Dokumente dokument) {
        BorderPane documentPane = new BorderPane();
        documentPane.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10px; -fx-border-radius: 5px; -fx-border-color: black; -fx-border-width: 2px; -fx-background-radius: 5px; -fx-border-insets: -1px");

        // Bild laden und als ImageView erstellen
        Image image = new Image("file:src/main/Media/dokument_icon.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100); // Hier kannst du die Breite des Icons anpassen
        imageView.setFitHeight(100); // Hier kannst du die Höhe des Icons anpassen

        // Text erstellen
        Text text = new Text(dokument.getD_Name());

        // Bild und Text zur Mitte (Center) der BorderPane hinzufügen
        documentPane.setCenter(imageView);
        documentPane.setBottom(text);
        BorderPane.setAlignment(text, Pos.CENTER); // Zentriert den Text am unteren Rand
        documentPane.setOnMouseClicked(downloadevent->{
            String pdfUrl = text.getText();
            try {
                String savePath = showSaveDialog();
                if (savePath != null) {
                    downloadPDF(pdfUrl, savePath);
                    System.out.println("PDF erfolgreich heruntergeladen!");
                } else {
                    System.out.println("Benutzer hat den Speicherpfad nicht ausgewählt.");
                }
            } catch (IOException e) {
                System.err.println("Fehler beim Herunterladen der PDF: " + e.getMessage());
            }
        });
        documentPane.setOnMouseEntered(Documententer->{
            documentPane.setCursor(Cursor.HAND);
        });
        return documentPane;
    }

    // ... andere vorhandene Deklarationen ...
    private void drawDokumente() {
        List<D_Dokumente> dokumenteList = emf.createQuery("select d From D_Dokumente d", D_Dokumente.class).getResultList();
        System.out.println(dokumenteList.size());

        VBox container = new VBox();

        for (D_Dokumente dokument : dokumenteList) {
            BorderPane documentPane = createDocumentPane(dokument);
            container.getChildren().add(documentPane);
        }

        Dokumente.getChildren().add(container);
    }

    public void downloadPDF(String pdfUrl, String savePath) throws IOException {
        URL url = new URL("file:src/main/Files/"+pdfUrl+".pdf");
        URLConnection connection = url.openConnection();

        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }catch (IOException e) {

        }
    }

    public String showSaveDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Speicherort wählen");
        fileChooser.setInitialFileName("document.pdf");
        FileChooser.ExtensionFilter fileExtensions =new FileChooser.ExtensionFilter("Datei typ","*.pdf");
        fileChooser.getExtensionFilters().add(fileExtensions);
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        } else {
            return null;
        }
    }

    @FXML
    public void zurück(javafx.scene.input.MouseEvent mouseEvent) {
        {
            Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Klasse.fxml"));
                Parent root= fxmlLoader.load();

                currentStage.getScene().setRoot(root);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}