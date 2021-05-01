
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
		Double amount = this.causeRepository.actualAmount(causeId);
		if (amount == null) {
			return 0.;
		} else {
			return amount;
		}
	}

	@Transactional
	public Collection<Cause> findCauses() {
		return this.causeRepository.findAll();
	}

	@Transactional
	public Collection<Donation> findDonations(final int causeId) {
		return this.donationRepository.findByCauseId(causeId);
	}

	@Transactional
	public Boolean checkCauseEditability(int causeId, Double newBudget) {
		boolean causeEditability = true;
		Integer numOfDonationsByCause = findNumberOfDonationsByCause(causeId);
		// there are donations
		if (numOfDonationsByCause > 0) {
			Double budget = causeRepository.findBudgetByCause(causeId); // get budget by cause
			if (newBudget < budget) { // the target budget can't be edited
				causeEditability = false;
			}
		}
		return causeEditability;
	}
	
	public Integer findNumberOfDonationsByCause(Integer causeId) {
		return causeRepository.findNumberOfDonationsByCause(causeId);
	}

	@Transactional
	public boolean checkCauseRemovability(Integer causeId) {
		return findNumberOfDonationsByCause(causeId).equals(0);
	}

	@Transactional
	public void removeById(Integer causeId) {
		causeRepository.removeById(causeId);
	}

	//public Owner findOwnerByCause(int causeId) {
		//return causeRepository.findOwnerById();
	//}

}
