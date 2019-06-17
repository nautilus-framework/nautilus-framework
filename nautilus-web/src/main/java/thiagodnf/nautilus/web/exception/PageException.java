package thiagodnf.nautilus.web.exception;

public abstract class PageException extends RuntimeException {

	private static final long serialVersionUID = 3720387679076928930L;
	
	public PageException(String message) {
		super(message);
	}
}
