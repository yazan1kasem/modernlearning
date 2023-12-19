/*
 * Die Klasse Kalender ist ein JavaFX-Controller für eine Kalenderanwendung.
 * Sie verwendet JavaFX für die Benutzeroberflächenkomponenten und ZonedDateTime zur
 * Verarbeitung von Datum und Uhrzeit. Der Kalender wird in einem FlowPane angezeigt,
 * und jeder Tag kann Aktivitäten enthalten, die als Rechtecke im Kalender dargestellt werden.
 * Durch Klicken auf einen Tag mit mehreren Aktivitäten wird eine Zusammenfassung angezeigt,
 * und durch Klicken auf einzelne Aktivitäten erhalten Sie weitere Details.
 */

package modern.learning.modernlearning;


import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
import modern.learning.modernlearning.CalenderClasses.KalenderPopover;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

import javafx.scene.layout.GridPane;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;


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
                        int jahr= dateFocus.getYear();
                        int monat=dateFocus.getMonthValue();

                        showCalendarPopover(rectangle, event, LocalDate.of(jahr,monat,calculatedDate - dateOffset));
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
    private Rectangle oldRectangle;
    private final EntityManager em = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

    private KalenderPopover kalenderPopover; // Declare a class-level variable

    private void showCalendarPopover(Rectangle ownerRectangle, MouseEvent event, LocalDate datum) {
        System.out.println("\u001B[32m" + "Test 1 succeeded (PopOver gestartet)" + "\u001B[0m");

        if (kalenderPopover == null) {
            // No existing instance, create a new one
            kalenderPopover = new KalenderPopover(ownerRectangle, datum);
        } else {
            // An instance already exists, you can choose to hide it or update its content
            kalenderPopover.hide(); // Example: hide the existing popover
            kalenderPopover = new KalenderPopover(ownerRectangle, datum);
            // You can also update the content if needed
            // kalenderPopover.updateContent(ownerRectangle, datum);
        }

        oldRectangle = ownerRectangle;
    }


    private @NotNull Label TextV(String text){
        Label label= new Label(text);
        label.setStyle("-fx-font-family: 'Arial Black'; -fx-text-fill: white");
        label.setAlignment(Pos.TOP_CENTER);
        label.setTranslateY(10);
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
//    private PopOver TerminePopover(PopOver popover, LocalDate datum, Rectangle ownerRectangle){
//        try {
//            VBox popoverLayout = new VBox(10);
//            popoverLayout.setPadding(new Insets(10));
//            List < K_Kalender > kalenders = em.createQuery(
//                            "SELECT k FROM K_Kalender k WHERE :datum > k.K_vonDatum AND :datum < k.K_bisDatum or :datum = k.K_bisDatum or :datum = k.K_vonDatum",
//                            K_Kalender.class)
//                    .setParameter("datum", LocalDateTime.of(datum, LocalTime.of(0, 0)))
//                    .getResultList();
//            VBox v = new VBox();
//            v.setSpacing(10);
//            if (!kalenders.isEmpty()) {
//                for (int i = 0; i < kalenders.size(); i++) {
//                    kalenders.get(i);
//                    VBox Terminebox = new VBox();
//                    Terminebox.setSpacing(10);
//                    Terminebox.setStyle("-fx-border-radius: 20; -fx-alignment: center; -fx-border-color: black");
//                    Terminebox.setPrefWidth(200);
//                    VBox.setMargin(popoverLayout, new Insets(0, 10, 0, 10));
//                    int finalI = i;
//                    Terminebox.setOnMouseClicked(UpdateEvent -> {
//                        //Hier sollte kommen das man es ändern kann
//                        //also ruf den Popover der dafür zuständig ist das findest du unter bei wenn man auf neu drückt
//                        UpdatePopover(popover,datum,ownerRectangle,kalenders.get(finalI));
//                    });
//                    Terminebox.setOnMouseEntered(transischenevent1 -> {
//                        addButtonToDelete(Terminebox, kalenders.get(finalI), popover, datum, ownerRectangle);
//                        applyScaleTransition(Terminebox, 1.05);
//                        Terminebox.setCursor(Cursor.HAND);
//                    });
//
//                    Terminebox.setOnMouseExited(transischenevent2 -> {
//                        removeButtonToDelete(Terminebox);
//                        applyScaleTransition(Terminebox, 1.0);
//                    });
//                    StackPane stackPane = new StackPane();
//                    Terminebox.getChildren().add(new Label(kalenders.get(i).getK_Title() != null ?   kalenders.get(i).getK_Title():"<KEIN TITLE>"));
//                    Terminebox.getChildren().add(new Label(kalenders.get(i).getK_vonDatum().getDayOfMonth() + "." + kalenders.get(i).getK_vonDatum().getMonthValue() + "." + kalenders.get(i).getK_vonDatum().getYear() + " um: " + kalenders.get(i).getK_vonDatum().getHour() + ":" + kalenders.get(i).getK_vonDatum().getMinute()));
//                    Terminebox.getChildren().add(new Label(kalenders.get(i).getK_bisDatum().getDayOfMonth() + "." + kalenders.get(i).getK_bisDatum().getMonthValue() + "." + kalenders.get(i).getK_bisDatum().getYear() + " um: " + kalenders.get(i).getK_bisDatum().getHour() + ":" + kalenders.get(i).getK_bisDatum().getMinute()));
//                    stackPane.getChildren().add(Terminebox);
//                    v.getChildren().add(stackPane);
//                }
//            }
//            Button b = new Button();
//            b.setText("neu");
//
//            v.getChildren().add(b);
//
//            popoverLayout.getChildren().clear();
//            popoverLayout.getChildren().addAll(v, b);
//            popover.setContentNode(popoverLayout);
//            popover.show(ownerRectangle);
//            b.setOnMouseClicked(Insertevent -> {
//                InsertPopover(popover,datum,ownerRectangle);
//            });
//            System.out.println("\u001B[32m" + "Test 2 succeeded (PopOver erstellt und gezeigt)" + "\u001B[0m");
//        } catch (Exception e) {
//            System.out.println("\u001B[31m" + "Test 1.5 failed (PopOver könnte nicht erstellt und gezeigt werden)" + "\u001B[0m");
//        }
//
//
//        return popover;
//    }
//    private PopOver UpdatePopover(PopOver popover, LocalDate datum, Rectangle ownerRectangle, K_Kalender kalender){
//        VBox popoverLayout = new VBox(10);
//        popoverLayout.setPadding(new Insets(10));
//        // Date and Time Picker
//        DateTimePicker dateTimePicker = new DateTimePicker();
//        dateTimePicker.setKastendatum(LocalDateTime.of(datum, LocalTime.of(8, 0)));
//        dateTimePicker.setinhalt();
//        // TextFields for Title and Description
//        TextField titleField = new TextField();
//        titleField.setPromptText("Title");
//
//        TextArea beschreibung = new TextArea();
//        beschreibung.setPromptText("Description");
//
//        // Save Button
//        Button saveButton = new Button("Save");
//        saveButton.setOnAction(UpdateEvent -> {
//            try {
//                dateTimePicker.getVondatepicker().setStyle("-fx-border-color: none;");
//                em.getTransaction().begin();
//                kalender.setK_Title(titleField.getText());
//                kalender.setK_Beschreibung(beschreibung.getText());
//
//                kalender.setK_vonDatum(
//                        LocalDateTime.of(dateTimePicker.getVondatepicker().getValue().getYear(),
//                                dateTimePicker.getVondatepicker().getValue().getMonth().getValue(),
//                                dateTimePicker.getVondatepicker().getValue().getDayOfMonth(),
//                                dateTimePicker.getVonhourComboBox().getValue(),
//                                dateTimePicker.getVonminuteComboBox().getValue()));
//                kalender.setK_bisDatum(
//                        LocalDateTime.of(dateTimePicker.getBisdatepicker().getValue().getYear(),
//                                dateTimePicker.getBisdatepicker().getValue().getMonth().getValue(),
//                                dateTimePicker.getBisdatepicker().getValue().getDayOfMonth(),
//                                dateTimePicker.getBishourComboBox().getValue(),
//                                dateTimePicker.getBisminuteComboBox().getValue())
//                );
//
//                if (kalender.getK_vonDatum().isBefore(kalender.getK_bisDatum())) {
//                    if (titleField.getText() != null) {
//                        em.persist(kalender);
//                        em.getTransaction().commit();
//                        popover.hide();
//                    } else {
//                        Alert alert = new Alert(Alert.AlertType.ERROR, "Die Eingabe wurde als fehlerhaft erkannt, da Ihr Termin keinen Title hat.");
//                        alert.setTitle("ungültiger Eintrag");
//                        alert.setHeaderText("Bitte tragen Sie einen gültigen Title ein");
//                        alert.setResizable(true);
//                        alert.show();
//                        titleField.setStyle("-fx-border-color: red;");
//                    }
//                } else {
//                    if (dateTimePicker.getVondatepicker().getValue().isBefore(dateTimePicker.getBisdatepicker().getValue())) {
//                        dateTimePicker.getVondatepicker().setStyle("-fx-border-color: red;");
//                    } else if (dateTimePicker.getVonhourComboBox().getValue() < dateTimePicker.getBishourComboBox().getValue()) {
//                        dateTimePicker.getVonhourComboBox().setStyle("-fx-border-color: red;");
//                    } else if (dateTimePicker.getVonminuteComboBox().getValue() < dateTimePicker.getBisminuteComboBox().getValue()) {
//                        dateTimePicker.getVonminuteComboBox().setStyle("-fx-border-color: red;");
//                    }
//
//                    Alert alert = new Alert(Alert.AlertType.ERROR, "Die Eingabe wurde als fehlerhaft erkannt, da das Anfangsdatum des Ereignisses später im Zeitverlauf liegt als das Enddatum.");
//                    alert.setTitle("ungültiger Datum");
//                    alert.setHeaderText("Bitte tragen Sie einen gültigen Termin ein");
//                    alert.setResizable(true);
//
//                    alert.show();
//                }
//
//            } catch (Exception e) {
//                // Handle exceptions, log, or throw them as needed
//                if (em.getTransaction() != null && em.getTransaction().isActive()) {
//                    em.getTransaction().rollback();
//                }
//                e.printStackTrace();
//            }
//
//        });
//        Button Termine = new Button("show more");
//        Termine.setStyle("-fx-text-fill: blue; -fx-underline: true");
//        popoverLayout.getChildren().clear();
//        popoverLayout.getChildren().addAll(dateTimePicker, titleField, beschreibung, saveButton, Termine);
//        System.out.println("\u001B[32m" + "Test 4 succeeded (Insert Popover)" + "\u001B[0m");
//        oldRectangle=null;
//        popover.setContentNode(popoverLayout);
//        popover.show(ownerRectangle);
//        return popover;
//    }
//    private void configureArrowLocation(PopOver popover, int mouseX, int mouseY) {
//        if (Toolkit.getDefaultToolkit().getScreenSize().width / 2 > mouseX) {
//            if (Toolkit.getDefaultToolkit().getScreenSize().height / 2 > mouseY) {
//                popover.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
//            } else if (Toolkit.getDefaultToolkit().getScreenSize().height / 2 < mouseY) {
//                popover.setArrowLocation(PopOver.ArrowLocation.LEFT_BOTTOM);
//            }
//        } else if (Toolkit.getDefaultToolkit().getScreenSize().width / 2 < mouseX) {
//            if (Toolkit.getDefaultToolkit().getScreenSize().height / 2 > mouseY) {
//                popover.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
//            } else if (Toolkit.getDefaultToolkit().getScreenSize().height / 2 < mouseY) {
//                popover.setArrowLocation(PopOver.ArrowLocation.RIGHT_BOTTOM);
//            }
//        }
//    }
//    private PopOver InsertPopover(PopOver popover, LocalDate datum, Rectangle ownerRectangle){
//        VBox popoverLayout = new VBox(10);
//        popoverLayout.setPadding(new Insets(10));
//        // Date and Time Picker
//        DateTimePicker dateTimePicker = new DateTimePicker();
//        dateTimePicker.setKastendatum(LocalDateTime.of(datum, LocalTime.of(8, 0)));
//        dateTimePicker.setinhalt();
//        // TextFields for Title and Description
//        TextField titleField = new TextField();
//        titleField.setPromptText("Title");
//
//        TextArea beschreibung = new TextArea();
//        beschreibung.setPromptText("Description");
//
//        // Save Button
//        Button saveButton = new Button("Save");
//        saveButton.setOnAction(event2 -> {
//            try {
//                dateTimePicker.getVondatepicker().setStyle("-fx-border-color: none;");
//                em.getTransaction().begin();
//                K_Kalender kalender = new K_Kalender();
//                kalender.setK_Title(titleField.getText());
//                kalender.setK_Beschreibung(beschreibung.getText());
//
//                kalender.setK_vonDatum(
//                        LocalDateTime.of(dateTimePicker.getVondatepicker().getValue().getYear(),
//                                dateTimePicker.getVondatepicker().getValue().getMonth().getValue(),
//                                dateTimePicker.getVondatepicker().getValue().getDayOfMonth(),
//                                dateTimePicker.getVonhourComboBox().getValue(),
//                                dateTimePicker.getVonminuteComboBox().getValue()));
//                kalender.setK_bisDatum(
//                        LocalDateTime.of(dateTimePicker.getBisdatepicker().getValue().getYear(),
//                                dateTimePicker.getBisdatepicker().getValue().getMonth().getValue(),
//                                dateTimePicker.getBisdatepicker().getValue().getDayOfMonth(),
//                                dateTimePicker.getBishourComboBox().getValue(),
//                                dateTimePicker.getBisminuteComboBox().getValue())
//                );
//
//                if (kalender.getK_vonDatum().isBefore(kalender.getK_bisDatum())) {
//                    if(titleField.getText()!=null){
//                        em.persist(kalender);
//                        em.getTransaction().commit();
//                        popover.hide();
//                    }else{
//                        Alert alert = new Alert(Alert.AlertType.ERROR, "Die Eingabe wurde als fehlerhaft erkannt, da Ihr Termin keinen Title hat.");
//                        alert.setTitle("ungültiger Eintrag");
//                        alert.setHeaderText("Bitte tragen Sie einen gültigen Title ein");
//                        alert.setResizable(true);
//                        alert.show();
//                        titleField.setStyle("-fx-border-color: red;");
//                    }
//                } else {
//                    if (dateTimePicker.getVondatepicker().getValue().isBefore(dateTimePicker.getBisdatepicker().getValue())) {
//                        dateTimePicker.getVondatepicker().setStyle("-fx-border-color: red;");
//                    } else if (dateTimePicker.getVonhourComboBox().getValue() < dateTimePicker.getBishourComboBox().getValue()) {
//                        dateTimePicker.getVonhourComboBox().setStyle("-fx-border-color: red;");
//                    } else if (dateTimePicker.getVonminuteComboBox().getValue() < dateTimePicker.getBisminuteComboBox().getValue()) {
//                        dateTimePicker.getVonminuteComboBox().setStyle("-fx-border-color: red;");
//                    }
//
//                    Alert alert = new Alert(Alert.AlertType.ERROR, "Die Eingabe wurde als fehlerhaft erkannt, da das Anfangsdatum des Ereignisses später im Zeitverlauf liegt als das Enddatum.");
//                    alert.setTitle("ungültiger Datum");
//                    alert.setHeaderText("Bitte tragen Sie einen gültigen Termin ein");
//                    alert.setResizable(true);
//
//                    alert.show();
//                }
//
//            } catch (Exception e) {
//                // Handle exceptions, log, or throw them as needed
//                if (em.getTransaction() != null && em.getTransaction().isActive()) {
//                    em.getTransaction().rollback();
//                }
//                e.printStackTrace();
//            }
//        });
//        Button Termine = new Button("show more");
//        Termine.setStyle("-fx-text-fill: blue; -fx-underline: true");
//        popoverLayout.getChildren().clear();
//        popoverLayout.getChildren().addAll(dateTimePicker, titleField, beschreibung, saveButton, Termine);
//        System.out.println("\u001B[32m" + "Test 4 succeeded (Insert Popover)" + "\u001B[0m");
//
//        popover.setContentNode(popoverLayout);
//        popover.show(ownerRectangle);
//        return popover;
//    }
//    private void addButtonToDelete(VBox terminebox, K_Kalender k,PopOver popover, LocalDate datum, Rectangle ownerRectangle) {
//        Button delete = new Button("Delete");
//        delete.setOnMouseClicked(deleteEvent -> {
//            // Handle delete event
//            //lösch den Termin der hier verwendet wird
//            em.getTransaction().begin();
//            em.remove(k);
//            em.getTransaction().commit();
//            TerminePopover(popover, datum, ownerRectangle);
//        });
//        terminebox.getChildren().add(delete);
//    }
//
//    private void removeButtonToDelete(VBox terminebox) {
//        terminebox.getChildren().removeIf(node -> node instanceof Button);
//    }
//
//    private void applyScaleTransition(VBox terminebox, double scaleValue) {
//        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), terminebox);
//        scaleTransition.setToX(scaleValue);
//        scaleTransition.setToY(scaleValue);
//        scaleTransition.play();
//    }