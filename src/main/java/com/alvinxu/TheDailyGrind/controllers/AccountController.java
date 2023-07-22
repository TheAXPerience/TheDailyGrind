package com.alvinxu.TheDailyGrind.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.alvinxu.TheDailyGrind.dto.RegisterDto;
import com.alvinxu.TheDailyGrind.models.Account;
import com.alvinxu.TheDailyGrind.services.AccountService;

import jakarta.validation.Valid;

@Controller
public class AccountController {
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/")
	public String index(Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}
		return "redirect:/home";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("registerDto", new RegisterDto());
		return "register-form";
	}
	
	@PostMapping("/register/")
	public String handle_register(Model model, @Valid RegisterDto registerDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("registerDto", registerDto);
			return "register-form";
		}
		
		if (!this.accountService.registerNewAccount(registerDto)) {
			// email exists, cannot make account
			bindingResult.rejectValue("email", "registerDto.email", "The given email already has an account.");
			model.addAttribute("registerDto", registerDto);
			return "register-form";
		}
		
		return "redirect:/login?registered=true";
	}
	
	@GetMapping("/register/termsofservice")
	public String terms_of_service() {
		return "termsofservice";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		return "login-form";
	}
	
	/*
	@PostMapping("/login/")
	public String handle_login() {
		return "redirect:/";
	}
	*/
}
