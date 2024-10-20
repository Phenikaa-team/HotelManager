module group.phenikaa.hotelmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.google.gson;
    requires com.google.common;
    requires java.desktop;
    requires java.sql;

    opens group.phenikaa.hotelmanager to javafx.fxml;
    exports group.phenikaa.hotelmanager;
    opens group.phenikaa.hotelmanager.api.info.api to com.google.gson;
    opens group.phenikaa.hotelmanager.api.info to com.google.gson;
    opens group.phenikaa.hotelmanager.api.info.impl.customer to com.google.gson;
    exports group.phenikaa.hotelmanager.controller;
    opens group.phenikaa.hotelmanager.controller to javafx.fxml;
}