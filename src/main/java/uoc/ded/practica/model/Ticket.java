package uoc.ded.practica.model;

public class Ticket {
	// Pointer to the user of this ticket
    private User user;
    // Pointer to the activity of this ticket
    private Activity activity;
    // Ticket seat number
    private int seat;

    public Ticket(User user, Activity activity) {
        this.setUser(user);
        this.setActivity(activity);
    }
    
    // Ticket user setter
    public void setUser(User user) {
    	this.user = user;
    }
    
    // Ticket user getter
    public User getUser() {
        return this.user;
    }
    
    // Ticket activity setter
    public void setActivity(Activity activity) {
    	this.activity = activity;
    }

    // Ticket activity getter
    public Activity getActivity() {
        return this.activity;
    }

    // Ticket seat number setter
    public void setSeat(int seat) {
        this.seat = seat;
    }
    
    // Ticket seat number getter
    public int getSeat() {
        return this.seat;
    }

    // Print the ticket with the activity id, the number of the seat and the user id
    @Override
    public String toString() {
        return "**" + activity.getActId() + " " + seat + " " + user.getId();
    }
}
