module com.example.oodprojectfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.oodprojectfx to javafx.fxml;
    exports com.example.oodprojectfx;
}