package modern.learning.modernlearning.CalenderClasses;

import modern.learning.modernlearning.KalenderController;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;

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


        while(isRunning){
            try {
                // Simulate some work
                if(LocalDateTime.now().equals(BenachrichtigungsZeit) || LocalDateTime.now().isAfter(BenachrichtigungsZeit)){
                    try {
                        KalenderController.removeNotificationById(em, Notificationsid);
                    } finally {
                        em.close();
                    }
                    // Aktuelle Zeit
                    LocalDateTime jetzt = LocalDateTime.now();

                    // Berechnung der Zeitverspätung
                    Duration zeitverspaetung = Duration.between(BenachrichtigungsZeit,jetzt);

                    // Formatierung des Strings
                    String formattedString = String.format("Benachrichtigung vor: %s", formatiereDauer(zeitverspaetung));
                    showNotification(BenachrichtigungsTitle==null?BenachrichtigungsTitle: "<Termin ohne Title>",  formattedString );
                    isRunning=false;
                    EntityManager em = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

                    System.out.println("hallo");
                }
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
    public static void showNotification(String title, String message) {
        try {
            // Create a SystemTray object
            SystemTray tray = SystemTray.getSystemTray();
            // Load an image for the TrayIcon
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("file:src/main/Media/SkillBuildersLogo.png");

            // Create a TrayIcon
            TrayIcon trayIcon = new TrayIcon(image, "Tray Icon");



            // Add the TrayIcon to the SystemTray
            tray.add(trayIcon);

            // Display the notification
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
            trayIcon.setToolTip("NotificationAkhie");
            // Remove the TrayIcon after a few seconds (optional)
            Thread.sleep(5000); // Wait for 5 seconds
            tray.remove(trayIcon);

        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
