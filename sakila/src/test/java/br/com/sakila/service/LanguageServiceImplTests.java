package br.com.sakila.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import br.com.sakila.model.LanguageModel;
import br.com.sakila.repository.LanguageRepository;

@ExtendWith(MockitoExtension.class)
class LanguageServiceImplTests {

	@Mock
	private LanguageRepository languageRepository;

	private LanguageServiceImpl languageService;

	@BeforeEach
	void setUp() {
		languageService = new LanguageServiceImpl(languageRepository);
	}

	@Test
	void createsLanguageWithNormalizedName() {
		when(languageRepository.existsByNameIgnoreCase("Português")).thenReturn(false);
		when(languageRepository.saveAndFlush(any(LanguageModel.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		languageService.create("  Português  ");

		ArgumentCaptor<LanguageModel> captor = ArgumentCaptor.forClass(LanguageModel.class);
		verify(languageRepository).saveAndFlush(captor.capture());
		assertEquals("Português", captor.getValue().getName());
	}

	@Test
	void rejectsDuplicateLanguage() {
		when(languageRepository.existsByNameIgnoreCase("English")).thenReturn(true);

		assertThrows(LanguageOperationException.class, () -> languageService.create("English"));

		verify(languageRepository, never()).saveAndFlush(any(LanguageModel.class));
	}

	@Test
	void updatesExistingLanguage() {
		LanguageModel language = language(7L, "Old name");
		when(languageRepository.findById(7L)).thenReturn(Optional.of(language));
		when(languageRepository.existsByNameIgnoreCaseAndLanguageIdNot("New name", 7L)).thenReturn(false);
		when(languageRepository.saveAndFlush(language)).thenReturn(language);

		LanguageModel updated = languageService.update(7L, " New name ");

		assertEquals("New name", updated.getName());
		verify(languageRepository).saveAndFlush(language);
	}

	@Test
	void deletesExistingLanguageAndFlushesChange() {
		LanguageModel language = language(8L, "Temporary");
		when(languageRepository.findById(8L)).thenReturn(Optional.of(language));

		languageService.delete(8L);

		verify(languageRepository).delete(language);
		verify(languageRepository).flush();
	}

	@Test
	void reportsForeignKeyErrorWhenDeletingLanguageInUse() {
		LanguageModel language = language(1L, "English");
		when(languageRepository.findById(1L)).thenReturn(Optional.of(language));
		org.mockito.Mockito.doThrow(new DataIntegrityViolationException("foreign key"))
				.when(languageRepository).flush();

		LanguageOperationException exception = assertThrows(LanguageOperationException.class,
				() -> languageService.delete(1L));

		assertEquals("Não foi possível excluir o idioma. Verifique se ele está sendo usado por algum filme.",
				exception.getMessage());
	}

	private LanguageModel language(Long id, String name) {
		LanguageModel language = new LanguageModel();
		language.setLanguageId(id);
		language.setName(name);
		return language;
	}
}
