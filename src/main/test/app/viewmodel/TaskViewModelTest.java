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

  public class TaskViewModelTest
  {
    private Model model;
    private TasksViewModel tasksViewModel;
    private ObjectProperty<ObservableList<Task>> tasks;
    private PropertyChangeSupport support;
    private SimpleStringProperty name;
    private  Field supportField;
    private  Field nameField;

    @BeforeEach
    void setUp() throws IllegalAccessException, NoSuchFieldException
    {
      User user=new User("","","","","",false,"",1122);
      model = Mockito.mock(ModelManager.class);
      name = new SimpleStringProperty("");
      tasks =  Mockito.mock(ObjectProperty.class);
      Mockito.when(model.getLoggedInUser()).thenReturn(user);
      tasksViewModel = new TasksViewModel(model);
      support = Mockito.mock(PropertyChangeSupport.class);

      supportField = tasksViewModel.getClass().getDeclaredField("support");
      supportField.setAccessible(true);
      supportField.set(tasksViewModel,support);
    }

    @Test
    void constructor_sets_property_change_support()
    {
      assertNotNull(support);
    }
    @Test
    void constructor_sets_property_change_listener()
    {
      Mockito.verify(model,Mockito.times(1)).addPropertyChangeListener(tasksViewModel);
    }
    @Test
    void constructor_sets_name_field()
    {
      assertEquals(model.getLoggedInUser().getFirstName(), name.getName());
    }

    @Test
    void constructor_sets_tasks_field(){
      //because taskviewmodel.getTasks usses this but itsalready tested in model manager, i did it two from here btw
     Mockito.verify(model).getList("tasks");
    }

    @Test
    void get_tasks_returns_arraylist_of_tasks() {
      Task task1 = new Task("title","description",new Date(3900-1900,2,2),"To do",1);
      Task task2 = new Task("title","description",new Date(3900-1900,2,2),"To do",2);

      ArrayList<Object> mockObjects = new ArrayList<>();
      mockObjects.add(task1);
      mockObjects.add(task2);

      Mockito.when(model.getList("tasks")).thenReturn(mockObjects);

      ArrayList<Task> tasks = tasksViewModel.getTasks();

      List<Task> expectedTasks = Arrays.asList(task1, task2);
      assertEquals(expectedTasks, tasks);
    }

    @Test
    void bind_user_name_binds_property_with_name() {
      StringProperty property = Mockito.mock(StringProperty.class);

      tasksViewModel.bindUserName(property);

      Mockito.verify(property).bindBidirectional(ArgumentMatchers.any(StringProperty.class));
    }

    @Test
    void is_manager_returns_model_boolean(){

      Mockito.when(model.isManager()).thenReturn(true);

      boolean result = tasksViewModel.isManager();

      Mockito.verify(model).isManager();

      assertEquals(true, result);
    }


    @Test
    void bind_task_binds_property_with_tasks() {
      ObjectProperty<ObservableList<Task>> property = Mockito.mock(ObjectProperty.class);

      tasksViewModel.bindTask(property);

      Mockito.verify(property).bindBidirectional(ArgumentMatchers.any(ObjectProperty.class));
    }


    @Test
    void add_property_change_listener_adds_listener_to_support() {
      PropertyChangeListener listener = Mockito.mock(PropertyChangeListener.class);

      tasksViewModel.addPropertyChangeListener(listener);

      Mockito.verify(support).addPropertyChangeListener(ArgumentMatchers.eq(listener));
    }

    @Test
    void add_task_adds_a_task() throws SQLException, RemoteException
    {
      ArrayList<String> array = new ArrayList<>();
      array.add("example@gmail.com");

      tasksViewModel.addTask("title", "description", new Date(3900 - 1900, 2, 2),
          "To do", 1, array);

      Mockito.verify(model).addObject(Mockito.any(Task.class), Mockito.eq(array));
    }

    @Test
    void edit_task_edits_a_task() throws SQLException, RemoteException
    {
      ArrayList<String> array = new ArrayList<>();
      array.add("example@gmail.com");

      Task task1 = new Task("title","description",new Date(3900-1900,2,2),"To do",1);
      Task task2 = new Task("title","description",new Date(3900-1900,2,2),"To do",2);
      //task viewModel is not a mock
      tasksViewModel.editTask(task1,task2,array);


      Mockito.verify(model).editObjectWithList(Mockito.any(Task.class),Mockito.any(Task.class),Mockito.any(ArrayList.class));
    }

    @Test
    void get_businesses_returns_list_of_businesses() {
      ArrayList<Object> mockObjects = new ArrayList<>();
      mockObjects.add(new Business("Business1","street1",1));
      mockObjects.add(new Business("Business2","street2",2));
      mockObjects.add(new Task("Haha I'm a task >:D","description",new Date(3900-1900,2,2),"To do",1));

      Mockito.when(model.getList("businesses")).thenReturn(mockObjects);

      ArrayList<Business> businesses = tasksViewModel.getBusinesses();

      Mockito.verify(model).getList("businesses");

      assertEquals(2, businesses.size());
      assertEquals("Business1", businesses.get(0).getName());
      assertEquals("Business2", businesses.get(1).getName());
    }

    @Test
    void property_change_reloads_tasks(){
      Task task = new Task("Task", "description", new Date(3900 - 1900, 2, 2), "To do", 1);
      ObservableList<Task> tasksList = FXCollections.observableArrayList(task);
      tasks.set(tasksList);

      PropertyChangeEvent reloadTasksEvent = new PropertyChangeEvent(this, "reloadTasks", false, true);

      tasksViewModel.propertyChange(reloadTasksEvent);

      Mockito.verify(tasks).set(Mockito.any(ObservableList.class));
      Mockito.verify(support).firePropertyChange("reloadTasks", false, true);


    }

    @Test
    void property_change_reloads_for_logged_in_user(){
      name.set("vlad");
      Task task = new Task("Task", "description", new Date(3900 - 1900, 2, 2), "To do", 1);
      ObservableList<Task> tasksList = FXCollections.observableArrayList(task);
      tasks.set(tasksList);

      PropertyChangeEvent reloadLoggedInUserEvent = new PropertyChangeEvent(this, "reloadLoggedInUser", false, true);

      tasksViewModel.propertyChange(reloadLoggedInUserEvent);
      assertEquals(name.getName(), model.getLoggedInUser().getFirstName());

    }

    @Test
    void get_users_returns_list_of_users() {
      ArrayList<Object> userList = new ArrayList<>();
      userList.add(new User("John","","Doe","johnny@gmail.com","12369",true,"street1",1234));
      userList.add(new User("Alice","From","Wonderland","witch@gmail.com","12369",true,"yellow street",1234));

      when(model.getList("users")).thenReturn(userList);

      ArrayList<User> users = tasksViewModel.getUsers();

      assertEquals(2, users.size());
      assertEquals("John", users.get(0).getFirstName());
      assertEquals("Alice", users.get(1).getFirstName());
    }

    @Test
    void get_assigned_users_returns_an_array_list() {
      Task task = new Task("Task", "description", new Date(2023 - 1900, 4, 25), "To do", 1);

      User user = new User("John", "", "Doe", "johnny@gmail.com", "12369", true, "street1", 1234);
      ArrayList<String> assignedUsers = new ArrayList<>();

      assignedUsers.add(user.getEmail());

      Mockito.when(tasksViewModel.getAssignedUsers(task)).thenReturn(assignedUsers);
      ArrayList<String> result = tasksViewModel.getAssignedUsers(task);
      System.out.println(tasksViewModel.getAssignedUsers(task));

      Mockito.verify(model).getList("tasks");


      assertEquals(result.getClass(), ArrayList.class);
      assertNotNull(result);
      assertEquals(1, result.size());
      assertEquals(user.getEmail(), result.get(0));
    }

    @Test
    void remove_task_removes_task() throws SQLException, RemoteException {
      Task task = new Task("Task", "description", new Date(2023 - 1900, 4, 25), "To do", 1);

      tasksViewModel.removeTask(task);

      Mockito.verify(model).removeObject(task);
    }




  }
