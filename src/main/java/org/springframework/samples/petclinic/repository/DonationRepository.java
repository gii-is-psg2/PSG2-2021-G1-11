package org.springframework.samples.petclinic.repository;

import org.springframework.samples.petclinic.model.Donation;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface DonationRepository extends Repository<Donation, Integer> {
	Donation findById(Integer id) throws DataAccessException;

	void save(Donation donation) throws DataAccessException;

	void delete(Integer id) throws DataAccessException;

	List<Donation> findAll();

	@Query("SELECT cause.donations FROM Cause cause where cause.id=:id")
	Collection<Donation> findByCauseId(@Param("id") int id) throws DataAccessException;

}
