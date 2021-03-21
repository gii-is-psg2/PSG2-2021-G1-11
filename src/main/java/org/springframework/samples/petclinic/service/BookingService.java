package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
	
	private BookingRepository bookingRepository;
	private PetService petService;

	@Autowired
	public BookingService(BookingRepository bookingRepository, PetService petService) {
		super();
		this.bookingRepository = bookingRepository;
		this.petService = petService;
	}
	
	public void saveBooking(Booking booking, int petId) {
		Pet pet = this.petService.findPetById(petId);
		booking.setPet(pet);
		bookingRepository.save(booking);
	}

	
	
	
}
