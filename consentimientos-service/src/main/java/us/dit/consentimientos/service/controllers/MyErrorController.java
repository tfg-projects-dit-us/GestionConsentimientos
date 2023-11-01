package us.dit.consentimientos.service.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

	/**
	 * This code has been found on:
	 * https://www.baeldung.com/spring-boot-custom-error-page
	 */
	@RequestMapping("/error")
	public String handleError(HttpServletRequest request,Model model) {
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		model.addAttribute("status", status);
		return "error";
	}
}