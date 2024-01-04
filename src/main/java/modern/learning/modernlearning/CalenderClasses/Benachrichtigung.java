package modern.learning.modernlearning.CalenderClasses;

import entities.N_Notifications;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modern.learning.modernlearning.KalenderController;
import modern.learning.modernlearning.Starter;
import org.controlsfx.control.Notifications;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;


public class Benachrichtigung implements Runnable{
    private volatile boolean isRunning = true;
    private final EntityManager em = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();
    private String BenachrichtigungsTitle;
    private LocalDateTime BenachrichtigungsZeit;
    private int Notificationsid;

    public Benachrichtigung() {
    }

    public Benachrichtigung(String benachrichtigungsTitle, LocalDateTime benachrichtigungsZeit, int notificationsid) {
        BenachrichtigungsTitle = benachrichtigungsTitle;
        BenachrichtigungsZeit = benachrichtigungsZeit;
        Notificationsid = notificationsid;

        new Thread(this).start();

    }

    @Override
    public void run() {

        while (isRunning) {
            try {
                // Simulate some work
                LocalDateTime jetzt = LocalDateTime.now();

                if (jetzt.isEqual(BenachrichtigungsZeit) || jetzt.isAfter(BenachrichtigungsZeit)) {

                    // EntityManager öffnen
                    try {
                        EntityTransaction transaction = em.getTransaction();
                        try {
                            if(!transaction.isActive()){
                                transaction.begin();
                            }
                            // Benachrichtigung löschen
                            N_Notifications notification = em.find(N_Notifications.class,Notificationsid);
                            if (notification != null) {
                                LOGGER.tracef("Deleting...");
                                em.remove(notification);
                                transaction.commit();
                                LOGGER.tracef("Deleted");
                                isRunning = false;
                                em.close();
                                // Berechnung der Zeitverspätung
                                Duration zeitverspaetung = Duration.between(BenachrichtigungsZeit, jetzt);

                                // Formatierung des Strings
                                String formattedString = String.format("Benachrichtigung vor: %s", formatiereDauer(zeitverspaetung));

                                // Benachrichtigung anzeigen
                                showNotification(BenachrichtigungsTitle == null ? "<Termin ohne Title>" : BenachrichtigungsTitle, formattedString);

                            } else {
                                System.out.println("1.Notification not found for ID: " + Notificationsid);
                                isRunning = true;
                            }

                        } catch (EntityNotFoundException e) {
                            // Benachrichtigung nicht gefunden
                            if (transaction.isActive()) {
                                transaction.rollback();
                            }
                            System.out.println("2.Notification not found for ID: " + Notificationsid);
                        } catch (Exception e) {
                            // Andere Ausnahmen behandeln
                            if (transaction.isActive()) {
                                transaction.rollback();
                            }
                            e.printStackTrace();
                        }
                    } catch (Exception e) {

                    }

                    // Beenden des Threads nach Abschluss der Arbeit
                }

                // Überprüfen, ob der Hauptbildschirm geschlossen wurde
                if (hauptbildschirmGeschlossen()) {
                    // Programm schließen
                    System.exit(0);
                }

                // Warten für den nächsten Durchlauf
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean hauptbildschirmGeschlossen() {
        // Hier prüfen, ob der Hauptbildschirm geschlossen wurde
        // Rückgabe entsprechend anpassen
       return Starter.geschlossen;
    }
    private static String formatiereDauer(Duration duration) {
        long days=duration.toDaysPart();
        long stunden = duration.toHours();
        long minuten = duration.toMinutesPart();
        long sekunden = duration.toSecondsPart();
        if(duration.toHours()>23){
            stunden = duration.toHours()-(days*24);
        }
        return String.format("%d Tage, %d Stunden, %d Minuten, %d Sekunden",days, stunden, minuten, sekunden);
    }
    public void showNotification(String title, String message) {
        try {
            // Create a SystemTray object
            SystemTray tray = SystemTray.getSystemTray();


            // Create a TrayIcon
            TrayIcon trayIcon = new TrayIcon(new BufferedImage(2,2,2), "Tray icon");

            // ActionListener für das TrayIcon hinzufügen
            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // Öffne Google im Standardbrowser
                        Desktop.getDesktop().browse(new URI("https://www.google.com"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Füge das TrayIcon zum SystemTray hinzu
            tray.add(trayIcon);

            // Zeige die Benachrichtigung an
            trayIcon.displayMessage("title", "message", TrayIcon.MessageType.NONE);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void navigateToFxmlPage() {
//        Platform.runLater(() -> {
//            try {
//                // Load FXML page
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("Kalender.fxml"));
//                Pane root = loader.load();
//
//                // Create a new scene
//                Scene scene = new Scene(root);
//
//                // Get the controller
//                KalenderController controller=loader.getController();
//                // Perform any setup or data initialization in the controller if needed
//
//                // Show the new scene
//                Stage stage = new Stage();
//                stage.setScene(scene);
//                stage.show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }

}
