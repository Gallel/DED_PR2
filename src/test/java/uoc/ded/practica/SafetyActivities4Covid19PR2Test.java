package uoc.ded.practica;

import org.junit.Assert;
import org.junit.Test;
import uoc.ded.practica.exceptions.DEDException;
import uoc.ded.practica.exceptions.OrganizationNotFoundException;
import uoc.ded.practica.model.*;
import uoc.ded.practica.exceptions.*;
import uoc.ded.practica.util.DateUtils;
import uoc.ei.tads.Iterador;



public class SafetyActivities4Covid19PR2Test extends SafetyActivities4Covid19PR {
    ///
    // PR2
    ///
    
    /**
     * feature*: (sobre la qual fem @Test): addRole del TAD SafetyActivities4Covid19
     * given*: Hi ha 5 rols
     * *scenario*:
     * - S'afegeix un nou rol en el sistema
     * - S'afegeix un segon rol en el sistema
     * - Es modifiquen les dades del segon rol
     */
    @Test
    public void testAddRole() {

        // GIVEN:
        Assert.assertEquals(5, safetyActivities4Covid19.numRoles());
        //

        safetyActivities4Covid19.addRole("R9", "Actor");
        Assert.assertEquals("Actor", safetyActivities4Covid19.getRole("R9").getName());
        Assert.assertEquals(6, safetyActivities4Covid19.numRoles());

        safetyActivities4Covid19.addRole("R10", "XXXXX");
        Assert.assertEquals("XXXXX", safetyActivities4Covid19.getRole("R10").getName());
        Assert.assertEquals(7, safetyActivities4Covid19.numRoles());


        safetyActivities4Covid19.addRole("R10", "assistant director");
        Assert.assertEquals("assistant director", safetyActivities4Covid19.getRole("R10").getName());
        Assert.assertEquals(7, safetyActivities4Covid19.numRoles());


        Assert.assertEquals(7, safetyActivities4Covid19.numRoles());
    }


    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * TOT!!!!!!!!!!!!! ===> CANVI DE ROL !!!!!!
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * <p>
     * feature*: (sobre la qual fem @Test): addWorker del TAD SafetyActivities4Covid19
     * given*:
     * Hi ha 19 usuaris
     * Hi ha 7 treballadors.
     * - 3 treballadors de l'organització C-FURA
     * - 2 treballadors de l'organització C-TRICICLE
     * - 2 treballadors de l'organització C-DAGOLL
     * - 5 rols:
     * - R1: 2
     * - R2: 1
     * - R3: 1
     * - R4: 2
     * - R5: 1
     * <p>
     * *scenario*:
     * - S'afegeix un nou treballador en el sistema
     * - S'afegeix un segon treballador en el sistema
     * - Es modifiquen les dades bàsiques del segon treballador
     * - Es modifica el rol del segon treballador
     * - Es modifica l'organització del segon treballador
     * - Es modifiquen les dades d'un usuari ja existent que serà un treballador d'una organització
     */
    @Test
    public void testAddWorker() {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(7, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-DAGOLL"));
        Assert.assertEquals(5, safetyActivities4Covid19.numRoles());
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkersByRole("R1"));
        Assert.assertEquals(1, safetyActivities4Covid19.numWorkersByRole("R2"));
        Assert.assertEquals(1, safetyActivities4Covid19.numWorkersByRole("R3"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkersByRole("R4"));
        Assert.assertEquals(1, safetyActivities4Covid19.numWorkersByRole("R5"));

        ////
        // add W89
        ////
        safetyActivities4Covid19.addWorker("W89", "Josep", "Paradells",
                DateUtils.createLocalDate("23-04-1955"), true, "R1", "C-TRICICLE");

        Assert.assertEquals(20, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(8, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-DAGOLL"));
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkersByRole("R1"));

        Worker workerW89 = safetyActivities4Covid19.getWorker("W89");
        Assert.assertEquals("Josep", workerW89.getName());

        ////
        // add W99
        ////
        safetyActivities4Covid19.addWorker("W99", "Oscar", "XXXXXXX",
                DateUtils.createLocalDate("23-04-1945"), true, "R2", "C-TRICICLE");

        Assert.assertEquals(21, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(9, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(4, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-DAGOLL"));
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkersByRole("R1"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkersByRole("R2"));

        Worker workerW99 = safetyActivities4Covid19.getWorker("W99");
        Assert.assertEquals("Oscar", workerW99.getName());

        ////
        // update W99
        ////
        safetyActivities4Covid19.addWorker("W99", "Oscar", "Sánchez",
                DateUtils.createLocalDate("25-04-1945"), true, "R2", "C-TRICICLE");
        Assert.assertEquals(21, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(9, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(4, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-DAGOLL"));
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkersByRole("R1"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkersByRole("R2"));

        workerW99 = safetyActivities4Covid19.getWorker("W99");
        Assert.assertEquals("Oscar", workerW99.getName());
        Assert.assertEquals("Sánchez", workerW99.getSurname());
        Assert.assertEquals("R2", workerW99.getRoleId());

        ////
        // update W99 - update Role
        ////
        safetyActivities4Covid19.addWorker("W99", "Oscar", "Sánchez",
                DateUtils.createLocalDate("25-04-1945"), true, "R1", "C-TRICICLE");
        Assert.assertEquals(21, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(9, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(4, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-DAGOLL"));
        Assert.assertEquals(4, safetyActivities4Covid19.numWorkersByRole("R1"));
        Assert.assertEquals(1, safetyActivities4Covid19.numWorkersByRole("R2"));

        workerW99 = safetyActivities4Covid19.getWorker("W99");
        Assert.assertEquals("Oscar", workerW99.getName());
        Assert.assertEquals("Sánchez", workerW99.getSurname());
        Assert.assertEquals("R1", workerW99.getRoleId());

        ////
        // update W99 - update organization
        ////
        safetyActivities4Covid19.addWorker("W99", "Oscar", "Sánchez",
                DateUtils.createLocalDate("23-04-1945"), true, "R1", "C-FURA");
        Assert.assertEquals(21, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(9, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(4, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-DAGOLL"));
        Assert.assertEquals(4, safetyActivities4Covid19.numWorkersByRole("R1"));
        Assert.assertEquals(1, safetyActivities4Covid19.numWorkersByRole("R2"));


        ////
        // update idUser10 becomes Worker
        ////
        safetyActivities4Covid19.addWorker("idUser10", "Pepet", "Marieta de l'ull viu",
                DateUtils.createLocalDate("23-04-2008"), true, "R2", "C-TRICICLE");
        Assert.assertEquals(10, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(4, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(4, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Worker workeridUser10 = safetyActivities4Covid19.getWorker("idUser10");
        Assert.assertEquals("Pepet", workeridUser10.getName());
        Assert.assertEquals(4, safetyActivities4Covid19.numWorkersByRole("R1"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkersByRole("R2"));

    }

    /**
     * feature*: (sobre la qual fem @Test): getWorkersByOrganization del TAD SafetyActivities4Covid19
     * *scenario*:
     * * - Es consulten els treballadors d'una organització inexistent
     */
    @Test(expected = OrganizationNotFoundException.class)
    public void testGetWorkersByOrganizationAndOrganizationNotFound() throws DEDException {
        safetyActivities4Covid19.getWorkersByOrganization("XXXXX");
    }


    /**
     * feature*: (sobre la qual fem @Test): getWorkersByOrganization del TAD SafetyActivities4Covid19
     * *scenario*:
     * * - Es consulten els treballadors d'una organització inexistent
     */
    @Test(expected = NoWorkersException.class)
    public void testGetWorkersByOrganizationAndNOWorkers() throws DEDException {
        safetyActivities4Covid19.getWorkersByOrganization("16");
    }

    /**
     * feature*: (sobre la qual fem @Test): getWorkersByOrganization del TAD SafetyActivities4Covid19
     * given*: Hi ha 5 treballadors.
     * - 3 treballadors de l'organització C-FURA
     * - 2 treballadors de l'organització C-TRICICLE
     * *scenario*:
     * * - Es consulten els treballadors d'una organització
     */
    @Test
    public void testGetWorkersByOrganization() throws DEDException {

        // GIVEN:
        Assert.assertEquals(7, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-DAGOLL"));
        //

        Iterador<Worker> it = safetyActivities4Covid19.getWorkersByOrganization("C-TRICICLE");

        Worker w1 = it.seguent();
        Assert.assertEquals("Jesus", w1.getName());

        Worker w2 = it.seguent();
        Assert.assertEquals("Juan", w2.getName());
    }


    /**
     * feature*: (sobre la qual fem @Test): getUsersInActivity del TAD SafetyActivities4Covid19
     * <p>
     * *scenario*:
     * * - Es consulten els usuaris d'una activitat
     */
    @Test
    public void testGetUsersInActivity() throws DEDException {
        Iterador<User> it = safetyActivities4Covid19.getUsersInActivity("ACT-1102");
        User o1 = it.seguent();
        Assert.assertEquals("Joana", o1.getName());
        User o2 = it.seguent();
        Assert.assertEquals("Armand", o2.getName());
        User o3 = it.seguent();
        Assert.assertEquals("Jesus", o3.getName());
        User o4 = it.seguent();
        Assert.assertEquals("Anna", o4.getName());
    }

    /**
     * feature*: (sobre la qual fem @Test): getBadge del TAD SafetyActivities4Covid19
     * given*:
     * - 15 usuaris en el sistema
     * - Hi ha 5 treballadors.
     * *scenario*:
     * - Es consulta la insígnia d'un usuari SENIOR_PLUS
     * - Es consulta la insígnia d'un usuari MASTER
     * - Es consulta la insígnia d'un usuari JUNIOR_PLUS
     * - Es consulta la insígnia d'un usuari DARK
     */
    @Test
    public void testGetBadge() throws DEDException {
        SafetyActivities4Covid19.Badge badge = safetyActivities4Covid19.getBadge("idUser1", createLocalDate("23-11-2021"));
        Assert.assertEquals(SafetyActivities4Covid19.Badge.SENIOR_PLUS, badge);

        SafetyActivities4Covid19.Badge badge2 = safetyActivities4Covid19.getBadge("idUser2", createLocalDate("23-11-2021"));
        Assert.assertEquals(SafetyActivities4Covid19.Badge.MASTER, badge2);

        SafetyActivities4Covid19.Badge badge3 = safetyActivities4Covid19.getBadge("idUser9", createLocalDate("23-11-2021"));
        Assert.assertEquals(SafetyActivities4Covid19.Badge.JUNIOR_PLUS, badge3);

        SafetyActivities4Covid19.Badge badge4 = safetyActivities4Covid19.getBadge("idUser7", createLocalDate("23-11-2021"));
        Assert.assertEquals(SafetyActivities4Covid19.Badge.DARK, badge4);
    }

    /**
     * feature*: (sobre la qual fem @Test): addGroup del TAD SafetyActivities4Covid19
     * given*:
     * <p>
     * scenario:
     * - S'afegeix un primer grup
     * - S'afegeix un segon grup
     * - Es modifica el segon grup
     */
    @Test
    public void testAddGroupAndMembersOfAndValueOfAndGetBadge() throws DEDException {

        safetyActivities4Covid19.addGroup("G1", "Description (G1)", createLocalDate("23-11-2021"),
                "idUser1", "idUser2", "idUser9", "idUser7");
        Assert.assertEquals(3, safetyActivities4Covid19.numGroups());
        Group g1 = safetyActivities4Covid19.getGroup("G1");
        Assert.assertEquals(true, g1.hasMembers());
        Assert.assertEquals(4, g1.numMembers());

        Iterador<User> it = safetyActivities4Covid19.membersOf("G1");
        User o1 = it.seguent();
        Assert.assertEquals("Maria", o1.getName());

        User o2 = it.seguent();
        Assert.assertEquals("Àlex", o2.getName());

        User o3 = it.seguent();
        Assert.assertEquals("Agustí", o3.getName());

        User o4 = it.seguent();
        Assert.assertEquals("Anna", o4.getName());

        Assert.assertEquals(SafetyActivities4Covid19.Badge.SENIOR_PLUS, o1.getBadge(g1.getDate()));
        Assert.assertEquals(100, o1.getBadge(g1.getDate()).getValue(), 0.1);

        Assert.assertEquals(SafetyActivities4Covid19.Badge.MASTER, o2.getBadge(g1.getDate()));
        Assert.assertEquals(50, o2.getBadge(g1.getDate()).getValue(), 0.1);

        Assert.assertEquals(SafetyActivities4Covid19.Badge.JUNIOR_PLUS, o3.getBadge(g1.getDate()));
        Assert.assertEquals(35, o3.getBadge(g1.getDate()).getValue(), 0.1);

        Assert.assertEquals(SafetyActivities4Covid19.Badge.DARK, o4.getBadge(g1.getDate()));
        Assert.assertEquals(0, o4.getBadge(g1.getDate()).getValue(), 0.1);

        Assert.assertEquals(46.25, safetyActivities4Covid19.valueOf("G1"), 0.1);


    }


    @Test
    public void testCreateTicketByGroup() throws DEDException {

        Order orderIdUser3 = safetyActivities4Covid19.createTicket("idUser3", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));
        Order orderIdUser2 = safetyActivities4Covid19.createTicket("idUser2", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));
        Order orderGX = safetyActivities4Covid19.createTicketByGroup("GX", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));
        Order orderG2021 = safetyActivities4Covid19.createTicketByGroup("G2021", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));

        orderG2021 = safetyActivities4Covid19.assignSeat("ACT-1105");
        Assert.assertEquals("O-20211124-G2021", orderG2021.getId());
        Assert.assertEquals(100.0, orderG2021.getValue(), 0.1);
        Iterador<Ticket> itG21 = orderG2021.tickets();

        Ticket G21a = itG21.seguent();
        Assert.assertEquals(4, G21a.getSeat());

        Ticket G21b = itG21.seguent();
        Assert.assertEquals(5, G21b.getSeat());

        Ticket G21c = itG21.seguent();
        Assert.assertEquals(6, G21c.getSeat());

        Ticket G21d = itG21.seguent();
        Assert.assertEquals(7, G21d.getSeat());

        Ticket G21i = itG21.seguent();
        Assert.assertEquals(8, G21i.getSeat());

        Ticket G21f = itG21.seguent();
        Assert.assertEquals(9, G21f.getSeat());

        orderIdUser2 = safetyActivities4Covid19.assignSeat("ACT-1105");
        Assert.assertEquals("O-20211124-idUser2", orderIdUser2.getId());
        Assert.assertEquals(50.0, orderIdUser2.getValue(), 0.1);
        Iterador<Ticket> itIdUser2 = orderIdUser2.tickets();

        Ticket tIdUser2 = itIdUser2.seguent();
        Assert.assertEquals(10, tIdUser2.getSeat());

        orderGX = safetyActivities4Covid19.assignSeat("ACT-1105");
        Assert.assertEquals("O-20211124-GX", orderGX.getId());
        Assert.assertEquals(0, orderGX.getValue(), 0.1);

        Iterador<Ticket> itGX = orderGX.tickets();

        Ticket tIdUser10 = itGX.seguent();
        Assert.assertEquals(11, tIdUser10.getSeat());
        Ticket tIdUser8 = itGX.seguent();
        Assert.assertEquals(12, tIdUser8.getSeat());

        orderIdUser3 = safetyActivities4Covid19.assignSeat("ACT-1105");
        Iterador<Ticket> itIdUser3 = orderIdUser3.tickets();

        Ticket tIdUser3 = itIdUser3.seguent();
        Assert.assertEquals(13, tIdUser3.getSeat());

        Assert.assertEquals("O-20211124-idUser3", orderIdUser3.getId());
        Assert.assertEquals(0, orderIdUser3.getValue(), 0.1);


    }

    @Test
    public void testGetOrder() throws DEDException {
        Order order = safetyActivities4Covid19.getOrder("O-20000423-idUser6");
        Assert.assertEquals("O-20000423-idUser6", order.getId());

    }


    /**
     * *feature*: (sobre la qual fem @Test): getRecordsByOrganization del TAD SafetyActivities4Covid19
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
    public void testGetRecordsByOrganization() throws DEDException {
        // GIVEN:
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        Assert.assertEquals(3, safetyActivities4Covid19.numActivitiesByOrganization("C-DAGOLL"));
        Assert.assertEquals(1, safetyActivities4Covid19.numActivitiesByOrganization("C-TRICICLE"));
        Assert.assertEquals(5, safetyActivities4Covid19.numRecordsByOrganization("C-DAGOLL"));
        Assert.assertEquals(1, safetyActivities4Covid19.numRecordsByOrganization("C-TRICICLE"));
        //
    }

}