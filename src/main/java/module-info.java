module com.example.oodprojectfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    //requires stanford.corenlp;
    requires edu.stanford.nlp.corenlp;

    // Export the controllers package to javafx.fxml
    exports com.example.oodprojectfx.controllers to javafx.fxml;
    opens com.example.oodprojectfx.controllers to javafx.fxml;

    //Because error came when trying to load table view
    opens com.example.oodprojectfx.models to javafx.base;

    opens com.example.oodprojectfx to javafx.fxml;
    exports com.example.oodprojectfx;
}