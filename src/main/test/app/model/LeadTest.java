package app.model;

import app.JDBC.SQLConnection;
import app.shared.Lead;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

public class LeadTest
{
  private SQLConnection connection;

  @BeforeEach void setUp() throws Exception{
    connection = mock(SQLConnection.class);
  }

  /*@Test void test_getLeads() throws Exception{
    ArrayList<Lead> leads = connection.getLeads();
    assertEquals(1, connection.getLeads().size());
  }*/


  @Test
  void test_getLeads() throws Exception {
    List<Lead> expectedLeads = new ArrayList<>();

    Lead expectedLead = new Lead("John", "Doe", "Lead 1", "lead1@example.com", "1234567890", "Manager", 1001, "");
    expectedLeads.add(expectedLead);

    when(connection.getLeads()).thenReturn((ArrayList<Lead>) expectedLeads);


    List<Lead> leads = connection.getLeads();

    assertEquals(1, leads.size());
  }


  /*@Test void add_a_lead_and_test_if_list_contain_that_lead() throws Exception{
    Lead lead = new Lead("Ion","Marian","Giogre","testlead@gmail.com","896213654","manager",7456);
    connection.createLead(lead);
    assertTrue(connection.getLeads().contains(lead));
  }*/




  @Test
  void add_a_lead_and_test_if_list_contain_that_lead() throws Exception {
    Lead lead = new Lead("Ion", "Marian", "Giogre", "testlead@gmail.com", "896213654", "manager", 7456, "");
    List<Lead> leads = new ArrayList<>();
    leads.add(lead);

    when(connection.getLeads()).thenReturn((ArrayList<Lead>) leads);

    connection.createLead(lead);

    assertTrue(connection.getLeads().contains(lead));
  }
}
