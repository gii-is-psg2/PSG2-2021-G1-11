package org.springframework.samples.petclinic.configuration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "petclinic.schedule")
public class ScheduleConfiguration {
	private boolean enabled;
	private Set<String> activeDays;
	private LocalTime startTime;
	private LocalTime endTime;
	private String maintenanceMessage;
	private String timeZone;
	
	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Set<String> getActiveDays() {
		return activeDays;
	}

	public void setActiveDays(Set<String> enabledDays) {
		this.activeDays = enabledDays;
	}
	
	public boolean isActiveDay(LocalDate date) {
		return getActiveDays().contains(date.getDayOfWeek().toString().toLowerCase());
	}

	public boolean isActiveDay(String dayOfWeek) {
		return getActiveDays().contains(dayOfWeek.toLowerCase());
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getMaintenanceMessage() {
		return maintenanceMessage;
	}

	public void setMaintenanceMessage(String maintenanceMessage) {
		this.maintenanceMessage = maintenanceMessage;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
}