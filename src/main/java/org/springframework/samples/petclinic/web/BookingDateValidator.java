package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.samples.petclinic.model.Booking;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BookingDateValidator implements Validator {

	public static final String START_DATE = "startDate";
	public static final String DATE_FORMAT = "yyyy/MM/dd";
	public static final String FINISH_DATE = "finishDate";
	
	@Override
	public void validate(Object obj, Errors errors) {
		Booking booking = (Booking) obj;
		String startDate = booking.getStartDate();
		String finishDate = booking.getFinishDate();
		LocalDate today = LocalDate.now();

		if (startDate.isEmpty() && startDate.trim().length() == 0) {
			errors.rejectValue(START_DATE, "required");
		} else {
			LocalDate date;
			try {
				date = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
				if (date.isBefore(today)) {
					errors.rejectValue(START_DATE, "dateBeforeToday");
				}
			} catch (Exception e) {
				errors.rejectValue(START_DATE, "invalidDateFormat");
			}
		}

		if (finishDate.isEmpty() && finishDate.trim().length() == 0) {
			errors.rejectValue(FINISH_DATE, "required");
		} else {
			try {
				LocalDate.parse(finishDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
			} catch (Exception e) {
				errors.rejectValue(FINISH_DATE, "invalidDateFormat");
			}
		}

		if (!errors.hasErrors()) { // There are not errors
			LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
			LocalDate end = LocalDate.parse(finishDate, DateTimeFormatter.ofPattern(DATE_FORMAT));
			if (start.isAfter(end)) {
				errors.rejectValue(FINISH_DATE, "endAfterStart");
			}
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Booking.class.isAssignableFrom(clazz);
	}

}
