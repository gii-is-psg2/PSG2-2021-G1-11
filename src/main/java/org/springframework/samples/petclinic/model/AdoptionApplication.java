package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "adoption_application")
public class AdoptionApplication extends BaseEntity {
	@OneToOne(optional = false)
	private Pet requestedPet;
	
	@OneToOne(optional = false)
	private Owner applicant;
	
	@Column(name = "description")
	private String description;
	
	public AdoptionApplication(Pet requestedPet, Owner applicant, String description) {
		super();
		this.requestedPet = requestedPet;
		this.applicant = applicant;
		this.description = description;
	}

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
