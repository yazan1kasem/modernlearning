module modern.learning.modernlearning {
    requires javafx.controls;
    requires javafx.fxml;


    requires java.sql;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires net.bytebuddy;
    requires com.fasterxml.classmate;
    requires java.xml.bind;
    requires java.desktop;
    requires org.jetbrains.annotations;

    opens entities to org.hibernate.orm.core, javafx.base;
    opens modern.learning.modernlearning to javafx.fxml;
    exports modern.learning.modernlearning;
}