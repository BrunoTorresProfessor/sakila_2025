package br.com.sakila.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sakila.model.LanguageModel;
import br.com.sakila.repository.LanguageRepository;

@Service
public class LanguageServiceImpl implements LanguageService {

	private final LanguageRepository languageRepository;

	public LanguageServiceImpl(LanguageRepository languageRepository) {
		this.languageRepository = languageRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<LanguageModel> findAll() {
		return languageRepository.findAll(Sort.by(Sort.Direction.ASC, "languageId"));
	}

	@Override
	@Transactional(readOnly = true)
	public LanguageModel findById(Long languageId) {
		return languageRepository.findById(languageId)
				.orElseThrow(() -> new LanguageNotFoundException(languageId));
	}

	@Override
	@Transactional
	public LanguageModel create(String name) {
		String normalizedName = normalize(name);
		if (languageRepository.existsByNameIgnoreCase(normalizedName)) {
			throw new LanguageOperationException("Já existe um idioma com esse nome.");
		}

		LanguageModel language = new LanguageModel();
		language.setName(normalizedName);
		return save(language);
	}

	@Override
	@Transactional
	public LanguageModel update(Long languageId, String name) {
		LanguageModel language = findById(languageId);
		String normalizedName = normalize(name);
		if (languageRepository.existsByNameIgnoreCaseAndLanguageIdNot(normalizedName, languageId)) {
			throw new LanguageOperationException("Já existe um idioma com esse nome.");
		}

		language.setName(normalizedName);
		return save(language);
	}

	@Override
	@Transactional
	public void delete(Long languageId) {
		LanguageModel language = findById(languageId);
		try {
			languageRepository.delete(language);
			languageRepository.flush();
		} catch (DataAccessException exception) {
			throw new LanguageOperationException(
					"Não foi possível excluir o idioma. Verifique se ele está sendo usado por algum filme.", exception);
		}
	}

	private LanguageModel save(LanguageModel language) {
		try {
			return languageRepository.saveAndFlush(language);
		} catch (DataAccessException exception) {
			throw new LanguageOperationException("Não foi possível salvar o idioma. Tente novamente.", exception);
		}
	}

	private String normalize(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new LanguageOperationException("Informe o nome do idioma.");
		}

		String normalizedName = name.trim();
		if (normalizedName.length() > 20) {
			throw new LanguageOperationException("O nome deve ter no máximo 20 caracteres.");
		}
		return normalizedName;
	}
}
