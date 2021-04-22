package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DonationController {
	private static final String VIEWS_DONATIONS_CREATE_FORM = "donations/createDonationForm";

	private final DonationService donationService;
	private final CauseService causeService;

	@Autowired
	public DonationController(DonationService donationService, CauseService causeService) {
		this.donationService = donationService;
		this.causeService = causeService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/causes/{causeId}/donations/new")
	public String initCreationForm(Map<String, Object> model) {
		Donation donation = new Donation();
		model.put("donation", donation);
		return VIEWS_DONATIONS_CREATE_FORM;
	}

	@PostMapping(value = "/causes/{causeId}/donations/new")
	public String processCreationForm(@PathVariable("causeId") int causeId, Principal principal,
			@Valid Donation donation, BindingResult result) {

		if (result.hasErrors()) {
			return VIEWS_DONATIONS_CREATE_FORM;
		}
		Cause cause = causeService.findCauseById(causeId);
		if (cause.getIsClosed() == true) {
			result.rejectValue("amount", "causeClosed");
			return VIEWS_DONATIONS_CREATE_FORM;
		} else {
			donation.setOwnerName(principal.getName());
			donation.setCause(cause);
			this.donationService.saveDonation(donation);
			Double total = causeService.actualAmountById(causeId);
			if (cause.getTarget() <= total) {
				cause.setIsClosed(true);
				this.causeService.saveCause(cause);
			}
			return "redirect:/causes/{causeId}";
		}

	}

}
