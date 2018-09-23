package thiagodnf.nautilus.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorController {

	@ExceptionHandler(RuntimeException.class)
	public ModelAndView handleRuntimeException(HttpServletRequest req, RuntimeException ex) {

		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it - like the OrderNotFoundException example
		// at the start of this post.
		// AnnotationUtils is a Spring Framework utility class.

		ResponseStatus status = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);

		ex.printStackTrace();
		
		if (status != null) {
			throw ex;
		}

		// Otherwise setup and send the user to a default error-view.
		ModelAndView mav = new ModelAndView();

		mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		mav.addObject("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		mav.addObject("exception", ex);
		mav.addObject("timestamp", System.currentTimeMillis());
		mav.addObject("message", ex.getMessage());
		mav.addObject("url", req.getRequestURL());
		mav.addObject("stackTrace", ExceptionUtils.getStackTrace(ex));
		mav.setViewName("error");

		return mav;
	}
}
