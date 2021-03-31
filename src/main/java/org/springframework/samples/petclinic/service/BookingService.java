package org.springframework.samples.petclinic.service;

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

	@Transactional(readOnly = true)
	public Booking findBookingById(Integer bookingId) throws DataAccessException {
		return bookingRepository.findById(bookingId);
	}
	
	@Transactional
	public void saveBooking(Booking booking) throws DataAccessException {
		bookingRepository.save(booking);
	}

	@Transactional
	public void removeBookingById(Integer bookingId) throws DataAccessException {
		bookingRepository.removeById(bookingId);
	}
}
