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




}