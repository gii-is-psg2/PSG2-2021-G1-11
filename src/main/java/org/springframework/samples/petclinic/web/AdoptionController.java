
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
import org.springframework.samples.petclinic.service.AdoptionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
public class AdoptionController {

	private final AdoptionService	adoptionApplicationService;
	private final OwnerService					ownerService;
	private final PetService					petService;


	@Autowired
	public AdoptionController(final AdoptionService adoptionApplicationService, final OwnerService ownerService, final PetService petService) {
		super();
		this.adoptionApplicationService = adoptionApplicationService;
		this.ownerService = ownerService;
		this.petService = petService;
	}

	@GetMapping(value = "/applications")
	public ModelAndView getPendingApplications(final Principal principal) {
		final Owner owner = this.ownerService.getOwnerByUserName(principal.getName());
		final List<AdoptionApplication> adopApp = this.adoptionApplicationService.getPendingAdoptionApplication(owner);
		final ModelAndView mav = new ModelAndView("owners/ownerAdoptionApplication");
		mav.addObject("adoptionApplications", adopApp);
		mav.addObject("adoptionApplicationsNumber", adopApp.size());
		return mav;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("applicant", "requestedPet");
	}

	@GetMapping(value = "{applications_id}/accept")
	public String acceptAdoptionApplication(@PathVariable("applications_id") final int applicationsId, final Principal principal) throws DataAccessException, DuplicatedPetNameException {
		// Comprobar antes que el owner logueado es el que quiera aceptar la solicitud
		final AdoptionApplication adopApp = this.adoptionApplicationService.findById(applicationsId);
		final Owner owner = this.ownerService.getOwnerByUserName(principal.getName());
		if (adopApp.getRequestedPet().getOwner().getId().equals(owner.getId()))
			this.adoptionApplicationService.acceptAdoptionApplication(adopApp);
		return "redirect:/adoptions/applications";
	}

	@GetMapping(value = "{applications_id}/decline")
	public String declineAdoptionApplication(@PathVariable("applications_id") final int applicationsId, final Principal principal) {
		final AdoptionApplication adopApp = this.adoptionApplicationService.findById(applicationsId);
		final Owner owner = this.ownerService.getOwnerByUserName(principal.getName());
		if (adopApp.getRequestedPet().getOwner().getId().equals(owner.getId()))
			this.adoptionApplicationService.declineAdoptionApplication(applicationsId);
		return "redirect:/adoptions/applications";
	}

	@GetMapping(value = "/pets")
	public String inAdoptionList(ModelMap model) {
		model.addAttribute("pets",petService.findPetsInAdoption());
		return "adoptions/listPetsInAdoption";
	}	
	
	@GetMapping(value = "/pets/{petId}/apply")
	public String createNewAdoptionApplication(final Map<String, Object> model, @PathVariable("petId") final int petId) {
		final AdoptionApplication adoptionApplication = new AdoptionApplication();
		final Pet adoptedPet = this.petService.findPetById(petId);
		if (adoptedPet == null || !adoptedPet.getinAdoption()) {
			return "redirect:/";
		}

		model.put("adoptionApplication", adoptionApplication);
		return "adoptions/createAdoptionApplication";
	}

	@PostMapping(value = "/pets/{petId}/apply")
	public String postNewAdoptionApplication(@Valid final AdoptionApplication adoptionApplication, final BindingResult result, @PathVariable("petId") final int petId, final Principal principal) {
		if (result.hasErrors()) {
			return "adoptions/createAdoptionApplication";
		}

		final Owner applicant = this.ownerService.getOwnerByUserName(principal.getName());
		adoptionApplication.setApplicant(applicant);
		final Pet adoptedPet = this.petService.findPetById(petId);
		if (adoptedPet == null) {
			result.rejectValue("description", "petDoesntExist");
			return "adoptions/createAdoptionApplication";
		} else if (!adoptedPet.getinAdoption().booleanValue()) {
			result.rejectValue("description", "notAdoptablePet");
			return "adoptions/createAdoptionApplication";
		} else if (this.adoptionApplicationService.findByApplicantAndRequestedPet(adoptionApplication.getApplicant(), adoptedPet) != null) {
			result.rejectValue("description", "alreadyApplied");
			return "adoptions/createAdoptionApplication";
		} else if (adoptedPet.getOwner().equals(applicant)) {
			result.rejectValue("description", "alreadyOwned");
			return "adoptions/createAdoptionApplication";
		}

		adoptionApplication.setRequestedPet(adoptedPet);
		this.adoptionApplicationService.save(adoptionApplication);
		return "redirect:/";
	}

}
