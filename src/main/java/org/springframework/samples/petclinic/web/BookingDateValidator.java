package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.samples.petclinic.model.Booking;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class BookingDateValidator implements Validator {
	
	@Override
	public void validate(Object obj, Errors errors) {
		
		Booking booking = (Booking) obj;
		String startDate = booking.getStartDate();
		String finishDate = booking.getFinishDate();
		LocalDate today = LocalDate.now();
		Integer ac = 0;
		
		try {
			if(startDate.isBlank()) {
				errors.rejectValue("startDate", "Required field", "Required field");
			}else {
				LocalDate date = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
				if(date.isBefore(today)) {
					errors.rejectValue("startDate", "Start date must be greater than today's date", "Start date must be greater than today's date");
				}else {
					ac += 1;
				}
			}
		}catch(Exception e) {
			errors.rejectValue("startDate", "Incorrect date format. It must be yyyy/mm/dd", "Incorrect date format. It must be yyyy/mm/dd");
		}
		
		try {
			if(finishDate.isBlank()) {
				errors.rejectValue("finishDate", "Required field", "Required field");
			}else {
				LocalDate.parse(finishDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
				ac += 1;
			}
		}catch(Exception e) {
			errors.rejectValue("finishDate", "Incorrect date format. It must be yyyy/mm/dd", "Incorrect date format. It must be yyyy/mm/dd");
		}
		
		if(ac == 2) {	//There are not errors
			LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			LocalDate end = LocalDate.parse(finishDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			if(start.isAfter(end)) {
				errors.rejectValue("finishDate", "End date must be greater than Start date", "End date must be greater than Start date");
			}
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Booking.class.isAssignableFrom(clazz);
	}

	


}
