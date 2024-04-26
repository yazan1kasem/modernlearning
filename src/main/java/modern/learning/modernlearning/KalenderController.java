/*
 * Die Klasse KalenderController ist ein JavaFX-Controller für eine Kalenderanwendung.
 * Sie verwendet JavaFX für die Benutzeroberflächenkomponenten und ZonedDateTime zur
 * Verarbeitung von Datum und Uhrzeit. Der KalenderController wird in einem FlowPane angezeigt,
 * und jeder Tag kann Aktivitäten enthalten, die als Rechtecke im KalenderController dargestellt werden.
 * Durch Klicken auf einen Tag mit mehreren Aktivitäten wird eine Zusammenfassung angezeigt,
 * und durch Klicken auf einzelne Aktivitäten erhalten Sie weitere Details.
 */

package modern.learning.modernlearning;


import entities.K_Kalender;
import entities.N_Notifications;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import modern.learning.modernlearning.CalenderClasses.Benachrichtigung;
import modern.learning.modernlearning.CalenderClasses.KalenderPopover;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javafx.scene.layout.GridPane;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;


public class KalenderController implements Initializable {
    // FXML-Komponenten, die verschiedene Teile der Benutzeroberfläche repräsentieren
    @FXML
    private AnchorPane KalenderF;
    @FXML
    private Text year;
    @FXML
    private Text month;
    @FXML
    private FlowPane calendar;
    @FXML
    private VBox calendarPanel;

    @FXML
    private Button back;
    @FXML
    private Button forward;
    @FXML
    private Button ZurückButton;

    // Datumvariablen, um das fokussierte Datum und das aktuelle Datum zu verfolgen
    private ZonedDateTime dateFocus;
    private ZonedDateTime today;
    private KalenderPopover kalenderPopover; // Declare a class-level variable
    EntityManager em= Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), calendarPanel);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
        List<N_Notifications> notificationsList= em.createQuery("select n from N_Notifications n where n.user.U_Name=:username").setParameter("username", Currentuser.getUsername()).getResultList();
        for (N_Notifications n_notification:notificationsList) {
            Benachrichtigung benachrichtigung=new Benachrichtigung( n_notification.getN_K_ID().getK_Title(),n_notification.getN_Vorzeit(),n_notification.getN_id());
        }


        // Setzt das Jahr und den Monat im UI entsprechend dem Fokusdatum.
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(getGermanMonthsName());
        addButtonHoverEffect();

    }
    private String getGermanMonthsName(){
        Locale germanLocale = new Locale("de", "AT");
        DateTimeFormatter germanFormatter = DateTimeFormatter.ofPattern("MMMM", germanLocale);
        String germanMonthName = dateFocus.getMonth().getDisplayName(TextStyle.FULL, germanLocale);
        return germanMonthName;
    }



    private void addButtonHoverEffect(){
        back.hoverProperty().addListener(enterbackevent->{
            back.setStyle("-fx-cursor: hand; -fx-padding: -3px 10px 0px 10px; -fx-font-size: 26px; -fx-text-align: center; -fx-text-decoration: none; -fx-border-width: 2px; -fx-border-color: #FFA500; -fx-text-fill: #FFFFFF; -fx-background-color: #FFA500; -fx-border-radius: 5px;");
        });
        back.setOnMouseExited(exitbackevent->{
            back.setStyle("-fx-cursor: hand; -fx-padding: -3px 10px 0px 10px;-fx-font-size: 26px; -fx-text-align: center; -fx-background-color: #FFFFFF; -fx-text-decoration: none; -fx-border-width: 2px; -fx-text-fill: #FFA500;-fx-border-radius: 5px;-fx-border-color: black");
        });
        forward.hoverProperty().addListener(enterforwardevent->{
            forward.setStyle("-fx-cursor: hand; -fx-padding: -3px 10px 0px 10px; -fx-font-size: 26px; -fx-text-align: center; -fx-text-decoration: none; -fx-border-width: 2px; -fx-border-color: #FFA500; -fx-text-fill: #FFFFFF; -fx-background-color: #FFA500; -fx-border-radius: 5px;");
        });
        forward.setOnMouseExited(exitforwardevent->{
            forward.setStyle("-fx-cursor: hand; -fx-padding: -3px 10px 0px 10px;-fx-font-size: 26px; -fx-text-align: center; -fx-background-color: #FFFFFF; -fx-text-decoration: none; -fx-border-width: 2px; -fx-text-fill: #FFA500;-fx-border-radius: 5px;-fx-border-color: black");
        });
        ZurückButton.hoverProperty().addListener(enterZurueckevent->{
            ZurückButton.setStyle("-fx-font-size: 16px; -fx-padding: 5 10; -fx-background-color: #FF8C00; -fx-text-fill: white; -fx-border-radius: 5px; -fx-cursor: hand;-fx-border-width: 2px; -fx-background-radius: 5px;-fx-border-insets: -1px; -fx-border-color: #FF8C00;");
        });
        ZurückButton.setOnMouseExited(exitZurueckevent->{
            ZurückButton.setStyle("-fx-font-size: 16px; -fx-padding: 5 10; -fx-background-color: white; -fx-text-fill: #FF8C00;-fx-border-radius: 5px;-fx-border-color: black;-fx-border-width: 2px; -fx-background-radius: 5px;-fx-border-insets: -1px");
        });
    }
    // Ereignismethoden zum Zurücksetzen des Kalenderansicht um einen Monat
    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(getGermanMonthsName());
        calendar.getChildren().clear();
        drawCalendar();
    }
    // Ereignismethoden zum Vorspringen des Kalenderansicht um einen Monat
    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(getGermanMonthsName());
        calendar.getChildren().clear();
        drawCalendar();
    }
    public void drawCalendar() {
        // Ermittelt die maximale Anzahl der Tage im Monat und den Wochentag des ersten Tages im Monat.
        int monthMaxDate = dateFocus.getMonth().maxLength();
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int ersterTagImMonat = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        calendarPanel.setStyle("-fx-background-radius: 20; -fx-background-color: white;-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 15, 0, 5,  5)");



        // Löscht vorhandene Kinder, bevor der KalenderController neu gezeichnet wird.
        calendar.getChildren().clear();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        // Add the day labels to the calendar
        gridPane.add(TextV("Sonntag"), 0, 0);
        gridPane.add(TextV("Montag"), 1, 0);
        gridPane.add(TextV("Dienstag"), 2, 0);
        gridPane.add(TextV("Mittwoch"), 3, 0);
        gridPane.add(TextV("Donnerstag"), 4, 0);
        gridPane.add(TextV("Freitag"), 5, 0);
        gridPane.add(TextV("Samstag"), 6, 0);

        List<K_Kalender> KalenderListe = em.createQuery(
                        "SELECT k FROM K_Kalender k where k.user.U_Name=:username", K_Kalender.class)
                .setParameter("username", Currentuser.getUsername())
                .getResultList();
        int jahr= dateFocus.getYear();
        int monat=dateFocus.getMonthValue();
        // Schleife für die Zeilen im KalenderController.
        for (int i = 0; i < 6; i++) {
            // Schleife für die Tage in jeder Zeile.
            for (int j = 0; j < 7; j++) {
                // Berechnet den Tag im Monat für das aktuelle Kästchen im KalenderController.
                int calculatedDate = (j + 1) + (7 * i);
                if (ersterTagImMonat==7){
                    ersterTagImMonat=0;
                }
                if(calculatedDate==monthMaxDate+ersterTagImMonat+1){
                    i=7;
                    break;
                }
                StackPane stackPane= new StackPane();
                Rectangle rectangle = creatDateRectangleDesign();
//              rectangle.setId(dateFocus.getYear()+"."+dateFocus.getMonthValue()+"."+(calculatedDate - ersterTagImMonat));
                // Fügt das Rechteck zur StackPane hinzu.
                stackPane.getChildren().add(rectangle);
                try {
                    int finalErsterTagImMonat = ersterTagImMonat;

                    // Überprüft, ob das Kästchen im gültigen Bereich des Monats liegt.
                    if (calculatedDate <= ersterTagImMonat + monthMaxDate) {
                        if (calculatedDate > ersterTagImMonat) {
                            List<K_Kalender> filteredList = KalenderListe.stream()
                                    .filter(k -> k.getK_vonDatum().toLocalDate().compareTo(LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), calculatedDate - finalErsterTagImMonat)) <= 0 &&
                                            k.getK_bisDatum().toLocalDate().compareTo(LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), calculatedDate - finalErsterTagImMonat)) >= 0)
                                    .collect(Collectors.toList());
                            // Zeigt den Tag im Kästchen an.
                            Text date = new Text(String.valueOf(calculatedDate - ersterTagImMonat));
                            date.setStyle("-fx-font-weight: bold; -fx-font-size: 30;");
                            Circle eintraegeanzahlkreis=new Circle(12);
                            Text eintraegeanzahl=new Text();
                            if(filteredList.size() > 0){
                                eintraegeanzahl.setText(String.valueOf(filteredList.size()));
                                eintraegeanzahl.setTranslateY(-(rectangle.getHeight() / 2) * 0.90);
                                eintraegeanzahl.setTranslateX((rectangle.getWidth() / 2)* 0.90);
                                eintraegeanzahl.setFill(Color.valueOf("#FFFFFF"));
                                eintraegeanzahl.setStyle("-fx-font-weight: bold;-fx-font-size: 13;");
                                eintraegeanzahlkreis.setTranslateY(-(rectangle.getHeight() / 2) * 0.90);
                                eintraegeanzahlkreis.setTranslateX((rectangle.getWidth() / 2)* 0.90);
                                eintraegeanzahlkreis.setFill(Paint.valueOf("#5dff5d"));

                                stackPane.getChildren().add(eintraegeanzahlkreis);

                            }

                            rectangle.heightProperty().addListener(new ChangeListener<Number>() {
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                    date.setTranslateY(0);
                                    eintraegeanzahl.setTranslateY(-(rectangle.getHeight() / 2) * 0.90);
                                    eintraegeanzahlkreis.setTranslateY(-(rectangle.getHeight() / 2) * 0.90);
                                }
                            });
                            rectangle.widthProperty().addListener(new ChangeListener<Number>() {
                                @Override
                                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                    date.setTranslateX(0);
                                    eintraegeanzahl.setTranslateX((rectangle.getWidth() / 2)* 0.90);
                                    eintraegeanzahlkreis.setTranslateX((rectangle.getWidth() / 2)* 0.90);

                                }
                            });

                            stackPane.getChildren().add(date);
                            stackPane.getChildren().add(eintraegeanzahl);
                            // Setzt den Rahmen von Rechtecken, die den heutigen Tag repräsentieren, auf blau.
                            selectColor(rectangle, calculatedDate);
                            // Setzt die Anfangsfarbe für das Rechteck und fügt Hover-Effekte hinzu.
                            addHoverEffect(stackPane,rectangle, calculatedDate,date,eintraegeanzahl);
                            stackPane.setOnMouseClicked(event -> {
                                showCalendarPopover(rectangle, event, LocalDate.of(jahr,monat,calculatedDate - finalErsterTagImMonat));

                            });
                        }
                    }
                }catch (Exception e ){
                    System.out.println(e.getMessage());
                }
                gridPane.add(stackPane, j,i+1);
            }
        }

        // Fügt die StackPane zum KalenderController hinzu.
        calendar.getChildren().add(gridPane);
        KalenderF.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                calendarPanel.setPrefHeight(newValue.floatValue()/1.2);
            }
        });
        KalenderF.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                calendarPanel.setPrefWidth(newValue.floatValue()/1.2);

            }
        });
    }






    private Rectangle creatDateRectangleDesign(){
        double calendarWidth = calendarPanel.getWidth();
        double calendarHeight = calendarPanel.getHeight();

        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(strokeWidth);
        double rectangleWidth = (calendarWidth / 8) - strokeWidth - spacingH;
        rectangle.setWidth(rectangleWidth);
        double rectangleHeight = (calendarHeight / 8.5) - strokeWidth - spacingV;
        rectangle.setHeight(rectangleHeight);
        rectangle.setArcWidth(10.0);
        rectangle.setArcHeight(10.0);

        calendarPanel.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                rectangle.setHeight((newValue.floatValue()/8.5) - strokeWidth - spacingV);
            }
        });
        calendarPanel.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                rectangle.setWidth((newValue.floatValue()/8 ) - strokeWidth - spacingH);

            }
        });

        return rectangle;
    }
    private void selectColor(Rectangle rectangle,int calculatedDate ){
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        // Setzt die Farbe basierend auf dem Monat.
        if (calculatedDate - dateOffset == today.getDayOfMonth() && today.getMonth() == dateFocus.getMonth() && today.getYear() == dateFocus.getYear()) {
            rectangle.setFill(Color.valueOf("#FFE07D")); // Farbe für den aktuellen Tag
        } else {
            rectangle.setFill(Color.valueOf("#FFC305")); // Farbe für den aktuellen Monat

        }

    }
    private void showCalendarPopover(Rectangle ownerRectangle, MouseEvent event, LocalDate datum) {
        System.out.println("\u001B[32m" + "Test 1 succeeded (PopOver gestartet)" + "\u001B[0m");

        if (kalenderPopover == null) {
            kalenderPopover = new KalenderPopover(ownerRectangle, datum);
            kalenderPopover.setKalenderController(this);
        } else {
            kalenderPopover.hide(); // Example: hide the existing popover
            kalenderPopover = new KalenderPopover(ownerRectangle, datum);
            kalenderPopover.setKalenderController(this);
        }
    }
    private @NotNull Label TextV(String text){
        Label label= new Label(text);
        label.setStyle("-fx-font-family: 'Arial Black'; -fx-text-fill: black");
        label.setAlignment(Pos.TOP_CENTER);
        label.setTranslateY(-10);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPrefWidth(calendarPanel.getWidth()/9);
        calendarPanel.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                label.setPrefWidth(newValue.floatValue()/9 );
            }
        });
        return label;
    }
    // Add hover effect to the box
    private void addHoverEffect(StackPane stackPane, Rectangle rectangle, int calculatedDate, Text date, Text eintraegeanzahl) {
        AtomicBoolean animated = new AtomicBoolean(false);

        stackPane.setOnMouseEntered( event -> {
            if (!animated.get()) {
                rectangle.setFill(Color.valueOf("#FFFFFF"));
                date.setFill(Color.valueOf("#FFA500"));
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), rectangle);
                scaleTransition.setToX(1.1);
                scaleTransition.setToY(1.1);
                scaleTransition.play();
                rectangle.setCursor(Cursor.HAND);
                date.setCursor(Cursor.HAND);
                eintraegeanzahl.setCursor(Cursor.HAND);
                animated.set(true);
            }
        });

        stackPane.setOnMouseExited(event -> {
            if (animated.get()) {
                date.setFill(Color.valueOf("#000000"));
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), rectangle);
                scaleTransition.setToX(1.0);
                scaleTransition.setToY(1.0);
                scaleTransition.play();
                selectColor(rectangle, calculatedDate);
                animated.set(false);
            }
        });
    }
    public void zurück(MouseEvent mouseEvent) {
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
