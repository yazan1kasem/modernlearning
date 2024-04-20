package modern.learning.modernlearning;

import javafx.scene.image.Image;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class EigeneDatein {
    @FXML
    private Pane dragDropArea;


    @FXML
    private void handleButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            saveFile(selectedFile);
        } else {
            System.out.println("Dateiauswahl wurde abgebrochen.");
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
    }


    private void saveFile(File file) {
        try {

            String projectSrcDirectory = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "EigeneDatein";
            Path destPath = Paths.get(projectSrcDirectory, file.getName());


            if (!Files.exists(destPath.getParent())) {
                Files.createDirectories(destPath.getParent());
            }

            Files.copy(file.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Datei wurde gespeichert: " + destPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler beim Speichern der Datei: " + e.getMessage());
        }
        saveFileInfoInDatabase(file, 1);
    }
    @FXML
    private ListView<File> pdfListView;

    /*public void initialize() {
        loadPdfFilesToListView();

        pdfListView.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
            @Override
            public ListCell<File> call(ListView<File> listView) {
                return new ListCell<File>() {
                    private final ImageView imageView = new ImageView();
                    private final HBox hbox = new HBox();
                    private final Text text = new Text();

                    {
                        hbox.getChildren().addAll(imageView, text);
                        hbox.setSpacing(10);
                    }

                    @Override
                    protected void updateItem(File file, boolean empty) {
                        super.updateItem(file, empty);

                        if (empty || file == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            text.setText(file.getName());

                            imageView.setImage(new Image(getClass().getResourceAsStream("/Media/pdf_icon.png")));
                            hbox.getChildren().clear();
                            hbox.getChildren().addAll(imageView, text);
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });

        /*pdfListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                File file = pdfListView.getSelectionModel().getSelectedItem();
                if (file != null) {
                    openPdfFile(file);
                }
            }
        });
    }*/

    /*private void loadPdfFilesToListView() {
        String projectDirectory = System.getProperty("user.dir") + File.separator + "EigeneDatein";
        File dir = new File(projectDirectory);
        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith(".pdf"));
        ObservableList<File> items = FXCollections.observableArrayList();

        if (files != null) {
            items.addAll(Arrays.asList(files));
        }

        pdfListView.setItems(items);
    }*/

    /*private void openPdfFile(File file) {
        try {

            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    private Connection connect() {
        String url = "jdbc:sqlite:Modernlearning.sqlite";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Methode zum Speichern der Dateiinformationen in der Datenbank
    private void saveFileInfoInDatabase(File file, int userId) {
        String sql = "INSERT INTO E_EigeneArbeitsblaetter(E_DateiName, E_Timestamp, E_U_id) VALUES(?,?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, file.getName());
            pstmt.setString(2, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
            pstmt.setInt(3, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
