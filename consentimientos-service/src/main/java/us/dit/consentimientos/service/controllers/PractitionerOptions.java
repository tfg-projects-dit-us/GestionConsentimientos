package us.dit.consentimientos.service.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.QuestionnaireResponse;
import org.kie.api.runtime.process.WorkItem;
import org.kie.server.api.model.instance.WorkItemInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
		session.setAttribute("processId", processId);
		Questionnaire questionnaire= claim.initTask(session);
		return mapper.map(questionnaire);
		}
	@PostMapping("/solicitud")
	public String responseQuestionnairePractitioner(HttpServletRequest request,HttpSession session) {
		String redirect = null;
		logger.info("Recibido formulario de solicitud");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		// Obtenemos los campos rellenados del Meta-Cuestionario en un Map
		Map<String, String[]> responseForm = request.getParameterMap();
		logger.info("claves del mapa de parámetros recibidos "+responseForm.keySet().toString());
		
		// Obtenemos el titulo
		//String title = responseForm.get("1.1")[0];
				
		// Obtenemos la lista de pacientes a los que va dirigido el consentimiento
		String patients = responseForm.get("patients")[0];
		logger.info("listado de pacientes como string "+ patients);
		List<String> patientList = Arrays.asList(patients.split(";"));
		logger.info("Pacientes a los que va destinado "+patientList);
		// Borramos el campo correspondiente a los pacientes
		responseForm = deleteFielsPatients(responseForm);
		
		claim.completeTask(responseForm,patientList,(Questionnaire) session.getAttribute("questionnaire"),(WorkItemInstance) session.getAttribute("wi"),principal.getUsername());		
		redirect = "redirect:/consentimientos/facultativo?success";	
		return redirect;
	}
	
	private Map<String, String[]> deleteFielsPatients(Map<String, String[]> response){
		Map<String, String[]> result = new HashMap<String, String[]>();
		
		for (Map.Entry<String, String[]> entry : response.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			
			if (!(key.equals("1.2"))) {
				result.put(key, values);
			}
		}		
		return result;
	}
	
}
