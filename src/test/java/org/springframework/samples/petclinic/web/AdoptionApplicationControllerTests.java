package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionApplicationService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AdoptionApplicationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class AdoptionApplicationControllerTests {

	private static final String TEST_OWNER_NAME = "owner1";

	@Autowired
	private AdoptionApplicationController adoptionApplicationController;

	@MockBean
	private AdoptionApplicationService adoptionApplicationService;

	@MockBean
	private OwnerService ownerService;

	@Autowired
	private MockMvc mockMvc;

	private Owner owner;

	@BeforeEach
	void setup() {
		owner = new Owner();
		owner.setFirstName(TEST_OWNER_NAME);
	}

	@WithMockUser(username = "owner1")
	@Test
	void testObtainPendingApplications() throws Exception {
		given(ownerService.getOwnerByUserName(TEST_OWNER_NAME)).willReturn(owner);
		given(adoptionApplicationService.getPendingAdoptionApplication(owner))
				.willReturn(new ArrayList<AdoptionApplication>());

		mockMvc.perform(get("/adoptions/applications")).andExpect(status().isOk())
				.andExpect(view().name("owners/ownerAdoptionApplication"));
	}

}
