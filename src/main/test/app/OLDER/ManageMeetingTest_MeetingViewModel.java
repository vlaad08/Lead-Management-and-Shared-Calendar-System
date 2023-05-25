package app.OLDER;

import static org.mockito.Mockito.*;

import app.model.ModelManager;
import app.server.ServerImplementation;
import app.shared.Communicator;
import app.shared.Meeting;
import app.viewmodel.MeetingViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ManageMeetingTest_MeetingViewModel {
  private MeetingViewModel viewModel;
  private ModelManager modelManager;
  private Meeting meeting;

  @BeforeEach
  void setUp() {
    this.modelManager = mock(ModelManager.class);
    this.viewModel = Mockito.mock(MeetingViewModel.class);

    this.meeting = new Meeting("Meeting3","test3",
        Date.valueOf(LocalDate.of(2023,6,5)),
        Time.valueOf(LocalTime.of(12,30,0)),
        Time.valueOf(LocalTime.of(15,45,0)), "example@gmail.com");
  }

  @Test
  void should_edit_meeting() throws SQLException, RemoteException
  {

    ArrayList<String> emails = new ArrayList<String>();
    emails.add("agostonbabicz@gmail.com");
    emails.add("emanuelduca@gmail.com");

    viewModel.editMeeting(meeting, meeting,emails);
    verify(viewModel, times(1)).editMeeting(meeting, meeting,emails);

  }

  @Test
  void should_remove_meeting() throws SQLException, RemoteException
  {
    viewModel.removeMeeting(meeting);
    verify(viewModel, times(1)).removeMeeting(meeting);
  }

  @Test
  void should_get_meetings() {
    viewModel.getMeetings();
    verify(viewModel, times(1)).getMeetings();
  }
}
