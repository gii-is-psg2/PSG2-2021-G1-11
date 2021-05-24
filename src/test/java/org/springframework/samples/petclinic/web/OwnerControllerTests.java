
package org.springframework.samples.petclinic.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
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
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */

@WebMvcTest(value = OwnerController.class, includeFilters = @ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE), excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class OwnerControllerTests {
	private static final int TEST_OWNER_ID = 1;
	private static final String TEST_OWNER_NAME = "George";
	private static final int TEST_PET_ID = 2;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthoritiesService authoritiesService;

	@MockBean
	AdoptionService adoptionApplicationService;

	@MockBean
	private PetService petService;

	@Autowired
	private MockMvc mockMvc;

	private Owner george;

	@BeforeEach
	void setup() {

		this.george = new Owner();
		this.george.setId(OwnerControllerTests.TEST_OWNER_ID);
		this.george.setFirstName(OwnerControllerTests.TEST_OWNER_NAME);
		this.george.setLastName("Franklin");
		this.george.setAddress("110 W. Liberty St.");
		this.george.setCity("Madison");
		this.george.setTelephone("6085551023");
		BDDMockito.given(this.ownerService.findOwnerById(OwnerControllerTests.TEST_OWNER_ID)).willReturn(this.george);

	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
				.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testPutUpForAdoption() throws Exception {
		Pet pet = new Pet();
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		pet.setOwner(owner);

		BDDMockito.given(petService.findPetById(TEST_PET_ID)).willReturn(pet);
		BDDMockito.given(ownerService.getOwnerByUserName(anyString())).willReturn(owner);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/inAdoption", TEST_OWNER_ID,
						TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/myProfile"));
		verify(petService, times(1)).savePet(pet);
	}

	@WithMockUser(value = "spring")
	@Test
	void testPutUpForAdoptionWithException() throws Exception {
		Pet pet = new Pet();
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		pet.setOwner(owner);

		BDDMockito.given(petService.findPetById(TEST_PET_ID)).willReturn(pet);
		BDDMockito.given(ownerService.getOwnerByUserName(anyString())).willReturn(owner);
		doThrow(new DuplicatedPetNameException()).when(petService).savePet(pet);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/inAdoption", TEST_OWNER_ID,
						TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/myProfile"));
		verify(petService, times(1)).savePet(pet);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testPutUpForAdoptionWithoutPermission() throws Exception {
		Pet pet = new Pet();
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		pet.setOwner(owner);

		BDDMockito.given(petService.findPetById(TEST_PET_ID)).willReturn(pet);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/inAdoption", TEST_OWNER_ID,
						TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/myProfile"));
		verify(petService, times(0)).savePet(pet);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testPutUpForAdoptionWithoutPermissionNull() throws Exception {
		Pet pet = new Pet();
		Owner owner = new Owner();
		owner.setId(TEST_OWNER_ID);
		pet.setOwner(owner);
		Owner notOwner = new Owner();
		notOwner.setId(99999);
		
		BDDMockito.given(petService.findPetById(TEST_PET_ID)).willReturn(pet);
		BDDMockito.given(ownerService.getOwnerByUserName(anyString())).willReturn(notOwner);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/inAdoption", TEST_OWNER_ID,
						TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/myProfile"));
		verify(petService, times(0)).savePet(pet);
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/new").param("firstName", "Joe")
				.param("lastName", "Bloggs").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("address", "123 Caramel Street").param("city", "London").param("telephone", "612345678"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("firstName", "Joe").param("lastName", "Bloggs").param("city", "London"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/find"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
				.andExpect(MockMvcResultMatchers.view().name("owners/findOwners"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormSuccess() throws Exception {
		BDDMockito.given(this.ownerService.findOwnerByLastName(""))
				.willReturn(Lists.newArrayList(this.george, new Owner()));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("owners/ownersList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormByLastName() throws Exception {
		BDDMockito.given(this.ownerService.findOwnerByLastName(this.george.getLastName()))
				.willReturn(Lists.newArrayList(this.george));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "Franklin"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/" + OwnerControllerTests.TEST_OWNER_ID));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFindFormNoOwnersFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "Unknown Surname"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "lastName"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
				.andExpect(MockMvcResultMatchers.view().name("owners/findOwners"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateOwnerForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/edit", OwnerControllerTests.TEST_OWNER_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("lastName", Matchers.is("Franklin"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("firstName", Matchers.is("George"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("address", Matchers.is("110 W. Liberty St."))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("city", Matchers.is("Madison"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("telephone", Matchers.is("6085551023"))))
				.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", OwnerControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe")
						.param("lastName", "Bloggs").param("address", "123 Caramel Street").param("city", "London")
						.param("telephone", "612345678"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", OwnerControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe")
						.param("lastName", "Bloggs").param("city", "London"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}", OwnerControllerTests.TEST_OWNER_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("lastName", Matchers.is("Franklin"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("firstName", Matchers.is("George"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("address", Matchers.is("110 W. Liberty St."))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("city", Matchers.is("Madison"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("telephone", Matchers.is("6085551023"))))
				.andExpect(MockMvcResultMatchers.view().name("owners/ownerDetails"));
	}

	@WithMockUser(username = "owner1")
	@Test
	void testRemoveOwner() throws Exception {
		BDDMockito.given(this.ownerService.getOwnerByUserName("owner1")).willReturn(this.george);
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/remove", OwnerControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
	}

	@WithMockUser(username = "owner1")
	@Test
	void testNotRemoveOwner() throws Exception {
		BDDMockito.given(this.ownerService.getOwnerByUserName("owner1")).willReturn(new Owner());
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/remove", OwnerControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/"));

	}

	@WithMockUser(username = "owner1")
	@Test
	void testNotRemoveNullOwner() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/remove", OwnerControllerTests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testObtainMyProfile() throws Exception {
		BDDMockito.given(this.ownerService.getOwnerByUserName(ArgumentMatchers.any(String.class)))
				.willReturn(new Owner());
		BDDMockito
				.given(this.adoptionApplicationService.getPendingAdoptionApplication(ArgumentMatchers.any(Owner.class)))
				.willReturn(new ArrayList<AdoptionApplication>());
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/myProfile"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("owners/ownerDetails"));
	}

}
