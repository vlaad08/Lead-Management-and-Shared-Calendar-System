package app.shared;

import app.model.Employee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Meeting implements Serializable
{
  private Date startDate;
  private Date endDate;
  private String description;
  private ArrayList<Employee> employeeList;

  public Meeting(Date startDate, Date endDate, String description){
    this.startDate = startDate;
    this.endDate = endDate;
    this.description = description;
    employeeList = new ArrayList<Employee>();
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public void addEmployee(Employee employee){
    employeeList.add(employee);
  }

  public void addListOfEmployee(ArrayList<Employee> employees){
    for(Employee element: employees){
      employeeList.add(element);
    }
  }

  public ArrayList<Employee> getEmployee(){
    return employeeList;
  }

  public void removeEmployee(Employee employee){
    //It will be removed only if the object is the same
    //overwise this method should be edited
    employeeList.remove(employee);

  }
}
