package pl.edu.agh.io.pdptw.reader.exception;

import lombok.ToString;

@ToString
public class InvalidFileFormatException extends Exception {
	private String message;
	
	public InvalidFileFormatException() {
		super();
	}
	public InvalidFileFormatException(String message) {
		this.message = message;
	}
}
