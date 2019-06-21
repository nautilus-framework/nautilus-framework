package thiagodnf.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "exception.ExecutionNotReadyException")
public class ExecutionNotReadyException extends AbstractRedirectException {

	private static final long serialVersionUID = -1137233755353340161L;
	
	public String getRedirectTo() {
        return "/home";
    }
}
