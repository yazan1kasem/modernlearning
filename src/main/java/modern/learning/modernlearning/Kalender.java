/*
 * Die Klasse Kalender ist ein JavaFX-Controller für eine Kalenderanwendung.
 * Sie verwendet JavaFX für die Benutzeroberflächenkomponenten und ZonedDateTime zur
 * Verarbeitung von Datum und Uhrzeit. Der Kalender wird in einem FlowPane angezeigt,
 * und jeder Tag kann Aktivitäten enthalten, die als Rechtecke im Kalender dargestellt werden.
 * Durch Klicken auf einen Tag mit mehreren Aktivitäten wird eine Zusammenfassung angezeigt,
 * und durch Klicken auf einzelne Aktivitäten erhalten Sie weitere Details.
 */

package modern.learning.modernlearning;


import entities.K_Kalender;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;
import javafx.scene.layout.GridPane;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.xml.stream.Location;


public class Kalender implements Initializable {

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

    // Initialisiert den Controller und zeichnet den anfänglichen Kalender
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), calendarPanel);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
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

    private void drawCalendar() {
        // Setzt das Jahr und den Monat im UI entsprechend dem Fokusdatum.
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        // Ermittelt die Größe und Abstände des Kalenders.
        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();


        // Ermittelt die maximale Anzahl der Tage im Monat und den Wochentag des ersten Tages im Monat.
        int monthMaxDate = dateFocus.getMonth().maxLength();
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        // Löscht vorhandene Kinder, bevor der Kalender neu gezeichnet wird.
        calendar.getChildren().clear();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);


        // Add the day labels to the calendar
        gridPane.add(TextV("Sonntag"), 0, 0);
        gridPane.add(TextV("Montag"), 1, 0);
        gridPane.add(TextV("Dienstag"), 2, 0);
        gridPane.add(TextV("Mittwoch"), 3, 0);
        gridPane.add(TextV("Donnerstag"), 4, 0);
        gridPane.add(TextV("Freitag"), 5, 0);
        gridPane.add(TextV("Samstag"), 6, 0);

        int tage=0;
        // Schleife für die Zeilen im Kalender.
        for (int i = 0; i < 6; i++) {
            // Schleife für die Tage in jeder Zeile.
            for (int j = 0; j < 7; j++) {
                tage++;
                if(tage== monthMaxDate+dateOffset+1){

                    break;
                }
                StackPane stackPane= new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);

                // Fügt das Rechteck zur StackPane hinzu.
                stackPane.getChildren().add(rectangle);


                // Berechnet den Tag im Monat für das aktuelle Kästchen im Kalender.
                int calculatedDate = (j + 1) + (7 * i);

                try {
                    rectangle.setOnMouseClicked(event -> {
                        String formattedDate = String.format(
                                "%02d.%02d.%d",
                                calculatedDate - dateOffset,
                                dateFocus.getMonthValue(),
                                dateFocus.getYear()
                        );

                        showCalendarPopover(rectangle, event, formattedDate);
                    });
                }catch (Exception e ){
                    System.out.println(e.getMessage());
                }
                // Überprüft, ob das Kästchen im gültigen Bereich des Monats liegt.
                if (calculatedDate <= dateOffset + monthMaxDate) {
                    if (calculatedDate > dateOffset) {

                        // Zeigt den Tag im Kästchen an.
                        Text date = new Text(String.valueOf(calculatedDate - dateOffset));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);



                        // Setzt den Rahmen von Rechtecken, die den heutigen Tag repräsentieren, auf blau.
                        selectColor(rectangle, calculatedDate);

                        // Setzt die Anfangsfarbe für das Rechteck und fügt Hover-Effekte hinzu.
                        addHoverEffect(rectangle, calculatedDate);
                    }

                }
                gridPane.add(stackPane, j,i+1);
            }
            if(tage== monthMaxDate+dateOffset+1){
                tage=0;
                break;
            }

        }
        // Fügt die StackPane zum Kalender hinzu.
        calendar.getChildren().add(gridPane);
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
    private void showCalendarPopover(Rectangle ownerRectangle, MouseEvent event, String datum) {
        System.out.println("Test 1 true");
        PopOver popover = new PopOver();
        popover.setTitle("Termin am: " + datum);

        VBox popoverLayout = new VBox(10);
        popoverLayout.setPadding(new Insets(10));
        int mouseX = MouseInfo.getPointerInfo().getLocation().x;
        int mouseY = MouseInfo.getPointerInfo().getLocation().y;
// Set the arrow location of the popover at the mouse pointer location
      // Date and Time Picker
        DateTimePicker dateTimePicker = new DateTimePicker();

        // TextFields for Title and Description
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextArea beschreibung = new TextArea();
        beschreibung.setPromptText("Description");

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event1 -> {
            EntityManager em = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();
            try  {
                em.getTransaction().begin();
                K_Kalender kalender = new K_Kalender();
                kalender.setK_Title(titleField.getText());
                kalender.setK_Beschreibung(beschreibung.getText());
                kalender.setK_vonZeitMinute(dateTimePicker.getVonminuteComboBox().getValue());
                kalender.setK_vonZeitStunde(dateTimePicker.getVonhourComboBox().getValue());
                kalender.setK_bisZeitMinute(dateTimePicker.getBisminuteComboBox().getValue());
                kalender.setK_bisZeitStunde(dateTimePicker.getBishourComboBox().getValue());
                em.persist(kalender);
                em.getTransaction().commit();
                popover.hide();

            } catch (Exception e) {
                // Handle exceptions, log, or throw them as needed
                if (em.getTransaction()!= null && em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                e.printStackTrace();
            }finally {
                em.close();
            }

        });
        Button Termine = new Button("show more");
        Termine.setStyle("-fx-text-fill: blue; -fx-underline: true");
        popoverLayout.getChildren().addAll(dateTimePicker, titleField, beschreibung, saveButton, Termine);


        popover.setContentNode(popoverLayout);
        popover.show(ownerRectangle);
        System.out.println("Test 2 true");
    }

    private @NotNull Label TextV(String text){
        Label label= new Label(text);
        label.setStyle("-fx-font-family: 'Arial Black'; -fx-text-fill: white");
        label.setAlignment(Pos.TOP_CENTER);
        label.setTranslateY(10);
        return label;
    }

    private @NotNull Label Textstring(String text){
        Label label= new Label(text);
        label.setStyle("-fx-font-family: Aldhabi");
        label.setFont(Font.font(30));
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
}
