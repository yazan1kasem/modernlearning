package modern.learning.modernlearning.CalenderClasses;

import entities.K_Kalender;
import entities.N_Notifications;
import javafx.animation.ScaleTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import modern.learning.modernlearning.KalenderController;
import org.controlsfx.control.PopOver;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.*;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.*;

public class KalenderPopover extends PopOver {
    /******************************************************
     * Attributes
     ******************************************************/
    // Date and Time Picker
    private DateTimePicker dateTimePicker;
    private LocalDate datum;
    // TextFields for Title and Description
    private TextField titleField;
    private TextArea beschreibung;
    //Buttons
    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    //Selected Node
    private Node node;
    private NotificationBox notificationBox=new NotificationBox();
    private KalenderController kalenderController;
    private final EntityManager em = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

    /******************************************************
     * Constructor und Initializer
     ******************************************************/
    public KalenderPopover(Node node, LocalDate datum) {
        this.setTitle("Termin am: " + datum);
        this.node=node;
        titleField= new TextField();
        beschreibung= new TextArea();
        saveButton=new Button();
        cancelButton= new Button();

        deleteButton= new Button();
        this.datum=datum;
        dateTimePicker= new DateTimePicker();
        dateTimePicker.setKastendatum(of(datum,LocalTime.of(0,0)));

        titleField.setPromptText("Title");
        beschreibung.setPromptText("Beschreibung");
        saveButton.setText("save");
        cancelButton.setText("cancel");
        deleteButton.setId("deleteButton");

        configureArrowLocation();
        TerminPopOver();
        this.show(node);
        if(datum.isBefore(LocalDate.now())){
            notificationBox.getEinAus().setSelected(false);
            notificationBox.getZeit().setDisable(true);
            notificationBox.getZahlenEingabe().setDisable(true);
            notificationBox.getEinAus().setDisable(true);
        }
        this.arrowSizeProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setMinWidth(newValue.floatValue() / 2);
            cancelButton.setMinWidth(newValue.floatValue() / 2);
        });

    }
    /******************************************************
     * Methods
     ******************************************************/
    public void insertPopOver(){
        try {
            dateTimePicker.setinhalt();
            this.setContentNode(PopOverLayout());
            this.show(node);
            saveButton.setOnMouseClicked(saveEvent->{
                if(isFilledCorrectly()){
                    em.getTransaction().begin();
                    K_Kalender kalender = new K_Kalender();
                    kalender.setK_Title(titleField.getText());
                    kalender.setK_Beschreibung(beschreibung.getText());

                    kalender.setK_vonDatum(
                            of(dateTimePicker.getVondatepicker().getValue().getYear(),
                                    dateTimePicker.getVondatepicker().getValue().getMonth().getValue(),
                                    dateTimePicker.getVondatepicker().getValue().getDayOfMonth(),
                                    dateTimePicker.getVonhourComboBox().getValue(),
                                    dateTimePicker.getVonminuteComboBox().getValue()));
                    kalender.setK_bisDatum(
                            of(dateTimePicker.getBisdatepicker().getValue().getYear(),
                                    dateTimePicker.getBisdatepicker().getValue().getMonth().getValue(),
                                    dateTimePicker.getBisdatepicker().getValue().getDayOfMonth(),
                                    dateTimePicker.getBishourComboBox().getValue(),
                                    dateTimePicker.getBisminuteComboBox().getValue())
                    );
                    try {
                        if(notificationBox.EinAus.isSelected()){
                            em.persist(notificationBox.GehtDataNotification(kalender));
                        }
                    }catch (Exception e){
                        System.out.println("Keine Benachrichtigung ");
                    }
                    em.persist(kalender);
                    em.getTransaction().commit();
                    kalenderController.drawCalendar();
                    this.hide();
                }else{
                    System.out.println("\u001B[31m" +"save failed"+ "\u001B[0m");
                }
            });
            System.out.println("\u001B[32m" + "Inserted"+"\u001B[0m");
        }catch (Exception e) {
            System.out.println("\u001B[31m" +"Insert failed because of: "+e.getMessage()+ "\u001B[0m");

        }
    }
    public void UpdatePopOver(K_Kalender kalender){
        try {
            this.setContentNode(PopOverLayout());
            this.show(node);
            dateTimePicker.getVondatepicker().setValue(LocalDate.of(kalender.getK_vonDatum().getYear(),kalender.getK_vonDatum().getMonthValue(),kalender.getK_vonDatum().getDayOfMonth()));
            dateTimePicker.getBisdatepicker().setValue(LocalDate.of(kalender.getK_bisDatum().getYear(),kalender.getK_bisDatum().getMonthValue(),kalender.getK_bisDatum().getDayOfMonth()));
            dateTimePicker.getVonhourComboBox().setValue(kalender.getK_vonDatum().getHour());
            dateTimePicker.getVonminuteComboBox().setValue(kalender.getK_vonDatum().getMinute());
            dateTimePicker.getBishourComboBox().setValue(kalender.getK_bisDatum().getHour());
            dateTimePicker.getBisminuteComboBox().setValue(kalender.getK_bisDatum().getMinute());
            titleField.setText(kalender.getK_Title()!=null ? kalender.getK_Title(): null);
            beschreibung.setText(kalender.getK_Beschreibung()!=null ? kalender.getK_Beschreibung():null);
            LocalDateTime notificationdate;
            try {
               notificationdate = (LocalDateTime) em.createQuery("select n.N_Vorzeit from N_Notifications n where n.N_K_ID=:id").setParameter("id", kalender).getSingleResult();
            }catch (Exception e){
                notificationdate=null;
            }
            if(notificationdate!=null){
                if(java.time.Duration.between(notificationdate,kalender.getK_vonDatum()).toMinutes()<60){
                    notificationBox.getZeit().setValue("Minuten");
                    notificationBox.getZahlenEingabe().setText(String.valueOf(java.time.Duration.between(notificationdate,kalender.getK_vonDatum()).toMinutes()));
                } else if (java.time.Duration.between(notificationdate,kalender.getK_vonDatum()).toHours()<24) {
                    notificationBox.getZeit().setValue("Stunden");
                    notificationBox.getZahlenEingabe().setText(String.valueOf(java.time.Duration.between(notificationdate,kalender.getK_vonDatum()).toHours()  ));

                } else if (java.time.Duration.between(notificationdate,kalender.getK_vonDatum()).toDays()%30!=0 ) {
                    notificationBox.getZeit().setValue("Tage");
                    notificationBox.getZahlenEingabe().setText(String.valueOf(java.time.Duration.between(notificationdate,kalender.getK_vonDatum()).toDays()  ));

                } else if (java.time.Duration.between(notificationdate,kalender.getK_vonDatum()).toDays()%30==0 ){
                    notificationBox.getZeit().setValue("Monate");
                    notificationBox.getZahlenEingabe().setText(String.valueOf( java.time.Duration.between(notificationdate,kalender.getK_vonDatum()).toDays()/30 ));

                }
            }
            else{
                notificationBox.getEinAus().setSelected(false);
                notificationBox.getZahlenEingabe().setDisable(true);
                notificationBox.getZeit().setDisable(true);
            }
            saveButton.setOnMouseClicked(UpdateEvent->{
                if(isFilledCorrectly()){
                    em.getTransaction().begin();
                    kalender.setK_Title(titleField.getText());
                    kalender.setK_Beschreibung(beschreibung.getText());

                    kalender.setK_vonDatum(
                            of(dateTimePicker.getVondatepicker().getValue().getYear(),
                                    dateTimePicker.getVondatepicker().getValue().getMonth().getValue(),
                                    dateTimePicker.getVondatepicker().getValue().getDayOfMonth(),
                                    dateTimePicker.getVonhourComboBox().getValue(),
                                    dateTimePicker.getVonminuteComboBox().getValue()));
                    kalender.setK_bisDatum(
                            of(dateTimePicker.getBisdatepicker().getValue().getYear(),
                                    dateTimePicker.getBisdatepicker().getValue().getMonth().getValue(),
                                    dateTimePicker.getBisdatepicker().getValue().getDayOfMonth(),
                                    dateTimePicker.getBishourComboBox().getValue(),
                                    dateTimePicker.getBisminuteComboBox().getValue())
                    );
                    try {
                        if(notificationBox.EinAus.isSelected()){
                            em.persist(notificationBox.GehtDataNotification(kalender));
                        }
                    }catch (Exception e){
                        System.out.println("Keine Benachrichtigung ");
                    }
                    em.persist(kalender);
                    em.getTransaction().commit();
                    this.hide();
                }
            });
            System.out.println("\u001B[32m" + "Updated"+"\u001B[0m");
        }catch (Exception e) {
            System.out.println("\u001B[31m" +"Update failed because of: "+e.getMessage()+ "\u001B[0m");
        }

    }
    private VBox PopOverLayout(){
        VBox Layout = new VBox();
        Layout.setSpacing(10);
        Layout.setPadding(new Insets(10));

        HBox dateAndNot=new HBox();
        HBox buttonsBox=new HBox();
        dateAndNot.setSpacing(20);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        buttonsBox.setSpacing(20);

        notificationBox.setSpacing(20);
        cancelButton= createStyledButton("Cancel", "GRAY", "WHITE", "BLACK");
        saveButton=createStyledButton("Save", "ORANGE", "WHITE", "BLACK");


        dateAndNot.getChildren().addAll(dateTimePicker);
        buttonsBox.getChildren().addAll(cancelButton,saveButton);
        cancelButton.setOnMouseClicked(hideEvent->{
            this.hide();
        });

        VBox notificationslayout = new VBox();
        notificationslayout.setSpacing(2);
        notificationslayout.getChildren().addAll(new Label("Notification"), notificationBox);

        notificationslayout.setAlignment(Pos.CENTER_LEFT);

        Layout.getChildren().addAll(dateAndNot,notificationslayout,titleField,beschreibung,buttonsBox);

        return Layout;
    }
    private Button createStyledButton(String text, String bgColor, String textColor, String borderColor) {
        Button button = new Button(text);

        button.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: " + textColor + ";" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-background-radius: 5;" +
                        "-fx-border-radius: 5;"
        );

        // Hover effect for the Save button
        button.setOnMouseEntered(event -> {
            button.setStyle(
                    "-fx-background-color: " + textColor + ";" +
                            "-fx-text-fill: " + bgColor + ";" +
                            "-fx-border-color: " + "BLACK" + ";" +
                            "-fx-background-radius: 5;" +
                            "-fx-border-radius: 5;"
            );
            button.setCursor(Cursor.HAND);
        });

        // Reset style on mouse exit
        button.setOnMouseExited(event -> {
            button.setStyle(
                    "-fx-background-color: " + bgColor + ";" +
                            "-fx-text-fill: " + textColor + ";" +
                            "-fx-border-color: " + borderColor + ";" +
                            "-fx-background-radius: 5;" +
                            "-fx-border-radius: 5;"
            );
        });


        return button;
    }


    public void TerminPopOver(){
        VBox Allgemein = new VBox(10);
        Allgemein.setPadding(new Insets(10));
        VBox TerminepopoverLayout = new VBox(10);
        TerminepopoverLayout.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true); // Enable horizontal scrolling if needed
        scrollPane.setMaxHeight(300);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: Transparent;");
        List<K_Kalender> KalenderListe = em.createQuery(
                        "SELECT k FROM K_Kalender k", K_Kalender.class)
                .getResultList();

        List<K_Kalender> filteredList = KalenderListe.stream()
                .filter(k -> k.getK_vonDatum().toLocalDate().compareTo(datum) <= 0 &&
                        k.getK_bisDatum().toLocalDate().compareTo(datum) >= 0)
                .collect(Collectors.toList());

        System.out.println(filteredList.size());



        if (!filteredList.isEmpty()) {
            for (int i = 0; i < filteredList.size(); i++) {
                HBox TerminDelete=new HBox();
                VBox TermineBox = new VBox();
                TerminDelete.setStyle("-fx-border-radius: 20; -fx-alignment: center; -fx-border-color: black");
                TerminDelete.setPrefWidth(200);
                VBox.setMargin(TerminepopoverLayout, new Insets(0, 10, 0, 10));
                int finalI = i;

                //Hier sollte kommen das man es ändern kann
                TerminDelete.setOnMouseClicked(UpdateEvent -> {
                    UpdatePopOver(filteredList.get(finalI));
                });
                //Transitions
                TerminDelete.setOnMouseEntered(transischeneventEnter -> {
                    addButtonToDelete(TerminDelete, filteredList.get(finalI));
                    applyScaleTransition(TerminDelete, 1.05);
                    TerminDelete.setCursor(Cursor.HAND);
                });
                TerminDelete.setOnMouseExited(transischeneventExite -> {
                    removeButtonToDelete(TerminDelete);
                    applyScaleTransition(TerminDelete, 1.0);
                });
                TermineBox.getChildren().add((new Label(!filteredList.get(i).getK_Title().isEmpty() ?   filteredList.get(i).getK_Title():"<KEIN TITLE>")));
                TermineBox.getChildren().add(new Label(filteredList.get(i).getK_vonDatum().getDayOfMonth() + "." + filteredList.get(i).getK_vonDatum().getMonthValue() + "." + filteredList.get(i).getK_vonDatum().getYear() + " um: " + filteredList.get(i).getK_vonDatum().getHour() + ":" + filteredList.get(i).getK_vonDatum().getMinute()));
                TermineBox.getChildren().add(new Label(filteredList.get(i).getK_bisDatum().getDayOfMonth() + "." + filteredList.get(i).getK_bisDatum().getMonthValue() + "." + filteredList.get(i).getK_bisDatum().getYear() + " um: " + filteredList.get(i).getK_bisDatum().getHour() + ":" + filteredList.get(i).getK_bisDatum().getMinute()));
                TerminDelete.getChildren().addAll(TermineBox);
                TerminepopoverLayout.getChildren().addAll(TerminDelete);
            }
            scrollPane.setContent(TerminepopoverLayout);
        }else{
            scrollPane.setContent(new Text("      <Keine Einträge>      "));
        }


        StackPane neuButton = createCircularButton();

        Allgemein.setAlignment(Pos.CENTER);
        Allgemein.getChildren().addAll(scrollPane,neuButton);

        this.setContentNode(Allgemein);
        configureArrowLocation();
        this.show(node);
    }
    private StackPane createCircularButton() {
        StackPane circlestackpane=new StackPane();
        Circle circle = new Circle(25);
        // Setze die Schriftart und -größe
        // Setze den Hintergrund des Buttons auf Orange
        circle.setStyle("-fx-background-color: orange; -fx-font-size: 20");
        circle.setFill(Paint.valueOf("orange"));
        // Erstelle den kreisförmigen Rand um den Button
        // Füge den Hover-Effekt hinzu


        Text plusText=new Text("+");
        plusText.setStyle("-fx-fill: white;-fx-font-size: 40;");
        plusText.setTranslateY(-4.5);
        plusText.setTranslateX(-0.5);

        AtomicBoolean animated = new AtomicBoolean(false);

        EventHandler MouseEntered=neuenterevent -> {
            if(!animated.get()){
                ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), circle);
                scaleTransition.setToX(1.1);
                scaleTransition.setToY(1.1);
                scaleTransition.play();
                circle.setFill(javafx.scene.paint.Paint.valueOf("white"));
                circlestackpane.setCursor(Cursor.HAND);
                circle.setStroke(Paint.valueOf("black"));

                plusText.setStyle("-fx-fill: orange;-fx-font-size: 40");
                animated.set(true);

            }
        };
        // Füge den Effekt für das Verlassen des Hover-Zustands hinzu
        EventHandler MouseExite= neuexitevent -> {
           if(animated.get()){
               ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), circle);
               scaleTransition.setToX(1.0);
               scaleTransition.setToY(1.0);
               scaleTransition.play();
               circle.setFill(javafx.scene.paint.Paint.valueOf("orange"));
               plusText.setStyle("-fx-fill: white;-fx-font-size: 40; ");
               circle.setStroke(Paint.valueOf("orange"));

               animated.set(false);
           }
        };

        EventHandler CircleClicked=  Insertevent -> {
            insertPopOver();
        };

        circle.setOnMouseEntered(MouseEntered);
        circle.setOnMouseExited(MouseExite);
        circle.setOnMouseClicked(CircleClicked);
        plusText.setOnMouseEntered(MouseEntered);
        plusText.setOnMouseExited(MouseExite);
        plusText.setOnMouseClicked(CircleClicked);




        circlestackpane.getChildren().addAll(circle,plusText);
        return circlestackpane;
    }
    //da wird geschaut, ob alles so gefüllt ist wie es sein sollte
    public boolean isFilledCorrectly() {

        if (
                of(
                        dateTimePicker.getVondatepicker().getValue().getYear(),
                        dateTimePicker.getVondatepicker().getValue().getMonthValue(),
                        dateTimePicker.getVondatepicker().getValue().getDayOfMonth(),
                        dateTimePicker.getVonhourComboBox().getValue(),
                        dateTimePicker.getVonminuteComboBox().getValue())
                        .isBefore(
                                of(
                                        dateTimePicker.getBisdatepicker().getValue().getYear(),
                                        dateTimePicker.getBisdatepicker().getValue().getMonthValue(),
                                        dateTimePicker.getBisdatepicker().getValue().getDayOfMonth(),
                                        dateTimePicker.getBishourComboBox().getValue(),
                                        dateTimePicker.getBisminuteComboBox().getValue()))
                        && titleField.getText()!=null){

            return true;
        }else{
            return false;
        }
    }
    private void addButtonToDelete(HBox terminebox, K_Kalender k) {
        //lösch den Termin der hier verwendet wird
        deleteButton= DeleteButtonDesign();
        deleteButton.setTranslateX((terminebox.getWidth()/6.3)-deleteButton.getWidth());
        deleteButton.setOnMouseClicked(deleteEvent -> {
            try {
                em.getTransaction().begin();
                em.remove(k);
                em.getTransaction().commit();
                TerminPopOver();
                kalenderController.drawCalendar();
                System.out.println("\u001B[32m" + "deleted"+"\u001B[0m");
            }catch (Exception e) {
                System.out.println("\u001B[31m" +"delete failed"+ "\u001B[0m");
            }
        });
        terminebox.getChildren().add(deleteButton);
    }
    private Button DeleteButtonDesign(){
        Button deleteButton = new Button();
        deleteButton.setText("x");
        String mainstyle="-fx-background-radius: 0 19 19 0; -fx-font-size: 20;-fx-fill-width: bold;";
        deleteButton.setPrefHeight(53.2);
        deleteButton.setStyle("-fx-background-color: rgba(175,175,175,0.76); -fx-border-color: gray;-fx-border-radius:0 19 19 0;"+mainstyle);
        deleteButton.setOnMouseEntered(makeredEvent->{
            deleteButton.setStyle("-fx-background-color: #ff3636;-fx-border-color: #ff3636;-fx-border-radius:0 19 19 0;-fx-text-fill: white;"+mainstyle );
        });
        deleteButton.setOnMouseExited(makenormalEvent->{
            deleteButton.setStyle("-fx-background-color: rgba(175,175,175,0.76); -fx-border-color: gray;-fx-border-radius:0 19 19 0;"+mainstyle);
        });




        return deleteButton;
    }
    private void removeButtonToDelete(HBox terminebox) {
        terminebox.getChildren().removeAll(deleteButton);
    }

    private void applyScaleTransition(Node terminebox, double scaleValue) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), terminebox);
        scaleTransition.setToX(scaleValue);
        scaleTransition.setToY(scaleValue);
        scaleTransition.play();
    }
    private void configureArrowLocation() {
        int mouseX = MouseInfo.getPointerInfo().getLocation().x;
        int mouseY = MouseInfo.getPointerInfo().getLocation().y;
        if (Toolkit.getDefaultToolkit().getScreenSize().width / 2 > mouseX) {
            if (Toolkit.getDefaultToolkit().getScreenSize().height / 2 > mouseY) {
                this.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
            } else if (Toolkit.getDefaultToolkit().getScreenSize().height / 2 < mouseY) {
                this.setArrowLocation(PopOver.ArrowLocation.LEFT_BOTTOM);
            }
        } else if (Toolkit.getDefaultToolkit().getScreenSize().width / 2 < mouseX) {
            if (Toolkit.getDefaultToolkit().getScreenSize().height / 2 > mouseY) {
                this.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
            } else if (Toolkit.getDefaultToolkit().getScreenSize().height / 2 < mouseY) {
                this.setArrowLocation(PopOver.ArrowLocation.RIGHT_BOTTOM);
            }
        }
    }

    /******************************************************
     * Getters and setters
     ******************************************************/
    public DateTimePicker getDateTimePicker() {
        return dateTimePicker;
    }

    public void setDateTimePicker(DateTimePicker dateTimePicker) {
        this.dateTimePicker = dateTimePicker;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public TextField getTitleField() {
        return titleField;
    }

    public void setTitleField(TextField titleField) {
        this.titleField = titleField;
    }

    public TextArea getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(TextArea beschreibung) {
        this.beschreibung = beschreibung;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }


    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }

    public KalenderController getKalenderController() {
        return kalenderController;
    }

    public void setKalenderController(KalenderController kalenderController) {
        this.kalenderController = kalenderController;
    }


}
