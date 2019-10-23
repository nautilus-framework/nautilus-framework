package org.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "exception.ExecutionAlreadyExistsException")
public class ExecutionAlreadyExistsException extends AbstractRedirectException {

	private static final long serialVersionUID = 3213128309967170653L;
	
}
