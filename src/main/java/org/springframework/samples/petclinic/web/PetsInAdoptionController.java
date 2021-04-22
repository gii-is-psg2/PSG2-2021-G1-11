package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PetsInAdoptionController {

	private static final String VIEWS_PETS_IN_ADOPTION = "pets/listPetsInAdoption";
	
	private final PetService petService;
	
	@Autowired
	public PetsInAdoptionController(PetService petService) {
		this.petService = petService;
	}
	
	@GetMapping(value = "/pets/inAdoption")
	public String inAdoptionList(ModelMap model) {
		model.addAttribute("pets",petService.findPetsInAdoption());
		return VIEWS_PETS_IN_ADOPTION;
	}
	
}
