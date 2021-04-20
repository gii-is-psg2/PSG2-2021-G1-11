package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AdoptionApplicationRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;

@Service
public class AdoptionApplicationService {

	private AdoptionApplicationRepository adoptionApplicationRepository;
	private PetService petService;
	
	@Autowired
	public AdoptionApplicationService(AdoptionApplicationRepository adoptionApplicationRepository, PetService petService) {
		super();
		this.adoptionApplicationRepository = adoptionApplicationRepository;
		this.petService = petService; 
	}

	public List<AdoptionApplication> getPendingRequest(Owner owner) {
		return adoptionApplicationRepository.getPendingRequest(owner.getId());
	}

	public AdoptionApplication findById(int requestId) {
		return adoptionApplicationRepository.findById(requestId).orElse(null);
	}
	
	public void save(AdoptionApplication adoptionApplication) {
		adoptionApplicationRepository.save(adoptionApplication);
	}
	
	public AdoptionApplication findByApplicantAndRequestedPet(Owner applicant, Pet pet) {
		return adoptionApplicationRepository.findByApplicantAndRequestedPet(applicant, pet);
	}
	
	public void acceptRequest(int a, AdoptionApplication adopApp) throws DataAccessException, DuplicatedPetNameException {
		Pet pet = adopApp.getRequestedPet();
		Owner newOwner = adopApp.getApplicant();
		pet.setinAdoption(false);
		pet.setOwner(newOwner);
		petService.savePet(pet);
		
		//Borrar todas las request que había de esa mascota
		
		List<AdoptionApplication> requestsByPet = adoptionApplicationRepository.findRequestByPet(pet.getId());
		for(AdoptionApplication adoption: requestsByPet) {
			adoptionApplicationRepository.delete(adoption);
		}
	}

	public void declineRequest(int requestId) {
		adoptionApplicationRepository.deleteById(requestId);
	}

}
