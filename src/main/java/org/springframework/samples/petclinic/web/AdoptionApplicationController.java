package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.AdoptionApplication;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AdoptionApplicationService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/adoptions")
@Controller
public class AdoptionApplicationController {

	private AdoptionApplicationService adoptionApplicationService;
	private OwnerService ownerService;
	private PetService petService;

	@Autowired
	public AdoptionApplicationController(AdoptionApplicationService adoptionApplicationService,
			OwnerService ownerService, PetService petService) {
		super();
		this.adoptionApplicationService = adoptionApplicationService;
		this.ownerService = ownerService;
		this.petService = petService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("applicant", "requestedPet");
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

	@GetMapping(value = "/pets/{petId}/apply")
	public String createNewAdoptionApplication(Map<String, Object> model, @PathVariable("petId") int petId) {
		AdoptionApplication adoptionApplication = new AdoptionApplication();
		Pet adoptedPet = petService.findPetById(petId);
		if (adoptedPet == null || !adoptedPet.getinAdoption()) {
			return "redirect:/";
		}
		
		model.put("adoptionApplication", adoptionApplication);
		return "adoptions/createAdoptionApplication";
	}

	@PostMapping(value = "/pets/{petId}/apply")
	public String postNewAdoptionApplication(@Valid AdoptionApplication adoptionApplication, BindingResult result,
			@PathVariable("petId") int petId, Principal principal) {
		if (result.hasErrors()) {
			return "adoptions/createAdoptionApplication";
		}

		Owner applicant = ownerService.getOwnerByUserName(principal.getName());
		adoptionApplication.setApplicant(applicant);
		Pet adoptedPet = petService.findPetById(petId);
		if (adoptedPet == null) {
			result.rejectValue("description", "petDoesntExist");
			return "adoptions/createAdoptionApplication";
		} else if (!adoptedPet.getinAdoption()) {
			result.rejectValue("description", "notAdoptablePet");
			return "adoptions/createAdoptionApplication";
		} else if (adoptionApplicationService
				.findByApplicantAndRequestedPet(adoptionApplication.getApplicant(), adoptedPet) != null) {
			result.rejectValue("description", "alreadyApplied");
			return "adoptions/createAdoptionApplication";
		} else if (adoptedPet.getOwner().equals(applicant)) {
			result.rejectValue("description", "alreadyOwned");
			return "adoptions/createAdoptionApplication";
		}

		adoptionApplication.setRequestedPet(adoptedPet);
		adoptionApplicationService.save(adoptionApplication);
		return "redirect:/";
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
