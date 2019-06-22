package thiagodnf.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED, reason = "exception.UserNotEditableException")
public class UserNotEditableException extends AbstractRedirectException {

	private static final long serialVersionUID = -1567473713353340111L;
	
	@Override
	public String getRedirectTo() {
		return "/admin";
	}
}
