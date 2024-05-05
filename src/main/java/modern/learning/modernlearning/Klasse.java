package modern.learning.modernlearning;

import entities.K_Kalender;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class Klasse implements Initializable {
    @FXML
    private FlowPane classSelection;

    @FXML
    private VBox classSelectionPanel;
    @FXML
    private AnchorPane ClassSelection;
    @FXML
    private VBox sidebar;
    @FXML
    private ImageView logo;

    private List<K_Kalender> kalenderList;
    public Klasse() {
        kalenderList= DatabaseConnection.getConnection().createQuery("SELECT k FROM K_Kalender k", K_Kalender.class).getResultList();
    }
    public void Kalender(MouseEvent mouseEvent) {
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Kalender.fxml"));
            Parent root= fxmlLoader.load();

            currentStage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void Dokumente(MouseEvent mouseEvent) {
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Dokumente.fxml"));
            Parent root= fxmlLoader.load();

            currentStage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void Fach(MouseEvent mouseEvent){
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Fach.fxml"));

            Parent root= fxmlLoader.load();

            Fach fach=fxmlLoader.getController();
            Node source=(Node) mouseEvent.getSource();

            fach.setKlasseId(source.getId());
            currentStage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image("file:src/main/Media/SkillBuildersLogo.png");
        logo.setImage(image);
        logo.setFitWidth(50);
        logo.setFitHeight(50);
        smallkalender();
        kalenderList= DatabaseConnection.getConnection().createQuery("SELECT k FROM K_Kalender k", K_Kalender.class).getResultList();
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), classSelectionPanel);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    public void MouseEntered(MouseEvent mouseEvent) {
        javafx.scene.control.Button button = (javafx.scene.control.Button) mouseEvent.getSource();
        button.setStyle("-fx-cursor: hand; -fx-font-size: 20px; -fx-padding: 10 20; -fx-background-color: #ffa500; -fx-text-fill: #ffffff; -fx-border-radius: 5px; -fx-border-color: #ffa500; -fx-border-width: 2px; -fx-background-radius: 5px; -fx-border-insets: -1px");
    }

    public void MouseExit(MouseEvent mouseEvent) {
        javafx.scene.control.Button button = (javafx.scene.control.Button) mouseEvent.getSource();
        button.setStyle("-fx-cursor: hand; -fx-font-size: 20px; -fx-padding: 10 20; -fx-background-color: #ffffff; -fx-text-fill: #FF8C00; -fx-border-radius: 5px; -fx-border-color: black; -fx-border-width: 2px; -fx-background-radius: 5px; -fx-border-insets: -1px");
    }

    public void MouseEntered1(MouseEvent mouseEvent) {
        javafx.scene.control.Button button = (javafx.scene.control.Button) mouseEvent.getSource();
        button.setStyle("-fx-cursor: hand; -fx-padding: 15px; -fx-font-size: 20px; -fx-background-color: #ffa500; -fx-text-fill: #ffffff; -fx-border-radius: 5px; -fx-border-color: #ffa500; -fx-border-width: 2px; -fx-background-radius: 5px; -fx-border-insets: -1px");
    }

    public void MouseExit1(MouseEvent mouseEvent) {
        javafx.scene.control.Button button = (javafx.scene.control.Button) mouseEvent.getSource();
        button.setStyle("-fx-cursor: hand; -fx-padding: 15px; -fx-font-size: 20px; -fx-background-color: #FFFFFF; -fx-text-fill: #FFA500; -fx-border-radius: 5px; -fx-border-color: black; -fx-border-width: 2px; -fx-background-radius: 5px; -fx-border-insets: -1px");
    }
    @FXML
    private Pane calenderpane;
    private void smallkalender(){

        GridPane calendarPane = new GridPane();
        calendarPane.setHgap(10);
        calendarPane.setVgap(10);


        LocalDate date = LocalDate.now();

        int month = date.getMonthValue();
        int year = date.getYear();

        javafx.scene.control.Label[] daysOfWeek = {
                CreateLabel("So"),
                CreateLabel("M"),
                CreateLabel("DI"),
                CreateLabel("M"),
                CreateLabel("Do"),
                CreateLabel("F"),
                CreateLabel("Sa")
        };

        for (int i = 0; i < 7; i++) {
            calendarPane.add(daysOfWeek[i], i, 0);
        }

        int startDay = date.withDayOfMonth(1).getDayOfWeek().getValue();

        int day = 1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                if (day <= date.lengthOfMonth() && (i > 0 || j >= startDay)) {
                    javafx.scene.control.Label lbl = CreateLabel(Integer.toString(day));
                    makeitred(lbl);
                    calendarPane.add(lbl, j, i + 1);
                    day++;
                }
            }
        }
        calenderpane.getChildren().add(calendarPane);


    }
    private Label CreateLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);

        calenderpane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                label.setPrefSize(newValue.intValue()/10, 30);
                label.setFont(new javafx.scene.text.Font( calenderpane.getWidth()/18));
            }
        });
        return label;
    }
    private void makeitred(Label label){
        kalenderList.stream().filter(k -> k.getK_vonDatum().equals(LocalDateTime.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), Integer.parseInt( label.getText()),0,0,0))).forEach(k -> {
            label.setStyle("-fx-text-fill: red");
        });

    }

    public void EigenenDateienFenster(MouseEvent mouseEvent) {
        Stage currentStage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource( "eigeneDatein.fxml"));

            Parent root= fxmlLoader.load();

            Node source=(Node) mouseEvent.getSource();

            currentStage.getScene().setRoot(root);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}