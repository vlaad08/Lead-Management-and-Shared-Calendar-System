module cl.sep2 {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires remoteobserver;
  requires java.rmi;
  requires org.postgresql.jdbc;
  requires java.sql;

  opens app to javafx.fxml;
  opens app.view to javafx.fxml;
  opens app.shared to java.rmi;

  exports app;
}