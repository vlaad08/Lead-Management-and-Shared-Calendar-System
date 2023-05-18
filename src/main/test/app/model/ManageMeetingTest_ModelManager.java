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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManageMeetingTest_ModelManager
{
  private ModelManager modelManager;
  private Communicator server;
  private Meeting meeting;

  @BeforeEach void setUp() throws Exception{
    this.server = Mockito.mock(ServerImplementation.class);
    this.modelManager = Mockito.mock(ModelManager.class);

    this.meeting = new Meeting("New Meeting","some description",
        Date.valueOf(LocalDate.of(2023,5,17)),
        Time.valueOf(LocalTime.of(12,45,0)),
        Time.valueOf(LocalTime.of(15,0,0)), "lead@gmail.com");

  }

  @Test void add_a_meeting_to_database() throws Exception{
    ArrayList<String> emails = new ArrayList<String>();
    emails.add("example@gmail.com");
    modelManager.addMeeting(meeting.getTitle(),meeting.getDescription(),meeting.getDate(),meeting.getStartTime(),meeting.getEndTime(),meeting.getLeadEmail(),emails);
    Mockito.verify(modelManager,Mockito.times(1)).addMeeting(meeting.getTitle(),meeting.getDescription(),meeting.getDate(),meeting.getStartTime(),meeting.getEndTime(),meeting.getLeadEmail(),emails);
  }

  @Test void create_a_meeting_and_get_back_from_database() throws Exception{
    ArrayList<String> emails = new ArrayList<String>();
    emails.add("agostonbabicz@gmail.com");
    emails.add("emanuelduca@gmail.com");
    modelManager.addMeeting(meeting.getTitle(),meeting.getDescription(),meeting.getDate(),meeting.getStartTime(),meeting.getEndTime(),meeting.getLeadEmail(),emails);
    modelManager.getMeetings();
    Mockito.verify(server,Mockito.times(2)).getMeetings();
  }

  /*@Test void edit_a_meeting() throws Exception{
    ArrayList<String> emails = new ArrayList<String>();
    emails.add("agostonbabicz@gmail.com");
    emails.add("emanuelduca@gmail.com");
    modelManager.editMeeting(meeting,meeting,emails);
    Mockito.verify(server,Mockito.times(1)).removeMeeting(meeting);
    Mockito.verify(server,Mockito.times(1)).createMeeting(meeting);
    // Should bee
    // Mockito.verify(communicator,Mockito.times(1)).editMeeting(meeting,meeting);
  }*/
  @Test
  void edit_a_meeting() throws Exception {
    ArrayList<String> emails = new ArrayList<String>();
    emails.add("agostonbabicz@gmail.com");
    emails.add("emanuelduca@gmail.com");
    modelManager.editMeeting(meeting, meeting, emails);
    Mockito.verify(server, Mockito.times(1)).editMeeting(Mockito.eq(meeting), Mockito.eq(meeting));


  }

  @Test void test_if_a_meeting_is_removed() throws Exception{
    modelManager.removeMeeting(meeting);
    Mockito.verify(server, Mockito.times(1)).removeMeeting(meeting);
  }

  /*@Test
  void test_if_a_null_object_is_removed_throw_exception() {
    assertThrows(NullPointerException.class, () -> {
      modelManager.removeMeeting(null);
    });
    Mockito.verifyNoInteractions(server); // Verify that no interactions occurred with the server
  }*/



  @Test void getMeetings() throws Exception{
    modelManager.getMeetings();
    Mockito.verify(server,Mockito.times(2)).getMeetings();
  }
}