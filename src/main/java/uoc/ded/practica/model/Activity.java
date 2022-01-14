package uoc.ded.practica.model;

import java.util.Comparator;
import java.util.Date;

import uoc.ded.practica.SafetyActivities4Covid19;
import uoc.ei.tads.*;
import uoc.ei.tads.LlistaEncadenada;

public class Activity implements Comparable<Activity> {
	// Comparator for the 10 best activities ordered vector
	public static final Comparator<Activity> CMP_V = (Activity a1, Activity a2) -> Double.compare(a1.rating(), a2.rating());
	// Comparator for the activities AVL tree of the TAD
    public static final Comparator<ClauValor<String, Activity>> CMP_K = (ClauValor<String, Activity> a1, ClauValor<String, Activity> a2) -> a1.getClau().compareTo(a2.getClau());

    // Activity ID
    private String actId;
    // Activity description
    private String description;
    // Activity date
    private Date date;
    // Activity mode
    private SafetyActivities4Covid19.Mode mode;
    // Activity total seats
    private int total;
    // Activity next available seat
    private int nextSeat;
    // Activity available seats amount
    private int availabilityOfTickets;
    // Pointer to the record of this activity
    private Record record;
    // Activity organization
    private Organization organization;
    // Activity ratings as a linked list
    private Llista<Rating> ratings;
    // Activity orders as a priority queue
    private Cua<Order> orders;
    // Activity users as a linked list
    private Llista<User> users;
    // Activity total ratings value
    private int totalRatings;

    public Activity(String actId, String description, Date dateAct, SafetyActivities4Covid19.Mode mode, int num, Record record, Organization organization) {
        this.setActId(actId);
        this.setDescription(description);
        this.setDate(dateAct);
        this.setMode(mode);
        this.setTotal(num);
        this.nextSeat = 1;
        this.setAvailabilityOfTickets(num);
        this.setRecord(record);
        this.setOrganization(organization);
        this.ratings = new LlistaEncadenada<Rating>();
        this.orders = new CuaAmbPrioritat<Order>(Order.CMP_V);
        this.users = new LlistaEncadenada<User>();
        this.totalRatings = 0;
    }

    // Activity ID setter
    public void setActId(String actId) {
    	this.actId = actId;
    }
    
    // Activity ID getter
    public String getActId() {
        return this.actId;
    }

    // Activity description setter
    public void setDescription(String description) {
    	this.description = description;
    }
    
    // Activity description getter
    public String getDescription() {
    	return this.description;
    }
    
    // Activity date setter
    public void setDate(Date date) {
    	this.date = date;
    }
    
    // Activity date getter
    public Date getDate() {
    	return this.date;
    }
    
    // Activity mode setter
    public void setMode(SafetyActivities4Covid19.Mode mode) {
    	this.mode = mode;
    }
    
    // Activity mode getter
    public SafetyActivities4Covid19.Mode getMode() {
    	return this.mode;
    }
    
    // Activity total seats setter
    public void setTotal(int total) {
    	this.total = total;
    }
    
    // Activity total seats getter
    public int getTotal() {
    	return this.total;
    }
    
    // Activity available seats amount setter
    public void setAvailabilityOfTickets(int availabilityOfTickets) {
    	this.availabilityOfTickets = availabilityOfTickets;
    }
    
    // Activity available seats amount getter
    public int availabilityOfTickets() {
        return this.availabilityOfTickets;
    }

    // Check if the activity has available seats
    public boolean hasAvailabilityOfTickets() {
        return (availabilityOfTickets > 0);
    }
    
    // Activity record setter
    public void setRecord(Record record) {
    	this.record = record;
    }
    
    // Activity record getter
    public Record getRecord() {
    	return this.record;
    }
    
    // Activity organization setter
    public void setOrganization(Organization organization) {
    	this.organization = organization;
    }
    
    // Activity organization getter
    public Organization getOrganization() {
    	return this.organization;
    }

    // Add a rating to the linked list
    public void addRating(SafetyActivities4Covid19.Rating rating, String message, User user) {
        Rating newRating = new Rating(rating, message, user);
        ratings.afegirAlFinal(newRating);
        totalRatings += rating.getValue();
    }
    
    // Check if the activity has ratings
    public boolean hasRatings() {
        return ratings.nombreElems() > 0;
    }

    // Get the average rating
    public double rating() {
        return (ratings.nombreElems() != 0 ? (double)totalRatings / ratings.nombreElems() : 0);
    }
    
    // Return an iterator with all the ratings of this activity
    public Iterador<Rating> ratings() {
        return ratings.elements();
    }
	
    // Enqueue an order to the priority queue
	public void addOrder(Order order) {
		this.orders.encuar(order);
		this.availabilityOfTickets -= order.numUsers();
	}
	
	// Dequeue an order from the priority queue
	public Order dequeueOrder() {
		Order order = this.orders.desencuar();
		
		for (Iterador<Ticket> it = order.tickets(); it.hiHaSeguent();)
			it.seguent().setSeat(this.nextSeat++);
		
		return order;
	}
	
	// Add a user to the linked list
	public void addUser(User user) {
		this.users.afegirAlFinal(user);
	}
	
	// Check if the activity has users
	public boolean hasUsers() {
		return this.users.nombreElems() > 0;
	}
	
	// Return an iterator with all the users of this activity
	public Iterador<User> users() {
		return this.users.elements();
	}
	
	// Check if the activity it's the same by its id
	public boolean is(String actId) {
        return this.actId.equals(actId);
    }

	@Override
	public int compareTo(Activity o) {
		return this.getActId().compareTo(o.getActId());
	}
}
