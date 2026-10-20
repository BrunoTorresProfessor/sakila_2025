package br.com.sakila.service;

public class LanguageOperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LanguageOperationException(String message) {
		super(message);
	}

	public LanguageOperationException(String message, Throwable cause) {
		super(message, cause);
	}
}
