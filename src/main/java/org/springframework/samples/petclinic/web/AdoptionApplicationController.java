package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.AdoptionApplicationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/adoptions")
public class AdoptionApplicationController {
	
	private AdoptionApplicationService adoptionApplicationService;
	private OwnerService ownerService;

	@Autowired
	public AdoptionApplicationController(AdoptionApplicationService adoptionApplicationService, OwnerService ownerService) {
		super();
		this.adoptionApplicationService = adoptionApplicationService;
		this.ownerService = ownerService;
	}
	
	@GetMapping(value="/request")
	public ModelAndView getPendingRequest(Principal principal) {
		Owner owner = ownerService.getOwnerByUserName(principal.getName());
		List<AdoptionApplication> adopApp = adoptionApplicationService.getPendingRequest(owner);
		ModelAndView mav = new ModelAndView("owners/ownerRequests");
		mav.addObject("requests", adopApp.size());
		return mav;
	}
}
