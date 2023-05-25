package app.server;

import app.JDBC.SQLConnection;
import app.shared.Address;
import dk.via.remote.observer.RemotePropertyChangeListener;
import dk.via.remote.observer.RemotePropertyChangeSupport;
import org.mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class TestServerImplementation
{


  @Mock
  RemotePropertyChangeSupport<String> support;
  @Mock
  SQLConnection sqlConnection;
  @InjectMocks
  ServerImplementation serverImplementation;

  private Field supportField;

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException {
    MockitoAnnotations.openMocks(this);

    supportField = serverImplementation.getClass().getDeclaredField("support");
    supportField.setAccessible(true);
    supportField.set(serverImplementation, support);
  }

  @Test
  void server_implementation_is_created() {
    assertNotNull(supportField);
  }

  @Test
  void listener_is_added() throws RemoteException {
    RemotePropertyChangeListener<String> listener = Mockito.mock(RemotePropertyChangeListener.class);

    serverImplementation.addListener(listener);
    Mockito.verify(serverImplementation).addListener(listener);
    Mockito.verify(support).addPropertyChangeListener(listener);
  }

  @Test
  void create_address_if_not_exists() throws SQLException, RemoteException {
    Address address = new Address("DummyStreet", "DummyCity", "DummyCountry", 123);

    serverImplementation.addObject(address);
    Mockito.verify(serverImplementation).addObject(address);

    Mockito.when(sqlConnection.getAddress(address)).thenReturn(null);

    serverImplementation.addObject(address);

    Address a = sqlConnection.getAddress(address);

    Mockito.verify(sqlConnection).getAddress(address);
    assertNotEquals(a, address);

    sqlConnection.createAddress(address);
    Mockito.verify(sqlConnection).createAddress(address);
  }

  @Test
  void create_lead() throws SQLException, RemoteException {
  }
}
