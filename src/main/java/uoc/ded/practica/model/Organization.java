package uoc.ded.practica.model;

import java.util.Comparator;

import uoc.ei.tads.Iterador;
import uoc.ei.tads.Llista;
import uoc.ei.tads.LlistaEncadenada;
import uoc.ei.tads.Posicio;
import uoc.ei.tads.Recorregut;

public class Organization implements Comparable<Organization> {
	// Comparator for the 5 best organizations ordered vector
	public static final Comparator<Organization> CMP_V = (Organization o1, Organization o2) -> Double.compare(o1.rating(), o2.rating());
	
	// Organization ID
    private String organizationId;
    // Organization name
    private String name;
    // Organization description
    private String description;
    // Organization activities as a linked list
    private Llista<Activity> activities;
    // Organization workers as a linked list
    private Llista<Worker> workers;
    // Organization records as a linked list
    private Llista<Record> records;

    public Organization(String organizationId, String name, String description) {
        this.setOrganizationId(organizationId);
        this.setName(name);
        this.setDescription(description);
        this.activities = new LlistaEncadenada<Activity>();
        this.workers = new LlistaEncadenada<Worker>();
        this.records = new LlistaEncadenada<Record>();
    }
    
    // Organization id setter
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    
    // Organization id setter
    public String getOrganizationId() {
        return this.organizationId;
    }
    
    // Organization name setter
    public void setName(String name) {
        this.name = name;
    }
    
    // Organization name getter
    public String getName() {
        return this.name;
    }
    
    // Orgainzation description setter
    public void setDescription(String description) {
        this.description = description;
    }
    
    // Organization description getter
    public String getDescription() {
        return this.description;
    }
    
    // Add an activity to the linked list
    public void addActivity(Activity activity) {
        this.activities.afegirAlFinal(activity);
    }
    
    // Check if the organization has activities
    public boolean hasActivities() {
        return this.activities.nombreElems() > 0;
    }
    
    // Return the amount of activities
    public int numActivities() {
        return this.activities.nombreElems();
    }

    // Return an iterator with all the activities of this organization
    public Iterador<Activity> activities() {
        return this.activities.elements();
    }
	
	// Add a worker to the linked list
	public void addWorker(Worker worker) {
		this.workers.afegirAlFinal(worker);
	}
	
	// Remove a worker from the linked list
	public void removeWorker(Worker worker) {
		Recorregut<Worker> recorregut = this.workers.posicions();
		
		Posicio<Worker> position;
		
		while (recorregut.hiHaSeguent())
		{
			position = recorregut.seguent();
			
			if (position.getElem().is(worker.getId()))
			{
				this.workers.esborrar(position);
				return;
			}
		}
	}
	
	// Check if the organization has workers
	public boolean hasWorkers() {
		return this.workers.nombreElems() > 0;
	}
	
	// Return the amount of workers
	public int numWorkers() {
		return this.workers.nombreElems();
	}
	
	// Return an iterator with all the workers of this organization
	public Iterador<Worker> workers() {
		return this.workers.elements();
	}
	
	// Add a record to the linked list
	public void addRecord(Record record) {
		this.records.afegirAlFinal(record);
	}
	
	// Check if the organization has records
	public boolean hasRecords() {
		return this.records.nombreElems() > 0;
	}
	
	// Return the amount of records
	public int numRecords() {
		return this.records.nombreElems();
	}

	// Return an iterator with all the records of this organization
	public Iterador<Record> records() {
		return this.records.elements();
	}
	
	public void incrementRatings(int value) {
		
	}
	
	// Return the mean of the valorations of this organization
	public double rating() {
		double totalRatings = 0.0;
		
		for (Iterador<Activity> it = this.activities(); it.hiHaSeguent();) {
			totalRatings = it.seguent().rating();
		}
		
		// Cannot divide by 0, so it has to be checked
		return (this.activities.nombreElems() != 0 ? (double)totalRatings / this.activities.nombreElems() : 0);
	}
	
	@Override
	public int compareTo(Organization o) {
		return this.getOrganizationId().compareTo(o.getOrganizationId());
	}
}
