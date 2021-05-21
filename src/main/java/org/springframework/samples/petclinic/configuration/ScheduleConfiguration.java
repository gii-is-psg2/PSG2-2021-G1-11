package org.springframework.samples.petclinic.configuration;

import java.time.LocalTime;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "petclinic.schedule")
public class ScheduleConfiguration {
	private Boolean enabled;
	private LocalTime startTime;
	private LocalTime endTime;
	private String maintenanceMessage;
	
	public Boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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
}
