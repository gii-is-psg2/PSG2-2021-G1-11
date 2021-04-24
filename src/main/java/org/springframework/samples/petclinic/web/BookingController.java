package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.BookingService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BookingController {
	private BookingService bookingService;
	private PetService petService;

	private static final String VIEW_CREATE_BOOKING_FORM = "pets/createBookingForm";

	@Autowired
	public BookingController(BookingService bookingService, PetService petService) {
		super();
		this.bookingService = bookingService;
		this.petService = petService;
	}

	@InitBinder("booking")
	public void initBookingBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new BookingDateValidator());
	}

	@ModelAttribute("booking")
	public Booking loadPetWithBooking(@PathVariable("petId") int petId) {
		Pet pet = petService.findPetById(petId);
		Booking booking = new Booking();
		booking.setPet(pet);
		return booking;
	}

	@GetMapping(value = "/owners/*/pets/{petId}/booking/new")
	public String initNewBookingForm(@PathVariable("petId") int petId, ModelMap model) {
		return VIEW_CREATE_BOOKING_FORM;
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/booking/new")
	public String processNewBookingForm(@Valid Booking booking, BindingResult result) {
		if (result.hasErrors()) {
			return VIEW_CREATE_BOOKING_FORM;
		} else {
			boolean existBooking = bookingService.saveBooking(booking);
			// booking created
			if (existBooking) {
				return "redirect:/owners/{ownerId}";

				// booking not created because of errors
			} else {
				result.rejectValue("finishDate", "bookingConflicts");
				return VIEW_CREATE_BOOKING_FORM;
			}
		}
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/booking/{bookingId}/cancel")
	public String cancelBooking(@PathVariable Integer bookingId, @PathVariable Integer petId) {
		Booking booking = bookingService.findBookingById(bookingId);
		if (booking == null) {
			return "redirect:/owners/{ownerId}";
		}

		booking.setCancelled(true);
		bookingService.saveBooking(booking);
		return "redirect:/owners/{ownerId}";
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/booking/{bookingId}/renew")
	public String renewBooking(@PathVariable Integer ownerId, @PathVariable Integer petId,
			@PathVariable Integer bookingId) {
		Booking booking = bookingService.findBookingById(bookingId);
		if (booking == null) {
			return "redirect:/owners/{ownerId}";
		}

		if (!bookingService.renewBooking(booking)) {
			Pet pet = petService.findPetById(petId);
			pet.removeBooking(booking);
			bookingService.removeBookingById(bookingId);
			return "pets/renewBookingFailed";
		}

		return "redirect:/owners/{ownerId}";
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/booking/{bookingId}/remove")
	public String removeBooking(@PathVariable Integer bookingId, @PathVariable Integer petId) {
		Pet pet = petService.findPetById(petId);
		Booking booking = bookingService.findBookingById(bookingId);
		pet.removeBooking(booking);
		bookingService.removeBookingById(bookingId);
		return "redirect:/owners/{ownerId}";
	}

}
