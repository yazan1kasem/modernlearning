package modern.learning.modernlearning;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class Starter3 extends Application {
    private LocalDate selectedDate = LocalDate.now();

    @Override
    public void start(Stage primaryStage) {
        GridPane calendarPane = new GridPane();
        calendarPane.setAlignment(Pos.CENTER);
        calendarPane.setHgap(5);
        calendarPane.setVgap(5);

        // Holen Sie sich das aktuelle Datum und Jahr
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        // Holen Sie sich den aktuellen Monat und den ersten Tag dieses Monats
        int currentMonthValue = currentDate.getMonthValue();
        LocalDate firstDayOfMonth = LocalDate.of(currentYear, currentMonthValue, 1);

        // Ermitteln Sie den Wochentag des ersten Tags des Monats (Montag = 1, Sonntag = 7)
        int firstDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();

        // Tage des Monats im Grid anzeigen
        int day = 1;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                // Tage vor dem ersten Tag des Monats als leere Labels anzeigen
                if (row == 0 && col < firstDayOfWeek - 1) {
                    calendarPane.add(new Label(), col, row);
                } else if (day <= firstDayOfMonth.lengthOfMonth()) {
                    // Tage des Monats anzeigen
                    Rectangle dayBox = new Rectangle(50, 50);
                    dayBox.setFill(day == LocalDate.now().getDayOfMonth() && selectedDate.getMonthValue() == currentMonthValue ? Color.YELLOW : Color.WHITE); // Heutiges Datum gelb hervorheben
                    dayBox.setOnMouseClicked(event -> {
                        selectedDate = LocalDate.of(currentYear, currentMonthValue, Integer.parseInt(((Label) event.getSource()).getText()));
                        updateCalendar(calendarPane, currentYear, currentMonthValue);
                    });
                    Label dayLabel = new Label(String.valueOf(day));
                    dayLabel.setAlignment(Pos.CENTER);
                    calendarPane.add(dayBox, col, row);
                    calendarPane.add(dayLabel, col, row);
                    day++;
                }
            }
        }

        Scene scene = new Scene(calendarPane, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Kalender");
        primaryStage.show();
    }

    // Aktualisiere den Kalender, um Änderungen am ausgewählten Datum anzuzeigen
    private void updateCalendar(GridPane calendarPane, int currentYear, int currentMonthValue) {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                int day = row * 7 + col + 1 - LocalDate.of(currentYear, currentMonthValue, 1).getDayOfWeek().getValue();
                Label dayLabel = (Label) calendarPane.getChildren().get(row * 7 + col);
                dayLabel.setText(day > 0 && day <= LocalDate.of(currentYear, currentMonthValue, 1).lengthOfMonth() ? String.valueOf(day) : "");
                dayLabel.setTextFill(day == selectedDate.getDayOfMonth() && selectedDate.getMonthValue() == currentMonthValue ? Color.YELLOW : Color.BLACK);
                // Hier können weitere Bedingungen für die Farbänderung hinzugefügt werden
            }
        }
    }
}
