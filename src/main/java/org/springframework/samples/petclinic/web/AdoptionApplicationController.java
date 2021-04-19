package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.AdoptionApplicationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/adoptions")
public class AdoptionApplicationController {

	private AdoptionApplicationService adoptionApplicationService;
	private OwnerService ownerService;

	@Autowired
	public AdoptionApplicationController(AdoptionApplicationService adoptionApplicationService,
			OwnerService ownerService) {
		super();
		this.adoptionApplicationService = adoptionApplicationService;
		this.ownerService = ownerService;
	}

	@GetMapping(value = "/applications")
	public ModelAndView getPendingApplications(Principal principal) {
		Owner owner = ownerService.getOwnerByUserName(principal.getName());
		List<AdoptionApplication> adopApp = adoptionApplicationService.getPendingAdoptionApplication(owner);
		ModelAndView mav = new ModelAndView("owners/ownerAdoptionApplication");
		mav.addObject("adoptionApplications", adopApp);
		mav.addObject("adoptionApplicationsNumber", adopApp.size());
		return mav;
	}

	@GetMapping(value = "{applications_id}/accept")
	public String acceptAdoptionApplication(@PathVariable("applications_id") int applicationsId, Principal principal)
			throws DataAccessException, DuplicatedPetNameException {
		// Comprobar antes que el owner logueado es el que quiera aceptar la solicitud
		AdoptionApplication adopApp = adoptionApplicationService.findById(applicationsId);
		Owner owner = ownerService.getOwnerByUserName(principal.getName());
		if (adopApp.getRequestedPet().getOwner().getId().equals(owner.getId()))
			adoptionApplicationService.acceptAdoptionApplication(adopApp);
		return "redirect:/adoptions/applications";
	}

	@GetMapping(value = "{applications_id}/decline")
	public String declineAdoptionApplication(@PathVariable("applications_id") int applicationsId, Principal principal) {
		AdoptionApplication adopApp = adoptionApplicationService.findById(applicationsId);
		Owner owner = ownerService.getOwnerByUserName(principal.getName());
		if (adopApp.getRequestedPet().getOwner().getId().equals(owner.getId()))
			adoptionApplicationService.declineAdoptionApplication(applicationsId);
		return "redirect:/adoptions/applications";
	}
}
