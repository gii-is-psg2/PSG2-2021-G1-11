package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Donation;

public interface DonationRepository extends Repository<Donation, Integer> {

	void save(Donation donation) throws DataAccessException;

	@Query("SELECT d FROM Donation d")
	Collection<Donation> findAll();

	Donation findById(@Param(value = "donationId") int donationId);

	@Query("SELECT d FROM Donation d where d.cause.id=:causeId")
	Collection<Donation> findByCauseId(@Param(value = "causeId") int causeId);

}
