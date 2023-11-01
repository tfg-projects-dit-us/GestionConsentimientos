package us.dit.consentimientos.service.services.kie;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.QueryServicesClient;
import org.kie.server.client.UIServicesClient;
import org.kie.server.client.UserTaskServicesClient;
import org.kie.server.client.admin.UserTaskAdminServicesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//import org.springframework.stereotype.Service;
/**
 * Esta clase debe contener los servicios que proporcionan capacidades para
 * interaccionar con el kie server Debe ser el único responsable de estos
 * aspectos, liberando a los demás de esta necesidad Debería ser un Bean, un
 * componente spring, para poder inyectarlo en todos aquellos que lo necesiten,
 * especialmente servicios
 */
@Service
public class KieUtil implements KieUtilService {
	@Value("${kieserver.location}")
	private String URL;
	@Value("${org.kie.server.user}")
	private String USERNAME;
	@Value("${org.kie.server.pwd}")
	private String PASSWORD;
	private static final Logger logger = LogManager.getLogger();
	
	

	private KieServicesConfiguration config;
	
	public KieUtil() {
		logger.info("Creando el kieutil con los valores por defecto user "+ USERNAME + "pwd: "+PASSWORD);
	}

	@Override
	public ProcessServicesClient getProcessServicesClient() {
		logger.info("getprocessservicesclient");
		KieServicesClient kieServicesClient = getKieServicesClient();
		ProcessServicesClient processServicesClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
		logger.info("salgo de getprocessservicesclient");
		return processServicesClient;
	}

	@Override
	public UserTaskServicesClient getUserTaskServicesClient() {
		logger.info("ENTRANDO EN USERTASKSERVICE");
		KieServicesClient kieServicesClient = getKieServicesClient();
		
		UserTaskServicesClient userClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
		logger.info("Se ha obtenido el cliente para la gestión de tareas");
		return userClient;
	}

	@Override
	public QueryServicesClient getQueryServicesClient() {
		KieServicesClient kieServicesClient = getKieServicesClient();
		QueryServicesClient queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);

		return queryClient;
	}

	@Override
	public UserTaskAdminServicesClient getUserTaskAdminServicesClient() {
		KieServicesClient kieServicesClient = getKieServicesClient();
		UserTaskAdminServicesClient client = kieServicesClient.getServicesClient(UserTaskAdminServicesClient.class);

		return client;
	}
	
	@Override
	public UIServicesClient getUIServicesClient() {
		KieServicesClient kieServicesClient = getKieServicesClient();
		UIServicesClient client = kieServicesClient.getServicesClient(UIServicesClient.class);

		return client;
	}

	private KieServicesClient getKieServicesClient() {
		logger.info("entro en getkieservicesclient con url " + URL);
		config = KieServicesFactory.newRestConfiguration(URL, USERNAME, PASSWORD);
		logger.info("salgo de newrestconfigurarion");
		config.setMarshallingFormat(MarshallingFormat.JSON);

		return KieServicesFactory.newKieServicesClient(config);
	}

}
