package app.view;

import app.shared.Meeting;
import app.shared.Task;
import app.viewmodel.CalendarViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.*;
import java.util.*;
import java.util.List;

public class CalendarController implements PropertyChangeListener
{

  @FXML  public Label nameLabel;
  ZonedDateTime dateFocus;
    ZonedDateTime today;
    private ViewHandler viewHandler;
    private CalendarViewModel calendarViewModel;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    @FXML private Button closeButton;


    @FXML private Button tasksButton;
    @FXML private Button clientsButton;
    @FXML private Button availableClientsButton;
    @FXML private Button meetingButton;
    @FXML private Button leadButton;
    @FXML private Button manageLeadsButton;

    private Region root;



    public void init(ViewHandler viewHandler, CalendarViewModel calendarViewModel, Region root){

        this.calendarViewModel = calendarViewModel;
        this.viewHandler = viewHandler;
        this.root = root;
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();

        calendarViewModel.addPropertyChangeListener(this);
        calendarViewModel.bindUserName(nameLabel.textProperty());


        Draw.hoverButtonNavbar(availableClientsButton, leadButton, meetingButton, tasksButton, clientsButton, manageLeadsButton, closeButton);

    }


    public void onCloseButton()
    {
        viewHandler.close();
    }

    public Region getRoot(){
        return this.root;
    }





    @FXML public void changeView(ActionEvent e)
        {
            if(e.getSource().getClass() == Button.class)
            {
                Button b = (Button) e.getSource();

                switch (b.getText())
                {
                    case "Calendar", "Plans" -> viewHandler.openView("Calendar");
                    case "Manage meeting" -> viewHandler.openView("Meeting");
                    case "Manage task" -> viewHandler.openView("Task");
                    case "Lead", "Available Clients" -> viewHandler.openView("AvailableClients");
                    case "All Users" -> viewHandler.openView("AllUsers");
                    case "Manage leads" -> viewHandler.openView("Leads");
                }
            }
        }

    @FXML
    void backOneMonth() {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth() {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar(){
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        //List of activities for a given month
        Map<Integer, List<CalendarActivity>> calendarActivityMap = getCalendarActivitiesMonth(dateFocus);

        int monthMaxDate = dateFocus.getMonth().maxLength();
        //Check for leap year
        if(dateFocus.getYear() % 4 != 0 && monthMaxDate == 29){
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1,0,0,0,0,dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.valueOf("#e2e0eb"));
                rectangle.setStyle("-fx-background-radius: 20px");
                rectangle.setArcHeight(20);
                rectangle.setArcWidth(20);
                rectangle.setStroke(Color.valueOf("#544997"));
                rectangle.setStrokeWidth(strokeWidth + 1);
                double rectangleWidth =(calendarWidth/7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight/6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j+1)+(7*i);
                if(calculatedDate > dateOffset){
                    int currentDate = calculatedDate - dateOffset;
                    if(currentDate <= monthMaxDate){
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        List<CalendarActivity> calendarActivities = calendarActivityMap.get(currentDate);
                        if(calendarActivities != null){
                            createCalendarActivity(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if(today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate){
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActivityBox = new VBox();
        VBox smallBox = new VBox(5);
        ScrollPane scrollPane = new ScrollPane();
        calendarActivityBox.setPadding(new Insets(2));
        calendarActivityBox.setStyle("-fx-background-radius: 10px;");
        for (CalendarActivity activity : calendarActivities) {

            TextField text = new TextField();
            text.setEditable(false);
            text.setCursor(Cursor.DEFAULT);
            text.setMaxWidth(rectangleWidth);
            text.setMaxHeight(rectangleHeight);

            if(activity.getMeeting() != null)
            {
                text.setText(activity.getMeeting().toString());
            }
            if(activity.getTask() != null)
            {
                text.setText(activity.getTask().toString());
            }


            smallBox.getChildren().add(text);
            text.setCursor(Cursor.DEFAULT);
            text.setStyle("-fx-background-color: none");
            text.setOnMouseClicked(event -> Draw.drawCalendarActivityPopUp(calendarActivities));
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color: white");
        calendarActivityBox.setOnMouseClicked(event ->
            Draw.drawCalendarActivityPopUp(calendarActivities)
        );

        scrollPane.setContent(smallBox);
        calendarActivityBox.getChildren().add(scrollPane);

        stackPane.getChildren().add(calendarActivityBox);
    }

    private Map<Integer, List<CalendarActivity>> createCalendarMap(List<CalendarActivity> calendarActivities) {
        Map<Integer, List<CalendarActivity>> calendarActivityMap = new HashMap<>();

        for (CalendarActivity activity: calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if(!calendarActivityMap.containsKey(activityDate)){
                calendarActivityMap.put(activityDate, List.of(activity));
            } else {
                List<CalendarActivity> OldListByDate = calendarActivityMap.get(activityDate);

                List<CalendarActivity> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                calendarActivityMap.put(activityDate, newList);
            }
        }
        return  calendarActivityMap;
    }

    private Map<Integer, List<CalendarActivity>> getCalendarActivitiesMonth(ZonedDateTime dateFocus) {
        List<CalendarActivity> calendarActivities = new ArrayList<>();


        if(calendarViewModel.getMeetings() != null)
        {
            ArrayList<Meeting> meetings = calendarViewModel.getMeetings();
            for (Meeting meeting : meetings) {
                LocalDateTime localDateTime = LocalDateTime.of(meeting.getDate().toLocalDate(), meeting.getStartTime().toLocalTime());
                ZonedDateTime time = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(),
                    localDateTime.getDayOfMonth(), localDateTime.getHour(),
                    localDateTime.getMinute(), 0,0,dateFocus.getZone());
                if(localDateTime.getYear() == time.getYear() && localDateTime.getMonth().getValue() == time.getMonthValue() &&
                    localDateTime.getDayOfMonth() == time.getDayOfMonth() && localDateTime.getHour() == time.getHour() && localDateTime.getMinute() == time.getMinute())
                {
                    calendarActivities.add(new CalendarActivity(time, meeting));
                }
            }
        }
        if(calendarViewModel.getTasks() != null)
        {
            ArrayList<Task> tasks = calendarViewModel.getTasks();
            for(Task task : tasks)
            {
                LocalDateTime localDateTime = LocalDateTime.of(task.getDate().toLocalDate(), LocalTime.of(0, 0));
                ZonedDateTime time = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), localDateTime.getDayOfMonth(),
                    localDateTime.getHour(), localDateTime.getMinute(), 0, 0, dateFocus.getZone());
                if(localDateTime.getYear() == time.getYear() && localDateTime.getMonth().getValue() == time.getMonthValue() &&
                    localDateTime.getDayOfMonth() == time.getDayOfMonth() && localDateTime.getHour() == time.getHour() && localDateTime.getMinute() == time.getMinute())
                {
                    calendarActivities.add(new CalendarActivity(time, task));
                }
            }
        }

        return createCalendarMap(calendarActivities);
    }

    @Override public void propertyChange(PropertyChangeEvent evt)
    {
        if(evt.getPropertyName().equals("reloadCalendar"))
        {
          for (Node node : calendar.getChildren())
          {
            Platform.runLater(() -> calendar.getChildren().remove(node));
          }
            Platform.runLater(this::drawCalendar);
        }
    }
}