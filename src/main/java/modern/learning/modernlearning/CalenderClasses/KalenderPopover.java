package modern.learning.modernlearning.CalenderClasses;

import entities.K_Kalender;
import javafx.animation.ScaleTransition;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.swing.*;


import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
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
    private final EntityManager em = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

    /******************************************************
     * Constructor und Initializer
     ******************************************************/
    public KalenderPopover(Node node, LocalDate datum) {
        this.setTitle("Termin am: " + datum.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
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
                    em.persist(kalender);
                    em.getTransaction().commit();
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
        NotificationBox notificationBox=new NotificationBox();

        notificationBox.setSpacing(20);

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
    public void TerminPopOver(){
        VBox Allgemein = new VBox(10);
        Allgemein.setPadding(new Insets(10));
        VBox TerminepopoverLayout = new VBox(10);
        TerminepopoverLayout.setPadding(new Insets(10));
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true); // Enable horizontal scrolling if needed
        scrollPane.setMaxHeight(300);
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
                filteredList.get(i);
                VBox TermineBox = new VBox();
                TermineBox.setStyle("-fx-border-radius: 20; -fx-alignment: center; -fx-border-color: black");
                TermineBox.setPrefWidth(200);
                VBox.setMargin(TerminepopoverLayout, new Insets(0, 10, 0, 10));
                int finalI = i;

                //Hier sollte kommen das man es ändern kann
                TermineBox.setOnMouseClicked(UpdateEvent -> {
                    UpdatePopOver(KalenderListe.get(finalI));
                });

                //Transitions
                TermineBox.setOnMouseEntered(transischeneventEnter -> {
                    addButtonToDelete(TermineBox, KalenderListe.get(finalI));
                    applyScaleTransition(TermineBox, 1.01);
                    TermineBox.setCursor(Cursor.HAND);
                });
                TermineBox.setOnMouseExited(transischeneventExite -> {
                    removeButtonToDelete(TermineBox);
                    applyScaleTransition(TermineBox, 1.0);
                });


                TermineBox.getChildren().add((new Label(!filteredList.get(i).getK_Title().isEmpty() ?   KalenderListe.get(i).getK_Title():"<KEIN TITLE>")));
                TermineBox.getChildren().add(new Label(filteredList.get(i).getK_vonDatum().getDayOfMonth() + "." + KalenderListe.get(i).getK_vonDatum().getMonthValue() + "." + KalenderListe.get(i).getK_vonDatum().getYear() + " um: " + KalenderListe.get(i).getK_vonDatum().getHour() + ":" + KalenderListe.get(i).getK_vonDatum().getMinute()));
                TermineBox.getChildren().add(new Label(filteredList.get(i).getK_bisDatum().getDayOfMonth() + "." + KalenderListe.get(i).getK_bisDatum().getMonthValue() + "." + KalenderListe.get(i).getK_bisDatum().getYear() + " um: " + KalenderListe.get(i).getK_bisDatum().getHour() + ":" + KalenderListe.get(i).getK_bisDatum().getMinute()));
                TerminepopoverLayout.getChildren().addAll(TermineBox);
            }
            scrollPane.setContent(TerminepopoverLayout);
        }else{
            scrollPane.setContent(new Text("      <Keine Einträge>      "));
        }


        Button neuButton = new Button();
        neuButton.setText("neu");
        neuButton.setOnMouseClicked(Insertevent -> {
            insertPopOver();
        });
        Allgemein.setAlignment(Pos.CENTER);
        Allgemein.getChildren().addAll(scrollPane,neuButton);


        this.setContentNode(Allgemein);
        configureArrowLocation();
        this.show(node);
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
    private void addButtonToDelete(VBox terminebox, K_Kalender k) {
        //lösch den Termin der hier verwendet wird
        deleteButton.setText("Delete");
        deleteButton.setOnMouseClicked(deleteEvent -> {
            try {
                em.getTransaction().begin();
                em.remove(k);
                em.getTransaction().commit();
                TerminPopOver();
                System.out.println("\u001B[32m" + "deleted"+"\u001B[0m");
            }catch (Exception e) {
                System.out.println("\u001B[31m" +"delete failed"+ "\u001B[0m");
            }
        });
        terminebox.getChildren().add(deleteButton);
    }
    private void removeButtonToDelete(VBox terminebox) {
        terminebox.getChildren().removeAll( deleteButton);
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
}
