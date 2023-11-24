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
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.List;
import javafx.scene.layout.GridPane;




public class Kalender implements Initializable {

    // FXML-Komponenten, die verschiedene Teile der Benutzeroberfläche repräsentieren
    @FXML
    private AnchorPane KalenderF;
    @FXML
    private VBox rightPanel;
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
    @FXML
    private Pane EigenschaftenPane;
    @FXML
    private BorderPane kleinfenster;
    // Datumvariablen, um das fokussierte Datum und das aktuelle Datum zu verfolgen
    ZonedDateTime dateFocus;
    ZonedDateTime today;

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

    // Zeichnet den Kalender basierend auf dem fokussierten Datum, einschließlich Aktivitäten und Hervorhebung des heutigen Datums
    /**
     * Zeichnet den Kalender für den angegebenen Monat.
     */
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

        // Ruft eine Map mit den Kalenderaktivitäten für den aktuellen Monat ab.
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

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
                rectangle.setOnMouseClicked(event -> Eventerstellen(calculatedDate - dateOffset, dateFocus.getMonthValue(), dateFocus.getYear()));

                // Überprüft, ob das Kästchen im gültigen Bereich des Monats liegt.
                if (calculatedDate <= dateOffset + monthMaxDate) {
                    if (calculatedDate > dateOffset) {

                        // Zeigt den Tag im Kästchen an.
                        Text date = new Text(String.valueOf(calculatedDate - dateOffset));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        // Ruft die Kalenderaktivitäten für diesen Tag ab und erstellt die Anzeige.
                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(calculatedDate - dateOffset);
                        if (calendarActivities != null) {
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }

                        // Setzt den Rahmen von Rechtecken, die den heutigen Tag repräsentieren, auf blau.
                        if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == (calculatedDate - dateOffset)) {
                            rectangle.setStroke(Color.BLUE);
                        }

                        // Setzt die Farbe basierend auf dem Monat.
                        if (calculatedDate - dateOffset == today.getDayOfMonth() && today.getMonth() == dateFocus.getMonth() && today.getYear() == dateFocus.getYear()) {
                            rectangle.setFill(Color.BLUE); // Farbe für den heutigen Tag
                        } else {
                            rectangle.setFill(Color.LIGHTGREEN); // Farbe für den aktuellen Monat
                        }

                        // Setzt die Anfangsfarbe für das Rechteck und fügt Hover-Effekte hinzu.
                        setInitialBoxColor(rectangle, (Color) rectangle.getFill());
                        addHoverEffect(rectangle);
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


    private @NotNull Label TextV(String text){
        Label label= new Label(text);
        label.setStyle("-fx-font-family: 'Arial Black'; -fx-text-fill: white");
        label.setAlignment(Pos.TOP_CENTER);
        label.setTranslateY(10);
        return label;
    }
    private void Eventerstellen(int tag, int Monat, int Jahr) {
        System.out.println("Der zugehoerige Datum ist: " + tag + ", " + Monat + ", " + Jahr);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);

        TextField titleField = new TextField();
        titleField.setId("titlefield");
        titleField.setPromptText("Title...");

        TextArea Beschreibung = new TextArea();
        Beschreibung.setPromptText("Beschreibung...");
        Beschreibung.setId("Beschreibungsfield");
        Beschreibung.setPrefWidth(rightPanel.getPrefWidth()/0.9);

        DatePicker datePicker = new DatePicker();
        LocalDate date= LocalDate.of(Jahr,Monat,tag);
        datePicker.setValue(date);

        Spinner<Integer> hourSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> hourFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        hourSpinner.setValueFactory(hourFactory);

        Spinner<Integer> minuteSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> minuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        minuteSpinner.setValueFactory(minuteFactory);

        VBox vBox = new VBox();
        vBox.getChildren().add(hourSpinner);
        vBox.getChildren().add(minuteSpinner);

        Button button= new Button("Speichern");
        button.setOnMouseEntered(event -> {
            button.setCursor(Cursor.HAND);
        });

        gridPane.add(Textstring("Title: "), 0, 0);
        gridPane.add(Textstring("Datum: "), 0, 1);
        gridPane.add(Textstring("Uhrzeit: "), 0, 2);
        gridPane.add(Textstring("Beschreibung: "), 0, 3);
        gridPane.add(titleField, 1 , 0);
        gridPane.add(datePicker, 1, 1);
        gridPane.add(vBox,1,2);
        gridPane.add(Beschreibung, 1, 3);
        gridPane.add(button, 1, 4);

        EigenschaftenPane.getChildren().add(gridPane);


    }

    private @NotNull Label Textstring(String text){
        Label label= new Label(text);
        label.setStyle("-fx-font-family: Aldhabi");
        label.setFont(Font.font(30));
        return label;
    }
    // Set initial color for the box
    private void setInitialBoxColor(Rectangle rectangle, Color color) {
        rectangle.setOnMouseEntered(event -> {
            rectangle.setFill(Color.LIGHTGRAY);
            drawCalendar();
        });

        rectangle.setOnMouseExited(event -> {
            rectangle.setFill(color);
        });
    }

    // Add hover effect to the box
    private void addHoverEffect(Rectangle rectangle) {
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
            drawCalendar();

        });

    }

    // Erstellt und zeigt Kalenderaktivitäten innerhalb einer Tageszelle an

    /**
     * Erstellt eine visuelle Darstellung von Kalenderaktivitäten in JavaFX.
     *
     *
     * @param calendarActivities Eine Liste von Kalenderaktivitäten.
     * @param rectangleHeight    Die Höhe des Rechtecks für die visuelle Darstellung.
     * @param rectangleWidth     Die Breite des Rechtecks für die visuelle Darstellung.
     * @param stackPane          Der StackPane, auf dem die Darstellung platziert wird.
     *
     */

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        // Erstellt eine vertikale Box für die Anzeige der Kalenderaktivitäten.
        VBox calendarActivityBox = new VBox();

        // Schleife durch jede Kalenderaktivität.
        for (int k = 0; k < calendarActivities.size(); k++) {
            // Bedingung für mehr als zwei Aktivitäten.
            if (k >= 2) {
                // Wenn mehr als zwei Aktivitäten vorhanden sind, zeige "..." an.
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);

                // Füge einen Event-Handler hinzu, um die gesamte Liste anzuzeigen, wenn auf "..." geklickt wird.
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    System.out.println(calendarActivities);
                });

                // Beende die Schleife, da "..." bereits hinzugefügt wurde.
                break;
            }

            // Anzeige der Kalenderaktivität.
            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate().toLocalTime());
            calendarActivityBox.getChildren().add(text);

            // Füge einen Event-Handler hinzu, um den Text der Aktivität anzuzeigen, wenn darauf geklickt wird.
            text.setOnMouseClicked(mouseEvent -> {
                System.out.println(text.getText());
            });
        }

        // Setze verschiedene Eigenschaften der VBox.
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color:GRAY");

        // Füge die VBox zur StackPane hinzu.
        stackPane.getChildren().add(calendarActivityBox);
    }

    // Erstellt eine Map von Kalenderaktivitäten, gruppiert nach Tag
    /**
     * Erstellt eine Map, die Kalenderaktivitäten nach Tag gruppiert.
     *
     * @param calendarActivities Eine Liste von Kalenderaktivitäten.
     * Eine Map, die Kalenderaktivitäten nach Tag gruppiert.
     */
    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        // Erstellt eine leere HashMap, um Kalenderaktivitäten nach Tag zu gruppieren.
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        // Schleife durch jede Kalenderaktivität.
        for (CalendarActivity activity : calendarActivities) {
            // Extrahiere den Tag der Aktivität.
            int activityDate = activity.getDate().getDayOfMonth();

            // Überprüfe, ob die Map bereits einen Eintrag für diesen Tag hat.
            if (!calendarActivityMap.containsKey(activityDate)) {
                // Wenn nicht, füge eine neue Liste mit der Aktivität für diesen Tag hinzu.
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                // Wenn bereits Aktivitäten für diesen Tag vorhanden sind, aktualisiere die Liste.
                List<CalendarActivity> oldListByDate = calendarActivityMap.get(activityDate);

                // Erstelle eine neue Liste, die die vorhandenen Aktivitäten enthält.
                List<CalendarActivity> newList = new ArrayList<>(oldListByDate);

                // Füge die neue Aktivität zur Liste hinzu.
                newList.add(activity);

                // Aktualisiere die Map mit der aktualisierten Liste für diesen Tag.
                calendarActivityMap.put(activityDate, newList);
            }
        }

        // Gib die erstellte Map zurück.
        return calendarActivityMap;
    }

    // Generiert zufällige Kalenderaktivitäten für den gegebenen Monat und das Jahr
    /**
    * @param dateFocus
    */

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();
        int year = dateFocus.getYear();
        int month = dateFocus.getMonth().getValue();
        // Hier wird random in jeden Monat einen Termin für Hans um 16 Uhr angelegt
//        Random random = new Random();
//        for (int i = 0; i < 50; i++) {
//            ZonedDateTime time = ZonedDateTime.of(year, month, random.nextInt(27)+1, 16,0,0,0,dateFocus.getZone());
//            calendarActivities.add(new CalendarActivity(time, "Hans", 111111));
//        }

        return createCalendarMap(calendarActivities);
    }
    @FXML
    public void animationEnter(MouseEvent mouseEvent) {
        try {
            Object source = mouseEvent.getSource();
            if (source instanceof Button) {
                Button button = (Button) source;
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
                scaleTransition.setToX(1.1);
                scaleTransition.setToY(1.1);
                scaleTransition.playFromStart();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        };
    }
    @FXML
    public void animationExit(MouseEvent mouseEvent) {

        try {
            Object source = (Button) mouseEvent.getSource();
            if (source instanceof Button) {
                Button button = (Button) source;
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
                scaleTransition.setToX(1.0);
                scaleTransition.setToY(1.0);
                scaleTransition.playFromStart();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        };


    }

}
