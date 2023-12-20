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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import modern.learning.modernlearning.CalenderClasses.Benachrichtigung;
import modern.learning.modernlearning.CalenderClasses.KalenderPopover;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.layout.GridPane;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class KalenderController implements Initializable {

    // FXML-Komponenten, die verschiedene Teile der Benutzeroberfläche repräsentieren
    @FXML
    private AnchorPane KalenderF;

    @FXML
    private HBox calendarBox;
    @FXML
    private Text year;
    @FXML
    private Text month;
    @FXML
    private FlowPane calendar;
    @FXML
    private VBox calendarPanel;

    // Datumvariablen, um das fokussierte Datum und das aktuelle Datum zu verfolgen
    private ZonedDateTime dateFocus;
    private ZonedDateTime today;
    private static EntityManager em = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();
    private KalenderPopover kalenderPopover; // Declare a class-level variable




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), calendarPanel);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
        List<N_Notifications> notificationsList= em.createQuery("select n from N_Notifications n").getResultList();
        for (N_Notifications n_notification:notificationsList) {
            Benachrichtigung benachrichtigung=new Benachrichtigung(n_notification.getN_K_ID().getK_Title(),n_notification.getN_Vorzeit(),n_notification.getN_id());
        }
        // Setzt das Jahr und den Monat im UI entsprechend dem Fokusdatum.
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));
    }

    // Ereignismethoden zum Zurücksetzen des Kalenderansicht um einen Monat
    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }
    // Ereignismethoden zum Vorspringen des Kalenderansicht um einen Monat
    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
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
                        "SELECT k FROM K_Kalender k", K_Kalender.class)
                .getResultList();
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
                // Fügt das Rechteck zur StackPane hinzu.
                stackPane.getChildren().add(rectangle);
                try {
                    int finalErsterTagImMonat = ersterTagImMonat;
                    rectangle.setOnMouseClicked(event -> {
                        int jahr= dateFocus.getYear();
                        int monat=dateFocus.getMonthValue();
                        showCalendarPopover(rectangle, event, LocalDate.of(jahr,monat,calculatedDate - finalErsterTagImMonat));
                    });
                    // Überprüft, ob das Kästchen im gültigen Bereich des Monats liegt.
                    if (calculatedDate <= ersterTagImMonat + monthMaxDate) {
                        if (calculatedDate > ersterTagImMonat) {
                            List<K_Kalender> filteredList = KalenderListe.stream()
                                    .filter(k -> k.getK_vonDatum().toLocalDate().compareTo(LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), calculatedDate - finalErsterTagImMonat)) <= 0 &&
                                            k.getK_bisDatum().toLocalDate().compareTo(LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), calculatedDate - finalErsterTagImMonat)) >= 0)
                                    .collect(Collectors.toList());
                            // Zeigt den Tag im Kästchen an.
                            Text date = new Text(String.valueOf(calculatedDate - ersterTagImMonat));
                            double textTranslationY = -(rectangle.getHeight() / 2) * 0.70;
                            date.setTranslateY(textTranslationY);
                            date.setTranslateX(-(rectangle.getWidth() / 2)*0.75 );
                            Text eintraegeanzahl=new Text(String.valueOf("("+filteredList.size() + ") Einträge"));
                            eintraegeanzahl.setTranslateY(-(rectangle.getHeight() / 2) * 0.20);
                            eintraegeanzahl.setStyle("-fx-text-fill: red");
                            stackPane.getChildren().add(date);
                            stackPane.getChildren().add(eintraegeanzahl);
                            // Setzt den Rahmen von Rechtecken, die den heutigen Tag repräsentieren, auf blau.
                            selectColor(rectangle, calculatedDate);
                            // Setzt die Anfangsfarbe für das Rechteck und fügt Hover-Effekte hinzu.
                            addHoverEffect(rectangle, calculatedDate);
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
    }


    private Rectangle creatDateRectangleDesign(){
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        Rectangle rectangle = new Rectangle();
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(strokeWidth);
        double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
        rectangle.setWidth(rectangleWidth);
        double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
        rectangle.setHeight(rectangleHeight);
        return rectangle;
    }
    private void selectColor(Rectangle rectangle,int calculatedDate ){
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == (calculatedDate - dateOffset)) {
            rectangle.setStroke(Color.BLUE);
        }

        // Setzt die Farbe basierend auf dem Monat.
        if (calculatedDate - dateOffset == today.getDayOfMonth() && today.getMonth() == dateFocus.getMonth() && today.getYear() == dateFocus.getYear()) {
            rectangle.setFill(Color.BLUE); // Farbe für den heutigen Tag
        } else {
            rectangle.setFill(Color.LIGHTGREEN); // Farbe für den aktuellen Monat
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
        label.setStyle("-fx-font-family: 'Arial Black'; -fx-text-fill: white");
        label.setAlignment(Pos.TOP_CENTER);
        label.setTranslateY(-10);
        label.setTranslateX(creatDateRectangleDesign().getWidth()/9);
        return label;
    }
    // Add hover effect to the box
    private void addHoverEffect(Rectangle rectangle,int calculatedDate) {
        rectangle.setOnMouseEntered(event -> {
            rectangle.setFill(Color.LIGHTGRAY);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), rectangle);
            scaleTransition.setToX(1.1);
            scaleTransition.setToY(1.1);
            scaleTransition.play();
            rectangle.setCursor(Cursor.HAND);
        });

        rectangle.setOnMouseExited(event -> {

            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), rectangle);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
            selectColor(rectangle, calculatedDate);

        });

    }
    public static void removeNotificationById(EntityManager em, int id) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(em.find(N_Notifications.class, id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

}
