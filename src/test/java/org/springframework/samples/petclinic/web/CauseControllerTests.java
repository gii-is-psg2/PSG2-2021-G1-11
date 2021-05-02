package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.mockito.ArgumentMatchers;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = CauseController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class CauseControllerTests {
	private static final int TEST_CAUSE_ID = 1;

	@MockBean
	private CauseService causeService;

	@MockBean
	private DonationService donationService;
	
	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;


	@BeforeEach
	void setup() {
		final Cause cause = new Cause();
		cause.setDescription("Queremos salvar a los pingüinos");
		cause.setName("Antártida");
		cause.setIsClosed(false);
		cause.setOrganization("Greenpeace");
		cause.setTarget(2000.);
		
		final User user = new User();
		user.setUsername("Lola");
		cause.setFounder(user);
		
		BDDMockito.given(this.causeService.findCauseById(CauseControllerTests.TEST_CAUSE_ID)).willReturn(cause);
		BDDMockito.given(this.userService.findUser("Lola")).willReturn(Optional.of(user));
	}

	@WithMockUser(value = "spring")
	@Test
	void testListCauses() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("causes/causeList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("map"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testListCausesWithData() throws Exception {
		final Cause cause = new Cause();
		cause.setDescription("Queremos salvar a los pingüinos");
		cause.setName("Antártida");
		cause.setIsClosed(false);
		cause.setOrganization("Greenpeace");
		cause.setTarget(2000.);

		final List<Cause> causeList = new ArrayList<>();
		causeList.add(cause);

		final List<Double> donationList = new ArrayList<>();
		donationList.add(20.0);

		BDDMockito.given(this.causeService.findCauses()).willReturn(causeList);
		BDDMockito.given(this.donationService.findDonationsByCauses(causeList)).willReturn(donationList);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("causes/causeList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("map"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cause"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/causes/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "Selva").param("description", "Salvar a los leones").param("target", "200")
						.param("organization", "Save The Children"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/causes"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/causes/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "Selva").param("description", "Salvar a los leones").param("target", "A")
						.param("organization", "Save The Children"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("cause"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowCause() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/{causeId}", CauseControllerTests.TEST_CAUSE_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("cause"))
				.andExpect(MockMvcResultMatchers.view().name("causes/causeDetails"));
	}
	
	@WithMockUser(username = "marrambla2")
	@Test
	void initForbiddenUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/{causeId}/edit", CauseControllerTests.TEST_CAUSE_ID))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/causes/{causeId}"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(username = "Lola")
	@Test
	void initUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/causes/{causeId}/edit", CauseControllerTests.TEST_CAUSE_ID))
				.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@WithMockUser(username = "Lola")
	@Test
	void processUpdateInvalidTargetCauseForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/{causeId}/edit", CauseControllerTests.TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Selva").param("description", "Salvar a los leones").param("target", "200")
				.param("organization", "Save The Children"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));
			
	}
	
	@WithMockUser(username = "marrambla2")
	@Test
	void processForbiddenUpdateCause() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/{causeId}/edit", CauseControllerTests.TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Selva").param("description", "Salvar a los leones").param("target", "200")
				.param("organization", "Save The Children"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/causes/{causeId}"));	
	}
	
	@WithMockUser(username = "Lola")
	@Test
	void processUpdateFailedCauseForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/{causeId}/edit", CauseControllerTests.TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "Salvar a los leones").param("target", "200")
				.param("organization", "Save The Children"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("causes/createOrUpdateCauseForm"));		
	}
	
	@WithMockUser(username = "Lola")
	@Test
	void processUpdateCauseForm() throws Exception {
		BDDMockito.given(this.causeService.checkCauseEditability(ArgumentMatchers.eq(TEST_CAUSE_ID), ArgumentMatchers.anyDouble())).willReturn(true);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/{causeId}/edit", CauseControllerTests.TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("name", "Selva").param("description", "Salvar a los leones").param("target", "3000")
				.param("organization", "Save The Children"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/causes/{causeId}"));
	}
	
	@WithMockUser(username = "Lola")
	@Test
	void removeAlreadyDonatedCause() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/{causeId}/remove", CauseControllerTests.TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.flash().attribute("status", "canNotRemoveCause"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/causes/{causeId}"));
	}
	
	@WithMockUser(username = "marrambla2")
	@Test
	void forbiddenRemoveCause() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/{causeId}/remove", CauseControllerTests.TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/causes/{causeId}"));
	}
	
	@WithMockUser(username = "Lola")
	@Test
	void removeCause() throws Exception {
		BDDMockito.given(this.causeService.checkCauseRemovability(TEST_CAUSE_ID)).willReturn(true);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/causes/{causeId}/remove", CauseControllerTests.TEST_CAUSE_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/"));
	}
	
}
