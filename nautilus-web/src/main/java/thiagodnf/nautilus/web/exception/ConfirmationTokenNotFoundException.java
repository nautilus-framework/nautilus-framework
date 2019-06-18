package thiagodnf.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "exception.ConfirmationTokenNotFoundException")
public class ConfirmationTokenNotFoundException extends AbstractRedirectException {

	private static final long serialVersionUID = -2267473712123340111L;
	
	@Override
    public String getRedirectTo() {
        return "/login";
    }
}
