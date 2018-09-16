package no.itminds.movies.errorhandling;

public class MovieException extends Exception {
	private String userMessage;
	
	public MovieException(String userMessage) {
		super();
		this.userMessage = userMessage;
	}
	public MovieException(String userMessage, Throwable throwable) {
		super(throwable);
		this.userMessage = userMessage;
	}
	public String getUserMessage() {
		return userMessage;
	}
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	
}
