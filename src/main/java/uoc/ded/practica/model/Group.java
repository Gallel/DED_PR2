package uoc.ded.practica.model;

import java.time.LocalDate;
import java.util.Comparator;

import uoc.ei.tads.ClauValor;
import uoc.ei.tads.Iterador;
import uoc.ei.tads.Llista;
import uoc.ei.tads.LlistaEncadenada;

public class Group {
	// Comparator for the groups AVL tree of the TAD
	public static final Comparator<ClauValor<String, Group>> CMP_K = (ClauValor<String, Group> g1, ClauValor<String, Group> g2) -> g1.getClau().compareTo(g2.getClau());
	
	// Group ID
	private String groupId;
	// Group description
	private String description;
	// Group creation date
	private LocalDate date;
	// Group members as a linked list
	private Llista<User> members;
	
	public Group(String groupId, String description, LocalDate date) {
		this.setId(groupId);
		this.setDescription(description);
		this.setDate(date);
	}
	
	// Group ID setter
	public void setId(String groupId) {
		this.groupId = groupId;
	}
	
	// Group ID getter
	public String getId() {
		return this.groupId;
	}
	
	// Group description setter
	public void setDescription(String description) {
		this.description = description;
	}
	
	// Group description getter
	public String getDescrition() {
		return this.description;
	}
	
	// Group creation date setter
	public void setDate(LocalDate date) {
		this.date = date;
	}

	// Group creation date getter
	public LocalDate getDate() {
		return this.date;
	}
	
	// Member list setter
	public void setMembers(User[] members) {
		this.members = new LlistaEncadenada<User>();
		
		for (User member: members)
			this.addMember(member);
	}
	
	// Add a single member to the linked list of members
	private void addMember(User member) {
		this.members.afegirAlFinal(member);
	}
	
	// Check if the group has members
	public boolean hasMembers() {
		return this.members.nombreElems() > 0;
	}
	
	// Return the amount of members
	public int numMembers() {
		return this.members.nombreElems();
	}
	
	// Return an iterator with all the members of this group
	public Iterador<User> members() {
		return this.members.elements();
	}

	// Return the mean of all badges of the members
	public double valueOf() {
		int value = 0;
		
		// Has to iterate to get badge value of all members
		for (Iterador<User> it = members(); it.hiHaSeguent();)
			value += it.seguent().getBadge(getDate()).getValue();
		
		return (double)value / (double)numMembers();
	}

}
