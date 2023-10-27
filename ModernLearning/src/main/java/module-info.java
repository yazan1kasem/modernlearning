module modern.learning.modernlearning {
    requires javafx.controls;
    requires javafx.fxml;


    opens modern.learning.modernlearning to javafx.fxml;
    exports modern.learning.modernlearning;
}