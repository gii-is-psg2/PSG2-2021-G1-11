package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DonationController {
	private static final String VIEWS_DONATIONS_CREATE_FORM = "donations/createDonationForm";

	private final DonationService donationService;
	
	@Autowired
	public DonationController(DonationService donationService) {
		this.donationService=donationService;
	}
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/donations/new")
	public String initCreationForm(Map<String, Object> model) {
		Donation donation=new Donation();
		model.put("donation", donation);
		return VIEWS_DONATIONS_CREATE_FORM;
	}

	@PostMapping(value = "/donations/new")
	public String processCreationForm(Owner owner,@Valid Donation donation, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_DONATIONS_CREATE_FORM;
		}
		else {
			//TODO:Añadir causa
			donation.setOwner(owner);
			this.donationService.saveDonation(donation);
			return "";
		}
	}
	
	@GetMapping(value = "/donations/{causeId}/list")
	public String showDonations(@PathVariable("causeId") int causeId, ModelMap model) {
		List<Donation> donations=donationService.findDonationsByCauseId(causeId).stream().collect(Collectors.toList());
		model.put("donations", donations);
		return "redirect:donations/donationsList";
	}
	
}
