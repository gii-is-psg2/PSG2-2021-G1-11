package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;

public interface AdoptionApplicationRepository extends CrudRepository<AdoptionApplication, Integer> {

	@Query("select r from AdoptionApplication r where r.requestedPet.owner.id =:ownerId")
	public List<AdoptionApplication> getPendingRequest(@Param("ownerId") Integer ownerId);

	@Query("select a from AdoptionApplication a where a.requestedPet.id =:petId")
	public List<AdoptionApplication> findRequestByPet(@Param("petId") Integer petId);
	
	public AdoptionApplication findByApplicantAndRequestedPet(Owner applicant, Pet requestedPet);
}
