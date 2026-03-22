module com.example.space_colonies {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.space_colonies to javafx.fxml;
    exports com.example.space_colonies;
    exports com.example.space_colonies.model;
}