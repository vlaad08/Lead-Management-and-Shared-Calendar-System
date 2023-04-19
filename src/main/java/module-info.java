module cl.sep2 {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires remoteobserver;
  requires java.rmi;

  opens app to javafx.fxml;
  exports app;
}