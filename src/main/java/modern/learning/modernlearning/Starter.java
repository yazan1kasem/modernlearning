package modern.learning.modernlearning;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

public class Starter extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("Kalender.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Modern learning");
        stage.getIcons().add(new Image("file:src/main/Media/SkillBuildersLogo.png"));
        stage.setMinHeight(500);
        stage.setMinWidth(750);
        stage.setWidth(scene.getWidth());

        stage.setScene(scene);

        stage.show();
        stage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("h: "+newValue);
            }
        });
        stage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                System.out.println("w: "+newValue);
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}