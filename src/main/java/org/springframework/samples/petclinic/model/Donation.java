
package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "donations")
public class Donation extends BaseEntity {

    @CreationTimestamp
    private Date donationDate;

    @Min(value = 1)
    @NotNull
    private Double amount;

    private String ownerName;

    @ManyToOne
    @JoinColumn(name = "cause_id")
    private Cause cause;


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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Cause getCause() {
        return this.cause;
    }

    public void setCause(final Cause cause) {
        this.cause = cause;
    }

}
