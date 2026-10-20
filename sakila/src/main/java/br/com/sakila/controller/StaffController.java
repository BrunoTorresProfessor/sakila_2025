package br.com.sakila.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.sakila.repository.StaffRepository;

@Controller
public class StaffController {
	
	@Autowired
	StaffRepository staffRepository;
	
	@GetMapping(value="/staffs")
    public String staff(ModelMap model) { 	
		
		model.addAttribute("staffs", staffRepository.findAll());
		
		
        return "staff.html"; 
    	    	
    }	

}
