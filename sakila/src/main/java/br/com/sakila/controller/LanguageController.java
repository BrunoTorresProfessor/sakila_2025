package br.com.sakila.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.sakila.repository.LanguageRepository;

@Controller
public class LanguageController {
	
	@Autowired
	LanguageRepository languageRepository;
	
	@Autowired
	LanguageRepository idiomaRepository;
	
	@GetMapping(value="/")
    public String language(ModelMap model) { 	
		
		model.addAttribute("linguas", languageRepository.findAll());
		
        return "language"; 
    	    	
    }	
	@GetMapping(value="/idiomas")
    public String idiomas(ModelMap model) { 	
		
		model.addAttribute("idiomas", idiomaRepository.findAll());
		
        return "idiomas.html"; 
    	    	
    }	

}
