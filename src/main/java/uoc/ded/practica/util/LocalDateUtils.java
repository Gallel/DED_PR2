package uoc.ded.practica.util;

import java.time.LocalDate;

public class LocalDateUtils {

	public static int getYearsBetweenLocaleDates(LocalDate date1, LocalDate date2) {
    	int years = date2.getYear() - date1.getYear();
    	
    	if (date2.getDayOfYear() > date1.getDayOfYear())
    		years--;
    	
    	return years;
    }
	
}
