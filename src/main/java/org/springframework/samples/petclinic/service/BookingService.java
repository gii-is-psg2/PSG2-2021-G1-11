package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

	private BookingRepository bookingRepository;

	@Autowired
	public BookingService(BookingRepository bookingRepository) {
		super();
		this.bookingRepository = bookingRepository;
	}

	private Boolean isBeforeOrEqual(LocalDate date1, LocalDate date2) {
		return date1.isBefore(date2) || date1.isEqual(date2);
	}

	private Boolean isAfterOrEqual(LocalDate date1, LocalDate date2) {
		return date1.isAfter(date2) || date1.isEqual(date2);
	}

	private Boolean doesBookingOverlaps(Booking booking) throws DataAccessException {
		boolean isOverlapping = false;

		LocalDate startDate = LocalDate.parse(booking.getStartDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		LocalDate finishDate = LocalDate.parse(booking.getFinishDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		List<Booking> bookings = bookingRepository.findAllByCancelledFalse();

		for (Booking b : bookings) {
			LocalDate bstartDate = LocalDate.parse(b.getStartDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			LocalDate bfinishDate = LocalDate.parse(b.getFinishDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

			// Overlapping cases
			if (isBeforeOrEqual(startDate, bfinishDate) && isAfterOrEqual(finishDate, bstartDate)) {
				isOverlapping = true;
				break;
			}
		}

		return isOverlapping;
	}

	@Transactional
	public Boolean saveBooking(Booking booking) throws DataAccessException {
		Boolean isOverlapping = (booking.isCancelled()) ? false : doesBookingOverlaps(booking);

		// Booking can be created
		if (!isOverlapping) {
			bookingRepository.save(booking);
		}

		return !isOverlapping;
	}

	@Transactional
	public Boolean renewBooking(Booking booking) throws DataAccessException {
		Boolean isOverlapping = doesBookingOverlaps(booking);

		// Booking can be created
		if (!isOverlapping) {
			booking.setCancelled(false);
			bookingRepository.save(booking);
		}

		return !isOverlapping;
	}

	@Transactional(readOnly = true)
	public Booking findBookingById(Integer bookingId) throws DataAccessException {
		return bookingRepository.findById(bookingId);
	}

	@Transactional
	public void removeBookingById(Integer bookingId) throws DataAccessException {
		bookingRepository.removeById(bookingId);
	}

}
