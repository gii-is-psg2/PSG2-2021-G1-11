
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.CauseRepository;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CauseService {

	private final CauseRepository causeRepository;
	private final DonationRepository donationRepository;

	@Autowired
	public CauseService(final CauseRepository causeRepository, final DonationRepository donationRepository) {
		this.causeRepository = causeRepository;
		this.donationRepository = donationRepository;
	}

	@Transactional
	public void saveCause(final Cause cause) {
		this.causeRepository.save(cause);
	}

	@Transactional
	public Cause findCauseById(final int causeId) {
		return this.causeRepository.findByCauseId(causeId);
	}

	@Transactional
	public Double actualAmountById(final int causeId) {
		return this.causeRepository.actualAmount(causeId);
	}

	@Transactional
	public Collection<Cause> findCauses() {
		return this.causeRepository.findAll();
	}

	@Transactional
	public Collection<Donation> findDonations(final int causeId)  {
		return this.donationRepository.findByCauseId(causeId);
	}

}
