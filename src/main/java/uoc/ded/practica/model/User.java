package uoc.ded.practica.model;

import java.time.LocalDate;

import uoc.ded.practica.util.LocalDateUtils;
import uoc.ded.practica.SafetyActivities4Covid19;
import uoc.ded.practica.SafetyActivities4Covid19.Badge;
import uoc.ei.tads.Iterador;
import uoc.ei.tads.Llista;
import uoc.ei.tads.LlistaEncadenada;

public class User {
	// User ID
    private String id;
    // User name
    private String name;
    // User surname
    private String surname;
    // User activities as a linked list
    private Llista<Activity> activities;
    // User birthday (only with year, month and day)
    private LocalDate birthday;
    // User covid certificate
    private boolean covidCertificate;
    // User badge
    private SafetyActivities4Covid19.Badge badge;
    // User number of ratings to calculate its badge rather than iterate the linked list of ratings of an activity
    private int numRatings;

	public User(String idUser, String name, String surname, LocalDate birthday, boolean covidCertificate) {
        this.setId(idUser);
        this.setName(name);
        this.setSurname(surname);
        this.setBirthday(birthday);
        this.setCovidCertificate(covidCertificate);
        this.activities = new LlistaEncadenada<Activity>();
        this.setNumRatings(0);
    }

	// User ID setter
	public void setId(String id) {
        this.id = id;
    }
	
	// User ID getter
	public String getId() {
        return this.id;
    }
	
	// User name setter
    public void setName(String name) {
        this.name = name;
    }
    
    // User name getter
    public String getName() {
        return this.name;
    }

    // User surname setter
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    // User surname getter
    public String getSurname() {
        return this.surname;
    }

    // User birthday setter
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
    
    // User birthday getter
    public LocalDate getBirthday() {
        return this.birthday;
    }

    // User covid certificate setter
    public void setCovidCertificate(boolean covidCertificate) {
        this.covidCertificate = covidCertificate;
    }
    
    // Check if the user has the covid certificate
    public boolean isCovidCertificate() {
		return this.covidCertificate;
	}

    // Add an activity to the linked list
    public void addActivity(Activity activity) {
        this.activities.afegirAlFinal(activity);
    }
    
    // Check if the user has activities
    public boolean hasActivities() {
        return this.activities.nombreElems() > 0;
    }

    // Return the amount of activities
    public int numActivities() {
        return this.activities.nombreElems();
    }
    
    // Return an iterator with all the activities of this user
    public Iterador<Activity> activities() {
        return this.activities.elements();
    }

    // Check if the user has an activity by its id
    public boolean isInActivity(String actId) {
        for (Iterador<Activity> it = this.activities.elements(); it.hiHaSeguent();)
        	if (it.seguent().is(actId))
        		return true;

        return false;
    }

    // User badge getter accordind to its birthday and the date received as a param
	public Badge getBadge(LocalDate date) {
		int age = LocalDateUtils.getYearsBetweenLocaleDates(this.getBirthday(), date);
		
		if (age < 12)
			this.badge = SafetyActivities4Covid19.Badge.JUNIOR;
		else if (isCovidCertificate())
		{
			if (age >= 65)
				this.badge = SafetyActivities4Covid19.Badge.SENIOR_PLUS;
			else if (age >= 50)
				this.badge = SafetyActivities4Covid19.Badge.SENIOR;
			else if (age >= 30)
			{
				if (this.getNumRatings() > 5)
					this.badge = SafetyActivities4Covid19.Badge.MASTER_PLUS;
				else
					this.badge = SafetyActivities4Covid19.Badge.MASTER;
			}
			else if (age >= 18)
			{
				if (this.getNumRatings() > 10)
					this.badge = SafetyActivities4Covid19.Badge.YOUTH_PLUS;
				else
					this.badge = SafetyActivities4Covid19.Badge.YOUTH;
			}
			else
				this.badge = SafetyActivities4Covid19.Badge.JUNIOR_PLUS;
		}
		else
			this.badge = SafetyActivities4Covid19.Badge.DARK;
		
		return this.badge;
	}
	
	// User number of ratings setter
	private void setNumRatings(int numRatings) {
		this.numRatings = numRatings;
	}
	
	// User number of ratings setter
	private int getNumRatings() {
		return this.numRatings;
	}
	
	// Increment the amount of ratings of this user
	public void incrementRatings() {
		this.numRatings++;
	}
	
	// Check if the user it's the same by its id
	public boolean is(String userId) {
        return this.getId().equals(userId);
    }
	
	// Application of a patter to know if the instance is a User or a Worker rather than use the keyword instanceof
	public boolean isWorker() {
		return false;
	}
}
