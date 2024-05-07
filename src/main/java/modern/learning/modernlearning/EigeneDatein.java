package modern.learning.modernlearning;


import entities.E_EigeneArbeitsblaetter;
import entities.U_user;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EigeneDatein implements Initializable {

    @FXML
    public FlowPane pdfListView;
    @FXML
    private Pane dragDropArea;
    private int userid;
    private ObservableList<File> fileList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPdfFiles();
    }
    public EigeneDatein() {
     userid=DatabaseConnection.getConnection().createQuery("SELECT u from U_user u where u.U_Name=:username", U_user.class).setParameter("username", Currentuser.getUsername()).getSingleResult().getU_id();
    }
    private void loadPdfFiles() {
        String sql = "SELECT E_DateiName, E_Timestamp FROM E_EigeneArbeitsblaetter WHERE E_U_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userid);
            ResultSet rs = pstmt.executeQuery();

            String basePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "EigeneDatein";
            fileList.clear(); // Die Liste aller geladenen Dateien löschen
            pdfListView.getChildren().clear(); // Die Anzeige leeren, um die neuen Dateien anzuzeigen

            while (rs.next()) {
                String fileName = rs.getString("E_DateiName");
                File file = new File(basePath, fileName);
                if (file.exists()) {
                    String timestamp = rs.getString("E_Timestamp"); // Zeitstempel als String aus der Datenbank lesen
                    BorderPane pane = designBorder(file, timestamp); // Zeitstempel übergeben
                    pdfListView.getChildren().add(pane);
                    fileList.add(file);
                } else {
                    System.out.println("File does not exist: " + file.getAbsolutePath());
                }
            }

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private BorderPane designBorder(File file, String timestamp) {
        BorderPane pane = new BorderPane();
        Image image = new Image("file:src/main/Media/pdf_icon.png"); // Pfad zum PDF-Icon anpassen
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);

        Text text = new Text(file.getName());
        text.setWrappingWidth(100);
        text.setTextAlignment(TextAlignment.CENTER);
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String formattedTimestamp = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Text timestampText = new Text(formattedTimestamp);
        VBox vbox = new VBox(text, timestampText);
        vbox.setAlignment(Pos.CENTER);

        pane.setCenter(imageView);
        pane.setBottom(vbox); // Setzen Sie die VBox mit Texten am unteren Rand
        BorderPane.setAlignment(vbox, Pos.CENTER);
        pane.setCursor(Cursor.HAND);

        pane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                openPdfFile(file);
            }
        });

        return pane;
    }

    private Connection connect() throws SQLException {
        String url = "jdbc:sqlite:Modernlearning.sqlite";
        return DriverManager.getConnection(url);
    }

    @FXML
    private void handleButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            saveFile(selectedFile);
        } else {
            showAlert(Alert.AlertType.valueOf("Dateiauswahl"), null, "Keine Datei ausgewählt.");
        }
    }
    public void SaveFile(File file){
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Es können nur PDF-Dateien gespeichert werden.");
            return;
        }
        try {
            String projectSrcDirectory = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "EigeneDatein";
            Path destPath = Paths.get(projectSrcDirectory, file.getName());

            if (!Files.exists(destPath.getParent())) {
                Files.createDirectories(destPath.getParent());
            }

            Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }
    public void saveFile(File file) {
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Es können nur PDF-Dateien gespeichert werden.");
            return;
        }

        try {
            String projectSrcDirectory = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "EigeneDatein";
            Path destPath = Paths.get(projectSrcDirectory, file.getName());

            if (!Files.exists(destPath.getParent())) {
                Files.createDirectories(destPath.getParent());
            }

            // Überprüfen, ob die Datei bereits existiert
            if (Files.exists(destPath)) {
                // Dateiname bereits vorhanden, daher Nummer anhängen
                String baseFileName = file.getName().substring(0, file.getName().lastIndexOf(".pdf"));
                String fileExtension = ".pdf";
                int count = 1;
                String numberedFileName;
                do {
                    numberedFileName = baseFileName + "_" + count + fileExtension;
                    destPath = Paths.get(projectSrcDirectory, numberedFileName);
                    count++;
                } while (Files.exists(destPath));
            }

            Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
            saveFileInfoInDatabase(destPath.toFile(), userid); // Verwenden Sie den neuen Dateinamen
            fileList.add(destPath.toFile()); // Verwenden Sie den neuen Dateinamen
            refreshView();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Fehler beim Speichern der Datei: " + e.getMessage());
        }
    }

    private void saveFileInfoInDatabase(File file, int userId) {
        String sql = "INSERT INTO E_EigeneArbeitsblaetter(E_DateiName, E_Timestamp, E_U_id) VALUES(?,?,?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, file.getName());
            pstmt.setString(2, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.valueOf("Database Error"), "Error saving file info to database", e.getMessage());
        }
    }

    private void openPdfFile(File file) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                showAlert(Alert.AlertType.valueOf("Error"), "Error opening file", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.valueOf("Error"), "Cannot open file", "Desktop is not supported.");
        }
    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != dragDropArea && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
        event.consume();
    }

    @FXML
    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;

            for (File file : db.getFiles()) {
                saveFile(file);
            }
        }
        event.setDropCompleted(success);
        event.consume();
        dragDropArea.visibleProperty().set(true);

    }

    @FXML
    private void handleFileDeletion() {
        if (fileList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warnung", "Es sind keine Dateien vorhanden.");
            return;
        }

        List<String> fileNames = fileList.stream().map(File::getName).collect(Collectors.toList());

        ChoiceDialog<String> dialog = new ChoiceDialog<>(fileNames.get(0), fileNames);
        dialog.setTitle("Datei zum Löschen auswählen");
        dialog.setHeaderText(null);
        dialog.setContentText("Wählen Sie die Datei aus, die Sie löschen möchten:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(fileName -> {
            File selectedFile = fileList.stream().filter(file -> file.getName().equals(fileName)).findFirst().orElse(null);

            if (selectedFile != null) {
                // Überprüfen, ob die ausgewählte Datei im EigeneDatein-Ordner liegt
                String filePath = selectedFile.getAbsolutePath();
                if (filePath.contains("EigeneDatein")) {
                    boolean deleteConfirmed = showDeleteConfirmation(selectedFile.getName());
                    if (deleteConfirmed) {
                        boolean deleted = selectedFile.delete();
                        if (deleted) {
                            fileList.remove(selectedFile);
                            deleteFileFromDatabase(selectedFile);
                            showAlert(Alert.AlertType.INFORMATION, "Erfolg", "Datei erfolgreich gelöscht.");
                            refreshView();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Fehler", "Die Datei konnte nicht gelöscht werden.");
                        }
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Warnung", "Sie können nur Dateien im EigeneDatein-Ordner löschen.");
                }
            }
        });


    }

    private void deleteFileFromDatabase(File file) {
        String sql = "DELETE FROM E_EigeneArbeitsblaetter WHERE E_DateiName = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, file.getName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Fehler", "Die Datei konnte nicht aus der Datenbank gelöscht werden: " + e.getMessage());
        }
    }

    private boolean showDeleteConfirmation(String fileName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bestätigung");
        alert.setHeaderText("Möchten Sie die Datei '" + fileName + "' wirklich löschen?");
        alert.setContentText("Diese Aktion kann nicht rückgängig gemacht werden.");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void refreshView() {
        pdfListView.getChildren().clear();
        loadPdfFiles();
    }

    public void handleDragentered(DragEvent dragEvent) {
        dragDropArea.visibleProperty().set(true);

    }

    public void handleDragexit(DragEvent dragEvent) {
        dragDropArea.visibleProperty().set(false);

    }

    public void zurück(MouseEvent mouseEvent) {
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource( "Klasse.fxml"));

            Parent root= fxmlLoader.load();

            Node source=(Node) mouseEvent.getSource();

            currentStage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
