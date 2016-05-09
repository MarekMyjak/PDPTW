package pl.edu.agh.io.pdptw.reader.exception;

public class InvalidFileFormatException extends Exception {
	private String message;
	
	public InvalidFileFormatException() {
		super();
	}
	public InvalidFileFormatException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}
}
