package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpecialtyService {
	private SpecialtyRepository specialtyRepository;

	@Autowired
	public SpecialtyService(SpecialtyRepository specialtyRepository) {
		this.specialtyRepository = specialtyRepository;
	}

	@Transactional(readOnly = true)
	public Specialty findSpecialtyByName(String name) throws DataAccessException {
		return specialtyRepository.findByName(name);
	}

	@Transactional(readOnly = true)
	public Specialty findSpecialtyById(Integer id) throws DataAccessException {
		return specialtyRepository.findById(id);
	}

	public void saveSpecialty(Specialty specialty) throws DataAccessException {
		specialtyRepository.save(specialty);
	}

	public List<Specialty> findAllSpecialties() {
		return specialtyRepository.findAll();
	}

}

