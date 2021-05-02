
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = AdoptionController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class AdoptionControllerTests {

	private static final String VIEW_CREATE_ADOPTION_APPLICATION = "adoptions/createAdoptionApplication";
	private static final int PET_ADOPTABLE_ID = 1;
	private static final int PET_NOT_ADOPTABLE_ID = 2;
	private static final String TEST_OWNER_NAME = "owner1";
	private static final int APPLICATION_ID = 1;
	private static final int OWNER_ID = 1;

	@MockBean
	private AdoptionService adoptionApplicationService;

	@MockBean
	private PetService petService;

	@MockBean
	private OwnerService ownerService;

	@Autowired
	private MockMvc mockMvc;

	private Owner owner;
	private AdoptionApplication adoptionApplication;

	@BeforeEach
	void setup() {
		this.owner = new Owner();
		this.owner.setFirstName(AdoptionControllerTests.TEST_OWNER_NAME);
		this.owner.setId(AdoptionControllerTests.OWNER_ID);

		this.adoptionApplication = new AdoptionApplication();
		this.adoptionApplication.setId(AdoptionControllerTests.APPLICATION_ID);
		final Pet pet = new Pet();
		pet.setOwner(this.owner);
		this.adoptionApplication.setRequestedPet(pet);

		final Pet adoptablePet = new Pet();
		adoptablePet.setinAdoption(true);
		BDDMockito.given(this.petService.findPetById(AdoptionControllerTests.PET_ADOPTABLE_ID))
				.willReturn(adoptablePet);
		BDDMockito.given(this.petService.findPetById(AdoptionControllerTests.PET_NOT_ADOPTABLE_ID))
				.willReturn(new Pet());
		BDDMockito.given(this.ownerService.getOwnerByUserName("spring")).willReturn(new Owner());
	}

	@WithMockUser(username = "owner1")
	@Test
	void testObtainPendingApplications() throws Exception {
		BDDMockito.given(this.ownerService.getOwnerByUserName(AdoptionControllerTests.TEST_OWNER_NAME))
				.willReturn(this.owner);
		BDDMockito.given(this.adoptionApplicationService.getPendingAdoptionApplication(this.owner))
				.willReturn(new ArrayList<AdoptionApplication>());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/adoptions/applications"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("owners/ownerAdoptionApplication"));
	}

	@WithMockUser(username = "owner")
	@Test
	void testAcceptAdoptionApplication() throws Exception {
		BDDMockito.given(this.adoptionApplicationService.findById(AdoptionControllerTests.APPLICATION_ID))
				.willReturn(this.adoptionApplication);
		BDDMockito.given(this.ownerService.getOwnerByUserName("owner")).willReturn(this.owner);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/adoptions/{applications_id}/accept",
						AdoptionControllerTests.APPLICATION_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/adoptions/applications"));

		Mockito.verify(this.adoptionApplicationService, Mockito.times(1))
				.acceptAdoptionApplication(this.adoptionApplication);
	}

	@WithMockUser(username = "owner1")
	@Test
	void testNotAcceptApplicationBecauseOfIncorrectPrincipal() throws Exception {
		BDDMockito.given(this.adoptionApplicationService.findById(AdoptionControllerTests.APPLICATION_ID))
				.willReturn(this.adoptionApplication);
		BDDMockito.given(this.ownerService.getOwnerByUserName("owner1")).willReturn(new Owner());

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/adoptions/{applications_id}/accept",
						AdoptionControllerTests.APPLICATION_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/adoptions/applications"));

		Mockito.verify(this.adoptionApplicationService, Mockito.times(0))
				.acceptAdoptionApplication(this.adoptionApplication);
	}

	@WithMockUser(username = "owner")
	@Test
	void testDeclineAdoptionApplication() throws Exception {
		BDDMockito.given(this.adoptionApplicationService.findById(AdoptionControllerTests.APPLICATION_ID))
				.willReturn(this.adoptionApplication);
		BDDMockito.given(this.ownerService.getOwnerByUserName("owner")).willReturn(this.owner);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/adoptions/{applications_id}/decline",
						AdoptionControllerTests.APPLICATION_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/adoptions/applications"));

		Mockito.verify(this.adoptionApplicationService, Mockito.times(1))
				.declineAdoptionApplication(AdoptionControllerTests.APPLICATION_ID);
	}

	@WithMockUser(username = "owner1")
	@Test
	void testNotDeclineApplicationBecauseOfIncorrectPrincipal() throws Exception {
		BDDMockito.given(this.adoptionApplicationService.findById(AdoptionControllerTests.APPLICATION_ID))
				.willReturn(this.adoptionApplication);
		BDDMockito.given(this.ownerService.getOwnerByUserName("owner1")).willReturn(new Owner());

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/adoptions/{applications_id}/decline",
						AdoptionControllerTests.APPLICATION_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/adoptions/applications"));

		Mockito.verify(this.adoptionApplicationService, Mockito.times(0))
				.declineAdoptionApplication(AdoptionControllerTests.APPLICATION_ID);
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitAdoptionApplicationForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/adoptions/pets/{petId}/apply",
						AdoptionControllerTests.PET_ADOPTABLE_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name(AdoptionControllerTests.VIEW_CREATE_ADOPTION_APPLICATION));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNotAdoptableApplicationForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/adoptions/pets/{petId}/apply",
						AdoptionControllerTests.PET_NOT_ADOPTABLE_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateAdoptionApplication() throws Exception {
		final Owner mockOwner = new Owner();
		mockOwner.setId(1);
		final Pet mockPet = new Pet();
		mockPet.setinAdoption(true);
		mockPet.setOwner(mockOwner);

		BDDMockito.given(this.petService.findPetById(1)).willReturn(mockPet);
		BDDMockito.given(this.ownerService.getOwnerByUserName(ArgumentMatchers.anyString())).willReturn(new Owner());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/adoptions/pets/{petId}/apply", 1)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "Hey! I want to take care of your pet!"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateAdoptionApplicationNoDescription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/adoptions/pets/{petId}/apply", AdoptionControllerTests.PET_ADOPTABLE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "         "))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("adoptions/createAdoptionApplication"))
				.andExpect(MockMvcResultMatchers.model().hasErrors());
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateAdoptionApplicationInvalidPet() throws Exception {
		final int INVALID_PET_ID = 666;
		BDDMockito.given(this.petService.findPetById(INVALID_PET_ID)).willReturn(null);

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/adoptions/pets/{petId}/apply", INVALID_PET_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "Hey! I want to take care of your pet!"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("adoptions/createAdoptionApplication"))
				.andExpect(MockMvcResultMatchers.model().hasErrors());
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateAdoptionApplicationNotAdoptablePet() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/adoptions/pets/{petId}/apply", AdoptionControllerTests.PET_NOT_ADOPTABLE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "Hey! I want to take care of your pet!"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("adoptions/createAdoptionApplication"))
				.andExpect(MockMvcResultMatchers.model().hasErrors());
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateAdoptionApplicationAlreadySent() throws Exception {
		BDDMockito.given(this.adoptionApplicationService
				.findByApplicantAndRequestedPet(ArgumentMatchers.any(Owner.class), ArgumentMatchers.any(Pet.class)))
				.willReturn(new AdoptionApplication());

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/adoptions/pets/{petId}/apply", AdoptionControllerTests.PET_ADOPTABLE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "Hey! I want to take care of your pet again!"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("adoptions/createAdoptionApplication"))
				.andExpect(MockMvcResultMatchers.model().hasErrors());
	}

	@WithMockUser(value = "spring")
	@Test
	void testCreateAdoptionApplicationAlreadyOwned() throws Exception {
		final Owner mockOwner = new Owner();
		mockOwner.setId(1);
		final Pet mockPet = new Pet();
		mockPet.setinAdoption(true);
		mockPet.setOwner(mockOwner);

		BDDMockito.given(this.petService.findPetById(1)).willReturn(mockPet);
		BDDMockito.given(this.ownerService.getOwnerByUserName(ArgumentMatchers.anyString())).willReturn(mockOwner);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/adoptions/pets/{petId}/apply", AdoptionControllerTests.PET_ADOPTABLE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "Hey! I want to take care of your pet again!"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("adoptions/createAdoptionApplication"))
				.andExpect(MockMvcResultMatchers.model().hasErrors());
	}

	@WithMockUser(value = "spring")
	@Test
	void testInAdoptionList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adoptions/pets"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("pets"))
				.andExpect(MockMvcResultMatchers.view().name("adoptions/listPetsInAdoption"));
	}
}
