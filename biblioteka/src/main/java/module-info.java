module org.example.biblioteka {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.biblioteka to javafx.fxml;
    exports org.example.biblioteka;
}