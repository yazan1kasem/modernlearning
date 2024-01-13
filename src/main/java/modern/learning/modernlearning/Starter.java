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
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EventListener;
import java.util.List;

public class Starter extends Application {
    public static boolean geschlossen=false;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Kalender.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add("file:src/main/resources/style.css");

        stage.setTitle("Modern learning");
        stage.getIcons().add(new Image("file:src/main/Media/SkillBuildersLogo.png"));
        stage.setMinHeight(615);
        stage.setMinWidth(Toolkit.getDefaultToolkit().getScreenSize().width / 2);

        stage.setScene(scene);

        stage.show();
        stage.setWidth(scene.getWidth());
        stage.setHeight(scene.getHeight());
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
        stage.setOnCloseRequest(closeThreads->{
            geschlossen=true;
            System.exit(0);
        });




    }





    public static void main(String[] args) {
        launch();
    }
}
