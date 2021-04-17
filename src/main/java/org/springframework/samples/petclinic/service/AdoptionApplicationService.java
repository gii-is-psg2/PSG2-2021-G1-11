package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.AdoptionApplicationRepository;
import org.springframework.stereotype.Service;

@Service
public class AdoptionApplicationService {

	private AdoptionApplicationRepository adoptionApplicationRepository;
	
	@Autowired
	public AdoptionApplicationService(AdoptionApplicationRepository adoptionApplicationRepository) {
		super();
		this.adoptionApplicationRepository = adoptionApplicationRepository;
	}

	public List<AdoptionApplication> getPendingRequest(Owner owner) {
		return adoptionApplicationRepository.getPendingRequest(owner.getId());
	}

}
