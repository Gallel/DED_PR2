package uoc.ded.practica.model;

import uoc.ded.practica.SafetyActivities4Covid19;

public class Rating {
	// Rating value
    private SafetyActivities4Covid19.Rating rating;
    // Rating message
    private String message;
    // Pointer to the user of this rating
    private User user;

    public Rating(SafetyActivities4Covid19.Rating rating, String message, User user) {
        this.setRating(rating);
        this.setMessage(message);
        this.setUser(user);
    }

    // Rating value setter
    public void setRating(SafetyActivities4Covid19.Rating rating) {
    	this.rating = rating;
    }
    
    // Rating value getter
    public SafetyActivities4Covid19.Rating getRating() {
        return this.rating;
    }
    
    // Rating message setter
    public void setMessage(String message) {
    	this.message = message;
    }
    
    // Rating message getter
    public String getMessage() {
    	return this.message;
    }
    
    // Rating user setter
    public void setUser(User user) {
    	this.user = user;
    }
    
    // Rating user getter
    public User getUser() {
        return this.user;
    }
}
