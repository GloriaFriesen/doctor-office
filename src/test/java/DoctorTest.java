import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.Arrays;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DoctorTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctor_office_test", null, null);
  }

  @After
  public void tearDown() {
   try(Connection con = DB.sql2o.open()) {
     String deleteDoctorsQuery = "DELETE FROM doctors *;";
     String deletePatientsQuery = "DELETE FROM patients *;";
     con.createQuery(deleteDoctorsQuery).executeUpdate();
     con.createQuery(deletePatientsQuery).executeUpdate();
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

  @Test
  public void getId_doctorsInstantiatesWithAnId_1() {
    Doctor testDoctor = new Doctor("Dr. Gloria", "child psychology");
    testDoctor.save();
    assertTrue(testDoctor.getId() > 0);
  }

  @Test
  public void find_returnsDoctorWithSameId_secondDoctor() {
    Doctor firstDoctor = new Doctor("Dr. Gloria", "child psychology");
    firstDoctor.save();
    Doctor secondDoctor = new Doctor("Dr. Blake", "neurology");
    secondDoctor.save();
    assertEquals(secondDoctor, Doctor.find(secondDoctor.getId()));
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() {
    Doctor firstDoctor = new Doctor("Dr. Gloria", "child psychology");
    Doctor secondDoctor = new Doctor("Dr. Gloria", "child psychology");
    assertTrue(firstDoctor.equals(secondDoctor));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Doctor testDoctor = new Doctor("Dr. Gloria", "child psychology");
    testDoctor.save();
    assertTrue(Doctor.all().get(0).equals(testDoctor));
  }

  @Test
  public void save_assignsIdToObject() {
    Doctor testDoctor = new Doctor("Dr. Gloria", "child psychology");
    testDoctor.save();
    Doctor savedDoctor = Doctor.all().get(0);
    assertEquals(testDoctor.getId(), savedDoctor.getId());
  }

  @Test
  public void getPatients_retrievesAllPatientsFromDB_patientsList() throws ParseException {
    Doctor testDoctor = new Doctor("Dr. Gloria", "child psychology");
    testDoctor.save();
    Patient firstPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), testDoctor.getId());
    firstPatient.save();
    Patient secondPatient = new Patient("Blake", new SimpleDateFormat("MM/dd/yyyy").parse("11/11/2011"), testDoctor.getId());
    secondPatient.save();
    Patient[] patients = new Patient[] { firstPatient, secondPatient };
    assertTrue(testDoctor.getPatients().containsAll(Arrays.asList(patients)));
  }
}
