/**
 * 
 */
package us.dit.consentimientos.service.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import us.dit.consentimientos.model.Signal;
import us.dit.consentimientos.service.services.kie.ClaimService;
import us.dit.consentimientos.service.services.kie.KieUtilService;
import us.dit.consentimientos.service.services.mapper.QuestionnaireToFormPractitioner;

/**
 * 
 */
@RestController
@RequestMapping("/signals")
public class SignalsController {
	private static final Logger logger = LogManager.getLogger();	

	@Autowired
	private KieUtilService kie;
		
	@PostMapping()	
	public String sendSignal(@RequestBody Signal signal,HttpSession session) {
		/**
		 * Difunde una señal por todos los servidores KIE gestionados en la aplicación
		 */
		logger.info("Enviando una señal a todos");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		logger.info("Datos de usuario (principal)" + principal);
		kie.sendSignal(signal.getName(), signal.getMessage());		
		return "OK";
		}
}
