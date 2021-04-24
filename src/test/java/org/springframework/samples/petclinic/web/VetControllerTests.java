package org.springframework.samples.petclinic.web;

import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.SpecialtyService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers = VetController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VetControllerTests {
	private static Integer TEST_VET_ID = 1;

	@MockBean
	private VetService clinicService;

	@MockBean
	private SpecialtyService specialtyService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {

		Vet james = new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(TEST_VET_ID);
		Vet helen = new Vet();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setId(2);
		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		helen.addSpecialty(radiology);
		given(this.clinicService.findVets()).willReturn(Lists.newArrayList(james, helen));
		given(this.clinicService.findVetById(TEST_VET_ID)).willReturn(james);
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVetListHtml() throws Exception {
		mockMvc.perform(get("/vets")).andExpect(status().isOk()).andExpect(model().attributeExists("vets"))
				.andExpect(view().name("vets/vetList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVetListXml() throws Exception {
		mockMvc.perform(get("/vets.xml").accept(MediaType.APPLICATION_XML)).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_XML_VALUE))
				.andExpect(content().node(hasXPath("/vets/vetList[id=1]/id")));
	}

	@WithMockUser(value = "spring")
	@Test
	void testRemoveVet() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/remove", TEST_VET_ID).with(csrf())).andExpect(status().isFound())
				.andExpect(redirectedUrl("/vets"));

		verify(clinicService, only()).removeVetById(TEST_VET_ID);
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewVetForm() throws Exception {
		mockMvc.perform(get("/vets/new")).andExpect(status().isOk())
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewVetFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf()).param("firstName", "George").param("lastName", "Fabra")
				.param("specialtiesLS", "radiology,surgery")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewVetFormHasErrors() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf()).param("lastName", "Alonso"))
				.andExpect(model().attributeHasErrors("vet")).andExpect(status().isOk())
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitVetUpdateForm() throws Exception {
		mockMvc.perform(get("/vets/{vetId}/edit", TEST_VET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("vet")).andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessVetUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID).with(csrf()).param("firstName", "Juana")
				.param("lastName", "Alonso").param("specialtiesLS", "surgery")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessVetUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/vets/{vetId}/edit", TEST_VET_ID).with(csrf()).param("lastName", "Alonso"))
				.andExpect(model().attributeHasErrors("vet")).andExpect(status().isOk())
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}
}
