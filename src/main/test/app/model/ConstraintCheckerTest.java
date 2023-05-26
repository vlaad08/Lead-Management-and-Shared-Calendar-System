package app.model;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
public class ConstraintCheckerTest
{
  @Test
  void checking_times_returns_boolean()
  {
    Time time1 = Time.valueOf(LocalTime.of(9,0));
    Time time2 = Time.valueOf(LocalTime.of(11,0));
    assertEquals(true, ConstraintChecker.checkTime(time1,time2));
  }

  @Test
  void checkTime_checks_correctly()
  {
    Time time1 = Time.valueOf(LocalTime.of(9,0));
    Time time2 = Time.valueOf(LocalTime.of(11,0));
    assertTrue(ConstraintChecker.checkTime(time1,time2));
    assertFalse(ConstraintChecker.checkTime(time2,time1));
  }

  @Test
  void checking_strings_by_parsing_them_returns_boolean()
  {
    String integer="1223";
    String text="text";
    assertEquals(true, ConstraintChecker.checkInt(integer));
    assertEquals(false, ConstraintChecker.checkInt(text));
  }

  @Test
  void checkInt_checks_correctly()
  {
    String integer="1223";
    String text="text";
    String mixed="te12xt23";
    assertTrue(ConstraintChecker.checkInt(integer));
    assertFalse(ConstraintChecker.checkInt(text));
    assertFalse(ConstraintChecker.checkInt(mixed));
  }

  @Test
  void checking_dates_returns_boolean()
  {
    LocalDate date=LocalDate.of( 2023, 5, 22);
    assertEquals(false,ConstraintChecker.checkDate(date));
  }

  @Test
  void checkDate_checks_for_today()
  {
    LocalDate date=LocalDate.of( 2023, 5, 22);
    LocalDate now=LocalDate.now();
    assertTrue(ConstraintChecker.checkDate(now));
    assertFalse(ConstraintChecker.checkDate(date));
  }

  @Test
  void checking_TextFields_checks_for_empty_textfields()
  {
    Platform.startup(() -> {
      TextField textField = new TextField("");

      boolean result = ConstraintChecker.checkFillOut(textField);
      assertEquals(false, result);

      Platform.exit();
    });
  }
}
