module com.ca2.caexercise2nationalgalleryroutefinder {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ca2.caexercise2nationalgalleryroutefinder to javafx.fxml;
    exports com.ca2.caexercise2nationalgalleryroutefinder;
}