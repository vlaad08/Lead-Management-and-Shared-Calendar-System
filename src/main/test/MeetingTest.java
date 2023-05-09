import app.model.User;
import app.shared.Meeting;
import app.view.MeetingController;
import app.viewmodel.MeetingViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

public class MeetingTest {

  @Mock
  private MeetingViewModel meetingViewModel;

  @InjectMocks
  private MeetingController meetingController;

  @BeforeEach
  public void setUp() {
    //??
    //empty mock meeting list
  }


  /*--------------------------------------------------------------ZOMBIE--------------------------------------------------------------*/




  /*----------------------------------------------------------------------------------------------------------------------------------*/



  /*-------------------------------------------------------BUTTON FUNCTIONALITY-------------------------------------------------------*/
  @Test
  void test_if_add_meeting_adds_a_meeting() throws SQLException
  {
    Date day = Date.valueOf(LocalDate.of(2069,9,11));
    Time startTime = new Time(10);
    Time endTime = new Time(11);
    String description = "meeting description asdasdasdasdasdasdasd";
    User user1 = new User("JohnDoe123","123asd","asd@gmail.com","John","Doe",true);
    User user2 = new User("JaneDoe123","dsa321","dsa321@gmail.com","Jane","Doe",false);
    Meeting mockMeeting = new Meeting("meeting",description,day,startTime,endTime);
    mockMeeting.addUser(user1);
    mockMeeting.addUser(user2);
      /*meetingController = Mockito.mock(MeetingController.class);
        meetingViewModel = Mockito.mock(MeetingViewModel.class); */

    // stub the behavior of the meetingViewModel
    Mockito.when(meetingViewModel.addMeeting("meeting",description,day,startTime,endTime).thenReturn(true));//??

    // simulate user clicking the '+' button to add a new meeting assuming method works
    meetingController.addMeeting();

    assertTrue(meetingController.getMeetings().contains(mockMeeting));
  }

  @Test
  void test_if_add_meeting_button_displays_a_popup() {
    Date day = Date.valueOf(LocalDate.of(2069,9,11));
    Time startTime = new Time(10);
    Time endTime = new Time(11);
    String description = "meeting description asdasdasdasdasdasdasd";
    Meeting meeting = new Meeting("meeting",description,day,startTime,endTime);//PLUS EMPLOYEE VALIDATION

    Mockito.when(meetingViewModel.getMeetings()).thenReturn(
        (ArrayList<Meeting>) Arrays.asList(meeting));

    meetingController.addMeeting(); //i assume this is how itll be done

    Mockito.verify(meetingController,Mockito.times(1).addMeeting());

    //assertTrue(meetingController.addMeeting());
  }

  /*-------------------------------------------------------------------------------------------------------------------------------------*/



  /*---------------------------------------------------------USE CASE EXCEPTIONS---------------------------------------------------------*/

  @Test
  void test_if_no_meetings_are_displayed_when_there_are_none() {

    Mockito.when(meetingViewModel.getMeetings()).thenReturn(Collections.emptyList());

    meetingController.init();

    assertEquals(Collections.emptyList(), meetingController.getMeetings());
  }

  @Test
  void test_if_one_meeting_is_displayed_when_there_is_only_one()
      throws SQLException
  {

    Date day = Date.valueOf(LocalDate.of(2069,9,11));
    Time startTime = new Time(10);
    Time endTime = new Time(11);
    String description = "meeting description asdasdasdasdasdasdasd";
    Meeting meeting = new Meeting("meeting",description,day,startTime,endTime);

    meetingViewModel.addMeeting("meeting",description,day,startTime,endTime);

    Mockito.when(meetingViewModel.getMeetings()).thenReturn(Arrays.asList(meeting));

    meetingController.init();

    assertEquals(Arrays.asList(meeting), meetingController.getMeetings());
  }
  @Test
  void test_if_process_can_be_cancelled_at_any_time() {
    //Waiting for implementation
  }

  @Test       //right case of this is the first one where we check if a meeting can be created
  void test_if_meeting_can_be_created_with_end_time_before_start_time()
      throws SQLException
  {
    Date day = Date.valueOf(LocalDate.of(2069,9,11));
    Time startTime = new Time(11);
    Time endTime = new Time(10); // end time before start time
    User user1 = new User("JohnDoe123","123asd","asd@gmail.com","John","Doe",true);
    User user2 = new User("JaneDoe123","dsa321","dsa@gmail.com","Jane","Doe",false);
    Meeting mockMeeting = new Meeting("meeting","",day,startTime,endTime);
    mockMeeting.addUser(user1);
    mockMeeting.addUser(user2);

    Mockito.when(meetingViewModel.addMeeting("meeting","",day,startTime,endTime)).thenReturn(false);

    meetingController.addMeeting();

    //Mockito.verify(meetingController, Mockito.times(1)).drawAddMeetingPopup();

    //meetingController.handleSaveMeeting(mockMeeting);

    // addMeeting shouldnt be called
    Mockito.verify(meetingViewModel, Mockito.never()).createMeeting(mockMeeting);

    // view shouldnt update since details were wrong and meeting wasnt added
    Mockito.verify(meetingController, Mockito.never()).init();

    // show error
    Mockito.verify(meetingController, Mockito.times(1)).showErrorMessage();

    assertTrue(meetingController.showErrorMessage);
  }

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
}
/*-----------------------------------------------------------------------------------------------------------------------------------*/
