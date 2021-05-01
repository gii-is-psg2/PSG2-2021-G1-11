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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cause;
import org.springframework.samples.petclinic.model.Donation;
import org.springframework.samples.petclinic.service.CauseService;
import org.springframework.samples.petclinic.service.DonationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CauseController {

    private static final String VIEWS_CAUSE_CREATE_OR_UPDATE_FORM = "causes/createOrUpdateCauseForm";
    private final CauseService causeService;
    private final DonationService donationService;

    @Autowired
    public CauseController(final CauseService causeService, final DonationService donationService) {
        this.causeService = causeService;
        this.donationService = donationService;
    }

    @GetMapping(value = {"/causes"})
    public String showCauseList(final Map<String, Object> model) {
        final List<Cause> causes = new ArrayList<>();
        causes.addAll(this.causeService.findCauses());

        final List<Double> donations = new ArrayList<>(this.donationService.findDonationsByCauses(causes));

        final Map<Cause, Double> res = new HashMap<>();
        for (int i = 0; i < causes.size(); i++) {
            res.put(causes.get(i), donations.get(i));
        }
        model.put("map", res);
        return "causes/causeList";
    }

    @GetMapping(value = "/causes/new")
    public String initCreationForm(final Map<String, Object> model) {
        final Cause cause = new Cause();
        cause.setIsClosed(false);
        model.put("cause", cause);
        return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/causes/new")
    public String processCreationForm(@Valid final Cause cause, final BindingResult result) {
        if (result.hasErrors()) {
            return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
        } else {
            this.causeService.saveCause(cause);
            return "redirect:/causes";
        }
    }

    @GetMapping("/causes/{causeId}")
    public String showCause(@PathVariable("causeId") final int causeId, final Map<String, Object> model, @ModelAttribute("status") String status, @ModelAttribute("returnAmount") String returnAmount) {
        Collection<Donation> donations;
        donations = this.donationService.findDonationsByCauseId(causeId);
        model.put("donations", donations);
        model.put("cause", this.causeService.findCauseById(causeId));
        return "causes/causeDetails";
    }
    
    @GetMapping(value="/causes/{causeId}/edit")
    public String initUpdateForm(@PathVariable("causeId") final int causeId, final Model model, Principal principal) {
    	//final Owner owner = this.ownerService.getOwnerByUserName(principal.getName());
    	//final OwnerCause = this.causeService.findOwnerByCause(causeId);
    	//if(owner.equals(OwnerCause)) {
    		final Cause cause = this.causeService.findCauseById(causeId);
        	model.addAttribute(cause);
        	return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
    	//}else {
    	//	return "redirect:/";
    	//}
    }
    
    @PostMapping(value="/causes/{causeId}/edit")
    public String processUpdateCauseForm(@Valid final Cause cause, final BindingResult result, 
    		@PathVariable("causeId") final int causeId) {
    	if(result.hasErrors()) {
    		return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
    	} else {
    		boolean causeEditability = causeService.checkCauseEditability(causeId,cause.getTarget());
    		// the cause can't be edited
    		if(!causeEditability) {
    			result.rejectValue("target", "targetError");
    			return CauseController.VIEWS_CAUSE_CREATE_OR_UPDATE_FORM;
    		// the cause can be edited
    		}else {
    			cause.setId(causeId);
        		this.causeService.saveCause(cause);
        		return "redirect:/causes/{causeId}";
    		}
    	}
    }
    
    @PostMapping(value="/causes/{causeId}/remove")
    public String removeCause(@PathVariable("causeId") final Integer causeId, Map<String, Object> model, RedirectAttributes redirectAttributes) {
    	// check if the cause has donations
    	boolean causeRemovability = causeService.checkCauseRemovability(causeId);
    	// si no hay donaciones se puede borrar la causa
    	if(causeRemovability) {
        	this.causeService.removeById(causeId);
        	return "redirect:/";
    	}else {
    		redirectAttributes.addFlashAttribute("status", "canNotRemoveCause");
    		return "redirect:/causes/{causeId}";
    	}
    }
    
}
