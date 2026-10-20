package br.com.sakila.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sakila.dto.LanguageForm;
import br.com.sakila.model.LanguageModel;
import br.com.sakila.service.LanguageNotFoundException;
import br.com.sakila.service.LanguageOperationException;
import br.com.sakila.service.LanguageService;
import jakarta.validation.Valid;

@Controller
public class LanguageController {

	private final LanguageService languageService;

	public LanguageController(LanguageService languageService) {
		this.languageService = languageService;
	}

	@GetMapping({ "/", "/languages" })
	public String list(Model model) {
		if (!model.containsAttribute("languageForm")) {
			model.addAttribute("languageForm", new LanguageForm());
		}
		populateList(model);
		model.addAttribute("editing", false);
		return "language";
	}

	@GetMapping("/languages/{languageId}/edit")
	public String edit(@PathVariable Long languageId, Model model, RedirectAttributes redirectAttributes) {
		try {
			LanguageModel language = languageService.findById(languageId);
			model.addAttribute("languageForm", toForm(language));
			model.addAttribute("editing", true);
			populateList(model);
			return "language";
		} catch (LanguageNotFoundException exception) {
			redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
			return "redirect:/languages";
		}
	}

	@PostMapping("/languages")
	public String create(@Valid @ModelAttribute("languageForm") LanguageForm form,
			BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("editing", false);
			populateList(model);
			return "language";
		}

		try {
			languageService.create(form.getName());
			redirectAttributes.addFlashAttribute("successMessage", "Idioma incluído com sucesso.");
			return "redirect:/languages";
		} catch (LanguageOperationException exception) {
			model.addAttribute("errorMessage", exception.getMessage());
			model.addAttribute("editing", false);
			populateList(model);
			return "language";
		}
	}

	@PostMapping("/languages/{languageId}")
	public String update(@PathVariable Long languageId,
			@Valid @ModelAttribute("languageForm") LanguageForm form, BindingResult bindingResult,
			Model model, RedirectAttributes redirectAttributes) {
		form.setLanguageId(languageId);
		if (bindingResult.hasErrors()) {
			model.addAttribute("editing", true);
			populateList(model);
			return "language";
		}

		try {
			languageService.update(languageId, form.getName());
			redirectAttributes.addFlashAttribute("successMessage", "Idioma atualizado com sucesso.");
			return "redirect:/languages";
		} catch (LanguageNotFoundException | LanguageOperationException exception) {
			model.addAttribute("errorMessage", exception.getMessage());
			model.addAttribute("editing", true);
			populateList(model);
			return "language";
		}
	}

	@PostMapping("/languages/{languageId}/delete")
	public String delete(@PathVariable Long languageId, RedirectAttributes redirectAttributes) {
		try {
			languageService.delete(languageId);
			redirectAttributes.addFlashAttribute("successMessage", "Idioma excluído com sucesso.");
		} catch (LanguageNotFoundException | LanguageOperationException exception) {
			redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
		}
		return "redirect:/languages";
	}

	@GetMapping("/idiomas")
	public String legacyList(Model model, @ModelAttribute("mensagem") String message) {
		model.addAttribute("linguas", languageService.findAll());
		model.addAttribute("mensagem", message);
		return "idiomas/idiomas";
	}

	@GetMapping("/idiomas/novo")
	public String legacyCreateForm(Model model) {
		model.addAttribute("idioma", new LanguageForm());
		return "idiomas/novo";
	}

	@PostMapping("/idiomas/alterar")
	public String legacyEditForm(@RequestParam Long languageId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			model.addAttribute("idioma", toForm(languageService.findById(languageId)));
			return "idiomas/alterar";
		} catch (LanguageNotFoundException exception) {
			redirectAttributes.addFlashAttribute("mensagem", exception.getMessage());
			return "redirect:/idiomas";
		}
	}

	@PostMapping("/idiomas/salvar")
	public String legacySave(@Valid @ModelAttribute("idioma") LanguageForm form,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			return form.getLanguageId() == null ? "idiomas/novo" : "idiomas/alterar";
		}

		try {
			if (form.getLanguageId() == null) {
				languageService.create(form.getName());
			} else {
				languageService.update(form.getLanguageId(), form.getName());
			}
			redirectAttributes.addFlashAttribute("mensagem", "Operação realizada com sucesso!");
		} catch (LanguageNotFoundException | LanguageOperationException exception) {
			redirectAttributes.addFlashAttribute("mensagem", exception.getMessage());
		}
		return "redirect:/idiomas";
	}

	private LanguageForm toForm(LanguageModel language) {
		LanguageForm form = new LanguageForm();
		form.setLanguageId(language.getLanguageId());
		form.setName(language.getName());
		return form;
	}

	private void populateList(Model model) {
		model.addAttribute("languages", languageService.findAll());
	}
}
