package app.view;

import app.shared.Meeting;
import app.viewmodel.CalendarViewModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class CalendarController {

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



    //THIS MAY BE ILLEGAL BUT ILL MAKE AN INIT HERE
    public void init(ViewHandler viewHandler, CalendarViewModel calendarViewModel, Region root){

        this.calendarViewModel = calendarViewModel;
        this.viewHandler = viewHandler;
        this.root = root;
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();



        Draw.hoverButtonNavbar(availableClientsButton, leadButton, meetingButton, tasksButton, clientsButton, manageLeadsButton, closeButton);

    }

    public void hoverButtonNavbar(Button b)
    {
        b.setOnMouseEntered(event -> {
            b.setStyle("-fx-background-color: #786FAC;");
        });
        b.setOnMouseExited(event -> {
            b.setStyle("-fx-background-color: none");
        });
    }

    public void onCloseButton()
    {
        viewHandler.close();
    }

    public Region getRoot(){
        return this.root;
    }



    //public void reset(){} //Ill just leave this here we may need this later for smt



    @FXML public void changeView(ActionEvent e)
        {
            if(e.getSource().getClass() == Button.class)
            {
                Button b = (Button) e.getSource();

                switch (b.getText())
                {
                    case "Calendar", "Plans" ->
                        viewHandler.openView("Calendar");
                    case "Manage meeting" -> viewHandler.openView("Meeting");
                    case "Manage task" -> viewHandler.openView("Task");
                    case "Lead", "Available Clients" ->
                        viewHandler.openView("AvailableClients");
                    case "All Clients" -> viewHandler.openView("AllClients");
                    case "Manage leads" -> viewHandler.openView("Leads");
                }
            }
        }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
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
        calendarActivityBox.setPadding(new Insets(2));
        calendarActivityBox.setStyle("-fx-background-radius: 10px;");
        for (int k = 0; k < calendarActivities.size(); k++) {
            if(k >= 2) {
                Text moreActivities = new Text("...");
                calendarActivityBox.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    //On ... click print all activities for given date
                    System.out.println(calendarActivities);
                });
                break;
            }
            Text text = new Text(calendarActivities.get(k).getClientName() + ", " + calendarActivities.get(k).getDate().toLocalTime());
            calendarActivityBox.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                System.out.println(text.getText());
            });
        }
        calendarActivityBox.setTranslateY((rectangleHeight / 2) * 0.20);
        calendarActivityBox.setMaxWidth(rectangleWidth * 0.8);
        calendarActivityBox.setMaxHeight(rectangleHeight * 0.65);
        calendarActivityBox.setStyle("-fx-background-color: white");
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
                    calendarActivities.add(new CalendarActivity(time, "Hans", 111111));
                }
            }
        }

        return createCalendarMap(calendarActivities);
    }
}