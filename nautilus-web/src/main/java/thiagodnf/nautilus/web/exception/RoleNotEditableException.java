package thiagodnf.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED, reason = "exception.RoleNotEditableException")
public class RoleNotEditableException extends AbstractRedirectException {

	private static final long serialVersionUID = -1567473712353340333L;
	
	@Override
	public String getRedirectTo() {
		return "/admin/roles";
	}
}
