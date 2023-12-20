package modern.learning.modernlearning.CalenderClasses;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
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
        vondatepicker.valueProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(bisdatepicker.getValue().isBefore(newValue)){
                    bisdatepicker.setValue(newValue);
                }
            }
        });
        vonhourComboBox.valueProperty().addListener(new ChangeListener<>(){
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if(newValue>bishourComboBox.getValue()){
                    if(newValue<23){
                        bishourComboBox.setValue(newValue+1);
                    }else{
                        bishourComboBox.setValue(newValue);
                        if(vonminuteComboBox.getValue()>bisminuteComboBox.getValue()){
                            if(vonminuteComboBox.getValue()<59){
                                bisminuteComboBox.setValue(vonminuteComboBox.getValue()+1);
                            }else{
                                bisminuteComboBox.setValue(vonminuteComboBox.getValue());
                            }
                        }
                    }
                }
            }
        });
        vonminuteComboBox.valueProperty().addListener(new ChangeListener<>(){

            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if(vonhourComboBox.getValue()>=bishourComboBox.getValue()){
                    if(vonhourComboBox.getValue()<23){
                        bishourComboBox.setValue(vonhourComboBox.getValue()+1);
                    }else{
                        bishourComboBox.setValue(vonhourComboBox.getValue());
                        if(newValue>bisminuteComboBox.getValue()){
                            if(newValue<59){
                                bisminuteComboBox.setValue(newValue+1);
                            }else{
                                bisminuteComboBox.setValue(newValue);
                            }
                        }
                    }
                }
            }
        });
        bishourComboBox.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if(vonhourComboBox.getValue()>1){
                    vonhourComboBox.setValue(newValue-1);
                } else if (vonhourComboBox.getValue()<=1) {
                    bishourComboBox.setValue(vonhourComboBox.getValue()+1);
                } else{
                    if(vonminuteComboBox.getValue()>0){
                        if(vonminuteComboBox.getValue()>bisminuteComboBox.getValue()){
                            bisminuteComboBox.setValue(newValue);
                        }else{
                            bisminuteComboBox.setValue(vonminuteComboBox.getValue()+1);
                        }
                    }
                }
            }
        });

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
