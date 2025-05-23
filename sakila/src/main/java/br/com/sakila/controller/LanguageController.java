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
	
	@GetMapping(value="/")
    public String language(ModelMap model) { 	
		
		model.addAttribute("languages", languageRepository.findAll());
		
        return "language"; 
    	    	
    }	

}
