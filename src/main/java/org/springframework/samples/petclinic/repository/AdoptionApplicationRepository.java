package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.AdoptionApplication;

public interface AdoptionApplicationRepository extends CrudRepository<AdoptionApplication, Integer> {

}
