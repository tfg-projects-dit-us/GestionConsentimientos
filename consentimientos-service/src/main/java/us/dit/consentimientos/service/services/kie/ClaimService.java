package us.dit.consentimientos.service.services.kie;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.QuestionnaireResponse;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.UserTaskServicesClient;
import org.kie.server.api.model.instance.WorkItemInstance;
import org.kie.server.api.model.instance.NodeInstance;
import org.kie.server.api.model.instance.TaskInstance;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import jdk.internal.org.jline.utils.Log;
import us.dit.consentimientos.service.services.fhir.FhirClient;
import us.dit.consentimientos.service.services.mapper.MapToQuestionnaireResponse;

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
	
	public Questionnaire initTask(HttpSession session) {
		logger.info("Entro en init task con processId: "+processId);
		WorkItemInstance wi=findNextTask((Long)session.getAttribute("processId"));
		session.setAttribute("wi",wi);		
		Questionnaire questionnaire= fhir.getQuestionnaire((String)wi.getParameters().get("fhirbase"), (String)wi.getParameters().get("questionnaireId"));
		logger.info("Recuperado cuestionario con id "+questionnaire.getId());	
		session.setAttribute("questionnaire", questionnaire);		
		return questionnaire;
	}
	
	private WorkItemInstance findNextTask(Long processId) {
		logger.info("Entro en findNextTask con processId: "+processId);				
		logger.info("Creo cliente de procesos");
		ProcessServicesClient processClient = kie.getProcessServicesClient();
		logger.info("Llamo a findNodeInstances del cliente de procesos");
		
		WorkItemInstance wi=processClient.getWorkItemByProcessInstance(containerId, processId).get(0);
		logger.info("WI: "+wi.toString());		
		return wi;
	}
	
	public void completeTask(Map<String, String[]> map,List<String> patientList,Questionnaire questionnaire, WorkItemInstance wi,String principal) {

		// Mapeamos la respuesta del formulario a un recurso fhir QuestionnaireResponse
		MapToQuestionnaireResponse mapToQuestionnaireResponse = new MapToQuestionnaireResponse(questionnaire);
		QuestionnaireResponse resp = mapToQuestionnaireResponse.map(map);
	
		
		String respId = fhir.saveQuestionnaireResponse(questionnaire.getIdElement().getBaseUrl(), resp);

		
		// Incluimos los parametros de salida de la tarea humana en un Map, en donde sus claves se corresponden con el nombre de la variable en la tarea
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("responseId", respId);
		params.put("patients", patientList);
	
				
		UserTaskServicesClient userClient = kie.getUserTaskServicesClient();
		TaskInstance task = userClient.findTaskByWorkItemId(wi.getId());
		userClient.startTask(containerId, task.getId(), principal);
		userClient.completeTask(containerId, task.getId(), principal, params);
		
		/*
		userClient.completeTask(containerId, idTask, user.getDni(), params);
		logger.info("TAREA CON ID = "+idTask+" COMPLETADA CON ÉXITO");
		
		// Asociamos la instancia del proceso al practitioner
		consentsRequestedPractitionerProcess(responseForm, patients);
				
		// Asociamos los pacientes con la instancia de proceso correspondiente
		pendingPatientProcess(patientList);
		*/
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
