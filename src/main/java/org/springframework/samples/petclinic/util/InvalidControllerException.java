package org.springframework.samples.petclinic.util;

public class InvalidControllerException extends Exception {

	public InvalidControllerException(String string) {
		super(string);
	}

	private static final long serialVersionUID = 3806272362329032817L;

}
