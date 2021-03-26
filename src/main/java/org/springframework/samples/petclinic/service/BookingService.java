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
	
	@Transactional(readOnly = true)
	public Boolean saveBooking(Booking booking) throws DataAccessException {
		
		LocalDate startDate = LocalDate.parse(booking.getStartDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		LocalDate finishDate = LocalDate.parse(booking.getFinishDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		
		List<Booking> bookings = bookingRepository.findAll();
		Integer ac  = 0;
		
		for(Booking b : bookings) {
			LocalDate bstartDate = LocalDate.parse(b.getStartDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
			LocalDate bfinishDate = LocalDate.parse(b.getFinishDate(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));

			//Overlapping cases
			
			if(((bfinishDate.isBefore(finishDate) || bfinishDate.equals(finishDate)) 
					&& (startDate.isBefore(bfinishDate)) || startDate.equals(bfinishDate)) || 
					
					((bstartDate.isBefore(finishDate) || bstartDate.equals(finishDate)) 
	                && (bstartDate.isAfter(startDate) || bstartDate.equals(startDate)))|| 
					
					((bstartDate.isBefore(startDate) || bstartDate.equals(startDate))
	                && (bfinishDate.isAfter(finishDate) || bfinishDate.equals(finishDate))) || 
					
					((bstartDate.isAfter(startDate) || bstartDate.equals(startDate))
					&& (bfinishDate.isBefore(finishDate) || bfinishDate.equals(finishDate)))){
				
				ac += 1;
			}
		}
		
		//Booking can be created
		if(ac == 0) {
			bookingRepository.save(booking);
			return true;
		
		//Booking can not be created
		}else {
			return false;
		}
	}
	
	
}
