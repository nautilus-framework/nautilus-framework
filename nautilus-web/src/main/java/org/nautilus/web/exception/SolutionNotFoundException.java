package org.nautilus.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "exception.SolutionNotFoundException")
public class SolutionNotFoundException extends AbstractRedirectException {

	private static final long serialVersionUID = -1137473712353340161L;
}
