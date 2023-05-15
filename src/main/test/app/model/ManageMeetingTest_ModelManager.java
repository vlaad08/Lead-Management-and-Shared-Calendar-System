package app.model;

import app.server.ServerImplementation;
import app.shared.Communicator;
import app.shared.Meeting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;


import static org.junit.jupiter.api.Assertions.*;

public class ManageMeetingTest_ModelManager
{
  private ModelManager modelManager;
  private Communicator server;
  private Meeting meeting;

  @BeforeEach void setUp() throws Exception{
      this.server = Mockito.mock(ServerImplementation.class);
      this.modelManager = new ModelManager(server);

      this.meeting = new Meeting("Meeting3","test3",
        Date.valueOf(LocalDate.of(2023,6,5)),
        Time.valueOf(LocalTime.of(12,30,0)),
        Time.valueOf(LocalTime.of(15,45,0)), "example@gmail.com");

  }

  @Test void add_a_meeting_to_database() throws Exception{
    modelManager.addMeeting(meeting.title(),meeting.description(),meeting.date(),meeting.startTime(),meeting.endTime(),meeting.email());
    Mockito.verify(server,Mockito.times(1)).createMeeting(meeting);
  }

  @Test void create_a_meeting_and_get_back_from_database() throws Exception{
    modelManager.addMeeting(meeting.title(),meeting.description(),meeting.date(),meeting.startTime(),meeting.endTime(),meeting.email());
    modelManager.getMeetings();
    Mockito.verify(server,Mockito.times(2)).getMeetings();
  }

  @Test void edit_a_meeting() throws Exception{
    modelManager.editMeeting(meeting,meeting);
    Mockito.verify(server,Mockito.times(1)).removeMeeting(meeting);
    Mockito.verify(server,Mockito.times(1)).createMeeting(meeting);
    // Should bee
    // Mockito.verify(communicator,Mockito.times(1)).editMeeting(meeting,meeting);
  }

  @Test void test_if_a_meeting_is_removed() throws Exception{
    modelManager.removeMeeting(meeting);
    Mockito.verify(server, Mockito.times(1)).removeMeeting(meeting);
  }

  @Test void test_if_a_null_object_is_removed_throw_exception(){
    assertThrows(NullPointerException.class, ()->{
      modelManager.removeMeeting(null);
    });
  }

  @Test void getMeetings() throws Exception{
    modelManager.getMeetings();
    Mockito.verify(server,Mockito.times(2)).getMeetings();
  }
}
