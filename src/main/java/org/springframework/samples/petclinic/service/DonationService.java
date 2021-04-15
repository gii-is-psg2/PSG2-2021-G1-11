package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {
	private DonationRepository donationRepository;

	@Autowired
	public DonationService(DonationRepository donationRepository) {
		this.donationRepository = donationRepository;
	}

	@Transactional(readOnly = true)
	public Donation findDonationById(int id) throws DataAccessException {
		return donationRepository.findById(id);
	}

	public void saveDonation(Donation donation) {
		donationRepository.save(donation);
	}

	public void deleteDonation(int id) throws DataAccessException {
		donationRepository.delete(id);
	}

	public Collection<Donation> findDonationsByCauseId(int causeId) {
		return donationRepository.findByCauseId(causeId);
	}

}
