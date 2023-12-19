package modern.learning.modernlearning;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

public class NotificationExample {

    public static void main(String[] args) {
        // Check if the SystemTray is supported
        if (SystemTray.isSupported()) {
            showNotification("Hello", "This is a notification!");
        } else {
            System.err.println("SystemTray is not supported.");
        }
    }

    public static void showNotification(String title, String message) {
        try {
            // Create a SystemTray object
            SystemTray tray = SystemTray.getSystemTray();

            // Load an image for the TrayIcon
            Image image = Toolkit.getDefaultToolkit().getImage("path/to/icon.png");

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
}
