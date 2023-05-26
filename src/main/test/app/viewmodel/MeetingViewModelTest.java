package app.viewmodel;

import app.model.Model;
import app.model.ModelManager;
import app.shared.Task;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import app.shared.*;
import org.checkerframework.checker.units.qual.A;
import org.mockito.*;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.api.mockito.PowerMockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.model.Model;

public class MeetingViewModelTest
{

  private Model model;
  private MeetingViewModel meetingViewModel;
  private ObjectProperty<ObservableList<Meeting>> meetings;
  private PropertyChangeSupport support;
  private SimpleStringProperty name;
  private  Field supportField;

  @BeforeEach
  void setUp() throws NoSuchFieldException, IllegalAccessException
  {
    User user=new User("","","","","",false,"",1122);
    model = Mockito.mock(ModelManager.class);
    name = new SimpleStringProperty("");
    meetings =  Mockito.mock(ObjectProperty.class);
    Mockito.when(model.getLoggedInUser()).thenReturn(user);
    meetingViewModel = new MeetingViewModel(model);
    support = Mockito.mock(PropertyChangeSupport.class);


    supportField = meetingViewModel.getClass().getDeclaredField("support");
    supportField.setAccessible(true);
    supportField.set(meetingViewModel,support);
  }

  @Test
  void constructor_sets_property_change_support()
  {
    assertNotNull(support);
  }

  @Test
  void constructor_sets_property_change_listener()
  {
    Mockito.verify(model,Mockito.times(1)).addPropertyChangeListener(meetingViewModel);
  }

  @Test
  void constructor_sets_name_field()
  {
    assertEquals(model.getLoggedInUser().getFirstName(), name.getName());
  }

  @Test
  void constructor_sets_meetings_field(){
    Mockito.verify(model).getList("meetings");
  }

  @Test
  void bind_user_name_binds_property() {
    StringProperty property = Mockito.mock(SimpleStringProperty.class);

    meetingViewModel.bindUserName(property);

    Mockito.verify(property).bindBidirectional(ArgumentMatchers.any(StringProperty.class));
  }

  @Test
  void is_manager_returns_model_is_manager() {
    Mockito.when(model.isManager()).thenReturn(true);

    assertTrue(meetingViewModel.isManager());

    Mockito.verify(model).isManager();
  }

  @Test
  void add_property_change_listener_adds_listener_to_support() {
    PropertyChangeListener listener = Mockito.mock(PropertyChangeListener.class);

    meetingViewModel.addPropertyChangeListener(listener);

    Mockito.verify(support).addPropertyChangeListener(ArgumentMatchers.eq(listener));
  }

  @Test
  void bind_meetings_binds_property() {
    ObjectProperty<ObservableList<Meeting>> property = Mockito.mock(ObjectProperty.class);

    meetingViewModel.bindMeetings(property);

    Mockito.verify(property).bindBidirectional(ArgumentMatchers.any(ObjectProperty.class));
  }

  @Test
  void add_meeting_calls_model_add_object() throws SQLException, RemoteException {
    ArrayList<String> array = new ArrayList<>();
    array.add("example@gmail.com");

    meetingViewModel.addMeeting("title", "description", new Date(3900-1900,2,2), new Time(10,10,10), new Time(11,11,11), "leademail@gmail.com", array);

    Mockito.verify(model).addObject(Mockito.any(Meeting.class), Mockito.eq(array));
  }

  @Test
  void edit_meeting_calls_model_edit_object_with_list() throws SQLException, RemoteException {
    Meeting oldMeeting = new Meeting("Old Title", "Old Description", new Date(3900-1900,2,2), new Time(10,10,10), new Time(11,11,11), "lead1@example.com");
    Meeting newMeeting = new Meeting("New Title", "New Description", new Date(3900-1900,2,2), new Time(10,10,10), new Time(11,11,11), "lead2@example.com");
    ArrayList<String> emails = new ArrayList<>();

    meetingViewModel.editMeeting(oldMeeting, newMeeting, emails);

    Mockito.verify(model).editObjectWithList(oldMeeting, newMeeting, emails);
  }

  @Test
  void remove_meeting_removes_meeting() throws SQLException, RemoteException {
    Meeting meeting = new Meeting("Title", "Description",new Date(3900-1900,2,2), new Time(10,10,10), new Time(11,11,11), "lead@example.com");

    meetingViewModel.removeMeeting(meeting);

    Mockito.verify(model).removeObject(meeting);
  }

  @Test
  void get_meetings_returns_an_arraylist_of_meetings() {
    Meeting m1 = new Meeting("Title", "Description", new Date(3900-1900,2,2),new Time(10,10,10), new Time(11,11,11), "lead@example.com");
    Meeting m2 = new Meeting("Title2", "Description2", new Date(3900-1900,2,2),new Time(12,12,12), new Time(13,13,13), "lead2@example.com");

    ArrayList<Meeting> meetings = new ArrayList<>();
    meetings.add(m1);
    meetings.add(m2);

    Mockito.when(model.getList("meetings")).thenReturn(new ArrayList<>(meetings));

    ArrayList<Meeting> result = meetingViewModel.getMeetings();

    Mockito.verify(model,Mockito.times(2)).getList("meetings");

    List<Meeting> expectedMeetings = Arrays.asList(m1, m2);
    assertEquals(result, expectedMeetings);
  }

  @Test
  void get_leads_returns_leads() {
    Lead l1 = new Lead("1John", "","1Doe","1email@gmail.com","123","1title",1,"1bname","To do");
    Lead l2 = new Lead("2John", "","2Doe","2email@gmail.com","123","2title",1,"2bname","To do");

    ArrayList<Lead> leads = new ArrayList<>();
    leads.add(l1);
    leads.add(l2);

    Mockito.when(model.getList("leads")).thenReturn(new ArrayList<>(leads));

    ArrayList<Lead> result = meetingViewModel.getLeads();

    Mockito.verify(model).getList("leads");

    List<Lead> expectedLeads = Arrays.asList(l1,l2);
    assertEquals(expectedLeads, result);
  }

  @Test
  void get_users_returns_list_of_users() {
    User u = new User("John", "", "Doe", "john@example.com", "123456",true,"street",123);

    ArrayList<User> users = new ArrayList<>();
    users.add(u);

    Mockito.when(model.getList("users")).thenReturn(new ArrayList<>(users));

    ArrayList<User> result = meetingViewModel.getUsers();

    Mockito.verify(model).getList("users");

    List<User> expectedUsers = Arrays.asList(u);
    assertEquals(expectedUsers, result);

  }

  @Test
  void get_available_users_returns_list_of_available_users() throws SQLException,RemoteException {
    User u = new User("John", "", "Doe", "john@example.com", "123456",true,"street",123);

    ArrayList<User> users = new ArrayList<>();
    users.add(u);

    Mockito.when(model.getList(new Date(3900-1900,2,2), new Time(10,10,10), new Time(11,11,11))).thenReturn(new ArrayList<>(users));

    ArrayList<User> result = meetingViewModel.getAvailableUsers(new Date(3900-1900,2,2), new Time(10,10,10), new Time(11,11,11));

    Mockito.verify(model).getList(new Date(3900-1900,2,2), new Time(10,10,10), new Time(11,11,11));

    assertNotNull(result);
    assertEquals(users.size(), result.size());
    assertEquals(users.get(0), result.get(0));
  }

  @Test
  void get_attendance_returns_list_of_attendance() {
    Meeting meeting = new Meeting("Title", "Description",new Date(3900-1900,2,2), new Time(10,10,10), new Time(11,11,11), "lead@example.com");

    ArrayList<String> attendance = new ArrayList<>();
    attendance.add("john@example.com");

    Mockito.when(model.getList(meeting)).thenReturn(new ArrayList<>(attendance));

    ArrayList<String> result = meetingViewModel.getAttendance(meeting);

    Mockito.verify(model).getList(meeting);

    assertNotNull(result);
    assertEquals(attendance.size(), result.size());
    assertEquals(attendance.get(0), result.get(0));
  }

  @Test
  void property_change_reload_meetings_updates_meetings_list() {
    Meeting meeting = new Meeting("Title", "Description",new Date(3900-1900,2,2), new Time(10,10,10), new Time(11,11,11), "lead@example.com");
    ObservableList<Meeting> meetingList = FXCollections.observableArrayList(meeting);
    meetings.set(meetingList);

    PropertyChangeEvent reloadMeetingsEvent = new PropertyChangeEvent(this, "reloadMeetings", false, true);

    meetingViewModel.propertyChange(reloadMeetingsEvent);

    Mockito.verify(meetings).set(Mockito.any(ObservableList.class));
    Mockito.verify(support).firePropertyChange("reloadMeetings", false, true);
  }

  @Test
  void propertyChange_reloadLoggedInUser_updatesName() {
    name.set("vlad");
    Meeting meeting = new Meeting("Title", "Description",new Date(3900-1900,2,2), new Time(10,10,10), new Time(11,11,11), "lead@example.com");
    ObservableList<Meeting> meetingList = FXCollections.observableArrayList(meeting);
    meetings.set(meetingList);

    PropertyChangeEvent reloadLoggedInUserEvent = new PropertyChangeEvent(this, "reloadLoggedInUser", false, true);

    meetingViewModel.propertyChange(reloadLoggedInUserEvent);
    assertEquals(name.getName(), model.getLoggedInUser().getFirstName());
  }

}
