package app.shared;

import app.model.User;

import java.io.Serializable;

public record Lead(String firstName, String middleName, String lastName, String email, String phone, String title, int businessID, String status) implements Serializable
{

  public String toString(){
    return "Name: "+firstName+" "+lastName+" ";
  }
}