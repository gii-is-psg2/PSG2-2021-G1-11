package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Specialty;

public interface SpecialtyRepository extends Repository<Specialty, Integer> {
	void save(Specialty specialty) throws DataAccessException;

	Specialty findById(int id) throws DataAccessException;

	Specialty findByName(String name) throws DataAccessException;

	List<Specialty> findAll() throws DataAccessException;
}
