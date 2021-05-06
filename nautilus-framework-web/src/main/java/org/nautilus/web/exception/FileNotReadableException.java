package org.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "exception.FileNotReadableException")
public class FileNotReadableException extends AbstractRedirectException {

	private static final long serialVersionUID = -1567473712353340161L;
}
