package org.springframework.samples.petclinic.configuration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class StringLocalTimeConverter implements Converter<String, LocalTime> {
	@Override
	public LocalTime convert(String source) {
		return LocalTime.parse(source, DateTimeFormatter.ISO_TIME);
	}
} 