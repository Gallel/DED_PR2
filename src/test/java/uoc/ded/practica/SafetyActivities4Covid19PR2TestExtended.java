package uoc.ded.practica;

import org.junit.Assert;
import org.junit.Test;

import uoc.ded.practica.exceptions.*;
import uoc.ded.practica.model.Activity;
import uoc.ded.practica.model.Worker;
import uoc.ded.practica.util.DateUtils;
import uoc.ei.tads.Iterador;

public class SafetyActivities4Covid19PR2TestExtended extends SafetyActivities4Covid19PR {
	@Test
	public void testAddWorkerExtended()
	{
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
        
        safetyActivities4Covid19.addWorker("W100", "Worker1", "Surname1", DateUtils.createLocalDate("01-01-1990"), true, "R1", "C-TRICICLE");
        
        Worker worker = safetyActivities4Covid19.getWorker("W100");
        
        Assert.assertEquals("Worker1", worker.getName());
        Assert.assertEquals("Surname1", worker.getSurname());
        Assert.assertEquals(DateUtils.createLocalDate("01-01-1990"), worker.getBirthday());
        Assert.assertTrue(worker.isCovidCertificate());
        Assert.assertEquals("R1", worker.getRoleId());
        Assert.assertEquals("C-TRICICLE", worker.getOrganizationId());
        
        Assert.assertEquals(20, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(8, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-DAGOLL"));
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkersByRole("R1"));
        
        safetyActivities4Covid19.addWorker("W100", "Worker1-New", "Surname1-New", DateUtils.createLocalDate("01-01-1991"), false, "R2", "C-TRICICLE");
        
        worker = safetyActivities4Covid19.getWorker("W100");
        
        Assert.assertEquals("Worker1-New", worker.getName());
        Assert.assertEquals("Surname1-New", worker.getSurname());
        Assert.assertEquals(DateUtils.createLocalDate("01-01-1991"), worker.getBirthday());
        Assert.assertFalse(worker.isCovidCertificate());
        Assert.assertEquals("R2", worker.getRoleId());
        Assert.assertEquals("C-TRICICLE", worker.getOrganizationId());
        
        Assert.assertEquals(20, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(8, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(3, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-DAGOLL"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkersByRole("R1"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkersByRole("R2"));
        
        safetyActivities4Covid19.addWorker("W100", "Worker1-New", "Surname1-New", DateUtils.createLocalDate("01-01-1991"), false, "R2", "C-FURA");
        
        Assert.assertEquals(20, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(8, safetyActivities4Covid19.numWorkers());
        Assert.assertEquals(4, safetyActivities4Covid19.numWorkers("C-FURA"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-TRICICLE"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkers("C-DAGOLL"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkersByRole("R1"));
        Assert.assertEquals(2, safetyActivities4Covid19.numWorkersByRole("R2"));
	}
	
	@Test(expected = ActivityNotFoundException.class)
	public void testGetUsersInActivityAndActivityNotFound() throws DEDException
	{
		safetyActivities4Covid19.getUsersInActivity("ACT-1199");
	}
	
	@Test(expected = NoUserException.class)
	public void testGetUsersInActivityAndNoUser() throws DEDException
	{
		safetyActivities4Covid19.getUsersInActivity("ACT-1101");
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testGetBadgeAndUserNotFound() throws DEDException
	{
		safetyActivities4Covid19.getBadge("idUser1-NoExist", createLocalDate("23-11-2021"));
	}
	
	@Test
	public void testCreateTicketByGroupExtended() throws DEDException
	{
		Activity activity = safetyActivities4Covid19.getActivity("ACT-1105");
		Assert.assertEquals(17, activity.availabilityOfTickets());
		
		safetyActivities4Covid19.createTicketByGroup("GX", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));
		
		Assert.assertEquals(15, activity.availabilityOfTickets());
		
		safetyActivities4Covid19.createTicketByGroup("G2021", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));
		
		Assert.assertEquals(9, activity.availabilityOfTickets());
	}
	
	@Test(expected = GroupNotFoundException.class)
	public void testCreateTicketByGroupAndGroupNotFound() throws DEDException
	{
		safetyActivities4Covid19.createTicketByGroup("GX-NoExist", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));
	}
	
	@Test(expected = ActivityNotFoundException.class)
	public void testCreateTicketByGroupAndActivityNotFound() throws DEDException
	{
		safetyActivities4Covid19.createTicketByGroup("GX", "ACT-1105-NoExist", DateUtils.createLocalDate("24-11-2021"));
	}
	
	@Test(expected = LimitExceededException.class)
	public void testCreateTicketByGroupAndLimitExceeded() throws DEDException
	{
		safetyActivities4Covid19.addRecord("R-006", "ACT-1106", "description ACT-1106" ,
                DateUtils.createDate("23-11-2022 23:00:00"),  DateUtils.createLocalDate("21-11-2021"),
                SafetyActivities4Covid19.Mode.FACE2FACE, 1, "C-TRICICLE");
		safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.ENABLED,
                DateUtils.createDate("24-11-2021 11:00:00"), "OK: XXX 0").getActivity();
		
		safetyActivities4Covid19.createTicketByGroup("GX", "ACT-1106", DateUtils.createLocalDate("24-11-2021"));
	}
	
	@Test(expected = OrderNotFoundException.class)
	public void testGetOrderAndOrderNotFound() throws DEDException
	{
		safetyActivities4Covid19.getOrder("O-20000423-idUser6-NoExist");
	}
	
	@Test
	public void testGetWorkersByRole() throws DEDException
	{
		Worker worker = null;
		Iterador<Worker> it = safetyActivities4Covid19.getWorkersByRole("R1");
		
		worker = it.seguent();
		Assert.assertEquals("W1", worker.getId());
		
		worker = it.seguent();
		Assert.assertEquals("W5", worker.getId());
	}
	
	@Test(expected = NoWorkersException.class)
	public void testGetWorkersByRoleAndNoWorkers() throws DEDException
	{
		safetyActivities4Covid19.addRole("R6", "scholar");
		safetyActivities4Covid19.getWorkersByRole("R6");
	}
	
	@Test
	public void testGetActivitiesByOrganization() throws DEDException
	{
		Activity activity = null;
		Iterador<Activity> it = safetyActivities4Covid19.getActivitiesByOrganization("C-DAGOLL");
		
		activity = it.seguent();
		Assert.assertEquals("ACT-1103", activity.getActId());
		
		activity = it.seguent();
		Assert.assertEquals("ACT-1101", activity.getActId());
		
		activity = it.seguent();
		Assert.assertEquals("ACT-1102", activity.getActId());
	}
	
	@Test(expected = NoActivitiesException.class)
	public void testGetActivitiesByOrganizationAndNoActivities() throws DEDException
	{
		safetyActivities4Covid19.getActivitiesByOrganization("C-FURA");
	}
	
	@Test(expected = NoRecordsException.class)
	public void testGetRecordsByOrganizationAndNoRecords() throws DEDException
	{
		safetyActivities4Covid19.getRecordsByOrganization("C-FURA");
	}
}
