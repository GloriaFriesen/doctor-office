import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class PatientTest {

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
  public void Patient_instantiatesCorrectly_true() throws ParseException {

    Patient myPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), 1);
    assertEquals(true, myPatient instanceof Patient);
  }

  @Test
  public void getName_instantiatesWithName_String() throws ParseException {
    Patient testPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), 1);
    assertEquals("Gloria", testPatient.getName());
  }

  @Test
  public void getBirthday_instantiatesWithBirthday_Date() throws ParseException {
    Patient testPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), 1);
    Date expectedOutput = new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011");
    assertEquals(expectedOutput, testPatient.getBirthday());
  }

  @Test
  public void all_returnsAllInstancesOfTask_true() throws ParseException {
    Patient firstPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), 1);
    firstPatient.save();
    Patient secondPatient = new Patient("Blake", new SimpleDateFormat("MM/dd/yyyy").parse("11/11/2011"), 1);
    secondPatient.save();
    assertEquals(true, Patient.all().get(0).equals(firstPatient));
    assertEquals(true, Patient.all().get(1).equals(secondPatient));
  }

  @Test
  public void getId_PatientsInstantiateWithAnId_1() throws ParseException {
    Patient testPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), 1);
    testPatient.save();
    assertTrue(testPatient.getId() > 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAreTheSame() throws ParseException {
    Patient firstPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), 1);
    Patient secondPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), 1);
    assertTrue(firstPatient.equals(secondPatient));
  }

  @Test
  public void save_returnsTrueIfNamesAreTheSame() throws ParseException {
    Patient testPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), 1);
    testPatient.save();
    assertTrue(Patient.all().get(0).equals(testPatient));
  }

  @Test
  public void save_assignsIdToObject() throws ParseException {
    Patient testPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), 1);
    testPatient.save();
    Patient savedPatient = Patient.all().get(0);
    assertEquals(testPatient.getId(), savedPatient.getId());
  }

  @Test
  public void find_returnsPatientWithSameId_secondPatient() throws ParseException {
    Patient firstPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), 1);
    firstPatient.save();
    Patient secondPatient = new Patient("Blake", new SimpleDateFormat("MM/dd/yyyy").parse("11/11/2011"), 1);
    secondPatient.save();
    assertEquals(secondPatient, Patient.find(secondPatient.getId()));
  }

  @Test
  public void save_savesDoctorIdIntoDB_true() throws ParseException {
    Doctor myDoctor = new Doctor("Dr. Gloria", "child psychology");
    myDoctor.save();
    Patient testPatient = new Patient("Gloria", new SimpleDateFormat("MM/dd/yyyy").parse("08/16/2011"), myDoctor.getId());
    testPatient.save();
    Patient savedPatient = Patient.find(testPatient.getId());
    assertEquals(savedPatient.getDoctorId(), myDoctor.getId());
  }
}
