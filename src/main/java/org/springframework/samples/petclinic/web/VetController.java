/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.SpecialtyService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {
	private final UserService userService;
	private final VetService vetService;
	private final SpecialtyService specialtyService;
	private static final String VIEWS_VET_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";
	private static final String VET_REDIRECT_URL = "redirect:/vets";

	@Autowired
	public VetController(UserService userService, VetService clinicService, SpecialtyService specialtyService) {
		this.userService = userService;
		this.vetService = clinicService;
		this.specialtyService = specialtyService;
	}

	@ModelAttribute("specialties")
	public List<Specialty> getSpecialties() {
		return specialtyService.findAllSpecialties();
	}

	@GetMapping(value = { "/vets" })
	public String showVetList(Map<String, Object> model, Principal principal) {
		// Here we are returning an object of type 'Vets' rather than a collection of
		// objects so it is simpler for Object-Xml mapping
		User user = userService.findUser(principal.getName()).orElse(null);
		model.put("canRemoveVets",
				user != null && user.getAuthorities().stream().anyMatch(x -> x.getAuthority().contentEquals("admin")));

		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@PostMapping(value = { "/vets/{vetId}/remove" })
	public String removeVet(@PathVariable Integer vetId, Principal principal) {
		User user = userService.findUser(principal.getName()).orElse(null);
		if (user != null && user.getAuthorities().stream().anyMatch(x -> x.getAuthority().contentEquals("admin"))) {
			vetService.removeVetById(vetId);
		}
		return VET_REDIRECT_URL;
	}

	@GetMapping(value = { "/vets.xml" })
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of
		// Vet
		// objects
		// so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}

	@GetMapping(value = "/vets/new")
	public String initCreationForm(Owner owner, ModelMap model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/vets/new")
	public String processCreationForm(@Valid Vet vet, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			model.put("vet", vet);
			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
		} else {
			Set<Specialty> specialtySet = saveSpecialties(vet.getSpecialtiesLS());
			vet.setSpecialties(specialtySet);
			try {
				this.vetService.saveVet(vet);
			} catch (DataAccessException ex) {
				return VIEWS_VET_CREATE_OR_UPDATE_FORM;
			}
			return VET_REDIRECT_URL;
		}
	}

	@GetMapping(value = "/vets/{vetId}/edit")
	public String initUpdateForm(@PathVariable("vetId") int vetId, ModelMap model) {
		Vet vet = this.vetService.findVetById(vetId);
		vet.setSpecialtiesLS(vet.getSpecialties().stream().map(Specialty::getName).collect(Collectors.toList()));
		model.put("vet", vet);
		return VIEWS_VET_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/vets/{vetId}/edit")
	public String processUpdateForm(@Valid Vet vet, BindingResult result, @PathVariable("vetId") int vetId,
			ModelMap model) {
		if (result.hasErrors()) {
			model.put("vet", vet);
			return VIEWS_VET_CREATE_OR_UPDATE_FORM;
		} else {
			Vet vetToUpdate = this.vetService.findVetById(vetId);
			BeanUtils.copyProperties(vet, vetToUpdate, "id", "firstname", "lastname");
			Set<Specialty> specialtySet = saveSpecialties(vet.getSpecialtiesLS());
			vetToUpdate.setSpecialties(specialtySet);
			try {
				this.vetService.saveVet(vetToUpdate);
			} catch (DataAccessException ex) {
				return VIEWS_VET_CREATE_OR_UPDATE_FORM;
			}
			return VET_REDIRECT_URL;
		}
	}

	private Set<Specialty> saveSpecialties(List<String> specialtyNameList) {
		Set<Specialty> specialtySet = new HashSet<>();
		for (String specialtyName : specialtyNameList) {
			Specialty specialty = specialtyService.findSpecialtyByName(specialtyName);
			if (specialty == null) {
				specialty = new Specialty();
				specialty.setName(specialtyName);
				specialtyService.saveSpecialty(specialty);
			}
			specialtySet.add(specialty);
		}
		return specialtySet;
	}

}