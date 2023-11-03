/**
 * 
 */
package us.dit.consentimientos.service.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.server.api.model.instance.TaskSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import us.dit.consentimientos.service.services.kie.ClaimService;

/**
 * 
 */
@Controller
@RequestMapping("/consentimientos/paciente")
public class PatientOptions {
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private ClaimService claim;
	@GetMapping()
	public String getAllMyTasks(HttpSession session, Model model) {
		logger.info("buscando todos los consentimientos pendientes del usuario");
		List<TaskSummary> tasksList = null;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails principal = (UserDetails) auth.getPrincipal();
		logger.info("Datos de usuario (principal)" + principal);
		

		//Para no tener que usar el password siempre se crean los clientes con el mismo usuario y contraseña (el kieutil está configurado)
		tasksList = claim.findConsentsToReview(principal.getUsername());
		model.addAttribute("tasks", tasksList);
		/**
		 * Ejemplo de datos de una taskSummary devuelta TaskSummary{ id=2,
		 * name='TareaDePrueba', description='', status='Reserved', actualOwner='user',
		 * createdBy='', createdOn=Sat Oct 07 13:23:42 CEST 2023, processInstanceId=2,
		 * processId='guardianes-kjar.prueba',
		 * containerId='guardianes-kjar-1.0-SNAPSHOT', correlationKey=null,
		 * processType=null}
		 */
		logger.info("vuelve de consultar tareas");
		return "myTasks";
	}

}
