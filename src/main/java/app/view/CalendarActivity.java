package app.view;

import app.shared.Meeting;
import app.shared.Task;

import java.time.ZonedDateTime;

public class CalendarActivity {
    private final ZonedDateTime date;
    private Meeting meeting;

    private Task task;

    public CalendarActivity(ZonedDateTime date, Meeting meeting) {
        this.date = date;
        this.meeting = meeting;
    }

    public CalendarActivity(ZonedDateTime date, Task task){
        this.date = date;
        this.task = task;
    }

    public Meeting getMeeting()
    {
        return meeting;
    }


    public Task getTask()
    {
        return task;
    }


    public ZonedDateTime getDate() {
        return date;
    }


/*
  The logic behind the CalendarController's and CalendarActivity's behaviour has been built upon
  and by using this open source code:
  GitHub,2023. Da9el00/Calendar.fxml [online]
  Available at:https://gist.github.com/Da9el00/f4340927b8ba6941eb7562a3306e93b6
  [Accessed 16 April 2023]
  */

}