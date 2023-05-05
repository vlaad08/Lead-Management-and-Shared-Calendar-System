import app.model.Employee;
import app.model.User;
import app.shared.Meeting;
import app.view.MeetingController;
import app.viewmodel.MeetingViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.Mockito.mock;

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

  @Test
  public void test_if_add_meeting_adds_a_meeting() {
    // set up of mock data
    Date startTime = new Date();
    Date endTime = new Date(startTime.getTime() + 60 * 60 * 1000);
    String description = "meeting description asdasdasdasdasdasdasd";
    User user1 = new User("JohnDoe123","123asd","asd@gmail.com","John","Doe",true);
    User user2 = new User("JaneDoe123","123asd","asdasd@gmail.com","Jane","Doe",false);
    Meeting mockMeeting = new Meeting(startTime, endTime, description);
    mockMeeting.addUser(user1);
    mockMeeting.addUser(user2);
      /*meetingController = Mockito.mock(MeetingController.class);
        meetingViewModel = Mockito.mock(MeetingViewModel.class); */

    // stub the behavior of the meetingViewModel
    Mockito.when(meetingViewModel.addMeeting(Mockito.any())).thenReturn(true);

    // simulate user clicking the '+' button to add a new meeting assuming method works
    meetingController.handleAddMeeting();

    // verify that a pop up is displayed to enter meeting details, assuming method works
    Mockito.verify(meetingController, Mockito.times(1)).showAddMeetingPopup();

    // simulate user entering meeting details and clicking 'save', assuming method is correct
    meetingController.handleSaveMeeting(mockMeeting);

    // verify that the meetingViewModels 'addMeeting' method is called with the correct arguments
    Mockito.verify(meetingViewModel, Mockito.times(1)).addMeeting(mockMeeting);

    // verify that the meeting view is updated with the new meeting
    Mockito.verify(meetingController, Mockito.times(1)).updateMeetingView();

    //assert equals if the only one i n the list is the one we just added
  }

  @Test
  public void test_if_add_meeting_button_displays_a_popup() {
    // set up mock data
    Date startTime = new Date();
    Date endTime = new Date(startTime.getTime() + 60 * 60 * 1000);
    String description = "meeting description asdasdasdasdasdasdasd";
    Meeting mockMeeting = new Meeting(startTime, endTime, description);//PLUS EMPLOYEE VALIDATION

    // stub the behavior of the meetingViewModel
    Mockito.when(meetingViewModel.getMeetings()).thenReturn(Arrays.asList(mockMeeting));

    // simulate user clicking on the first meeting tile
    meetingController.handleMeetingTileClick(0); //i assume this is how itll be done

    // verify that a popup is displayed showing the meeting details
    Mockito.verify(meetingController, Mockito.times(1)).showMeetingDetailsPopup(mockMeeting);

    assertTrue(meetingController.showMeetingDetailsPopup);
  }

  @Test
  public void test_if_meeting_can_be_created_with_wrong_details() {
    // set up mock data
    Date startTime = new Date();
    Date endTime = new Date(startTime.getTime() - 60 * 60 * 1000); // end time after start time
    User user1 = new User("JohnDoe123","123asd","asd@gmail.com","John","Doe",true);
    User user2 = new User("JaneDoe123","123asd","asdasd@gmail.com","Jane","Doe",false);
    Meeting mockMeeting = new Meeting(startTime, endTime, "");
    mockMeeting.addUser(user1);
    mockMeeting.addUser(user2);

    // stub the behavior of the meetingViewModel
    Mockito.when(meetingViewModel.addMeeting(Mockito.any())).thenReturn(false);

    // simulate user clicking the '+' button to add a new meeting
    meetingController.handleAddMeeting();

    // verify that a popup is displayed to enter meeting details
    Mockito.verify(meetingController, Mockito.times(1)).showAddMeetingPopup();

    // simulate user entering invalid meeting details and clicking 'Save'
    meetingController.handleSaveMeeting(mockMeeting);

    // verify that the meetingViewModels 'addMeeting' method is not called
    Mockito.verify(meetingViewModel, Mockito.never()).addMeeting(mockMeeting);

    // verify that the meeting view is not updated with the wrong meeting
    Mockito.verify(meetingController, Mockito.never()).updateMeetingView();

    // verify that an error message is displayed to the user
    Mockito.verify(meetingController, Mockito.times(1)).showErrorMessage();

    assertTrue(meetingController.showErrorMessage);
  }

  @Test
  public void test_if_meeting_can_be_created_without_enough_employees() {
    // set up mock data
    Date startTime = new Date();
    Date endTime = new Date(startTime.getTime() + 60 * 60 * 1000); // end time after start time
    User user = new User("JohnDoe123", "123asd", "asd@gmail.com", "John", "Doe", true);
    Meeting mockMeeting = new Meeting(startTime, endTime, "");
    mockMeeting.addUser(user);

    // stub the behavior of the meetingViewModel
    Mockito.when(meetingViewModel.addMeeting(Mockito.any())).thenReturn(true);

    // simulate user clicking the '+' button to add a new meeting
    meetingController.handleAddMeeting();

    // verify that a popup is displayed to enter meeting details
    Mockito.verify(meetingController, Mockito.times(1)).showAddMeetingPopup();

    // simulate user entering valid meeting details and clicking 'Save'
    meetingController.handleSaveMeeting(mockMeeting);

    // verify that the meetingViewModels 'addMeeting' method is not called
    Mockito.verify(meetingViewModel, Mockito.never()).addMeeting(mockMeeting);

    // verify that the meeting view is not updated with the wrong meeting
    Mockito.verify(meetingController, Mockito.never()).updateMeetingView();

    // verify that an error message is displayed to the user
    Mockito.verify(meetingController, Mockito.times(1)).showErrorMessage();
    assertTrue(meetingController.showErrorMessage);
  }


  @Test
  public void test_if_employee_can_create_meeting() {
    // set up mock data
    User user = new User("JohnDoe123", "123asd", "asd@gmail.com", "John", "Doe", false);

    // assert that the manager tile is enabled
    assertFalse(meetingController.addMeetingIsClickable);
  }

  @Test
  public void test_if_only_manager_can_add_meeting() {
    // set up mock data
    User user = new User("JohnDoe123", "123asd", "asd@gmail.com", "John", "Doe", true);

    // assert that the manager tile is enabled
    assertTrue(meetingController.addMeetingIsClickable);
  }


}


