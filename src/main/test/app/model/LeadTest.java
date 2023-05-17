package app.model;

import app.JDBC.SQLConnection;
import app.shared.Lead;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class LeadTest
{
  private SQLConnection connection;

  @BeforeEach void setUp() throws Exception{
    this.connection = SQLConnection.getInstance();
  }

  @Test void test_getLeads() throws Exception{
    ArrayList<Lead> leads = connection.getLeads();
    assertEquals(1, connection.getLeads().size());
  }

  /*
  @Test void add_a_lead_and_test_if_list_contain_that_lead() throws Exception{
    String email = "1example@gmail.com";
    Lead lead = new Lead("testLead","","nma",email,"sdasdsad","manager",7456);
    connection.addLead(lead);
    assertTrue(connection.getLeads().contains(lead));
  }

   */
}
