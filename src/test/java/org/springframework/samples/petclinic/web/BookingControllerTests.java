package org.springframework.samples.petclinic.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.BookingService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = BookingController.class,
includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class BookingControllerTests {
	
	private static final String VIEW_CREATE_BOOKING_FORM = "pets/createBookingForm";
	private static final int PET_ID = 1;
	private static final int OWNER_ID = 1;

	@Autowired
	private BookingController bookingController;
	
	@MockBean
	private BookingService bookingService;
	
	@MockBean
	private PetService petService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach
	void setup() {
		given(petService.findPetById(PET_ID)).willReturn(new Pet());
	}
	
	@WithMockUser(value = "spring")
    @Test
	void testInitCreationBookingForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/booking/new", PET_ID)).andExpect(status().isOk())
			.andExpect(view().name(VIEW_CREATE_BOOKING_FORM));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSucess() throws Exception {
		given(bookingService.saveBooking(any(Booking.class))).willReturn(true);
		
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/booking/new", OWNER_ID, PET_ID)
				.with(csrf())
				.param("startDate", "3080/01/01")
				.param("finishDate", "3080/02/02"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormFailure() throws Exception {
		given(bookingService.saveBooking(any(Booking.class))).willReturn(false);
		
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/booking/new", OWNER_ID, PET_ID)
				.with(csrf())
				.param("startDate", "3080/01/01")
				.param("finishDate", "3080/02/02"))
			.andExpect(view().name(VIEW_CREATE_BOOKING_FORM));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormStartDateNull() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/booking/new", OWNER_ID, PET_ID)
				.with(csrf())
				.param("startDate", "")
				.param("finishDate", "2020/02/02"))
			.andExpect(model().attributeHasErrors("booking")).andExpect(status().isOk())
			.andExpect(view().name(VIEW_CREATE_BOOKING_FORM));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormEndDateNull() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/booking/new", OWNER_ID, PET_ID)
				.with(csrf())
				.param("startDate", "2020/01/02")
				.param("finishDate", ""))
			.andExpect(model().attributeHasErrors("booking")).andExpect(status().isOk())
			.andExpect(view().name(VIEW_CREATE_BOOKING_FORM));
	}
		
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormStartDateGreaterThanEndDate() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/booking/new", OWNER_ID, PET_ID)
				.with(csrf())
				.param("startDate", "3059/03/03")
				.param("finishDate", "3059/02/02"))
			.andExpect(model().attributeHasErrors("booking")).andExpect(status().isOk())
			.andExpect(view().name(VIEW_CREATE_BOOKING_FORM));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormIncorrectDateFormat() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/booking/new", OWNER_ID, PET_ID)
				.with(csrf())
				.param("startDate", "20210202")
				.param("finishDate", "20200303"))
			.andExpect(model().attributeHasErrors("booking")).andExpect(status().isOk())
			.andExpect(view().name(VIEW_CREATE_BOOKING_FORM));
	}

	
}
