package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Booking;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class BookingDateValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Booking.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
		Booking booking = (Booking) obj;
		LocalDate startDate = booking.getStartDate();
		LocalDate finishDate = booking.getFinishDate();

		if(startDate != null && finishDate != null && startDate.isAfter(finishDate)) {
			errors.rejectValue("startDate", "The start date must be before end date", "The start date must be before end date");
		}
		
		if(startDate == null) {
			errors.rejectValue("startDate", "Required field", "Required field");
		}
		if(finishDate == null) {
			errors.rejectValue("finishDate", "Required field", "Required field");
		}
	}

	

	


}
