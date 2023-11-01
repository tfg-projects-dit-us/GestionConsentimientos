/**
 * 
 */
package us.dit.consentimientos.service.services.kie;

import us.dit.consentimientos.service.services.kie.KieUtil;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.server.api.model.instance.TaskInstance;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.UIServicesClient;
/* https://javadoc.io/doc/org.kie.server/kie-server-api/latest/org/kie/server/api/model/instance/TaskSummary.html */
import org.kie.server.client.UserTaskServicesClient;
import org.kie.server.client.admin.UserTaskAdminServicesClient;
/* https://javadoc.io/doc/org.kie.server/kie-server-client/latest/org/kie/server/client/UserTaskServicesClient.html */
import org.kie.api.task.model.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TasksService {

	private static final Logger logger = LogManager.getLogger();
	@Autowired
	private KieUtilService kie;
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
	
	public List<TaskSummary> findPotential(String principal) {
		List<TaskSummary> taskList = null;
		UserTaskServicesClient client=kie.getUserTaskServicesClient();
		taskList=client.findTasksAssignedAsPotentialOwner(principal,0,0);
		return taskList;
	}
	/**
	 * Busca las tareas asignadas a un usuario que tengan un estado determinado
	 * 
	 * @param user
	 * @param password
	 * @param state
	 * @return Listado de TaskSummaries de las tareas asignadas al usuario en el
	 *         estado indicado como argumento de entrada
	 */
	public List<TaskSummary> findByStatus(String user, String password, String state) {
		List<String> status = new ArrayList<String>();
		status.add(state);

	//	KieUtilService kie = new KieUtil(URL,user, password);
		logger.info("el kieUTIL creado ok");
		List<TaskSummary> taskList = null;

		UserTaskServicesClient client = kie.getUserTaskServicesClient();
		logger.info("Llamo a findTasksByVariableAndValue de UserTaskServicesClient");
		taskList = client.findTasksByVariableAndValue(user, "actualOwner", user, status, 0, 0);
		logger.info("Termino findTasksByVariableAndValue");
		for (TaskSummary task : taskList) {
			System.out.println("Tarea: " + task);
		}
		return taskList;
	}

	/**
	 * status.add("Completed"); status.add("Created"); status.add("Error");
	 * status.add("Exited"); status.add("Failed"); status.add("InProgress");
	 * status.add("Obsolete"); status.add("Ready"); status.add("Reserved");
	 * status.add("Suspended"); taskList = uTSC.findTasksByVariableAndValue(user,
	 * "user", user,status , 7, 7); for(TaskSummary task : taskList){
	 * System.out.println("Tarea: "+task); }
	 */
	/**
	 * extraído de
	 * https://access.redhat.com/documentation/en-us/red_hat_process_automation_manager/7.9/html/deploying_and_managing_red_hat_process_automation_manager_services/kie-server-java-api-con_kie-apis
	 * Para usar el QueryService
	 * 
	 * // Client setup KieServicesConfiguration conf =
	 * KieServicesFactory.newRestConfiguration(SERVER_URL, LOGIN, PASSWORD);
	 * KieServicesClient client = KieServicesFactory.newKieServicesClient(conf);
	 * 
	 * // Get the QueryServicesClient QueryServicesClient queryClient =
	 * client.getServicesClient(QueryServicesClient.class);
	 * 
	 * // Build the query QueryDefinition queryDefinition =
	 * QueryDefinition.builder().name(QUERY_NAME) .expression("select * from Task
	 * t") .source("java:jboss/datasources/ExampleDS") .target("TASK").build();
	 * 
	 * // Specify that two queries cannot have the same name
	 * queryClient.unregisterQuery(QUERY_NAME);
	 * 
	 * // Register the query queryClient.registerQuery(queryDefinition);
	 * 
	 * // Execute the query with parameters: query name, mapping type (to map the
	 * fields to an object), page number, page size, and return type
	 * List<TaskInstance> query = queryClient.query(QUERY_NAME,
	 * QueryServicesClient.QUERY_MAP_TASK, 0, 100, TaskInstance.class);
	 * 
	 * // Read the result for (TaskInstance taskInstance : query) {
	 * System.out.println(taskInstance); }
	 * 
	 * 
	 */
	/**
	 * Devuelve una instancia de tarea (TaskInstance) a partir del identificador de
	 * la tarea
	 * 
	 * @param user
	 * @param password
	 * @param taskId
	 * @return Instancia de la tarea indicada en el argumento de entrada taskId
	 */
	public TaskInstance findById(Long taskId) {
		logger.info("En findAll de TaskService");

		TaskInstance task = null;

		logger.info("el kieUTIL creado ok");
		UserTaskServicesClient client = kie.getUserTaskServicesClient();
		logger.info("Llamo a findTaskById de UserTaskServicesClient");
		task = client.findTaskById(taskId);
		logger.info("Termino findTaskById");

		return task;
	}

	/**
	 * Devuelve el conjunto de tareas que el usuario puede reclamar para ejecutar,
	 * no está terminado OJO ESTE MÉTODO NO ESTÁ PROBADO
	 * 
	 * @param user
	 * @param password
	 * @return Listado de TaskSummaries de las tareas que cumplen el criterio de
	 *         búsqueda
	 */
	public List<TaskSummary> findTasksPool(String principal) {
		List<TaskSummary> tasks = null;

		UserTaskServicesClient client = kie.getUserTaskServicesClient();
		client.findTasksAssignedAsPotentialOwner(principal, 0, 0);
		return tasks;
	}
	
	public String findTaskForm(TaskInstance task,String principal) {
		UIServicesClient client=kie.getUIServicesClient();
	//return client.getTaskForm(task.getContainerId(),task.getId());
		return client.getTaskFormAsUser(task.getContainerId(),task.getId(),null,principal);
	}

}
