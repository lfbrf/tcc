package com.example.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.User;
import com.example.service.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/teste" }, method = RequestMethod.GET)
	public ModelAndView teste() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("teste");
		return modelAndView;
	}

	@RequestMapping(value = { "/ttt" }, method = RequestMethod.GET)
	public ModelAndView ttt() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("ttt");
		return modelAndView;
	}

	@RequestMapping(value = { "/user", "/user/home" }, method = RequestMethod.GET)
	public ModelAndView user() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("user/home");
		return modelAndView;
	}

	@RequestMapping("/403")
	public String accessDenied() {
		return "403";
	}

	public boolean isAdmin() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasUserRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"));
		if (hasUserRole)
			return true;
		return false;
	}

	public boolean isUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_USER"))) {
			System.out.println("EPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		} else {
			System.out.println(
					"NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNEPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");

		}
		boolean hasUserRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("USER"));
		if (hasUserRole)
			return true;
		return false;
	}

	public boolean isManager() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		boolean hasUserRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ADMIN"));
		if (hasUserRole)
			return true;
		return false;
	}

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public String login(Principal principal, HttpServletRequest request) {
		// ModelAndView modelAndView = new ModelAndView();
		System.out.println("ME CHAMOU NOTTTTTTTTTTTTTTTTTTTTTEEE" + request.getParameter("email"));
		if (principal == null) {
			System.out.println("ME CHAMOU ASSIM");
			return "login";
		}
		if (isUser())
			return "redirect:" + "/user";
		if (isAdmin())
			return "redirect:" + "/admin/home";
		if (isManager())
			return "redirect:" + "/manager";
		return "";
	}

	@RequestMapping(value = { "/manager/products" }, method = RequestMethod.GET)
	public ModelAndView ManagerProducts() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("manager/products");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			userService.saveUser(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("registration");

		}
		return modelAndView;
	}

	@RequestMapping(value = "/admin/registermanager", method = RequestMethod.GET)
	public ModelAndView regis() {
		ModelAndView modelAndView = new ModelAndView();
		User user = new User();
		modelAndView.addObject("user", user);
		modelAndView.setViewName("admin/registermanager");
		return modelAndView;
	}

	@RequestMapping(value = "/admin/registermanager", method = RequestMethod.POST)
	public ModelAndView createNewManager(@Valid User user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = userService.findUserByEmail(user.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("admin/registermanager");
		} else {
			userService.saveManager(user);
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("user", new User());
			modelAndView.setViewName("admin/registermanager");

		}
		return modelAndView;
	}

	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName",
				"Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
		modelAndView.addObject("adminMessage", "Content Available Only for Users with Admin Role");
		modelAndView.setViewName("admin/home");
		return modelAndView;
	}

}
