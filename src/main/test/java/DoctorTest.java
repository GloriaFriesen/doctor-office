import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;

public class DoctorTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctor_office_test", null, null);
  }

  @After
  public void tearDown() {
   try(Connection con = DB.sql2o.open()) {
     String deleteTasksQuery = "DELETE FROM tasks *;";
     String deleteCategoriesQuery = "DELETE FROM categories *;";
     con.createQuery(deleteTasksQuery).executeUpdate();
     con.createQuery(deleteCategoriesQuery).executeUpdate();
   }
  }

  @Test
  public void doctor_instantiesCorrectly_true() {
    Doctor testDoctor = new Doctor("Dr. Gloria", "child psychology");
    assertEquals(true, testDoctor instanceof Doctor);
  }

  @Test
  public void doctor_instantiatesWithName_String() {
    Doctor testDoctor = new Doctor("Dr. Gloria", "child psychology");
    assertEquals("Dr. Gloria", testDoctor.getName());
  }

  @Test
  public void doctor_instantiatesWithSpecialty_String() {
    Doctor testDoctor = new Doctor("Dr. Gloria", "child psychology");
    assertEquals("child psychology", testDoctor.getSpecialty());
  }

  @Test
  public void all_returnsAllInstancesOfCategory_true() {
    Doctor firstDoctor = new Doctor("Dr. Gloria", "child psychology");
    firstDoctor.save();
    Doctor secondDoctor = new Doctor("Dr. Blake", "neurology");
    secondDoctor.save();
    assertEquals(true, Doctor.all().get(0).equals(firstDoctor));
    assertEquals(true, Doctor.all().get(1).equals(secondDoctor));
  }
}
