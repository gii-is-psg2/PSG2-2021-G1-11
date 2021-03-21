package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.BookingService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookingController {

	private BookingService bookingService;

	@Autowired
	public BookingController(BookingService bookingService) {
		super();
		this.bookingService = bookingService;
	}

	@GetMapping(value = "/owners/*/pets/{petId}/booking/new")
	public String initNewBookingForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		Booking booking = new Booking(); 
		model.put("booking", booking);
		return "pets/createBookingForm";
	}
	
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/booking/new")
	public String processNewBookingForm(@Valid Booking booking, @PathVariable("petId") int petId, BindingResult result) {
		if (result.hasErrors()) {
			return "pets/createBookingForm";
		}else {
			this.bookingService.saveBooking(booking,petId);
			return "redirect:/owners/{ownerId}";
		}
	}

	
	
	
	
	
}
