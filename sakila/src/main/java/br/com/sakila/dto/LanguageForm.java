package br.com.sakila.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LanguageForm {

	private Long languageId;

	@NotBlank(message = "Informe o nome do idioma.")
	@Size(max = 20, message = "O nome deve ter no máximo 20 caracteres.")
	private String name;

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
