package uoc.ded.practica;

import org.junit.Assert;
import org.junit.Test;
import uoc.ded.practica.exceptions.*;
import uoc.ded.practica.model.*;
import uoc.ded.practica.model.Record;
import uoc.ded.practica.util.DateUtils;
import uoc.ei.tads.Iterador;


public class SafetyActivities4Covid19PR1Test extends SafetyActivities4Covid19PR {

    /**
     * *feature*: (sobre la qual fem @Test): addUser del TAD SafetyActivities4Covid19
     * *given*: Hi ha 19 usuaris en el sistema
     * *scenario*:
     * - S'afegeix un nou usuari en el sistema
     * - S'afegeix un segon usuari en el sistema
     * - Es modifiquen les dades del segon usuari inserir
     */
    @Test
    public void testAddUser() {

        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        //

        safetyActivities4Covid19.addUser("idUser1000", "Robert", "Lopez", createLocalDate("02-01-1942"), false);
        Assert.assertEquals("Robert", safetyActivities4Covid19.getUser("idUser1000").getName());
        Assert.assertEquals(20, safetyActivities4Covid19.numUsers());

        safetyActivities4Covid19.addUser("idUser9999", "XXXXX", "YYYYY", createLocalDate("12-11-1962"), true);
        Assert.assertEquals("XXXXX", safetyActivities4Covid19.getUser("idUser9999").getName());
        Assert.assertEquals(21, safetyActivities4Covid19.numUsers());

        safetyActivities4Covid19.addUser("idUser9999", "Lluis", "Casals", createLocalDate("22-07-1938"), true);
        Assert.assertEquals("Lluis", safetyActivities4Covid19.getUser("idUser9999").getName());
        Assert.assertEquals("Casals", safetyActivities4Covid19.getUser("idUser9999").getSurname());
        Assert.assertEquals(21, safetyActivities4Covid19.numUsers());
    }


    /**
     * *feature*: (sobre la qual fem @Test): addOrganization del TAD SafetyActivities4Covid19
     * *given*: Hi ha 19 usuaris en el sistema i 5 organitzacions
     * *scenario*:
     * - S'afegeix una nova organització en el sistema
     * - S'afegeix una segona organització en el sistema
     * - Es modifiquen les dades de la segona organització
     */
    @Test
    public void testAddOrganization() {

        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        //

        safetyActivities4Covid19.addOrganization("15", "ORG_VDA", "description VDA");
        Assert.assertEquals("ORG_VDA", safetyActivities4Covid19.getOrganization("15").getName());
        Assert.assertEquals("description VDA", safetyActivities4Covid19.getOrganization("15").getDescription());
        Assert.assertEquals(6, safetyActivities4Covid19.numOrganizations());

        safetyActivities4Covid19.addOrganization("17", "ORG_XXX", "description XXX");
        Assert.assertEquals("ORG_XXX", safetyActivities4Covid19.getOrganization("17").getName());
        Assert.assertEquals("description XXX", safetyActivities4Covid19.getOrganization("17").getDescription());
        Assert.assertEquals(7, safetyActivities4Covid19.numOrganizations());

        safetyActivities4Covid19.addOrganization("17", "ORG_AWS", "description AW");
        Assert.assertEquals("ORG_AWS", safetyActivities4Covid19.getOrganization("17").getName());
        Assert.assertEquals("description AW", safetyActivities4Covid19.getOrganization("17").getDescription());
        Assert.assertEquals(7, safetyActivities4Covid19.numOrganizations());
    }


    /**
     * *feature*: (sobre la qual fem @Test): addRecord del TAD SafetyActivities4Covid19
     * *given*: Hi ha:
     * - 19 usuaris en el sistema
     * - 5 organitzacions
     * - 6 Expedients en el sistema
     * - 1 Expedient pendent de validar
     * - 1 Expedient rebutjat
     * - 4 Activitats
     * <p>
     * *scenario*:
     * - S'afegeix un nou expedient en el sistema
     * - S'afegeix un segon expedient en el sistema
     */
    @Test
    public void testAddRecord() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //
        Record record = null;

        safetyActivities4Covid19.addRecord("XXX-001", "ACT-010", "description ACT-010",
                createDate("22-11-2022 23:00:00"), createLocalDate("24-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 100, "C-FURA");

        Assert.assertEquals(2, safetyActivities4Covid19.numPendingRecords());

        record = safetyActivities4Covid19.currentRecord();
        Assert.assertEquals("XXX-001", record.getRecordId());
        Assert.assertEquals(SafetyActivities4Covid19.Status.PENDING, record.getStatus());


        safetyActivities4Covid19.addRecord("XXX-002", "ACT-011", "description ACT-011",
                createDate("25-11-2022 23:00:00"), createLocalDate("21-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 100, "C-FURA");

        Assert.assertEquals(3, safetyActivities4Covid19.numPendingRecords());

        record = safetyActivities4Covid19.currentRecord();
        Assert.assertEquals("XXX-002", record.getRecordId());
        Assert.assertEquals(SafetyActivities4Covid19.Status.PENDING, record.getStatus());

    }


    /**
     * *feature*: (sobre la qual fem @Test): addRecord del TAD SafetyActivities4Covid19
     * *given*: Hi ha:
     * - 19 usuaris en el sistema
     * - 5 organitzacions
     * - 6 Expedients en el sistema
     * - 1 Expedient pendent de validar
     * - 1 Expedient rebutjat
     * - 4 Activitats
     * *scenario*:
     * - S'afegeix un nou expedient en el sistema sobre una organització inexistent
     */
    @Test(expected = OrganizationNotFoundException.class)
    public void testAddRecordAndOrganizationNotFound() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //

        safetyActivities4Covid19.addRecord("XXX-002", "ACT-011", "description ACT-011",
                createDate("25-11-2022 23:00:00"), createLocalDate("24-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 100, "2");

    }


    /**
     * *feature*: (sobre la qual fem @Test): addRecord & update del TAD SafetyActivities4Covid19
     * *given*: Hi ha:
     * - 19 usuaris en el sistema
     * - 5 organitzacions
     * - 6 Expedients en el sistema
     * - 1 Expedient pendent de validar
     * - 1 Expedient rebutjat
     * - 4 Activitats
     * <p>
     * *scenario*:
     * - S'afegeixen "records" del test testAddRecord
     */
    @Test
    public void testAddRecordAndUpdate() throws DEDException {

        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //

        this.testAddRecord();


        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(8, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(3, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //
        Record record = null;

        Assert.assertEquals(0.12, safetyActivities4Covid19.getInfoRejectedRecords(), 0.03);

        record = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.DISABLED,
                createDate("25-11-2021 23:00:00"), "KO X1");
        Assert.assertNull(record.getActivity());

        Assert.assertEquals(2, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(8, safetyActivities4Covid19.numRecords());


        Assert.assertEquals(0.25, safetyActivities4Covid19.getInfoRejectedRecords(), 0.03);

    }


    /**
     * *feature*: (sobre la qual fem @Test): getActivitiesByOrganization del TAD SafetyActivities4Covid19
     * *given*: Hi ha:
     * - 19 usuaris en el sistema
     * - 5 organitzacions
     * - 6 Expedients en el sistema
     * - 1 Expedient pendent de validar
     * - 1 Expedient rebutjat
     * - 4 Activitats
     * <p>
     * *scenario*:
     * - Es consulten les activitats d'una organització
     */
    @Test
    public void testGetActivitiesByOrganization() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //


        Assert.assertEquals(3, safetyActivities4Covid19.numActivitiesByOrganization("C-DAGOLL"));

        Iterador<Activity> it = safetyActivities4Covid19.getActivitiesByOrganization("C-DAGOLL");
        Activity activity1 = it.seguent();
        Assert.assertEquals("ACT-1103", activity1.getActId());

        Activity activity2 = it.seguent();
        Assert.assertEquals("ACT-1101", activity2.getActId());
    }

    /**
     * *feature*: (sobre la qual fem @Test): getActivitiesByOrganization del TAD SafetyActivities4Covid19
     * *given*: Hi ha:
     * - 19 usuaris en el sistema
     * - 5 organitzacions
     * - 6 Expedients en el sistema
     * - 1 Expedient pendent de validar
     * - 1 Expedient rebutjat
     * - 4 Activitats
     * <p>
     * *scenario*:
     * - Es consulten les activitats d'una organització que NO té activitats (3)
     */
    @Test(expected = NoActivitiesException.class)
    public void testGetActivitiesByOrganizationAndNOActiviesException() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //
        @SuppressWarnings("unused")
		Iterador<Activity> it = safetyActivities4Covid19.getActivitiesByOrganization("C-FURA");
    }


    /**
     * *feature*: (sobre la qual fem @Test): getAllActivities del TAD SafetyActivities4Covid19
     * *given*: Hi ha:
     * - 10 usuaris en el sistema
     * - 5 organitzacions
     * - 6 Expedients en el sistema
     * - 1 Expedient pendent de validar
     * - 1 Expedient rebutjat
     * - 4 Activitats
     * <p>
     * *scenario*:
     * - Es consulten totes les activitats
     */
    @Test
    public void testGetAllActivities() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //
        Iterador<Activity> it = safetyActivities4Covid19.getAllActivities();

        Activity activity1 = it.seguent();
        Assert.assertEquals("ACT-1101", activity1.getActId());

        Activity activity2 = it.seguent();
        Assert.assertEquals("ACT-1102", activity2.getActId());

        Activity activity3 = it.seguent();
        Assert.assertEquals("ACT-1103", activity3.getActId());

    }

    /**
     * *feature*: (sobre la qual fem @Test): getAllActivitiesByUser del TAD SafetyActivities4Covid19
     * *given*: Hi ha:
     * - 19 usuaris en el sistema
     * - 5 organitzacions
     * - 6 Expedients en el sistema
     * - 1 Expedient pendent de validar
     * - 1 Expedient rebutjat
     * - 4 Activitats
     * <p>
     * *scenario*:
     * - Es consulten les activitats d'un usuari i no existeix cap
     */
    @Test(expected = NoActivitiesException.class)
    public void testGetAllActivitiesByUserAndNoActivitiesException() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //
        @SuppressWarnings("unused")
		Iterador<Activity> it = safetyActivities4Covid19.getActivitiesByUser("idUser9");

    }

    /**
     * *feature*: (sobre la qual fem @Test): addRating & getRatings del TAD SafetyActivities4Covid19
     * *given*: Hi ha:
     * - 19 usuaris en el sistema
     * - 5 organitzacions
     * - 6 Expedients en el sistema
     * - 1 Expedient pendent de validar
     * - 1 Expedient rebutjat
     * - 4 Activitats
     * <p>
     * *scenario*:
     * - S'afegeixen valoracions sobre un parell d'activitats
     * culturals que van alternant ser la millor activitat cultural
     */
    @Test
    public void testAddRating() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        //

        Activity activity1105 = safetyActivities4Covid19.getActivity("ACT-1105");

        Assert.assertEquals(0, activity1105.rating(), 0);

        safetyActivities4Covid19.addRating("ACT-1105",
                SafetyActivities4Covid19.Rating.FIVE, "Very good", "idUser1");

        Assert.assertEquals(5, activity1105.rating(), 0);

        safetyActivities4Covid19.addRating("ACT-1105",
                SafetyActivities4Covid19.Rating.FOUR, "Good", "idUser2");

        Assert.assertEquals(4.5, activity1105.rating(), 0);

        safetyActivities4Covid19.addRating("ACT-1105",
                SafetyActivities4Covid19.Rating.TWO, "CHIPI - CHAPI", "idUser3");
        Assert.assertEquals(3.6, activity1105.rating(), 0.09);

        Activity bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1105", bestActivity.getActId());
        //
        //

        Activity activity1102 = safetyActivities4Covid19.getActivity("ACT-1102");

        safetyActivities4Covid19.addRating("ACT-1102",
                SafetyActivities4Covid19.Rating.FOUR, "Good!!!", "idUser4");
        Assert.assertEquals(4, activity1102.rating(), 0);
        Assert.assertEquals(3.6, activity1105.rating(), 0.09);

        bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1102", bestActivity.getActId());


        safetyActivities4Covid19.addRating("ACT-1102",
                SafetyActivities4Covid19.Rating.ONE, "Bad!!!", "idUser5");
        Assert.assertEquals(2.5, activity1102.rating(), 0.09);
        Assert.assertEquals(3.6, activity1105.rating(), 0.09);

        bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1105", bestActivity.getActId());

        safetyActivities4Covid19.addRating("ACT-1102",
                SafetyActivities4Covid19.Rating.FOUR, "Good!!!", "idUser6");
        Assert.assertEquals(3, activity1102.rating(), 0.09);
        Assert.assertEquals(3.6, activity1105.rating(), 0.09);

        bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1105", bestActivity.getActId());

        safetyActivities4Covid19.addRating("ACT-1102",
                SafetyActivities4Covid19.Rating.FIVE, "Very Good!!!", "idUser7");
        Assert.assertEquals(3.5, activity1102.rating(), 0);
        Assert.assertEquals(3.6, activity1105.rating(), 0.09);

        bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1105", bestActivity.getActId());

        safetyActivities4Covid19.addRating("ACT-1102",
                SafetyActivities4Covid19.Rating.FIVE, "Very Good!!!", "idUser7");
        Assert.assertEquals(3.8, activity1102.rating(), 0);
        Assert.assertEquals(3.6, activity1105.rating(), 0.09);

        bestActivity = safetyActivities4Covid19.bestActivity();
        Assert.assertEquals("ACT-1102", bestActivity.getActId());


        Iterador<Rating> it = safetyActivities4Covid19.getRatings("ACT-1102");
        Rating rating = it.seguent();
        Assert.assertEquals(SafetyActivities4Covid19.Rating.FOUR, rating.getRating());
        Assert.assertEquals("idUser4", rating.getUser().getId());


    }


    /**
     * *feature*: (sobre la qual fem @Test): createTicket & assignSeat del TAD SafetyActivities4Covid19
     * *given*: Hi ha:
     * - 19 usuaris en el sistema
     * - 5 organitzacions
     * - 6 Expedients en el sistema
     * - 1 Expedient pendent de validar
     * - 1 Expedient rebutjat
     * - 4 Activitats
     * - 7 entrades comprades sobre una activitat
     * - 4 seients assignats
     * <p>
     * *scenario*:
     * - Es compra 1 entrada i s'assigna el seu seient
     */
    @Test
    public void testCreateTicketAndAssign() throws DEDException {

        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords() );
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords() );
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        Assert.assertEquals(7, safetyActivities4Covid19.numOrders());
        Assert.assertEquals(3, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));
        //

        Order o1 = safetyActivities4Covid19.createTicket("idUser8", "ACT-1102", DateUtils.createLocalDate("23-04-2021"));
        Assert.assertEquals(2, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        Order o2 = safetyActivities4Covid19.createTicket("idUser9", "ACT-1102", DateUtils.createLocalDate("23-04-2021"));
        Assert.assertEquals(1, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        Order o3 = safetyActivities4Covid19.createTicket("idUser10", "ACT-1102", DateUtils.createLocalDate("23-04-2021"));
        Assert.assertEquals(0, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        o2 = safetyActivities4Covid19.assignSeat("ACT-1102");
        Assert.assertEquals(SafetyActivities4Covid19.Badge.JUNIOR_PLUS.getValue(), o2.getValue(),0);

        Iterador<Ticket> it5 = o2.tickets();

        Ticket ticket5 = it5.seguent();
        Assert.assertEquals(5, ticket5.getSeat());
        Assert.assertEquals("idUser9", ticket5.getUser().getId());

        o3 = safetyActivities4Covid19.assignSeat("ACT-1102");
        Assert.assertEquals(SafetyActivities4Covid19.Badge.DARK.getValue(), o3.getValue(),0);
        Iterador<Ticket> it6 = o3.tickets();

        Ticket ticket6 = it6.seguent();
        Assert.assertEquals(6, ticket6.getSeat());
        Assert.assertEquals("idUser10", ticket6.getUser().getId());

        o1 = safetyActivities4Covid19.assignSeat("ACT-1102");
        Assert.assertEquals(SafetyActivities4Covid19.Badge.DARK.getValue(), o1.getValue(),0);
        Iterador<Ticket> it7 = o1.tickets();

        Ticket ticket7 = it7.seguent();
        Assert.assertEquals(7, ticket7.getSeat());
        Assert.assertEquals("idUser8", ticket7.getUser().getId());
    }


    /**
     * *feature*: (sobre la qual fem @Test): createTicket del TAD SafetyActivities4Covid19
     * *given*: Hi ha:
     * - 19 usuaris en el sistema
     * - 5 organitzacions
     * - 6 Expedients en el sistema
     * - 1 Expedient pendent de validar
     * - 1 Expedient rebutjat
     * - 4 Activitats
     * - 4 entrades comprades sobre una activitat
     * - 4 seients assignats
     * <p>
     * *scenario*:
     * - Es compren més de 50 entrades i s'excedeix el nombre màxim d'entrades
     */
    @Test(expected = LimitExceededException.class)
    public void testCreateTicketAndLimitExceededException() throws DEDException {

        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        Assert.assertEquals(3, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));
        //

        safetyActivities4Covid19.createTicket("idUser7", "ACT-1102", DateUtils.createLocalDate("23-04-2000"));
        Assert.assertEquals(2, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        safetyActivities4Covid19.createTicket("idUser8", "ACT-1102", DateUtils.createLocalDate("23-04-2000"));
        Assert.assertEquals(1, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        safetyActivities4Covid19.createTicket("idUser9", "ACT-1102", DateUtils.createLocalDate("23-04-2000"));
        Assert.assertEquals(0, safetyActivities4Covid19.availabilityOfTickets("ACT-1102"));

        safetyActivities4Covid19.createTicket("idUser10", "ACT-1102", DateUtils.createLocalDate("23-04-2000"));

    }


}
