package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "adoption_application")
public class AdoptionApplication extends BaseEntity {
	@ManyToOne(optional = false)
	private Pet requestedPet;
	
	@ManyToOne(optional = false)
	private Owner applicant;
	
	@Column(name = "description")
	@NotBlank
	private String description;

	public Pet getRequestedPet() {
		return requestedPet;
	}

	public void setRequestedPet(Pet requestedPet) {
		this.requestedPet = requestedPet;
	}

	public Owner getApplicant() {
		return applicant;
	}

	public void setApplicant(Owner applicant) {
		this.applicant = applicant;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
