module com.ca2.caexercise2nationalgalleryroutefinder {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ca.two to javafx.fxml;
    exports com.ca.two;
    exports com.ca.two.graph;
    opens com.ca.two.graph to javafx.fxml;
}