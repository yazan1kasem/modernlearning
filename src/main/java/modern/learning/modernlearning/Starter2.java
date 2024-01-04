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
import javax.persistence.EntityTransaction;
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

public class Starter2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Dokumente.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Modern learning");
        stage.getIcons().add(new Image("file:src/main/Media/SkillBuildersLogo.png"));
        stage.setMinHeight(640);
        stage.setMinWidth(1000);
        stage.setWidth(scene.getWidth());

        stage.setScene(scene);

        stage.show();


    }




    public static void main(String[] args) {
        launch();
    }
}

