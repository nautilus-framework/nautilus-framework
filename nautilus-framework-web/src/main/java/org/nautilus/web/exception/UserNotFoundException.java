package org.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "exception.UserNotFoundException")
public class UserNotFoundException extends AbstractRedirectException {

	private static final long serialVersionUID = -1567473712123340111L;
}
