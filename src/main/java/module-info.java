module cl.sep2 {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires remoteobserver;
  requires java.rmi;
  requires powermock.api.mockito2;
  requires org.postgresql.jdbc;
  requires java.sql;
  requires java.base;


  opens app to javafx.fxml;
  opens app.view to javafx.fxml;
  opens app.JDBC to java.base;



  exports app;
  exports app.JDBC;
  exports app.view;
  exports app.viewmodel;
  exports app.shared;
  exports app.model;

  opens app.shared to java.rmi, javafx.fxml;
}