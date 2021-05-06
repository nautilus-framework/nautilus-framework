package org.nautilus.web.exception;

public abstract class AbstractRedirectException extends RuntimeException {

	private static final long serialVersionUID = 3720387679076912330L;
	
	private String redirectTo = "/home";
	
	public AbstractRedirectException redirectTo(String path) {
		this.redirectTo = path;
		return this;
	}
	
	public String getRedirectTo() {
		return this.redirectTo;
	}
}
