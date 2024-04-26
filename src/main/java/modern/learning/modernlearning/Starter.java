package modern.learning.modernlearning;

import entities.U_user;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.persistence.*;
import java.awt.*;
import java.io.IOException;

public class Starter extends Application {
    public static boolean geschlossen=false;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Starter.class.getResource("eigeneDatein.fxml"));
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
        Currentuser.setUsername(System.getProperty("user.name"));
        EntityManager em = DatabaseConnection.getConnection();
        try {
            // Check if user with the given username already exists

            U_user existingUser = em.createQuery(
                            "SELECT u FROM U_user u WHERE u.U_Name = :username", U_user.class)
                    .setParameter("username", Currentuser.getUsername()).getSingleResult();

            if (existingUser != null) {
                System.out.println("User bereits vorhanden: " + Currentuser.getUsername());
            }
        } catch (NoResultException e) {
            // User does not exist, create a new user
            U_user newUser = new U_user();
            newUser.setU_Name(Currentuser.getUsername());

            try {
                em.getTransaction().begin();
                em.persist(newUser);
                em.getTransaction().commit();
                System.out.println("Neuer Benutzer erstellt: " + Currentuser.getUsername());
            } catch (PersistenceException pe) {
                em.getTransaction().rollback();
                System.out.println("Fehler beim Erstellen des Benutzers: " + pe.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Abfragen des Benutzers: " + e.getMessage());
        }

    }





    public static void main(String[] args) {
        launch();
    }
}
