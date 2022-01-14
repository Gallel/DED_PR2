package uoc.ded.practica.model;

import java.time.LocalDate;

import uoc.ei.tads.Iterador;

public class OrderGroup extends Order {
	// Pointer to the group of this order
	private Group group;
	
	public OrderGroup(Group group, Activity activity, LocalDate date) {
		super(group.getId(), date);
		this.setGroup(group);
	}
	
	// Group order group setter
	public void setGroup(Group group) {
		this.group = group;
	}
	
	// Group order group getter
	public Group getGroup() {
		return this.group;
	}

	// Create the tickets for all the members of the group
	@Override
	public void createTickets(Activity activity) {
		for (Iterador<User> it = group.members(); it.hiHaSeguent();)
			this.addTicket(new Ticket(it.seguent(), activity));
	}
	
	// Get the mean badge value of all members
	@Override
	public double getValue() {
		return this.getGroup().valueOf();
	}
}
