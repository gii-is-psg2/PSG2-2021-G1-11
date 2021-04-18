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
		mav.addObject("requests", adopApp);
		mav.addObject("requestsNumber", adopApp.size());
		return mav;
	}
	
	@GetMapping(value="{request_id}/accept")
	public String acceptRequest(@PathVariable("request_id") int requestId, Principal principal) throws DataAccessException, DuplicatedPetNameException {
		//	Comprobar antes que el owner logueado es el que quiera aceptar la solicitud
		AdoptionApplication adopApp = adoptionApplicationService.findById(requestId);
		Owner owner = ownerService.getOwnerByUserName(principal.getName());
		if(adopApp.getRequestedPet().getOwner().getId().equals(owner.getId()))
			adoptionApplicationService.acceptRequest(requestId, adopApp);
		return "redirect:/adoptions/request";
	}
	
	@GetMapping(value="{request_id}/decline")
	public String declineRequest(@PathVariable("request_id") int requestId, Principal principal) {
		AdoptionApplication adopApp = adoptionApplicationService.findById(requestId);
		Owner owner = ownerService.getOwnerByUserName(principal.getName());
		if(adopApp.getRequestedPet().getOwner().getId().equals(owner.getId()))
			adoptionApplicationService.declineRequest(requestId);
		return "redirect:/adoptions/request";
	}
}
