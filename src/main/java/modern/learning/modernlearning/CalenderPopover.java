package modern.learning.modernlearning;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

import static java.time.LocalDateTime.*;

public class CalenderPopover extends PopOver implements Initializable {
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
    private Button notificationButton;
    private Button deleteButton;
    //Selected Node
    private Node node;
    private EntityManager em = Persistence.createEntityManagerFactory("Modernlearning").createEntityManager();

    /******************************************************
     * Constructor und Initializer
     ******************************************************/
    public CalenderPopover(Node node, LocalDate datum) {
        this.show(node);
        this.setTitle("Termin am: " + datum);
        this.node=node;

        dateTimePicker= new DateTimePicker();
        titleField= new TextField();
        beschreibung= new TextArea();
        saveButton=new Button();
        cancelButton= new Button();
        notificationButton = new Button();
        deleteButton= new Button();
        this.datum=datum;

        titleField.setPromptText("Title");
        beschreibung.setPromptText("Beschreibung");
        saveButton.setText("save");
        cancelButton.setText("cancel");
        notificationButton.setText("+");

        TerminPopOver();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TerminPopOver();
        configureArrowLocation();
        cancelButton.setOnMouseClicked(hideEvent->{
            this.hide();
        });
    }

    /******************************************************
     * Methods
     ******************************************************/
    public void insertPopOver(){
        try {
            dateTimePicker.setinhalt();
            this.setContentNode(PopOverLayout());
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

        dateAndNot.getChildren().addAll(dateTimePicker, notificationButton);
        buttonsBox.getChildren().addAll(cancelButton,saveButton);

        Layout.getChildren().addAll(dateAndNot,titleField,beschreibung,buttonsBox);
        return Layout;
    }
    public void TerminPopOver(){
        VBox TerminepopoverLayout = new VBox(10);
        TerminepopoverLayout.setPadding(new Insets(10));


        List<K_Kalender> KalenderListe = em.createQuery(
                        "SELECT k FROM K_Kalender k WHERE :datum > k.K_vonDatum AND :datum < k.K_bisDatum or :datum = k.K_bisDatum or :datum = k.K_vonDatum",
                        K_Kalender.class)
                        .setParameter("datum", of(datum, LocalTime.of(0, 0)))
                        .getResultList();


        if (!KalenderListe.isEmpty()) {
            for (int i = 0; i < KalenderListe.size(); i++) {
                KalenderListe.get(i);
                VBox TermineBox = new VBox();
                TermineBox.setSpacing(10);
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
                    applyScaleTransition(TermineBox, 1.05);
                    TermineBox.setCursor(Cursor.HAND);
                });
                TermineBox.setOnMouseExited(transischeneventExite -> {
                    removeButtonToDelete(TermineBox);
                    applyScaleTransition(TermineBox, 1.0);
                });


                TermineBox.getChildren().add(new Label(KalenderListe.get(i).getK_Title() != null ?   KalenderListe.get(i).getK_Title():"<KEIN TITLE>"));
                TermineBox.getChildren().add(new Label(KalenderListe.get(i).getK_vonDatum().getDayOfMonth() + "." + KalenderListe.get(i).getK_vonDatum().getMonthValue() + "." + KalenderListe.get(i).getK_vonDatum().getYear() + " um: " + KalenderListe.get(i).getK_vonDatum().getHour() + ":" + KalenderListe.get(i).getK_vonDatum().getMinute()));
                TermineBox.getChildren().add(new Label(KalenderListe.get(i).getK_bisDatum().getDayOfMonth() + "." + KalenderListe.get(i).getK_bisDatum().getMonthValue() + "." + KalenderListe.get(i).getK_bisDatum().getYear() + " um: " + KalenderListe.get(i).getK_bisDatum().getHour() + ":" + KalenderListe.get(i).getK_bisDatum().getMinute()));
            }
        }


        Button neuButton = new Button();
        neuButton.setText("neu");
        neuButton.setOnMouseClicked(Insertevent -> {
            insertPopOver();
        });

        TerminepopoverLayout.getChildren().addAll(neuButton);
        this.setContentNode(TerminepopoverLayout);

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
        Button delete = new Button("Delete");
        //lösch den Termin der hier verwendet wird
        delete.setOnMouseClicked(deleteEvent -> {
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
        terminebox.getChildren().add(delete);
    }
    private void removeButtonToDelete(VBox terminebox) {
        terminebox.getChildren().removeIf(node -> node instanceof Button);
    }
    private void applyScaleTransition(VBox terminebox, double scaleValue) {
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

    public Button getNotificationButton() {
        return notificationButton;
    }

    public void setNotificationButton(Button notificationButton) {
        notificationButton = notificationButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(Button deleteButton) {
        this.deleteButton = deleteButton;
    }
}
