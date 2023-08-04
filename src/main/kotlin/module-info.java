module com.example.javafx_demo2 {
    requires javafx.controls;

    requires javafx.fxml;
    requires kotlin.stdlib;
    requires javafx.web;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;

    opens com.example.benben_front to javafx.fxml;
    exports com.example.benben_front;
}