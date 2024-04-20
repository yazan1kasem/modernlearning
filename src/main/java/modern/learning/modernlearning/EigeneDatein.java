package modern.learning.modernlearning;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EigeneDatein {

    @FXML
    private AnchorPane mainPane;

    // Diese Felder speichern die anfängliche Verschiebung zwischen dem Fenster und dem Mauszeiger.
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private void initialize() {
        // Event-Handler für den Mausklick zum Initialisieren der Verschiebung.
        mainPane.setOnMousePressed(this::handleMousePressed);

        // Event-Handler für das Ziehen des Fensters.
        mainPane.setOnMouseDragged(this::handleMouseDragged);
    }

    private void handleMousePressed(MouseEvent event) {
        xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

    private void handleMouseDragged(MouseEvent event) {
        Stage stage = (Stage) mainPane.getScene().getWindow();
        stage.setX(event.getScreenX() - xOffset);
        stage.setY(event.getScreenY() - yOffset);
    }
}