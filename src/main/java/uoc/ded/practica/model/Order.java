package uoc.ded.practica.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import uoc.ei.tads.ClauValor;
import uoc.ei.tads.Iterador;
import uoc.ei.tads.Llista;
import uoc.ei.tads.LlistaEncadenada;

public abstract class Order {
	// Comparator for the orders AVL tree of the TAD
	public static final Comparator<ClauValor<String, Order>> CMP_K = (ClauValor<String, Order> o1, ClauValor<String, Order> o2) -> o1.getClau().compareTo(o2.getClau());
	// Comparator for the orders priority queue of an activity
	public static final Comparator<Order> CMP_V = (Order o1, Order o2) -> Double.compare(o2.getValue(), o1.getValue());
	
	// Order ID
	private String id;
	// Order date
	private LocalDate date;
	// Order tickets as a linked list
	private Llista<Ticket> tickets;

	public Order(String id, LocalDate date) {
		this.setId(id, date);
		this.setDate(date);
		this.tickets = new LlistaEncadenada<Ticket>();
	}
	
	// Order ID setter
	public void setId(String id, LocalDate date) {
		this.id = new String("O-"+date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))+"-"+id);
	}
	
	// Order ID getter
	public String getId() {
		return this.id;
	}
	
	// Order date setter
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	// Order date getter
	public LocalDate getDate() {
		return this.date;
	}
	
	// Add the tickets from the subclasses
	protected void addTicket(Ticket ticket) {
		this.tickets.afegirAlFinal(ticket);
	}
	
	// Return the amount of users of this order
	public int numUsers() {
		return this.tickets.nombreElems();
	}
	
	// Return an iterator with all the members of this order
	public Iterador<Ticket> tickets() {
		return this.tickets.elements();
	}

	// Abstract method to be defined by its subclasses to create the tickets for this order
	public abstract void createTickets(Activity activity);
	
	// Abstract method to be defined by its subclasses to obtain the mean value of the badge
	public abstract double getValue();
}
