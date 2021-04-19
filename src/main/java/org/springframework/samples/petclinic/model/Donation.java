
package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "donations")
public class Donation extends BaseEntity {

	@CreationTimestamp
	private Date	donationDate;

	@Min(value = 1)
	@NotEmpty
	private Double	amount;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner	owner;

	@ManyToOne
	@JoinColumn(name = "cause_id")
	private Cause	cause;

	
	public Date getDonationDate() {
		return this.donationDate;
	}

	public void setDonationDate(final Date donationDate) {
		this.donationDate = donationDate;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(final Double amount) {
		this.amount = amount;
	}

	public Owner getOwner() {
		return this.owner;
	}

	public void setOwner(final Owner owner) {
		this.owner = owner;
	}

	public Cause getCause() {
		return this.cause;
	}

	public void setCause(final Cause cause) {
		this.cause = cause;
	}

}
