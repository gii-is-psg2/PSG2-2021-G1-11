package org.springframework.samples.petclinic.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AdoptionRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class AdoptionServiceTests {

	private static Pet pet;
	private static List<AdoptionApplication> adoptionApplications;
	private static AdoptionApplication adoptionApplication1;
	private static AdoptionApplication adoptionApplication2;
	private static Owner owner;

	@Mock
	PetService petService;

	@Mock
	AdoptionRepository adoptionApplicationRepository;

	@InjectMocks
	AdoptionService adoptionApplicationService;

	@BeforeEach
	void data() {
		pet = new Pet();
		pet.setId(1);

		owner = new Owner();
		owner.setId(1);

		adoptionApplication1 = new AdoptionApplication();
		adoptionApplication1.setRequestedPet(pet);
		adoptionApplication2 = new AdoptionApplication();
		adoptionApplications = new ArrayList<AdoptionApplication>();
		adoptionApplications.add(adoptionApplication1);
		adoptionApplications.add(adoptionApplication2);
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldAcceptAdoptionApplication() throws DataAccessException, DuplicatedPetNameException {
		when(adoptionApplicationRepository.findAdoptionApplicationByPet(pet.getId())).thenReturn(adoptionApplications);
		adoptionApplicationService.acceptAdoptionApplication(adoptionApplication1);

		verify(petService, times(1)).savePet(pet);
		verify(adoptionApplicationRepository, times(2)).delete(any(AdoptionApplication.class));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldGetPendingAdoptionApplication() {
		adoptionApplicationService.getPendingAdoptionApplication(owner);
		verify(adoptionApplicationRepository, times(1)).getPendingAdoptionApplication(any(Integer.class));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldGetRequestByApplicant() {
		adoptionApplicationService.getRequestsByApplicant(owner.getId());
		verify(adoptionApplicationRepository, times(1)).getRequestByApplicant(any(Integer.class));
	}
}
