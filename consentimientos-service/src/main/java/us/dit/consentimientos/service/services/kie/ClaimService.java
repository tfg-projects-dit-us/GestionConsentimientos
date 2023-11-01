package us.dit.consentimientos.service.services.kie;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hl7.fhir.r5.model.Questionnaire;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.UserTaskServicesClient;
import org.kie.server.api.model.instance.WorkItemInstance;
import org.kie.server.api.model.instance.NodeInstance;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import us.dit.consentimientos.service.services.fhir.FhirClient;

/**
 * ESTE SERVICIO DESAPARECERÁ, ES SÓLO PARA PRUEBAS
 */
@Service
public class ClaimService {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private KieUtilService kie;
	
	@Autowired
	private FhirClient fhir;
	
	private String containerId="consentimientos-kjar-1_0-SNAPSHOT";
	private String processId="consentimientos-kjar.solicitudConsentimiento";
	
	public Long newInstance(String principal) {
		Map<String,Object> variables= new HashMap<String,Object>();
        
	    variables.put("principal", principal);
		ProcessServicesClient client = kie.getProcessServicesClient();
		Long idInstanceProcess = client.startProcess(containerId, processId,variables);
		logger.info("conseguido??? " + idInstanceProcess.toString());
		return idInstanceProcess;
	}
	
	public Questionnaire initTask(String principal,Long processId) {
		logger.info("Entro en init task con principal: " + principal+" y processId: "+processId);
		WorkItemInstance wi=findNextTask(principal,processId);
		logger.info("Creo cliente de tareas");
		UserTaskServicesClient taskClient = kie.getUserTaskServicesClient();		
		Questionnaire questionnaire= fhir.getQuestionnaire((String)wi.getParameters().get("fhirbase"), (String)wi.getParameters().get("questionnaireId"));
		return questionnaire;
	}
	
	private WorkItemInstance findNextTask(String principal,Long processId) {
		logger.info("Entro en findNextTask con principal: " + principal+" y processId: "+processId);
		NodeInstance task=null;
		
		logger.info("Creo cliente de procesos");
		ProcessServicesClient processClient = kie.getProcessServicesClient();
		logger.info("Llamo a findNodeInstances del cliente de procesos");
		WorkItemInstance wi=processClient.getWorkItem(containerId, processId, (long) 1);
		logger.info("WI: "+wi.toString());			
		
		
		return wi;
	}
	
	/**
	 * Busca todas las tareas de un usuario
	 * 
	 * @param user     El id del "actualOwner" de la tarea (ActorId en las variables
	 *                 de entrada a la tarea)
	 * @param password Password del usuario
	 * @return Una lista de TaskSummaries (con la información más relevante de las
	 *         tareas asignadas al usuario
	 */
	public List<TaskSummary> findAll(String principal) {
		logger.info("En findAll de TaskService con principal= "+principal);

		//KieUtilService kie = new KieUtil(URL,user, password);
	
		List<TaskSummary> taskList = null;

		UserTaskServicesClient client = kie.getUserTaskServicesClient();
		logger.info("Llamo a FINDTASKS de UserTaskServicesClient con principal= "+principal);
		/**
		 * Si no se pone la propiedad -Dorg.kie.server.bypass.auth.user=true
		 * El método findTasks devuelve las tareas asignadas al usuario que está en el cliente (el que se usó al crearlo), no las asignadas al
		 * usuario del argumento
		 * Para que se considere las asignadas al usuario
		 * "optional user id to be used instead of authenticated user - only when bypass authenticated user is enabled"
		 * Es decir bypass authenticated tiene que ser true y eso lo hago en la línea de comandos, al ejecutar la aplicación
		 */
		taskList = client.findTasks(principal, 0, 0);
		//taskList=client.findTasksOwned(principal, null, null);
		//Esta igual como hace query 'http://localhost:8090/rest/server/queries/tasks/instances/owners?user=user&page=null&pageSize=null&sort=&sortOrder=true no va bien
		//taskList = client.findTasksByVariableAndValue(principal, "actualowner_id", principal, null, null, null);
		/**
		 * Esta llamada crea la invocación
		 * http://localhost:8090/rest/server/queries/tasks/instances/variables/actualowner_id?page=null&pageSize=null&sort=&sortOrder=true&varValue=valordeprincipal'
		 * Que da error "not found"
		 */
		/**
		taskList=client.findTasksOwned(principal, null, null);
		Y esta
		'http://localhost:8090/rest/server/queries/tasks/instances/owners?page=null&pageSize=null&sort=&sortOrder=true'
		Mismo error
		**/
		logger.info("Termino findTasks");
		for (TaskSummary task : taskList) {
			System.out.println("Tarea: " + task);
		}
		return taskList;
	}

}
