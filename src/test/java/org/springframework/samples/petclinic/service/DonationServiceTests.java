package org.springframework.samples.petclinic.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class DonationServiceTests {
	
	@Autowired
	DonationService donationService;
	
	@Autowired
	CauseService causeService;
	
	@Autowired
	UserRepository userRepository;
	
	@Test
	void findDonationsByCausesTest() {
		final Double totalAmount = 20.0;
		
		final Cause cause = new Cause();
		cause.setDescription("Queremos salvar a marrambla2");
		cause.setName("AntÃ¡rtida");
		cause.setIsClosed(false);
		cause.setOrganization("Greenpeace");
		cause.setTarget(2000.);
		User user = new User();
		user.setUsername("guillex7");
		user.setPassword("pwd");
		userRepository.save(user);
		cause.setFounder(user);
		this.causeService.saveCause(cause);
		
		final List<Cause> causeList = new ArrayList<>();
		causeList.add(cause);
		
		Donation donation1 = new Donation();
		donation1.setAmount(totalAmount);
		donation1.setCause(cause);
		donationService.saveDonation(donation1);
		
		final List<Double> amountList = new ArrayList<>();
		amountList.add(totalAmount);
		
		assertEquals(donationService.findDonationsByCauses(causeList), amountList);
	}
}