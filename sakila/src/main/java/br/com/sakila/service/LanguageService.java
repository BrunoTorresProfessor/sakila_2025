package br.com.sakila.service;

import java.util.List;

import br.com.sakila.model.LanguageModel;

public interface LanguageService {

	List<LanguageModel> findAll();

	LanguageModel findById(Long languageId);

	LanguageModel create(String name);

	LanguageModel update(Long languageId, String name);

	void delete(Long languageId);
}
