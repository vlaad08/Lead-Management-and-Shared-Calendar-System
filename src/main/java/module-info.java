module cl.sep2 {
  requires javafx.controls;
  requires javafx.fxml;

  opens cl.sep2 to javafx.fxml;
  exports cl.sep2;
}