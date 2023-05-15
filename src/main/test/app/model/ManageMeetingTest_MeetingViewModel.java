package app.model;

import app.server.ServerImplementation;
import app.shared.Communicator;
import app.shared.Meeting;
import app.viewmodel.MeetingViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ManageMeetingTest_MeetingViewModel
{
  private MeetingViewModel viewModel;
  private ModelManager modelManager;
  private Meeting meeting;

  @BeforeEach void setUp() throws Exception {
    this.modelManager = Mockito.mock(ModelManager.class);
    this.viewModel = new MeetingViewModel(modelManager);

    this.meeting = new Meeting("Meeting3","test3",
        Date.valueOf(LocalDate.of(2023,6,5)),
        Time.valueOf(LocalTime.of(12,30,0)),
        Time.valueOf(LocalTime.of(15,45,0)), "example@gmail.com");

  }

  @Test void edit_a_meeting() throws Exception{
    // Know it's show an error because the method doesn't exist
    //viewModel.editMeeting(meeting,meeting);
    Mockito.verify(modelManager,Mockito.times(1)).editMeeting(meeting,meeting);

  }

  @Test void test_if_a_meeting_is_removed() throws Exception{
    viewModel.removeMeeting(meeting);
    Mockito.verify(modelManager,Mockito.times(1)).removeMeeting(meeting);
  }

  @Test void getMeetings(){
    viewModel.getMeetings();
    Mockito.verify(modelManager,Mockito.times(2)).getMeetings();
  }
}
