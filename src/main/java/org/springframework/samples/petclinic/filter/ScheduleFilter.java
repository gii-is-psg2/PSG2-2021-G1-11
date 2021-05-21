package org.springframework.samples.petclinic.filter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.samples.petclinic.configuration.ScheduleConfiguration;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ScheduleFilter implements Filter {
	@Autowired
	ScheduleConfiguration configuration;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		LocalTime now = LocalTime.now();
		if (configuration.isEnabled() &&
			(!configuration.isActiveDay(LocalDate.now()) || now.isBefore(configuration.getStartTime()) || now.isAfter(configuration.getEndTime()))) {
			response.resetBuffer();
			response.getWriter().append(configuration.getMaintenanceMessage());
			response.flushBuffer();
		} else {
			chain.doFilter(request, response);
		}
	}
	

}