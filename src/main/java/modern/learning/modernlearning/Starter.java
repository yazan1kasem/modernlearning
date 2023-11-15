package modern.learning.modernlearning;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.io.IOException;

public class Starter extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        EntityManager emf= Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

//        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}