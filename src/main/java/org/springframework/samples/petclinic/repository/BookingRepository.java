package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Booking;

public interface BookingRepository extends Repository<Booking, Integer> {
	Booking findById(Integer id) throws DataAccessException;

	void save(Booking booking) throws DataAccessException;

	List<Booking> findAllByCancelledFalse();

	void removeById(Integer id) throws DataAccessException;
}
