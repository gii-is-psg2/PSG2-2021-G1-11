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

		if (startDate.isEmpty() && startDate.trim().length() == 0) {
			errors.rejectValue("startDate", "required");
		} else {
			LocalDate date;
			try {
				date = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
				if (date.isBefore(today)) {
					errors.rejectValue("startDate", "dateBeforeToday");
				}
			} catch (Exception e) {
				errors.rejectValue("startDate", "invalidDateFormat");
			}
		}

		if (finishDate.isEmpty() && finishDate.trim().length() == 0) {
			errors.rejectValue("finishDate", "required");
		} else {
			try {
				LocalDate.parse(finishDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			} catch (Exception e) {
				errors.rejectValue("finishDate", "invalidDateFormat");
			}
		}

		if (!errors.hasErrors()) { // There are not errors
			LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			LocalDate end = LocalDate.parse(finishDate, DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			if (start.isAfter(end)) {
				errors.rejectValue("finishDate", "endAfterStart");
			}
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Booking.class.isAssignableFrom(clazz);
	}

}
