package org.springframework.samples.petclinic.web;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = DonationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class DonationControllerTests {
	private static final String VIEWS_DONATIONS_CREATE_FORM = "donations/createDonationForm";
	private static final int CAUSE_ID = 1;
	private static Cause causa;
	@Autowired
	private DonationController donationController;

	@MockBean
	private DonationService donationService;

	@MockBean
	private CauseService causeService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {
		causa = new Cause();
		causa.setTarget(12.0);
		causa.setIsClosed(false);
		given(causeService.findCauseById(CAUSE_ID)).willReturn(causa);
		given(causeService.actualAmountById(CAUSE_ID)).willReturn(0.0);
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationDonationForm() throws Exception {
		mockMvc.perform(get("/causes/{causeId}/donations/new", CAUSE_ID)).andExpect(status().isOk())
				.andExpect(view().name(VIEWS_DONATIONS_CREATE_FORM));
	}

	@WithMockUser(value = "spring")
	@Test
	void processCreationDonationFormSucess() throws Exception {
		mockMvc.perform(post("/causes/{causeId}/donations/new", CAUSE_ID).with(csrf()).param("amount", "2.0"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/causes/{causeId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void processCreationDonationFormError() throws Exception {
		causa.setIsClosed(true);
		given(causeService.findCauseById(CAUSE_ID)).willReturn(causa);
		mockMvc.perform(post("/causes/{causeId}/donations/new", CAUSE_ID).with(csrf()).param("amount", "hola"))
				.andExpect(model().hasErrors()).andExpect(view().name(VIEWS_DONATIONS_CREATE_FORM));
	}

	@WithMockUser(value = "spring")
	@Test
	void processCreationDonationFormCloseCauseError() throws Exception {
		causa.setIsClosed(true);
		given(causeService.findCauseById(CAUSE_ID)).willReturn(causa);
		mockMvc.perform(post("/causes/{causeId}/donations/new", CAUSE_ID).with(csrf()).param("amount", "2.0"))
				.andExpect(model().hasErrors()).andExpect(view().name(VIEWS_DONATIONS_CREATE_FORM));
	}

}
