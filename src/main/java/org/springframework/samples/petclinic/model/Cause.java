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
package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

@Entity
@Table(name = "causes")
public class Cause extends BaseEntity {

	@NotBlank
	private String name;

	@NotBlank
	private String description;

	@NotNull
	@Min(0)
	private Double target;

	@NotBlank
	private String organization;

	@NotNull
	private Boolean isClosed;
	
	@ManyToOne(optional = false)
	private User founder;

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Double getTarget() {
		return this.target;
	}

	public void setTarget(final Double target) {
		this.target = target;
	}

	public String getOrganization() {
		return this.organization;
	}

	public void setOrganization(final String organization) {
		this.organization = organization;
	}

	public Boolean getIsClosed() {
		return this.isClosed;
	}

	public void setIsClosed(final Boolean isClosed) {
		this.isClosed = isClosed;
	}

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "cause")
	private Set<Donation> donations;

	protected Set<Donation> getDonationsInternal() {
		if (this.donations == null) {
			this.donations = new HashSet<>();
		}
		return this.donations;
	}

	public List<Donation> getDonations() {
		final List<Donation> sortedDonations = new ArrayList<>(this.getDonationsInternal());
		PropertyComparator.sort(sortedDonations, new MutableSortDefinition("date", false, false));
		return Collections.unmodifiableList(sortedDonations);
	}

	public void addDonation(final Donation donation) {
		this.getDonationsInternal().add(donation);
		donation.setCause(this);
	}

	public User getFounder() {
		return founder;
	}

	public void setFounder(User founder) {
		this.founder = founder;
	}
	
}
