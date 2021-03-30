package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Booking;

public interface BookingRepository extends Repository<Booking, Integer> {
	Booking findById(Integer id) throws DataAccessException;
	
	void save(Booking booking) throws DataAccessException;
	
	void removeById(Integer id) throws DataAccessException;
}
