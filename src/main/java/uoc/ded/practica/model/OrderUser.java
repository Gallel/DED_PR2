package uoc.ded.practica.model;

import java.time.LocalDate;

public class OrderUser extends Order {
	// Pointer to the user of this order
	private User user;
	
	public OrderUser(User user, Activity activity, LocalDate date) {
		super(user.getId(), date);
		this.setUser(user);
	}
	
	// User order user setter
	public void setUser(User user) {
		this.user = user;
	}
	
	// User order user getter
	public User getUser() {
		return this.user;
	}

	// Create the ticket for the user
	@Override
	public void createTickets(Activity activity) {
		this.addTicket(new Ticket(this.getUser(), activity));
	}
	
	// Get the badge value of the user
	@Override
	public double getValue() {
		return (double)this.getUser().getBadge(this.getDate()).getValue();
	}
}
