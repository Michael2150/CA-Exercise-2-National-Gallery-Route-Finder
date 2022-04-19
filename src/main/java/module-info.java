module com.ca.two {
    requires javafx.controls;
    requires javafx.fxml;
    requires jmh.core;

    opens com.ca.two to javafx.fxml;
    opens com.ca.two.graph to javafx.fxml;

    exports com.ca.two;
    exports com.ca.two.graph;
    exports com.ca.two.listviews;
    exports com.ca.two.models;
    opens com.ca.two.models to javafx.fxml;
    opens com.ca.two.listviews to javafx.fxml;
    exports com.ca.two.benchmarking;
    opens com.ca.two.benchmarking.jmh_generated to jmh.core;
}