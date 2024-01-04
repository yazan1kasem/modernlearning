package modern.learning.modernlearning;

import javafx.application.Application;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Starter3 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Calendar calendar = Calendar.getInstance();

        // Aktuelles Datum und Zeit anzeigen
        System.out.println("Aktuelles Datum und Zeit: " + calendar.getTime());

        // Monat im Format MMMM (zum Beispiel: Januar)
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
        String month = monthFormat.format(calendar.getTime());
        System.out.println("Aktueller Monat: " + month);

        // Jahr anzeigen
        int year = calendar.get(Calendar.YEAR);
        System.out.println("Aktuelles Jahr: " + year);

        // Kalender für den aktuellen Monat einstellen
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // Wochentage anzeigen
        System.out.println("Mo Di Mi Do Fr Sa So");

        // Erste Tagposition im Monat bestimmen
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // Leerzeichen für die erste Tagposition einfügen
        for (int i = 1; i < firstDayOfWeek; i++) {
            System.out.print("   ");
        }

        // Tage des Monats anzeigen
        while (calendar.get(Calendar.MONTH) == monthFormat.parse(month).getMonth()) {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            System.out.printf("%2d ", day);

            // Nächster Tag
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            // Neue Zeile für den Anfang einer neuen Woche
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
