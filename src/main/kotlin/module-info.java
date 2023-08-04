module com.example.javafx_demo2 {
    requires javafx.controls;

    requires javafx.fxml;
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires javafx.web;
    requires java.sql;

    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;
    requires cn.hutool;
    requires okhttp3;
    requires fastjson;

    opens com.example.benben_front to javafx.fxml;
    exports com.example.benben_front;
}