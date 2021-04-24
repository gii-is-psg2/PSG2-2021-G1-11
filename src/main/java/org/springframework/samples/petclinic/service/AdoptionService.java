package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AdoptionRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;

@Service
public class AdoptionService {

	private final AdoptionRepository adoptionApplicationRepository;
	private final PetService petService;

	@Autowired
	public AdoptionService(final AdoptionRepository adoptionApplicationRepository,
			final PetService petService) {
		super();
		this.adoptionApplicationRepository = adoptionApplicationRepository;
		this.petService = petService;
	}

	public List<AdoptionApplication> getPendingAdoptionApplication(final Owner owner) {
		return this.adoptionApplicationRepository.getPendingAdoptionApplication(owner.getId());
	}

	public AdoptionApplication findById(final int applicationId) {
		return this.adoptionApplicationRepository.findById(applicationId).orElse(null);
	}
	
	public void save(final AdoptionApplication adoptionApplication) {
		this.adoptionApplicationRepository.save(adoptionApplication);
	}
	
	public AdoptionApplication findByApplicantAndRequestedPet(final Owner applicant, final Pet pet) {
		return this.adoptionApplicationRepository.findByApplicantAndRequestedPet(applicant, pet);
	}
	
	public void acceptAdoptionApplication(final AdoptionApplication adopApp)
		throws DataAccessException, DuplicatedPetNameException {
		final Pet pet = adopApp.getRequestedPet();
		final Owner newOwner = adopApp.getApplicant();
		pet.setinAdoption(false);
		pet.setOwner(newOwner);
		this.petService.savePet(pet);

		// Borrar todas las solicitudes de adopcion que tenia esa mascota

		final List<AdoptionApplication> adoptionApplicationByPet = this.adoptionApplicationRepository.findAdoptionApplicationByPet(pet.getId());
		for (final AdoptionApplication adoptionApplication : adoptionApplicationByPet) {
			this.adoptionApplicationRepository.delete(adoptionApplication);
		}
	}

	public void declineAdoptionApplication(final int applicationId) {
		this.adoptionApplicationRepository.deleteById(applicationId);
	}

	public List<AdoptionApplication> getRequestsByApplicant(final int ownerId) {
		return this.adoptionApplicationRepository.getRequestByApplicant(ownerId);
	}

}
