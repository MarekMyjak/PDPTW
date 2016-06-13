package pl.edu.agh.io.pdptw.configuration.exception;

import lombok.ToString;

@ToString
public class InvalidFileFormatException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	
	public InvalidFileFormatException() {
		super();
	}
	public InvalidFileFormatException(String message) {
		this.message = message;
	}
}
