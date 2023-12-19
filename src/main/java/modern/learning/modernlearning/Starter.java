package modern.learning.modernlearning;

import entities.K_Kalender;
import entities.N_Notifications;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EventListener;
import java.util.List;

public class Starter extends Application implements Runnable {
    private volatile boolean isRunning = true;
    private final EntityManager em = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

    @Override
    public void start(Stage stage) throws IOException {
        em.getTransaction().begin();
        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Kalender.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Modern learning");
        stage.getIcons().add(new Image("file:src/main/Media/SkillBuildersLogo.png"));
        stage.setMinHeight(640);
        stage.setMinWidth(1000);
        stage.setWidth(scene.getWidth());

        stage.setScene(scene);

        stage.show();
        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("h: " + newValue);
            }
        });
        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("w: " + newValue);
            }
        });
        stage.setOnCloseRequest(event -> {
            // Set the flag to stop the thread
            isRunning = false;
        });
        // Start the thread
    }

    @Override
    public void run() {
        List<N_Notifications> notificationsList = em.createQuery(
                        "SELECT n FROM N_Notifications n", N_Notifications.class)
                .getResultList();

        while (isRunning) {
            // Your thread logic goes here

            try {
                // Simulate some work
                Thread.sleep(1000);
                for (N_Notifications notification:
                     notificationsList) {
                    if(notification.getN_Vorzeit().equals(LocalDateTime.now().plusHours(1)) || notification.getN_Vorzeit().isBefore(LocalDateTime.now().plusHours(1))){
                        K_Kalender kalender = em.createQuery("Select k from K_Kalender k where k.K_ID=:id",K_Kalender.class).setParameter("id",notification.getN_K_ID()).getSingleResult();
                        showNotification(kalender.getK_Title()==null?"<Kein Title>":kalender.getK_Title(),String.format("dd-MM-yyyy","Zeitverspätung: "+ Duration.between(notification.getN_Vorzeit(),LocalDateTime.now())) );
                        em.remove(notification);
                        em.getTransaction().commit();
                    }
                }
                System.out.println("hallo");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void showNotification(String title, String message) {
        try {
            // Create a SystemTray object
            SystemTray tray = SystemTray.getSystemTray();

            // Load an image for the TrayIcon
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("path/to/icon.png");

            // Create a TrayIcon
            TrayIcon trayIcon = new TrayIcon(image, "Tray Icon");

            // Add an ActionListener to open Google on click
            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        // Open Google in the default browser
                        Desktop.getDesktop().browse(new URI("https://www.google.com"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Add the TrayIcon to the SystemTray
            tray.add(trayIcon);

            // Display the notification
            trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);

            // Remove the TrayIcon after a few seconds (optional)
            Thread.sleep(5000); // Wait for 5 seconds
            tray.remove(trayIcon);
        } catch (AWTException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
