package br.com.sakila.controller;

import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sakila.model.LanguageModel;
import br.com.sakila.repository.LanguageRepository;

@Controller
public class LanguageController {

    @Autowired
    private LanguageRepository languageRepository;

    // Exibir lista de idiomas
    @GetMapping("/")
    public String listarIdiomas(ModelMap model, @ModelAttribute("mensagem") String mensagem) {
        model.addAttribute("linguas", languageRepository.findAll());
        model.addAttribute("mensagem", mensagem);
        return "idiomas/idiomas"; // templates/idiomas/idiomas.html
    }

    // Formulário para novo idioma
    @GetMapping("/idiomas/novo")
    public String novoIdiomaForm(ModelMap model) {
        model.addAttribute("idioma", new LanguageModel());
        return "idiomas/novo"; // templates/idiomas/novo.html
    }

    // Processar o cadastro
    @PostMapping("/idiomas/salvar")
    public String salvarIdioma(@ModelAttribute("idioma") LanguageModel idioma, RedirectAttributes redirectAttributes) {
    	
    	idioma.setLastUpdate(Timestamp.from(Instant.now())); // ✅ seta o timestamp atual 
    	languageRepository.save(idioma);
        redirectAttributes.addFlashAttribute("mensagem", "Idioma cadastrado com sucesso!");
        return "redirect:/";
    }
}
