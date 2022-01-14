package uoc.ded.practica.model;

import uoc.ded.practica.SafetyActivities4Covid19;

import java.time.LocalDate;
import java.util.Date;
import java.util.Comparator;

public class Record {
	// Comparator for the records priority queue of the TAD
	public static final Comparator<Record> CMP_Q = (Record r1, Record r2) -> {
    	int cmp = r1.getDateRecord().compareTo(r2.getDateRecord());
    	
    	if (cmp == 0)
    		cmp = ((Integer)r1.getOrganization().numWorkers()).compareTo((Integer)r2.getOrganization().numWorkers());
    	
    	return cmp;
    };
	
    // Record ID
    private String recordId;
    // Record activity ID
    private String actId;
    // Record description
    private String description;
    // Record activity date
    private Date dateAct;
    // Record creation date
    private LocalDate dateRecord;
    // Record mode
    private SafetyActivities4Covid19.Mode mode;
    // Record activity max capacity
    private int num;
    // Pointer to the organization of this record
    private Organization organization;
    // Record status
    private SafetyActivities4Covid19.Status status;
    // Record update description
    private String descriptionStatus;
    // Record update date
    private Date dateStatus;
    // Pointer to the activity of this record (can be null whereas the record is pending or disabled)
    private Activity activity;


    public Record(String recordId, String actId, String description, Date date, LocalDate dateRecord, SafetyActivities4Covid19.Mode mode, int num, Organization organization) {
        this.setRecordId(recordId);
        this.setActId(actId);
        this.setDescription(description);
        this.setDateAct(date);
        this.setDateRecord(dateRecord);
        this.setMode(mode);
        this.setNum(num);
        this.setOrganization(organization);
        this.setStatus(SafetyActivities4Covid19.Status.PENDING);
    }
    
    // Record ID setter
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
    
    // Record ID getter
    public String getRecordId() {
        return this.recordId;
    }
    
    // Record activity ID setter
    public void setActId(String actId) {
        this.actId = actId;
    }
    
    // Record activity ID getter
    public String getActId() {
        return this.actId;
    }    

    // Record description setter
    public void setDescription(String description) {
        this.description = description;
    }
    
    // Record description getter
    public String getDescription() {
        return this.description;
    }
    
    // Record activity date setter
    public void setDateAct(Date date) {
        this.dateAct = date;
    }
    
    // Record activity date getter
    public Date getDateAct() {
        return this.dateAct;
    }
    
    // Record creation date setter
    public void setDateRecord(LocalDate dateRecord) {
    	this.dateRecord = dateRecord;
    }
    
    // Record creation date getter
    public LocalDate getDateRecord() {
    	return this.dateRecord;
    }

    // Record mode setter
    public void setMode(SafetyActivities4Covid19.Mode mode) {
        this.mode = mode;
    }
    
    // Record mode getter
    public SafetyActivities4Covid19.Mode getMode() {
        return this.mode;
    }
    
    // Record activity max capacity setter
    public void setNum(int num) {
        this.num = num;
    }

    // Record activity max capacity getter
    public int getNum() {
        return this.num;
    }
    
    // Record organization setter
    public void setOrganization(Organization organization) {
    	this.organization = organization;
    }
    
    // Record organization getter
    public Organization getOrganization() {
		return this.organization;
	}
    
    // Record status setter
    public void setStatus(SafetyActivities4Covid19.Status status) {
        this.status = status;
    }

    // Record status getter
    public SafetyActivities4Covid19.Status getStatus() {
        return this.status;
    }
    
    // Record update description setter
    public void setDescriptionStatus(String descriptionStatus) {
        this.descriptionStatus = descriptionStatus;
    }

    // Record update description getter
    public String getDescriptionStatus() {
        return this.descriptionStatus;
    }

    // Record update date setter
    public void setDateStatus(Date dateStatus) {
        this.dateStatus = dateStatus;
    }
    
    // Record update date getter
    public Date getDateStatus() {
        return this.dateStatus;
    }
    
    // Check if the record is enabled
    public boolean isEnabled() {
        return this.status == SafetyActivities4Covid19.Status.ENABLED;
    }
    
    // Record activity setter
    public void setActivity(Activity activity) {
    	this.activity = activity;
    }
    
    // Record activity getter
	public Activity getActivity() {
		return this.activity;
	}

    // Update the record with new status, update date and new description
    public void update(SafetyActivities4Covid19.Status status, Date date, String description) {
        this.setStatus(status);
        this.setDateStatus(date);
        this.setDescriptionStatus(description);
    }

    // Create an activity for this record
    public Activity newActivity() {
        Activity activity = new Activity(this.getActId(), this.getDescription(), this.getDateAct(), this.getMode(), this.getNum(), this, this.getOrganization());
        this.organization.addActivity(activity);
        this.setActivity(activity);

        return activity;
    }
}
