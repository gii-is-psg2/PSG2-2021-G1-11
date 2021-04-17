package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.repository.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.AdoptionApplication;

public interface AdoptionApplicationRepository extends Repository<AdoptionApplication, Integer> {

	@Query("select r from AdoptionApplication r where r.requestedPet.owner.id =:ownerId")
	public List<AdoptionApplication> getPendingRequest(@Param("ownerId")Integer ownerId);

}
