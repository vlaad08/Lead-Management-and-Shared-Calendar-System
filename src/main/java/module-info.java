module cl.sep2 {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;

  opens app to javafx.fxml;
  opens app.view to javafx.fxml;

  exports app;
}