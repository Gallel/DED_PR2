package uoc.ded.practica;

import org.junit.Assert;
import org.junit.Test;

import uoc.ded.practica.exceptions.*;
import uoc.ded.practica.exceptions.DEDException;
import uoc.ded.practica.model.*;
import uoc.ded.practica.model.Record;
import uoc.ded.practica.util.DateUtils;
import uoc.ei.tads.Iterador;

public class SafetyActivities4Covid19PR1TestExtended extends SafetyActivities4Covid19PR {
	@Test
	public void testAddUserExtended()
	{
		Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
		
		safetyActivities4Covid19.addUser("idUser2000", "Name2000", "Surname2000", createLocalDate("01-01-1990"), true);
        Assert.assertEquals("Name2000", safetyActivities4Covid19.getUser("idUser2000").getName());
        Assert.assertEquals("Surname2000", safetyActivities4Covid19.getUser("idUser2000").getSurname());
        Assert.assertEquals(createLocalDate("01-01-1990"), safetyActivities4Covid19.getUser("idUser2000").getBirthday());
        Assert.assertEquals(true, safetyActivities4Covid19.getUser("idUser2000").isCovidCertificate());
        Assert.assertEquals(20, safetyActivities4Covid19.numUsers());
        
        safetyActivities4Covid19.addUser("idUser2001", "Name2001", "Surname1 Surname2", createLocalDate("02-01-1990"), false);
        Assert.assertEquals("Name2001", safetyActivities4Covid19.getUser("idUser2001").getName());
        Assert.assertEquals("Surname1 Surname2", safetyActivities4Covid19.getUser("idUser2001").getSurname());
        Assert.assertEquals(createLocalDate("02-01-1990"), safetyActivities4Covid19.getUser("idUser2001").getBirthday());
        Assert.assertEquals(false, safetyActivities4Covid19.getUser("idUser2001").isCovidCertificate());
        Assert.assertEquals(21, safetyActivities4Covid19.numUsers());
        
        safetyActivities4Covid19.addUser("idUser2001", "Name2001", "Surname1 Nosurname", createLocalDate("02-01-1990"), true);
        Assert.assertEquals("Name2001", safetyActivities4Covid19.getUser("idUser2001").getName());
        Assert.assertEquals("Surname1 Nosurname", safetyActivities4Covid19.getUser("idUser2001").getSurname());
        Assert.assertEquals(createLocalDate("02-01-1990"), safetyActivities4Covid19.getUser("idUser2001").getBirthday());
        Assert.assertEquals(true, safetyActivities4Covid19.getUser("idUser2001").isCovidCertificate());
        Assert.assertEquals(21, safetyActivities4Covid19.numUsers());
	}
	
	@Test
	public void testAddRecordExtended() throws DEDException
	{
		Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        
        Record record = null;
        
        safetyActivities4Covid19.addRecord("XXX-101", "ACT-101", "description ACT-101",
                createDate("22-11-2022 23:00:00"), createLocalDate("24-11-2021"),
                SafetyActivities4Covid19.Mode.ON_LINE, 100, "C-TRICICLE");

        Assert.assertEquals(2, safetyActivities4Covid19.numPendingRecords());

        record = safetyActivities4Covid19.currentRecord();
        Assert.assertEquals("XXX-101", record.getRecordId());
        Assert.assertEquals("ACT-101", record.getActId());
        Assert.assertEquals("description ACT-101", record.getDescription());
        Assert.assertEquals(createDate("22-11-2022 23:00:00"), record.getDateAct());
        Assert.assertEquals(createLocalDate("24-11-2021"), record.getDateRecord());
        Assert.assertEquals(SafetyActivities4Covid19.Mode.ON_LINE, record.getMode());
        Assert.assertEquals(100, record.getNum());
        Assert.assertEquals("C-TRICICLE", record.getOrganization().getOrganizationId());
        Assert.assertEquals(SafetyActivities4Covid19.Status.PENDING, record.getStatus());
        Assert.assertNull(record.getDescriptionStatus());
        Assert.assertNull(record.getDateStatus());
        Assert.assertEquals(false, record.isEnabled());
        Assert.assertNull(record.getActivity());
	}
	
	@Test(expected = NoRecordsException.class)
	public void testAddRecordAndUpdateExtended() throws DEDException
	{
		Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(6, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        
        this.testAddRecordExtended();
        
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(7, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(2, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(4, safetyActivities4Covid19.numActivities());
        
        Record record = null;
        Activity activity = null;

        Assert.assertEquals(0.12, safetyActivities4Covid19.getInfoRejectedRecords(), 0.03);
        
        record = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.ENABLED, createDate("01-01-2022 12:00:00"), "OK");
        activity = record.getActivity();
        
        Assert.assertEquals("XXX-101", record.getRecordId());
        Assert.assertEquals("ACT-101", record.getActId());
        Assert.assertEquals("description ACT-101", record.getDescription());
        Assert.assertEquals(createDate("22-11-2022 23:00:00"), record.getDateAct());
        Assert.assertEquals(createLocalDate("24-11-2021"), record.getDateRecord());
        Assert.assertEquals(SafetyActivities4Covid19.Mode.ON_LINE, record.getMode());
        Assert.assertEquals(100, record.getNum());
        Assert.assertEquals("C-TRICICLE", record.getOrganization().getOrganizationId());
        Assert.assertEquals(SafetyActivities4Covid19.Status.ENABLED, record.getStatus());
        Assert.assertEquals("OK", record.getDescriptionStatus());
        Assert.assertEquals(createDate("01-01-2022 12:00:00"), record.getDateStatus());
        Assert.assertEquals(true, record.isEnabled());
        Assert.assertEquals(activity.getActId(), record.getActivity().getActId());
        
        Assert.assertEquals("ACT-101", activity.getActId());
        Assert.assertEquals("description ACT-101", activity.getDescription());
        Assert.assertEquals(createDate("22-11-2022 23:00:00"), activity.getDate());
        Assert.assertEquals(SafetyActivities4Covid19.Mode.ON_LINE, activity.getMode());
        Assert.assertEquals(100, activity.getTotal());
        Assert.assertTrue(activity.hasAvailabilityOfTickets());
        Assert.assertEquals(activity.getRecord().getRecordId(), record.getRecordId());
        Assert.assertEquals(activity.getOrganization().getOrganizationId(), record.getOrganization().getOrganizationId());
        
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(7, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(1, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(5, safetyActivities4Covid19.numActivities());
        
        record = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.DISABLED, createDate("01-01-2022 12:00:00"), "KO X2");
        
        Assert.assertEquals(19, safetyActivities4Covid19.numUsers());
        Assert.assertEquals(5, safetyActivities4Covid19.numOrganizations());
        Assert.assertEquals(7, safetyActivities4Covid19.numRecords());
        Assert.assertEquals(0, safetyActivities4Covid19.numPendingRecords());
        Assert.assertEquals(2, safetyActivities4Covid19.numRejectedRecords());
        Assert.assertEquals(5, safetyActivities4Covid19.numActivities());
        
        record = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.ENABLED, createDate("01-01-2022 12:00:00"), "OK");
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testCreateTicketAndUserNotFound() throws DEDException
	{
		safetyActivities4Covid19.createTicket("idUserNoExists", "ACT-1102", DateUtils.createLocalDate("23-04-2021"));
	}
	
	@Test(expected = ActivityNotFoundException.class)
	public void testCreateTicketAndActivityNotFound() throws DEDException
	{
		safetyActivities4Covid19.createTicket("idUser8", "ACT-1102-NoExists", DateUtils.createLocalDate("23-04-2021"));
	}
	
	@Test(expected = ActivityNotFoundException.class)
	public void testAssignSeatAndActivityNotFound() throws DEDException
	{
		safetyActivities4Covid19.assignSeat("ACT-1102-Noexist");
	}
	
	@Test
	public void testAddRatingExtended() throws DEDException
	{
		Activity activity1105 = safetyActivities4Covid19.getActivity("ACT-1105");
		
		Assert.assertEquals(0, activity1105.rating(), 0);
		
		safetyActivities4Covid19.addRating("ACT-1105", SafetyActivities4Covid19.Rating.FIVE, "Very good", "idUser1");
		
		Assert.assertEquals(5, activity1105.rating(), 0);
		
		safetyActivities4Covid19.addRating("ACT-1105", SafetyActivities4Covid19.Rating.FOUR, "Good", "idUser2");

        Assert.assertEquals(4.5, activity1105.rating(), 0);
        
        Rating rating = null;
        Iterador<Rating> it = activity1105.ratings();
        
        rating = it.seguent();
        Assert.assertEquals("Very good", rating.getMessage());
        Assert.assertEquals("idUser1", rating.getUser().getId());
        
        rating = it.seguent();
        Assert.assertEquals("Good", rating.getMessage());
        Assert.assertEquals("idUser2", rating.getUser().getId());
        
        Activity activity1102 = safetyActivities4Covid19.getActivity("ACT-1102");

        safetyActivities4Covid19.addRating("ACT-1102", SafetyActivities4Covid19.Rating.FOUR, "Good!!!", "idUser4");
        
        Assert.assertEquals(4, activity1102.rating(), 0);
        Assert.assertEquals(4.5, activity1105.rating(), 0.09);
        
        Activity activity1101 = safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.ENABLED, DateUtils.createDate("24-11-2021 11:00:00"), "OK: XXX 0").getActivity();
        
        Assert.assertEquals("ACT-1101", activity1101.getActId());
        
        safetyActivities4Covid19.createTicket("idUser1", "ACT-1101", DateUtils.createLocalDate("24-11-2021"));
        safetyActivities4Covid19.assignSeat("ACT-1101");
        
        safetyActivities4Covid19.addRating("ACT-1101", SafetyActivities4Covid19.Rating.FIVE, "Good!!!", "idUser1");
        
        Assert.assertEquals(5, activity1101.rating(), 0);
        Assert.assertEquals(4, activity1102.rating(), 0);
        Assert.assertEquals(4.5, activity1105.rating(), 0.09);
        
        Activity activity = null;
        Iterador<Activity> it2 = safetyActivities4Covid19.best10Activities();
        
        activity = it2.seguent();
        Assert.assertEquals("ACT-1101", activity.getActId());
        
        activity = it2.seguent();
        Assert.assertEquals("ACT-1105", activity.getActId());
        
        activity = it2.seguent();
        Assert.assertEquals("ACT-1102", activity.getActId());
        
        safetyActivities4Covid19.addRating("ACT-1101", SafetyActivities4Covid19.Rating.ONE, "So bad.", "idUser1");
        
        Organization organization = null;
        Iterador<Organization> it3 = safetyActivities4Covid19.best5Organizations();
        
        organization = it3.seguent();
        Assert.assertEquals(4.5, organization.rating(), 0.09);
        Assert.assertEquals("C-TRICICLE", organization.getOrganizationId());
        
        organization = it3.seguent();
        Assert.assertEquals(0.75, organization.rating(), 0.009);
        Assert.assertEquals("C-DAGOLL", organization.getOrganizationId());
        
	}
	
	@Test(expected = ActivityNotFoundException.class)
	public void testAddRatingAndActivityNotFound() throws DEDException
	{
		safetyActivities4Covid19.addRating("ACT-1105-NoExists", SafetyActivities4Covid19.Rating.FIVE, "Very good", "idUser1");
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testAddRatingAndUserNotFound() throws DEDException
	{
		safetyActivities4Covid19.addRating("ACT-1105", SafetyActivities4Covid19.Rating.FIVE, "Very good", "idUser1-NoExists");
	}
	
	@Test(expected = UserNotInActivityException.class)
	public void testAddRatingAndUserNotInActivity() throws DEDException
	{
		safetyActivities4Covid19.addRating("ACT-1105", SafetyActivities4Covid19.Rating.FIVE, "Very good", "idUser9");
	}
	
	@Test(expected = ActivityNotFoundException.class)
	public void testGetRatingsAndActivityNotFound() throws DEDException
	{
		safetyActivities4Covid19.getRatings("ACT-1105-NoExists");
	}
	
	@Test(expected = NoRatingsException.class)
	public void testGetRatingsAndNoRatings() throws DEDException
	{
		safetyActivities4Covid19.updateRecord(SafetyActivities4Covid19.Status.ENABLED, DateUtils.createDate("24-11-2021 11:00:00"), "OK: XXX 0").getActivity();
		safetyActivities4Covid19.getRatings("ACT-1101");
	}
	
	@Test
	public void testMostActiveUser() throws DEDException
	{
		Assert.assertEquals("idUser1", safetyActivities4Covid19.mostActiveUser().getId());
		
		safetyActivities4Covid19.createTicket("idUser2", "ACT-1101", DateUtils.createLocalDate("24-11-2021"));
        safetyActivities4Covid19.assignSeat("ACT-1101");
        
        safetyActivities4Covid19.createTicket("idUser2", "ACT-1102", DateUtils.createLocalDate("24-11-2021"));
        safetyActivities4Covid19.assignSeat("ACT-1102");
        
        safetyActivities4Covid19.createTicket("idUser2", "ACT-1105", DateUtils.createLocalDate("24-11-2021"));
        safetyActivities4Covid19.assignSeat("ACT-1105");
        
        Assert.assertEquals("idUser2", safetyActivities4Covid19.mostActiveUser().getId());
	}
	
	@Test
	public void testGetActivitiesByUser() throws DEDException
	{
		safetyActivities4Covid19.createTicket("idUser1", "ACT-1101", DateUtils.createLocalDate("24-11-2021"));
        safetyActivities4Covid19.assignSeat("ACT-1101");
		
		Activity activity = null;
		Iterador<Activity> it = safetyActivities4Covid19.getActivitiesByUser("idUser1");
		
		activity = it.seguent();
		Assert.assertEquals("ACT-1105", activity.getActId());
		
		activity = it.seguent();
		Assert.assertEquals("ACT-1101", activity.getActId());
	}
}
