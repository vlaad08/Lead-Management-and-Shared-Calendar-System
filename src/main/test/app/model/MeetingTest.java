package app.model;

import app.JDBC.SQLConnection;
import app.model.ClientListener;
import app.model.Model;
import app.model.ModelManager;
import app.model.User;
import app.shared.Meeting;
import app.view.MeetingController;
import app.viewmodel.MeetingViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.sql.Date;
import java.util.Random;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MeetingTest {

  private MeetingViewModel meetingViewModel;

  //@InjectMocks
  //private MeetingController meetingController;
  private ClientListener listener;


  @BeforeEach
  public void setUp() throws Exception{
    Registry registry = LocateRegistry.getRegistry(1099);
    Server server = (Server) registry.lookup("communicator");
    ClientListener listener = new ClientListener(server);

    ModelManager model = new ModelManager(listener);
    this.meetingViewModel = new MeetingViewModel(model);
  }

  @Test
  void a_new_database_is_empty(){
    when(meetingViewModel.getMeetings()).thenReturn(new ArrayList<Meeting>());
    assertTrue(meetingViewModel.getMeetings().isEmpty());
  }

  @Test
  void check_getMeetings_with_Mockito(){
    Meeting meeting = new Meeting("Meeting3","test3",
        Date.valueOf(LocalDate.of(2023,6,5)),
        Time.valueOf(LocalTime.of(12,30,0)),
        Time.valueOf(LocalTime.of(15,45,0)), "example@gmail.com");
    ArrayList<Meeting> meetings = new ArrayList<>();
    meetings.add(meeting);

    when(meetingViewModel.getMeetings()).thenReturn(meetings);

    assertEquals(1, meetingViewModel.getMeetings().size());
  }

  /*--------------------------------------------------------------ZOMBIE--------------------------------------------------------------*/


  /*-------------------------------------------------------BUTTON FUNCTIONALITY-------------------------------------------------------*/
  @Test
  void test_if_add_meeting_adds_a_meeting_in_real_database() throws SQLException
  {
    Meeting meeting = new Meeting("Meeting2","test",
        Date.valueOf(LocalDate.of(2023,6,5)),
        Time.valueOf(LocalTime.of(10,30,0)),
        Time.valueOf(LocalTime.of(10,45,0)), "example@gmail.com");

    meetingViewModel.addMeeting(meeting.title(),meeting.description(),meeting.date(),meeting.startTime(),meeting.endTime(),meeting.email());
    assertEquals(true,meetingViewModel.getMeetings().contains(meeting));
  }

  /* //Should test Conttroler
  @Test
  void test_if_add_meeting_button_displays_a_popup() {
    Meeting meeting = new Meeting("Meeting3","test3",
        Date.valueOf(LocalDate.of(2069,6,5)),
        Time.valueOf(LocalTime.of(12,30,0)),
        Time.valueOf(LocalTime.of(15,45,0)), "example@gmail.com");
    ArrayList<Meeting> meetings = new ArrayList<>();
    meetings.add(meeting);

    when(meetingViewModel.getMeetings()).thenReturn(meetings);
    meetingViewModel.addMeeting(meeting.title(),meeting.description(),meeting.date(),meeting.startTime(),meeting.endTime(),meeting.email());

    Mockito.verify(meetingViewModel,Mockito.times(1).addMeeting());

    //assertTrue(meetingController.addMeeting());
  }
   */

  /*
  /*-------------------------------------------------------------------------------------------------------------------------------------*/



  /*---------------------------------------------------------USE CASE EXCEPTIONS---------------------------------------------------------*/

  @Test
  void test_if_no_meetings_are_displayed_when_there_are_none() {

    Mockito.when(meetingViewModel.getMeetings()).thenReturn(new ArrayList<>());
    assertEquals(new ArrayList<>(), meetingViewModel.getMeetings());
  }


  @Test
  void when_a_meeting_is_created_size_is_increasing_and_database_is_returning_that_object() throws SQLException
  {
    int i = meetingViewModel.getMeetings().size();
    Random random = new Random();
    String title = "Meeting"+random.nextInt(1000);
    Meeting meeting = new Meeting(title,"test4",
        Date.valueOf(LocalDate.of(2069,6,5)),
        Time.valueOf(LocalTime.of(12,30,0)),
        Time.valueOf(LocalTime.of(15,45,0)), "example@gmail.com");

    meetingViewModel.addMeeting(meeting.title(),meeting.description(),meeting.date(),meeting.startTime(),meeting.endTime(),meeting.email());

    assertTrue(meetingViewModel.getMeetings().contains(meeting));
    assertEquals((i+1),meetingViewModel.getMeetings().size());
  }

  @Test
  void test_if_meeting_can_be_created_with_end_time_before_start_time() {

    Meeting meeting = new Meeting("Iligal_argument","test4",
        Date.valueOf(LocalDate.of(2069,6,5)),
        Time.valueOf(LocalTime.of(16,30,0)),
        Time.valueOf(LocalTime.of(12,45,0)), "example@gmail.com");
    assertThrows(IllegalArgumentException.class, ()->{
      meetingViewModel.addMeeting(meeting.title(),meeting.description(),meeting.date(),meeting.startTime(),meeting.endTime(),meeting.email());
    });
    assertFalse(meetingViewModel.getMeetings().contains(meeting));
  }

  /*
  @Test
  void test_if_meeting_can_be_created_with_enough_employees()
      throws SQLException
  {
    Date day = Date.valueOf(LocalDate.of(2069,9,11));
    Time startTime = new Time(10);
    Time endTime = new Time(11);
    User user1 = new User("JohnDoe123", "123asd", "asd@gmail.com", "John", "Doe", true);
    //User user2 = new User("JaneDoe123", "dsa321", "dsa@gmail.com", "Jane", "Doe", false);
    Meeting mockMeeting = new Meeting("meeting","",day,startTime,endTime);
    mockMeeting.addUser(user1);
    // mockMeeting.addUser(user2);

    Mockito.when(meetingViewModel.addMeeting("meeting","",day,startTime,endTime));

    meetingController.addMeeting();

    //Mockito.verify(meetingController, Mockito.times(1)).showAddMeetingPopup();

    //meetingController.handleSaveMeeting(mockMeeting);

    Mockito.verify(meetingViewModel, Mockito.times(1)).createMeeting);

    Mockito.verify(meetingController, Mockito.times(1)).init();


  }

  @Test
  void test_if_meeting_can_be_created_without_enough_employees()
      throws SQLException
  {
    Date day = Date.valueOf(LocalDate.of(2069,9,11));
    Time startTime = new Time(10);
    Time endTime = new Time(11);
    //User user = new User("JohnDoe123", "123asd", "asd@gmail.com", "John", "Doe", true);
    Meeting mockMeeting = new Meeting("meeting","",day,startTime,endTime);
    //mockMeeting.addUser(user);

    Mockito.when(meetingViewModel.addMeeting("meeting","",day,startTime,endTime)).thenReturn(true);

    meetingController.addMeeting();

    //Mockito.verify(meetingController, Mockito.times(1)).showAddMeetingPopup();

    //meetingController.handleSaveMeeting(mockMeeting);

    Mockito.verify(meetingViewModel, Mockito.never()).createMeeting();

    Mockito.verify(meetingController, Mockito.never()).init();

    Mockito.verify(meetingController, Mockito.times(1)).showErrorMessage();
    //assertTrue(meetingController.showErrorMessage);

  }

  @Test
  void test_if_description_is_null_when_user_doesnt_add_any() {
    Date day = Date.valueOf(LocalDate.of(2069,9,11));
    Time startTime = new Time(10);
    Time endTime = new Time(11);;
    User user1 = new User("JohnDoe123", "123asd", "asd@gmail.com", "John", "Doe", true);
    Meeting mockMeeting = new Meeting("meeting","",day,startTime,endTime);
    mockMeeting.addUser(user1);

    assertNull(mockMeeting.getDescription());
  }

  /*----------------------------------------------------------------------------------------------------------------------------------*/



  /*-------------------------------------------------------TESTS FOR PERMISSION-------------------------------------------------------*/
  /*
  @Test
  void test_if_manager_can_add_meeting() {
    // set up mock data
    User user = new User("JohnDoe123", "123asd", "asd@gmail.com", "John", "Doe", true);

    // assert that the manager tile is enabled
    assertTrue(meetingController.addMeetingIsClickable);
  }

  @Test
  void test_if_employee_can_add_meeting(){
    User user = new User("JohnDoe123", "123asd", "asd@gmail.com", "John", "Doe", false);

    assertFalse(meetingController.addMeetingIsClickable);
  }

   */

}
/*-----------------------------------------------------------------------------------------------------------------------------------*/
