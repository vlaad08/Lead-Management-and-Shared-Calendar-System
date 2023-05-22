package app.JDBC;

import app.shared.Meeting;
import org.mockito.MockedStatic;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SQLConnectionTest {
  private SQLConnection sqlConnection;

  @BeforeEach
  void setUp() throws SQLException
  {
    sqlConnection = Mockito.mock(SQLConnection.class);
    PreparedStatement statement = Mockito.mock(PreparedStatement.class);
    Connection connection = Mockito.mock(Connection.class);

    Mockito.when(sqlConnection.getMeetings()).thenReturn(new ArrayList<>(
        List.of(new Meeting("Meeting1", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
            Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le1@gmail.com"),
            new Meeting("Meeting2", "", Date.valueOf(LocalDate.of( 2023, 5, 22)),
                Time.valueOf(LocalTime.of(7, 0)), Time.valueOf(LocalTime.of(11,0)), "le2@gmail.com"))
    ));

  }

  @Test
  void sqlConnection_is_singleton() throws SQLException {


    try (MockedStatic<SQLConnection> mockedConnection = Mockito.mockStatic(SQLConnection.class)) {
      mockedConnection.when(SQLConnection::getInstance).thenReturn(sqlConnection);

      SQLConnection instance1 = SQLConnection.getInstance();
    SQLConnection instance2 = SQLConnection.getInstance();

    assertSame(sqlConnection, instance1);
    assertSame(sqlConnection, instance2);
  }
  }

  @Test
  void connection_is_existing() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, SQLException {
    SQLConnection sqlConnection = SQLConnection.getInstance();

    Method getConnectionMethod = SQLConnection.class.getDeclaredMethod("getConnection");
    getConnectionMethod.setAccessible(true);
    Connection connection = (Connection) getConnectionMethod.invoke(sqlConnection);

    assertNotNull(connection);
  }

  @Test
  void get_Meetings_gives_back_an_arraylist() throws SQLException
  {
    assertEquals(ArrayList.class,sqlConnection.getMeetings().getClass());
  }

  @Test
  void get_Meetings_gives_back_meetings() throws Exception
  {
    ArrayList<Meeting> meeting = sqlConnection.getMeetings();
    Mockito.verify(sqlConnection).getMeetings();
    assertEquals("Meeting1",meeting.get(0).getTitle());
    assertEquals("Meeting2",meeting.get(1).getTitle());

  }
}
