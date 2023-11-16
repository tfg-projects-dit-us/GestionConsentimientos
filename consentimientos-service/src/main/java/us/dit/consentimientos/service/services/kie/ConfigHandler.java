package us.dit.consentimientos.service.services.kie;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.springframework.stereotype.Component;

@Component("ProcessConfig")
public class ConfigHandler implements WorkItemHandler {
	private static final Logger logger = LogManager.getLogger();

	@Override
	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		logger.info("Se está ejecutando mi manejador con los detalles de workItem " + workItem);
		
		Map<String,Object> parametros = workItem.getParameters();
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("fhirbase","http://hapi.fhir.org/baseR4");
		results.put("questionnaireId","33326707");
		logger.info("Nombre proceso Entrada: "+(String)parametros.get("processName"));
		
		manager.completeWorkItem(workItem.getId(), results);
	}

	@Override
	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {

	}

}
