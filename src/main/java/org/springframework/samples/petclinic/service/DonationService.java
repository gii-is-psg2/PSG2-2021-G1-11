package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {
    private final DonationRepository donationRepository;

    @Autowired
    public DonationService(final DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @Transactional(readOnly = true)
    public Donation findDonationById(final int id) throws DataAccessException {
        return this.donationRepository.findByDonationId(id);
    }

    public void saveDonation(final Donation donation) {
        this.donationRepository.save(donation);
    }

    public Collection<Donation> findDonationsByCauseId(final int causeId) {
        return this.donationRepository.findByCauseId(causeId);
    }

    public List<Double> findDonationsByCauses(final List<Cause> causes) {
        final List<Double> res = new ArrayList<>();
        for (final Cause c : causes) {
            Double res1 = 0.;
            for (final Donation d : this.findDonationsByCauseId(c.getId())) {
                res1 += d.getAmount();

            }
            res.add(res1);
        }
        return res;
    }

}
