package br.com.sakila.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import br.com.sakila.repository.ActorRepository;

@Controller
public class ActorController {
	
    @Autowired
    private ActorRepository actorRepository;
	
    @GetMapping("/atores")
    public String listarIdiomas(ModelMap model, @ModelAttribute("mensagem") String mensagem) {
        model.addAttribute("atores", actorRepository.findAll());
        model.addAttribute("mensagem", mensagem);
        return "atores/atores"; 
    }

}
