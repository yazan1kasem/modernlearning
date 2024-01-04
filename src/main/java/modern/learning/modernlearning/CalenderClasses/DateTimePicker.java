package modern.learning.modernlearning.CalenderClasses;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.hibernate.type.ZonedDateTimeType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class DateTimePicker extends VBox {
    private LocalDateTime Kastendatum=LocalDateTime.now();
    private DatePicker vondatepicker;
    private DatePicker bisdatepicker;
    private final ComboBox<Integer> vonhourComboBox;
    private final ComboBox<Integer> vonminuteComboBox;
    private final ComboBox<Integer> bishourComboBox;
    private final ComboBox<Integer> bisminuteComboBox;

    public DateTimePicker() {
        super(5);

        this.vonhourComboBox = new ComboBox<>();
        this.vonminuteComboBox = new ComboBox<>();
        this.bishourComboBox = new ComboBox<>();
        this.bisminuteComboBox = new ComboBox<>();
        this.vondatepicker= new DatePicker();
        this.bisdatepicker= new DatePicker();


        HBox hBoxvon = new HBox();
        hBoxvon.setSpacing(10);
        hBoxvon.setAlignment(Pos.CENTER);
        HBox hBoxbis = new HBox();
        hBoxbis.setSpacing(10);
        hBoxbis.setAlignment(Pos.CENTER);


        // Layout
        hBoxvon.getChildren().addAll(new Label("von: "),vondatepicker,vonhourComboBox, new Label(":"), vonminuteComboBox);
        hBoxbis.getChildren().addAll(new Label("bis:  "),bisdatepicker, bishourComboBox, new Label(":"), bisminuteComboBox);
        this.getChildren().addAll(hBoxvon, hBoxbis);

    }
    public void setinhalt(){
        int stunde= Kastendatum.getHour();
        int Minute= Kastendatum.getMinute();

        // Populate hour and minute ComboBoxes
        for (int i = 0; i < 24; i++) {
            vonhourComboBox.getItems().add(i);
            bishourComboBox.getItems().add(i);
        }
        for (int i = 0; i < 60; i++) {
            vonminuteComboBox.getItems().add(i);
            bisminuteComboBox.getItems().add(i);
        }
        vondatepicker.setValue(LocalDate.of(Kastendatum.getYear(),Kastendatum.getMonth(),Kastendatum.getDayOfMonth()));
        bisdatepicker.setValue(LocalDate.of(Kastendatum.getYear(),Kastendatum.getMonth(),Kastendatum.getDayOfMonth()));

        // Set default values
        vonhourComboBox.setValue(stunde);
        vonminuteComboBox.setValue(Minute);
        if(vonhourComboBox.getValue()<23){
            bishourComboBox.setValue(stunde+1);
            bisminuteComboBox.setValue(0);
        }else{
            bishourComboBox.setValue(stunde);
            if(vonminuteComboBox.getValue()<59){
                bisminuteComboBox.setValue(vonminuteComboBox.getValue()+1);
            }else{
                bisminuteComboBox.setValue(vonminuteComboBox.getValue());
            }
        }

    }
    private void addChangeListener() {
        // Überwachen Sie Änderungen am Datum
        vondatepicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            checkDateValidity();
        });

        bisdatepicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            checkDateValidity();
        });

        // Überwachen Sie Änderungen an Stunden und Minuten
        vonhourComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            checkDateValidity();
        });

        vonminuteComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            checkDateValidity();
        });

        bishourComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            checkDateValidity();
        });

        bisminuteComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            checkDateValidity();
        });
    }

    public boolean checkDateValidity() {
        LocalDate vonDate = vondatepicker.getValue();
        int vonHour = vonhourComboBox.getValue();
        int vonMinute = vonminuteComboBox.getValue();

        LocalDate bisDate = bisdatepicker.getValue();
        int bisHour = bishourComboBox.getValue();
        int bisMinute = bisminuteComboBox.getValue();

        // Überprüfen Sie, ob das Startdatum vor dem Enddatum liegt
        if (vonDate.isAfter(bisDate) || (vonDate.isEqual(bisDate) && vonHour * 60 + vonMinute >= bisHour * 60 + bisMinute)) {
            addChangeListener();
            MarkiereMeinenFehler();
            return false;
        } else {
            // Setzen Sie die normale Farbe zurück
            resetErrorMarking();
            return true;
        }
    }
    private void resetErrorMarking() {
        // Hier können Sie die Markierung zurücksetzen, z.B., den Hintergrund auf den Standardwert setzen
        String removeStyle="";
        vondatepicker.setStyle(removeStyle);
        bisdatepicker.setStyle(removeStyle);
        vonhourComboBox.setStyle(removeStyle);
        vonminuteComboBox.setStyle(removeStyle);
        bishourComboBox.setStyle(removeStyle);
        bisminuteComboBox.setStyle(removeStyle);
    }
    private void MarkiereMeinenFehler() {
        markiereFalscheDaten(vondatepicker);
        markiereFalscheDaten(bisdatepicker);
        markiereFalscheDaten(vonhourComboBox);
        markiereFalscheDaten(vonminuteComboBox);
        markiereFalscheDaten(bishourComboBox);
        markiereFalscheDaten(bisminuteComboBox);
    }

    private void markiereFalscheDaten(Control control) {
        control.setStyle("-fx-border-color: #ff1818;");
    }
    public void setKastendatum(LocalDateTime kastendatum) {
        Kastendatum = kastendatum;
    }

    public DatePicker getVondatepicker() {
        return vondatepicker;
    }

    public void setVondatepicker(DatePicker vondatepicker) {
        this.vondatepicker = vondatepicker;
    }

    public DatePicker getBisdatepicker() {
        return bisdatepicker;
    }

    public void setBisdatepicker(DatePicker bisdatepicker) {
        this.bisdatepicker = bisdatepicker;
    }

    public ComboBox<Integer> getVonhourComboBox() {
        return vonhourComboBox;
    }

    public ComboBox<Integer> getVonminuteComboBox() {
        return vonminuteComboBox;
    }

    public ComboBox<Integer> getBishourComboBox() {
        return bishourComboBox;
    }

    public ComboBox<Integer> getBisminuteComboBox() {
        return bisminuteComboBox;
    }
}
