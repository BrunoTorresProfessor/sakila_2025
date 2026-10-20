package br.com.sakila.service;

public class LanguageNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LanguageNotFoundException(Long languageId) {
		super("Idioma " + languageId + " não encontrado.");
	}
}
