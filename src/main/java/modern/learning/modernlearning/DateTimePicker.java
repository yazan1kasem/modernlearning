package modern.learning.modernlearning;

import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.hibernate.type.ZonedDateTimeType;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class DateTimePicker extends HBox {
    private ZonedDateTime jetzt= ZonedDateTime.now();
    private final ComboBox<Integer> vonhourComboBox;
    private final ComboBox<Integer> vonminuteComboBox;
    private final ComboBox<Integer> bishourComboBox;
    private final ComboBox<Integer> bisminuteComboBox;

    public DateTimePicker() {
        super(50);

        this.vonhourComboBox = new ComboBox<>();
        this.vonminuteComboBox = new ComboBox<>();
        this.bishourComboBox = new ComboBox<>();
        this.bisminuteComboBox = new ComboBox<>();
        int stunde= jetzt.getHour();
        int Minute= jetzt.getMinute();

        // Populate hour and minute ComboBoxes
        for (int i = 0; i < 24; i++) {
            vonhourComboBox.getItems().add(i);
            bishourComboBox.getItems().add(i);
        }
        for (int i = 0; i < 60; i++) {
            vonminuteComboBox.getItems().add(i);
            bisminuteComboBox.getItems().add(i);
        }

        // Set default values
        vonhourComboBox.setValue(stunde);
        vonminuteComboBox.setValue(Minute);
        bishourComboBox.setValue(stunde+1);
        bisminuteComboBox.setValue(0);

        HBox hBoxvon = new HBox();
        hBoxvon.setSpacing(10);
        hBoxvon.setAlignment(Pos.CENTER);
        HBox hBoxbis = new HBox();
        hBoxbis.setSpacing(10);
        hBoxbis.setAlignment(Pos.CENTER);

        // Layout
        hBoxvon.getChildren().addAll(new Label("von: "),vonhourComboBox, new Label(":"), vonminuteComboBox);
        hBoxbis.getChildren().addAll(new Label("bis: "), bishourComboBox, new Label(":"), bisminuteComboBox);
        this.getChildren().addAll(hBoxvon, hBoxbis);

    }

    public ZonedDateTime getJetzt() {
        return jetzt;
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
