package us.dit.consentimientos.service.controllers;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hl7.fhir.r5.model.Questionnaire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import us.dit.consentimientos.service.services.kie.ClaimService;
import us.dit.consentimientos.service.services.mapper.QuestionnaireToFormPractitioner;

@Controller
@RequestMapping("/consentimientos/facultativo")
public class PractitionerOptions {
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private ClaimService claim;
	
	@Autowired
	private QuestionnaireToFormPractitioner mapper;
	
	@GetMapping()
	public String menu() {
    return "menuPractitioner";
	}
	@GetMapping("/solicitud")
	@ResponseBody
	public String initClaim(HttpSession session) {
		/**
		 * Inicia proceso
		 * Reclama tarea
		 * Pide questionnarie
		 * Construye formulario
		 */
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		logger.info("Datos de usuario (principal)" + principal);
		Long processId=claim.newInstance(principal.getUsername());
		Questionnaire questionnaire= claim.initTask(principal.getUsername(), processId);
		
	    return mapper.map(questionnaire);
		}
	
}
