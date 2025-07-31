package es.unex.cum.mdai.vrp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	public MainController() {
		System.out.println("\t HomeController builder");
	}
	
	@GetMapping("/")
	public String holaTh(Model model) {
		String text = "Hola Thymeleaf";
		model.addAttribute("message", text);
		return "Home";
	}
}
